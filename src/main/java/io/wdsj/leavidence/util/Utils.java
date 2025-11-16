package io.wdsj.leavidence.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class Utils {
    private Utils() {
    }

    public static Entity getTargetedEntity(Player player, double maxDistance) {
        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getLocation().getDirection(),
                maxDistance,
                entity -> entity instanceof LivingEntity
        );
        if (rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
            return rayTraceResult.getHitEntity();
        }
        return null;
    }

    public static double getBlockInteractionDistance(Player player) {
        return player.getGameMode() == GameMode.CREATIVE ? 5.0D : 4.5D;
    }
    public static double getEntityInteractionDistance(Player player) {
        return player.getGameMode() == GameMode.CREATIVE ? 5.0D : 3.0D;
    }
}
