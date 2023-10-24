package dev.spring93.zaparmor.commands;

import org.bukkit.command.CommandSender;

public class ArmorCommand extends BaseCommand {

    public ArmorCommand(String commandName) {
        super(commandName);
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        return false;
    }
}
