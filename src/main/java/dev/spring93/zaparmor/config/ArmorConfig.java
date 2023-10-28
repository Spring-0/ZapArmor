package dev.spring93.zaparmor.config;

import org.bukkit.Color;

public class ArmorConfig extends Config{

    public ArmorConfig(String fileName) {
        super(fileName);
    }

    public boolean isArmorSetEnabled() {
        return config.getBoolean("armor-set.enabled");
    }

    /**
     * Method to check if the custom effect is enabled in the configuration.
     * @param effectName The name of the custom effect as a string.
     * @return
     */
    public boolean isCustomEffectEnabled(String effectName) {
        return config.getBoolean("armor-set.full-set-equipped-effects.custom-effects." + effectName + ".enabled");
    }

    /**
     * Method to retrieve the "value" key from the custom effect ConfigSection.
     * @param effectName The name of the effect to retrieve the value for.
     * @return int The configured value.
     */
    public int getCustomEffectInteger(String effectName) {
        return config.getInt("armor-set.full-set-equipped-effects.custom-effects." + effectName + ".value");
    }

    /**
     * Method to retrieve the "value" key from the custom effect ConfigSection.
     * @param effectName The name of the effect to retrieve the value for.
     * @return double The configured value.
     */
    public double getCustomEffectDouble(String effectName) {
        return config.getDouble("armor-set.full-set-equipped-effects.custom-effects." + effectName + ".value");
    }

    /**
     * Method used to retrieve RGB values from the configuration.
     * @param path The path to the configuration key.
     * @return Bukkit.Color object.
     */
    public Color getRGB(String path) {
        String[] rawRGB = config.getString(path).split(",");
        int red = Integer.parseInt(rawRGB[0].trim());
        int green = Integer.parseInt(rawRGB[1].trim());
        int blue = Integer.parseInt(rawRGB[2].trim());
        return Color.fromRGB(red, green, blue);
    }

}
