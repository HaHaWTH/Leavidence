package io.wdsj.leavidence.listener;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import io.wdsj.leavidence.util.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.leavesmc.leaves.event.bot.BotActionExecuteEvent;

import java.util.Locale;

public class BotActionListener implements Listener {

    @EventHandler
    public void onBotAction(BotActionExecuteEvent event) {
        var bot = event.getBot();
        switch (event.getActionName().toLowerCase(Locale.ROOT)) {
            case "break" -> {
                Block block = bot.getTargetBlockExact(5, FluidCollisionMode.NEVER);
                if (block != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !resPlayer.canBreakBlock(block, false)) {
                            event.hardCancel();
                        }
                    }
                }
            }
            case "use_on", "use_on_offhand" -> {
                Block block = bot.getTargetBlockExact(5, FluidCollisionMode.NEVER);
                if (block != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !resPlayer.canPlaceBlock(block, false)) {
                            event.hardCancel();
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
                            event.hardCancel();
                        }
                    }
                }
            }
            case "use_to", "use_to_offhand" -> {
                Entity entity = Utils.getTargetedEntity(bot, 3);
                if (entity != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(entity.getLocation());
                    if (residence != null) {
                        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                        if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.animals, false)) {
                            event.hardCancel();
                        }
                    }
                }
            }
            case "drop" -> {
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(bot.getLocation());
                if (residence != null) {
                    var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                    if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.itemdrop, false)) {
                        event.hardCancel();
                    }
                }
            }
            case "jump", "swim" -> {
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(bot.getLocation());
                if (residence != null) {
                    var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
                    if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.move, false)) {
                        event.hardCancel();
                    }
                }
            }
        }
    }
}
