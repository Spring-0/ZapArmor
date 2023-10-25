package dev.spring93.zaparmor.config;

public class ArmorConfig extends Config{
    private static ArmorConfig instance;

    public ArmorConfig(String fileName) {
        super(fileName);
    }

    public boolean isArmorSetEnabled() {
        return config.getBoolean("armor-set.enabled");
    }

    public boolean isCustomEffectEnabled(String effectName) {
        return config.getBoolean("armor-set.full-set-equipped-effects.custom-effects" + effectName + ".enabled");
    }

    public int getCustomEffectInteger(String effectName) {
        return config.getInt("armor-set.full-set-equipped-effects.custom-effects" + effectName + ".value");
    }
}
