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

    public static void sendErrorMessage(CommandSender sender, String msg) {
        sender.sendMessage(config.getMessagePrefix() + ChatColor.RED + msg);
    }

    public static String getHelpMenu() {
        StringBuilder helpMenu = new StringBuilder();
        helpMenu.append(ChatColor.GOLD).append("--- ZapArmor Help Menu ---\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor give <player> <armor_name> <piece_name,random,all>: ")
                .append(ChatColor.GRAY).append("Gives the player specified armor\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor help: ").append(ChatColor.GRAY).append("Displays this help menu\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor list: ").append(ChatColor.GRAY).append("Lists all the current armor\n");
        return helpMenu.toString();
    }

    public static String getVersionMessage() {
        return ChatColor.LIGHT_PURPLE + "Plugin Name: " + ChatColor.GREEN + "ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Author: " + ChatColor.GREEN + "Spring93" + "\n" +
                ChatColor.LIGHT_PURPLE + "GitHub: " + ChatColor.GREEN + "https://github.com/Spring-0/ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Spigot: " + ChatColor.GREEN + "https://www.spigotmc.org/" + "\n" +
                ChatColor.LIGHT_PURPLE + "Version: " + ChatColor.GREEN + ZapArmor.getInstance().getDescription().getVersion();
    }

    public static String getArmorList() {
        StringBuilder armorList = new StringBuilder();
        armorList.append(ChatColor.GOLD).append("--- ZapArmor List ---\n");
        armorList.append(ChatColor.YELLOW).append("sugar_cane_armor\n");
        armorList.append(ChatColor.YELLOW).append("patching_armor\n");
        return armorList.toString();
    }

}
