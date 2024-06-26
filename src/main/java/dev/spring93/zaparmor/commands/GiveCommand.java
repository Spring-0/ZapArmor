package dev.spring93.zaparmor.commands;

import dev.spring93.zaparmor.armor.Armor;
import dev.spring93.zaparmor.armor.PatchingArmor;
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
                new SugarCaneArmor(),
                new PatchingArmor()
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
            if(armor.getName().equalsIgnoreCase(armorName) && armor.getConfig().isArmorSetEnabled()) {
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

        MessageManager.sendErrorMessage(sender, "Unknown '" + armorName + "' argument.");
        sender.sendMessage(MessageManager.getArmorList());

        return true;
    }

    /**
     * Method used to give a player a random piece of armor.
     * @param player The player to receive the random piece.
     * @param armor The armor type to give.
     */
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

    /**
     * Method used to give all pieces of an armor type to the player.
     * @param player The player to receive the full armor set.
     * @param armor The armor type to give the player.
     */
    private void giveAllPieces(Player player, Armor armor) {
        player.getInventory().addItem(
                armor.getHelmet(),
                armor.getChestplate(),
                armor.getLeggings(),
                armor.getBoots()
        );
    }

    /**
     * Method used to give a specific piece of an armor to the player.
     * @param sender The user that executed the give command.
     * @param player The user that will receive the armor.
     * @param armor The armor type.
     * @param pieceName The name of the armor piece.
     */
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
                MessageManager.sendMessage(sender, "Unknown param: " + pieceName + ". Try 'helmet', 'chestplate'," +
                        "'leggings', 'boots', 'all', 'random'");
                break;
        }
    }
}
