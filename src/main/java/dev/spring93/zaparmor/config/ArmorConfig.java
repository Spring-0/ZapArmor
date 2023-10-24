package dev.spring93.zaparmor.config;

import dev.spring93.zaparmor.ZapArmor;

public class ArmorConfig extends Config{
    private static ArmorConfig instance;

    public ArmorConfig(String fileName) {
        super(fileName);
    }

    public boolean isArmorSetEnabled() {
        return config.getBoolean("armor-set.enabled");
    }

    public boolean isEffectEnabled(String effectName) {
        return config.getBoolean("armor-set.full-set-equipped-effects." + effectName + ".enabled");
    }

    public int getEffectInteger(String effectName) {
        return config.getInt("armor-set.full-set-equipped-effects." + effectName + ".value");
    }

}
