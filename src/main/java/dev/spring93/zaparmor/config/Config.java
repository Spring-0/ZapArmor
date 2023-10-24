package dev.spring93.zaparmor.config;

import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Config {
    protected FileConfiguration config;
    protected File configFile;
    protected String fileName;

    public Config(String fileName) {
        ZapArmor plugin = ZapArmor.getInstance();
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        configFile = new File(dataFolder, fileName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig(CommandSender sender) {
        ZapArmor plugin = ZapArmor.getInstance();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        MessageManager.sendMessage(sender, "has been reloaded.");
    }

    protected String getConfigString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }
}