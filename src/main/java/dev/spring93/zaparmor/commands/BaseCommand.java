package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.config.DefaultConfig;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand implements CommandExecutor {

    private String commandName;
    private final DefaultConfig config = new DefaultConfig();

    public BaseCommand(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(commandName)) {
            if (args.length < getMinArgs() || args.length > getMaxArgs()) {
                MessageManager.sendMessage(sender, config.getInvalidArgsNumberMessage());
                return false;
            }
            return execute(sender, args);
        }
        return false;
    }

    protected abstract boolean execute(CommandSender sender, String[] args);

    protected int getMinArgs() {
        return 0;
    }

    protected int getMaxArgs() {
        return Integer.MAX_VALUE;
    }
}

