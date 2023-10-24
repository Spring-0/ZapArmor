package dev.spring93.zaparmor.armor;

import dev.spring93.zaparmor.config.ArmorConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class Armor {
    protected String name;
    protected ArmorConfig armorConfig;
    protected ItemStack helmet;
    protected ItemStack chestplate;
    protected ItemStack leggings;
    protected ItemStack boots;

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

        item.setItemMeta(meta);
        return item;
    }

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

    public abstract void applySpecialEffects();

}
