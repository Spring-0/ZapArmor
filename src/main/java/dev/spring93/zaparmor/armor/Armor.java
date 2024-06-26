package dev.spring93.zaparmor.armor;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.config.DefaultConfig;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class Armor {
    private Set<UUID> fullSetEquipped = new HashSet<>();
    private Map<UUID, BukkitRunnable> potionEffectTasks = new HashMap<>();
    protected String name;
    protected ArmorConfig armorConfig;
    protected DefaultConfig defaultConfig;
    protected ItemStack helmet;
    protected ItemStack chestplate;
    protected ItemStack leggings;
    protected ItemStack boots;
    protected BukkitRunnable potionEffectTask;

    public Armor(String configName) {
        this.name = configName;
        this.armorConfig = new ArmorConfig(configName);
        this.defaultConfig = new DefaultConfig();
        this.helmet = createArmorItem("helmet");
        this.chestplate = createArmorItem("chestplate");
        this.leggings = createArmorItem("leggings");
        this.boots = createArmorItem("boots");
    }

    /**
     * Method used to create the custom armor item.
     * @param armorPiece
     * @return
     */
    private ItemStack createArmorItem(String armorPiece) {
        String path = "armor-set." + armorPiece;
        Material material = Material.valueOf(armorConfig.getConfig().getString(path + ".material"));
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', armorConfig.getConfig().getString(path + ".display-name")));
        meta.setLore(armorConfig.getConfigStringList(path + ".lore"));

        if(meta instanceof LeatherArmorMeta) {
            Color color = armorConfig.getRGB(path + ".dye-color");
            ((LeatherArmorMeta) meta).setColor(color);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        }

        List<String> enchants = armorConfig.getConfigStringList(path + ".enchantments");
        for(String enchantName : enchants) {
            String[] parts = enchantName.split(":");
            Enchantment enchantment = Enchantment.getByName(parts[0]);
            int level = Integer.parseInt(parts[1]);
            meta.addEnchant(enchantment, level, true);
        }

        if (armorConfig.getConfig().getBoolean(path + ".glow") && !meta.hasEnchants()) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.spigot().setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Event handler triggered when armor is equipped.
     * @param event
     */
    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getNewArmorPiece();

        if(item != null && isArmorPiece(item)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(isArmorSetFullyEquipped(player)) {
                        startPotionEffectTask(player);
                        onArmorEquipAction(player);
                        fullSetEquipped.add(player.getUniqueId());
                    }
                }
            }.runTaskLater(ZapArmor.getInstance(), 1L); // Delay of 1 tick
        }
    }

    /**
     * Event handler triggered when armor is un-equipped.
     * @param event
     */
    @EventHandler
    public void onArmorDequip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getOldArmorPiece();

        if(item != null && isArmorPiece(item)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!isArmorSetFullyEquipped(player) && fullSetEquipped.remove(player.getUniqueId())) {
                        stopPotionEffectTask(player.getUniqueId());
                        onArmorDequipAction(player);
                    }
                }
            }.runTaskLater(ZapArmor.getInstance(), 1L); // Delay of 1 tick
        }
    }

    /**
     * Event handler triggered when a player quits the game.
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(fullSetEquipped.remove(player.getUniqueId())) {
            stopPotionEffectTask(player.getUniqueId());
        }
    }

    /**
     * Event handler triggered when a player joins the game.
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(isArmorSetFullyEquipped(player)) {
            startPotionEffectTask(player);
        }
    }

    /**
     * Method used to start the potion effect task for a player.
     * @param player The player to start the effect task timer for.
     */
    protected void startPotionEffectTask(Player player) {
        potionEffectTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(isArmorSetFullyEquipped(player)){
                    applyPotionEffects(player);
                }
                else stopPotionEffectTask(player.getUniqueId());

            }
        };
        potionEffectTask.runTaskTimer(ZapArmor.getInstance(), 0L, 100L); // 5 seconds
        potionEffectTasks.put(player.getUniqueId(), potionEffectTask);
    }

    /**
     * Method used to stop the potion effect timer.
     * @param playerId Player UUID.
     */
    protected void stopPotionEffectTask(UUID playerId) {
        BukkitRunnable task = potionEffectTasks.remove(playerId);
        if(task != null) {
            task.cancel();
        }
    }

    /**
     * Method used to check if the player is wearing a full set of custom armor.
     * @param player
     * @return True if the player is wearing a full set, otherwise false.
     */
    protected boolean isArmorSetFullyEquipped(Player player){
        ItemStack playerHelmet = player.getInventory().getHelmet();
        ItemStack playerChestplate = player.getInventory().getChestplate();
        ItemStack playerLeggings = player.getInventory().getLeggings();
        ItemStack playerBoots = player.getInventory().getBoots();

        return (playerHelmet != null && isSimilar(playerHelmet, helmet)) &&
                (playerChestplate != null && isSimilar(playerChestplate, chestplate)) &&
                (playerLeggings != null && isSimilar(playerLeggings, leggings)) &&
                (playerBoots != null && isSimilar(playerBoots, boots));
    }

    /**
     * Method used to check if an item is a custom armor piece.
     * @param armorPiece
     * @return
     */
    protected boolean isArmorPiece(ItemStack armorPiece) {
        return isSimilar(armorPiece, helmet) ||
                isSimilar(armorPiece, chestplate) ||
                isSimilar(armorPiece, leggings) ||
                isSimilar(armorPiece, boots);
    }

    /**
     * Method used to compare 2 item stacks by comparing lore and display name.
     * @param item The item to be compared.
     * @param configItem The item comparing to.
     * @return True if item is a custom item, otherwise false.
     */
    private boolean isSimilar(ItemStack item, ItemStack configItem) {
        if (item.hasItemMeta() && configItem.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            ItemMeta configItemMeta = configItem.getItemMeta();

            if (itemMeta.hasDisplayName() && configItemMeta.hasDisplayName()) {
                if (!itemMeta.getDisplayName().equals(configItemMeta.getDisplayName())) {
                    return false;
                }
            }

            if (itemMeta.hasLore() && configItemMeta.hasLore()) {
                List<String> itemLore = itemMeta.getLore();
                List<String> configLore = configItemMeta.getLore();

                for (String lore : configLore) {
                    if (!itemLore.contains(lore)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Method used to retrieve a List of PotionEffect objects configured by the user for a specific armor set.
     * @return ArrayList of potion effect objects.
     */
    protected List<PotionEffect> getPotionEffects() {
        List<PotionEffect> potionEffects = new ArrayList<>();
        ConfigurationSection effects = armorConfig.getConfig().getConfigurationSection("armor-set.full-set-equipped-effects.potion-effects");
        if(effects != null) {
            for(String effectName : effects.getKeys(false)) {
                int amplifier = effects.getInt(effectName + ".value") - 1;

                if(amplifier < 0) continue;

                int duration = effects.getInt(effectName + ".time") * 20;
                PotionEffectType type = PotionEffectType.getByName(effectName.toUpperCase());
                if(type != null) {
                    PotionEffect effect = new PotionEffect(type, duration, amplifier);
                    potionEffects.add(effect);
                }
            }
        }
        return potionEffects;
    }

    /**
     * Method used to apply the potion effects to the player.
     * @param player The player to apply potion effects to.
     */
    protected void applyPotionEffects(Player player) {
        List<PotionEffect> potionEffectsList = getPotionEffects();
        for(PotionEffect potionEffect : potionEffectsList) {
            player.removePotionEffect(potionEffect.getType());
            player.addPotionEffect(potionEffect);
        }
    }

    protected abstract void onArmorEquipAction(Player player);
    protected abstract void onArmorDequipAction(Player player);

    public String getName() {
        return name;
    }
    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ArmorConfig getConfig() {
        return armorConfig;
    }

}
