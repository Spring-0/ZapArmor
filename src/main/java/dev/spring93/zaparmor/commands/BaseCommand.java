package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand implements CommandExecutor {

    protected ZapArmor plugin;
    protected String commandName;
    protected int minArgs;
    protected int maxArgs;

    public BaseCommand(String commandName, int minArgs, int maxArgs) {
        this.plugin = ZapArmor.getInstance();
        this.commandName = commandName;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < minArgs || args.length > maxArgs) {
            MessageManager.sendMessage(sender, "Invalid number of arguments.");
            return true;
        }

        return execute(sender, args);
    }

    protected abstract boolean execute(CommandSender sender, String[] args);
}

