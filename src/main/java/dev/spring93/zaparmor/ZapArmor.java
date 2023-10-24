package dev.spring93.zaparmor;

import dev.spring93.zaparmor.armor.SugarCaneArmor;
import dev.spring93.zaparmor.commands.BaseCommand;
import dev.spring93.zaparmor.commands.GiveCommand;
import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.config.DefaultConfig;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ZapArmor extends JavaPlugin {

    private static ZapArmor instance;
    private ArmorConfig sugarCaneArmorConfig;
    private DefaultConfig defaultConfig;
    private Map<String, BaseCommand> commands;

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
        commands = new HashMap<>();
        commands.put("give", new GiveCommand());

        this.getCommand("zaparmor").setExecutor((sender, command, label, args) -> {
            if (args.length == 0) {
                MessageManager.sendMessage(sender, "Please specify a sub-command.");
                return true;
            }

            BaseCommand subCommand = commands.get(args[0]);
            if (subCommand == null) {
                MessageManager.sendMessage(sender, "Invalid sub-command.");
                return true;
            }

            return subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        });
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SugarCaneArmor(), this);
    }

    private void initConfigs() {
        defaultConfig = new DefaultConfig();
        sugarCaneArmorConfig = new ArmorConfig("sugar_cane_armor");
    }

    public static ZapArmor getInstance() {
        return instance;
    }

}
