package dev.spring93.zaparmor.armor;

import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.utils.MessageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class SugarCaneArmor extends Armor implements Listener {

    private final ArmorConfig config = this.armorConfig;

    public SugarCaneArmor() {
        super("sugar_cane_armor");
    }


    @Override
    protected void onArmorEquipAction(Player player) {
        MessageManager.sendMessage(player, "You have equipped the sugar cane set.");
    }

    @Override
    protected void onArmorDequipAction(Player player) {
        MessageManager.sendMessage(player, "You have un-equipped the sugar cane set.");
    }

    @Override
    protected void applyPotionEffects(Player player) {
        List<PotionEffect> potionEffectsList = getPotionEffects();
        for(PotionEffect potionEffect : potionEffectsList) {
            player.removePotionEffect(potionEffect.getType());
            player.addPotionEffect(potionEffect);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(super.isArmorSetFullyEquipped(event.getPlayer())) {
            Block block = event.getBlock();
            breakWithRadius(block);
        }
    }

    private void breakWithRadius(Block centerBlock) {
        String effectName = "shockwave";

        if (!armorConfig.isCustomEffectEnabled(effectName))
            return;

        int radius = config.getCustomEffectInteger(effectName);
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
