package dev.spring93.zaparmor.config;

import dev.spring93.zaparmor.ZapArmor;

public class ArmorConfig extends Config{
    private static ArmorConfig instance;

    public ArmorConfig(String fileName) {
        super(fileName);
    }

    public boolean isArmorSetEnabled(String armorSetName) {
        return config.getBoolean("armor-sets." + armorSetName + ".enabled");
    }

}
