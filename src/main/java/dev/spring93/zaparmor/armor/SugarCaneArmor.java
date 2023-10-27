package dev.spring93.zaparmor.armor;

import com.golfing8.kore.event.HarvesterHoeUseEvent;
import com.golfing8.kore.object.HarvesterHoeMode;
import dev.spring93.zaparmor.ZapArmor;
import dev.spring93.zaparmor.config.ArmorConfig;
import dev.spring93.zaparmor.utils.MessageManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SugarCaneArmor extends Armor implements Listener {

    private final ArmorConfig config = this.armorConfig;
    private Economy eco;

    public SugarCaneArmor() {
        super("sugar_cane_armor");
        eco = ZapArmor.getEconomy();
    }

    @Override
    protected void onArmorEquipAction(Player player) {
        MessageManager.sendMessage(player, "You have equipped the sugar cane set.");
    }

    @Override
    protected void onArmorDequipAction(Player player) {
        MessageManager.sendMessage(player, "You have un-equipped the sugar cane set.");
    }

//    @EventHandler
//    public void onBlockBreak(BlockBreakEvent event) {
//        if(super.isArmorSetFullyEquipped(event.getPlayer())) {
//            breakWithRadius(event.getBlock());
//            Bukkit.broadcastMessage("Nutz");
//        }
//    }

    @EventHandler
    public void onHarvesterHoeUse(HarvesterHoeUseEvent event) {
        Player player = event.getPlayer();

        if(isArmorSetFullyEquipped(player)) {
            int caneCounter = breakWithRadiusReturnAmount(event.getBlock());
            int adjustedCounter = caneCounter + event.getHarvested();
            player.sendMessage("" + adjustedCounter);
            if(event.getHarvesterHoeMode().equals(HarvesterHoeMode.SELL)) {
                eco.depositPlayer(player, event.getExistingMultiplier() * adjustedCounter * 60);
            } else if (event.getHarvesterHoeMode().equals(HarvesterHoeMode.HARVEST)) {
                ItemStack cane = new ItemStack(Material.SUGAR_CANE);
                cane.setAmount(adjustedCounter);
                player.getInventory().addItem(new ItemStack[] {cane});
            }
        }
    }


    private int breakWithRadiusReturnAmount(Block centerBlock) {
        String effectName = "shockwave";
        int caneCounter = 0;

        if (!armorConfig.isCustomEffectEnabled(effectName))
            return caneCounter;

        int radius = config.getCustomEffectInteger(effectName);
        int bx = centerBlock.getX();
        int by = centerBlock.getY();
        int bz = centerBlock.getZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int z = bz - radius; z <= bz + radius; z++) {
                for(int y = by + radius; y >= by; y--) {
                    Block block = centerBlock.getWorld().getBlockAt(x, y, z);
                    if(block.getType() == Material.SUGAR_CANE_BLOCK) {
                        Block blockBelow = centerBlock.getWorld().getBlockAt(x, y-1, z);
                        if(blockBelow.getType() != Material.SUGAR_CANE_BLOCK) {
                            continue;
                        }
                        caneCounter++;
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return caneCounter;
    }
}
