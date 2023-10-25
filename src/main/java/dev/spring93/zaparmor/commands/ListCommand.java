package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.command.CommandSender;

public class ListCommand extends BaseCommand{

    public ListCommand() {
        super("list", 0, 0);
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(MessageManager.getArmorList());
        return true;
    }
}
