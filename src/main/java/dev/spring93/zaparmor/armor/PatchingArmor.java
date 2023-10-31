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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PatchingArmor extends Armor implements Listener {

    public PatchingArmor(){
        super("patching_armor");
    }

    @Override
    protected void onArmorEquipAction(Player player) {
        MessageManager.sendMessage(player, defaultConfig.getIndexString("on-equip-message"));
    }

    @Override
    protected void onArmorDequipAction(Player player) {
        MessageManager.sendMessage(player, defaultConfig.getIndexString("on-un-equip-message"));
    }

    /*
     * Damage Reduction Feature
     */

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!armorConfig.isCustomEffectEnabled("damage-reduction")) return;
        if((event.getEntity() instanceof Player) && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            Player player = (Player) event.getEntity();

            if(!isArmorSetFullyEquipped(player)) return;
            if(!armorConfig.isCustomEffectEnabled("damage-reduction")) return;
            if(!MiscUtils.isPlayerInOwnClaims(player)) return;

            double damageReductionPercentage = armorConfig.getCustomEffectDouble("damage-reduction");
            double originalDamage = event.getDamage();
            double reducedDamage = originalDamage * (1 - damageReductionPercentage);

            event.setDamage(reducedDamage);
            if(armorConfig.getConfig().getBoolean("armor-set.full-set-equipped-effects.custom-effects.damage-reduction.enable-damage-reduced-message"))
                MessageManager.sendMessage(player, "Damage reduced by: " + damageReductionPercentage * 100 + "%");
        }
    }



    /*
     * Quick Stack Feature
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!armorConfig.isCustomEffectEnabled("quick-stack")) return;
        Player player = event.getPlayer();
        if(isArmorSetFullyEquipped(player)){
            quickStack(player, event);
        }
    }

    /**
     * Method used to implement the "quick stack" effect.
     * @param player The player using the quick stack feature.
     * @param event The PlayerInteractEvent
     */
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
