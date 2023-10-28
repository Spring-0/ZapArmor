package dev.spring93.zaparmor.utils;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.perms.PermissibleAction;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * General Utilities class
 */
public class MiscUtils {

    /**
     * Check for WorldGuard and Factions build permissions on the player and location
     *
     * @param player
     * @param location
     * @return
     */
    public static boolean canPlayerBuild(Player player, Location location) {
        boolean canBuildFactions = FactionsBlockListener.playerCanBuildDestroyBlock(
                player,
                location,
                PermissibleAction.BUILD,
                false
        );
        boolean canBuildWorldGuard = WorldGuardPlugin.inst().canBuild(player, location);

        return canBuildFactions && canBuildWorldGuard;
    }

    /**
     * Method to check if the player is in their own Faction claims.
     *
     * @param player The player to check.
     * @return True if the player is in their own claims, otherwise false.
     */
    public static boolean isPlayerInOwnClaims(Player player) {
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
        return fplayer.isInOwnTerritory();
    }

}
