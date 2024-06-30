package io.wdsj.leavidence.listener;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import io.wdsj.leavidence.util.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.leavesmc.leaves.event.bot.BotActionEvent;

import java.util.Locale;

public class BotListener implements Listener {

    @EventHandler
    public void onBotAction(BotActionEvent event) {
        var bot = event.getBot();
        switch (event.getActionName().toLowerCase(Locale.ROOT)) {
            case "break" -> {
                Block block = bot.getTargetBlockExact(7, FluidCollisionMode.NEVER);
                if (block != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !resPlayer.canBreakBlock(block, false)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            case "use_on", "use_on_offhand" -> {
                Block block = bot.getTargetBlockExact(7, FluidCollisionMode.NEVER);
                if (block != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !resPlayer.canPlaceBlock(block, false)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            case "attack" -> {
                Entity entity = Utils.getTargetedEntity(bot, 7);
                if (entity != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(entity.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !resPlayer.canDamageEntity(entity, false)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
