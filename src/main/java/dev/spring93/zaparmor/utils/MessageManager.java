package dev.spring93.zaparmor.utils;

import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.config.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Message Utility class containing helper methods to send broadcast & player messages.
 */
public class MessageManager {

    private static DefaultConfig config = new DefaultConfig();

    /**
     * Broadcast a message with the configured message prefix.
     * @param msg
     */
    public static void broadcastMessage(String msg) {
        Bukkit.broadcastMessage(config.getMessagePrefix() + msg);
    }

    /**
     * Send a message to the player with the configured message prefix.
     * @param player The player to send the message to.
     * @param msg
     */
    public static void sendMessage(CommandSender player, String msg) {
        player.sendMessage(config.getMessagePrefix() + msg);
    }

    /**
     * Send a red error message to the player with the configured message prefix.
     * @param player
     * @param msg
     */
    public static void sendErrorMessage(CommandSender player, String msg) {
        player.sendMessage(config.getMessagePrefix() + ChatColor.RED + msg);
    }

    /**
     * Helper method used to return the help menu.
     * @return Help menu as string
     */
    public static String getHelpMenu() {
        StringBuilder helpMenu = new StringBuilder();
        helpMenu.append(ChatColor.GOLD).append("--- ZapArmor Help Menu ---\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor give <player> <armor_name> <piece_name,random,all>: ")
                .append(ChatColor.GRAY).append("Gives the player specified armor\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor help: ").append(ChatColor.GRAY).append("Displays this help menu\n");
        helpMenu.append(ChatColor.YELLOW).append("/zapArmor list: ").append(ChatColor.GRAY).append("Lists all the current armor\n");
        return helpMenu.toString();
    }

    /**
     * Helper method used to return the version menu.
     * @return Version menu as a string.
     */
    public static String getVersionMessage() {
        return ChatColor.LIGHT_PURPLE + "Plugin Name: " + ChatColor.GREEN + "ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Author: " + ChatColor.GREEN + "Spring93" + "\n" +
                ChatColor.LIGHT_PURPLE + "GitHub: " + ChatColor.GREEN + "https://github.com/Spring-0/ZapArmor" + "\n" +
                ChatColor.LIGHT_PURPLE + "Spigot: " + ChatColor.GREEN + "https://www.spigotmc.org/" + "\n" +
                ChatColor.LIGHT_PURPLE + "Version: " + ChatColor.GREEN + ZapArmor.getInstance().getDescription().getVersion();
    }

    /**
     * Helper method used to return a list of the available armors.
     * @return List of armors as a string.
     */
    public static String getArmorList() {
        StringBuilder armorList = new StringBuilder();
        armorList.append(ChatColor.GOLD).append("--- ZapArmor List ---\n");
        armorList.append(ChatColor.YELLOW).append("sugar_cane_armor\n");
        armorList.append(ChatColor.YELLOW).append("patching_armor\n");
        return armorList.toString();
    }

}
