package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.armor.Armor;
import dev.spring93.zaparmor.armor.SugarCaneArmor;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GiveCommand extends BaseCommand {

    private Armor[] armors;

    public GiveCommand() {
        super("give", 3, 3);
        this.armors = new Armor[] {
                new SugarCaneArmor()
        };
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            MessageManager.sendMessage(sender, "Player " + args[0] + " not found.");
            return true;
        }

        String armorName = args[1];
        String pieceName = args[2];

        for(Armor armor : armors) {
            if(armor.getName().equalsIgnoreCase(armorName)) {
                if(pieceName.equalsIgnoreCase("random")) {
                    giveRandomPiece(target, armor);
                } else if(pieceName.equalsIgnoreCase("all")) {
                    giveAllPieces(target, armor);
                } else {
                    giveSpecificPiece(sender, target, armor, pieceName);
                }
                return true;
            }
        }

        MessageManager.sendMessage(sender, "Invalid armor set or piece.");
        return true;
    }

    private void giveRandomPiece(Player player, Armor armor) {
        ItemStack[] pieces = new ItemStack[] {
                armor.getHelmet(),
                armor.getChestplate(),
                armor.getLeggings(),
                armor.getBoots(),
        };

        ItemStack randomPiece = pieces[new Random().nextInt(pieces.length)];
        player.getInventory().addItem(randomPiece);
    }

    private void giveAllPieces(Player player, Armor armor) {
        player.getInventory().addItem(
                armor.getHelmet(),
                armor.getChestplate(),
                armor.getLeggings(),
                armor.getBoots()
        );
    }

    private void giveSpecificPiece(CommandSender sender, Player player, Armor armor, String pieceName) {

        switch(pieceName.toLowerCase()) {
            case "helmet":
                player.getInventory().addItem(armor.getHelmet());
                break;
            case "chestplate":
                player.getInventory().addItem(armor.getChestplate());
                break;
            case "leggings":
                player.getInventory().addItem(armor.getLeggings());
                break;
            case "boots":
                player.getInventory().addItem(armor.getBoots());
                break;
            default:
                MessageManager.sendMessage(sender, "Invalid piece name.");
                break;
        }
    }
}
