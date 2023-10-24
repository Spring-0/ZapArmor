package dev.spring93.zaparmor;

import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.config.DefaultConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZapArmor extends JavaPlugin {

    private static ZapArmor instance;
    private ArmorConfig sugarCaneArmorConfig;
    private DefaultConfig defaultConfig;

    @Override
    public void onEnable() {
        instance = this;
        initConfigs();
        registerListeners();
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
    }

    private void registerListeners() {
    }

    private void initConfigs() {
        defaultConfig = new DefaultConfig();
        sugarCaneArmorConfig = new ArmorConfig("sugar_cane_armor");
    }

    public static ZapArmor getInstance() {
        return instance;
    }

}
