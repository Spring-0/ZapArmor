package dev.spring93.zaparmor.armor;

import dev.spring93.zaparmor.utils.MessageManager;
import dev.spring93.zaparmor.utils.MiscUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PatchingArmor extends Armor implements Listener {

    public PatchingArmor(){
        super("patching_armor");
    }

    @Override
    protected void onArmorEquipAction(Player player) {
        MessageManager.sendMessage(player, "You have equipped the patching set.");
    }

    @Override
    protected void onArmorDequipAction(Player player) {
        MessageManager.sendMessage(player, "You have un-equipped the patching set.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(isArmorSetFullyEquipped(player)){
            quickStack(player, event);
        }
    }

    private void quickStack(Player player, PlayerInteractEvent event) {
        ItemStack item = player.getInventory().getItemInHand();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && item.getType().equals(Material.OBSIDIAN)) {
            Block clickedBlock = event.getClickedBlock();
            BlockFace clickedFace = event.getBlockFace();

            if (!clickedBlock.getType().isSolid())
                return;

            int extraBlocks = armorConfig.getCustomEffectInteger("quick-stack");

            for (int i = 0; i <= extraBlocks; i++) {
                Location newBlockLocation = clickedBlock.getRelative(clickedFace, i).getLocation();

                if(!MiscUtils.canPlayerBuild(player, newBlockLocation)) continue;

                if (newBlockLocation.getBlock().getType() == Material.AIR) {
                    if (item.getAmount() > 0) {
                        newBlockLocation.getBlock().setType(item.getType());
                        if (item.getAmount() == 1) {
                            player.getInventory().removeItem(item);
                            break;
                        } else item.setAmount(item.getAmount() - 1);
                    } else break;
                }
            }
        }
    }
}
