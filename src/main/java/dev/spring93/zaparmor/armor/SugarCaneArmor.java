package dev.spring93.zaparmor.armor;

import dev.spring93.zaparmor.config.ArmorConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SugarCaneArmor extends Armor implements Listener {

    private final ArmorConfig config = this.armorConfig;

    public SugarCaneArmor() {
        super("sugar_cane_armor");
    }


    @Override
    protected void onArmorEquipAction(Player player) {
        player.sendMessage("You have equipped the SugarCane set.");
    }

    @Override
    protected void onArmorDequipAction(Player player) {
        player.sendMessage("You have de-equipped the SugarCane set.");
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(super.isArmorSetFullyEquipped(event.getPlayer())) {
            Block block = event.getBlock();
            breakWithRadius(block, event);
        }
    }

    private void breakWithRadius(Block centerBlock, BlockBreakEvent event) {

        String effectName = "shockwave";

        if (!armorConfig.isEffectEnabled(effectName))
            return;

        int radius = config.getEffectInteger(effectName);
        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int z = bz - radius; z <= bz + radius; z++) {
                Block block = centerBlock.getWorld().getBlockAt(x, by, z);
                if(block.getType() == Material.SUGAR_CANE_BLOCK) {
                    block.breakNaturally();
                }
            }
        }
    }

}
