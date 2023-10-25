package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.command.CommandSender;

public class HelpCommand extends BaseCommand{

    public HelpCommand() {
        super("help", 0, Integer.MAX_VALUE);
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(MessageManager.getHelpMenu());
        return true;
    }
}
