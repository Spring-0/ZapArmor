package dev.spring93.zaparmor.utils;

import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.config.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    private static DefaultConfig config = new DefaultConfig();

    public static void broadcastMessage(String msg) {
        Bukkit.broadcastMessage(config.getMessagePrefix() + msg);
    }

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(config.getMessagePrefix() + msg);
    }

    public static String getHelpMenu() {
        return "";
    }

    public static String getVersionMessage() {
        return ChatColor.LIGHT_PURPLE + "Plugin Name: " + ChatColor.GREEN + "ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Author: " + ChatColor.GREEN + "Spring93" + "\n" +
                ChatColor.LIGHT_PURPLE + "GitHub: " + ChatColor.GREEN + "https://github.com/Spring-0/ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Spigot: " + ChatColor.GREEN + "https://www.spigotmc.org/" + "\n" +
                ChatColor.LIGHT_PURPLE + "Version: " + ChatColor.GREEN + ZapArmor.getInstance().getDescription().getVersion();
    }

}
