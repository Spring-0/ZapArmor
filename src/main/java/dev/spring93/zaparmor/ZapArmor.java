package dev.spring93.zaparmor;

import dev.spring93.zaparmor.armor.PatchingArmor;
import dev.spring93.zaparmor.armor.SugarCaneArmor;
import dev.spring93.zaparmor.commands.BaseCommand;
import dev.spring93.zaparmor.commands.GiveCommand;
import dev.spring93.zaparmor.commands.HelpCommand;
import dev.spring93.zaparmor.commands.ListCommand;
import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.config.DefaultConfig;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ZapArmor extends JavaPlugin {

    private static ZapArmor instance;
    private ArmorConfig sugarCaneArmorConfig;
    private DefaultConfig defaultConfig;
    private ArmorConfig patchingArmorConfig;
    private Map<String, BaseCommand> commands;
    private static Economy eco;

    @Override
    public void onEnable() {
        instance = this;
        if(!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no vault dependency found!"));
            getServer().getPluginManager().disablePlugin(this);
        }
        initConfigs();
        registerListeners();
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    private void registerCommands() {
        commands = new HashMap<>();
        commands.put("give", new GiveCommand());
        commands.put("help", new HelpCommand());
        commands.put("list", new ListCommand());

        this.getCommand("zaparmor").setExecutor((sender, command, label, args) -> {
            if (args.length == 0) {
                sender.sendMessage(MessageManager.getHelpMenu());
                return true;
            }

            BaseCommand subCommand = commands.get(args[0]);
            if (subCommand == null) {
                sender.sendMessage(MessageManager.getHelpMenu());
                return true;
            }

            return subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        });
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SugarCaneArmor(), this);
        this.getServer().getPluginManager().registerEvents(new PatchingArmor(), this);
    }

    private void initConfigs() {
        defaultConfig = new DefaultConfig();
        sugarCaneArmorConfig = new ArmorConfig("sugar_cane_armor");
        patchingArmorConfig = new ArmorConfig("patching_armor");
    }

    public static ZapArmor getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return eco;
    }

}
