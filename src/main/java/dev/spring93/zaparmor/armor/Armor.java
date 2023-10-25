package dev.spring93.zaparmor.armor;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.config.ArmorConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class Armor {
    private Set<UUID> fullSetEquipped = new HashSet<>();
    protected String name;
    protected ArmorConfig armorConfig;
    protected ItemStack helmet;
    protected ItemStack chestplate;
    protected ItemStack leggings;
    protected ItemStack boots;
    protected BukkitRunnable potionEffectTask;

    public Armor(String configName) {
        this.name = configName;
        this.armorConfig = new ArmorConfig(configName);
        this.helmet = createArmorItem("helmet");
        this.chestplate = createArmorItem("chestplate");
        this.leggings = createArmorItem("leggings");
        this.boots = createArmorItem("boots");
    }

    private ItemStack createArmorItem(String armorPiece) {
        String path = "armor-set." + armorPiece;
        Material material = Material.valueOf(armorConfig.getConfig().getString(path + ".material"));
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', armorConfig.getConfig().getString(path + ".display-name")));
        meta.setLore(armorConfig.getConfigStringList(path + ".lore"));

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

    @EventHandler
    public void onArmorDequip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getOldArmorPiece();

        if(item != null && isArmorPiece(item)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!isArmorSetFullyEquipped(player) && fullSetEquipped.remove(player.getUniqueId())) {
                        stopPotionEffectTask();
                        onArmorDequipAction(player);
                    }
                }
            }.runTaskLater(ZapArmor.getInstance(), 1L); // Delay of 1 tick
        }
    }

    protected void startPotionEffectTask(Player player) {
        potionEffectTask = new BukkitRunnable() {
            @Override
            public void run() {
                applyPotionEffects(player);
            }
        };
        potionEffectTask.runTaskTimer(ZapArmor.getInstance(), 0L, 100L); // 5 seconds
    }

    protected void stopPotionEffectTask() {
        if(potionEffectTask != null) {
            potionEffectTask.cancel();
            potionEffectTask = null;
        }
    }

    protected boolean isArmorSetFullyEquipped(Player player){
        ItemStack playerHelmet = player.getInventory().getHelmet();
        ItemStack playerChestplate = player.getInventory().getChestplate();
        ItemStack playerLeggings = player.getInventory().getLeggings();
        ItemStack playerBoots = player.getInventory().getBoots();

        return (playerHelmet != null && playerHelmet.isSimilar(helmet)) &&
                (playerChestplate != null && playerChestplate.isSimilar(chestplate)) &&
                (playerLeggings != null && playerLeggings.isSimilar(leggings)) &&
                (playerBoots != null && playerBoots.isSimilar(boots));
    }

    protected boolean isArmorPiece(ItemStack armorPiece) {
        return armorPiece.isSimilar(helmet) ||
                armorPiece.isSimilar(chestplate) ||
                armorPiece.isSimilar(leggings) ||
                armorPiece.isSimilar(boots);
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

    protected void applyPotionEffects(Player player) {};
}
