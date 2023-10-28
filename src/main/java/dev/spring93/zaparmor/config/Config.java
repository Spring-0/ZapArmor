package dev.spring93.zaparmor.config;

import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public abstract class Config {
    protected FileConfiguration config;
    protected File configFile;
    protected String fileName;

    public Config(String fileName) {
        this.fileName = fileName;
        ZapArmor plugin = ZapArmor.getInstance();
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        configFile = new File(dataFolder, fileName + ".yml");
        if (!configFile.exists()) {
            plugin.saveResource(fileName + ".yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Method used to reload the config
     * @param sender The user that requested to reload the config.
     */
    public void reloadConfig(CommandSender sender) {
        ZapArmor plugin = ZapArmor.getInstance();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        MessageManager.sendMessage(sender, "has been reloaded.");
    }

    /**
     * Method used to retrieve a string from the config translating color codes to ampersand.
     * @param path
     * @return String
     */
    protected String getConfigString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    /**
     * Method used to retrieve a List of strings from the config, translating color codes to ampersand.
     * @param path
     * @return List<String>
     */
    public List<String> getConfigStringList(String path) {
        List<String> lore = config.getStringList(path);
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.set(i, line);
        }
        return lore;
    }

    public FileConfiguration getConfig() {
        return config;
    }

}