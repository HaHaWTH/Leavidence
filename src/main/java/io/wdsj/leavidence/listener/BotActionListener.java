package io.wdsj.leavidence.listener;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import io.papermc.paper.entity.CollarColorable;
import io.papermc.paper.entity.Shearable;
import io.wdsj.leavidence.Leavidence;
import io.wdsj.leavidence.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.jetbrains.annotations.Nullable;
import org.leavesmc.leaves.event.bot.BotActionExecuteEvent;

import java.util.Locale;

public class BotActionListener implements Listener {

    @EventHandler
    public void onBotAction(BotActionExecuteEvent event) {
        var bot = event.getBot();
        switch (event.getActionName().toLowerCase(Locale.ROOT)) {
            case "break" -> {
                Block block = bot.getTargetBlockExact(5, FluidCollisionMode.NEVER);
                if (block == null) return;
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                if (residence == null) return;
                var resPlayer = getBotOrOwnerResPlayer(event);
                if (resPlayer != null && !resPlayer.canBreakBlock(block, false)) {
                    event.hardCancel();
                }
            }
            case "use_on", "use_on_offhand" -> {
                Block block = bot.getTargetBlockExact(5, FluidCollisionMode.NEVER);
                if (block == null) return;
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(block.getLocation());
                if (residence == null) return;
                ItemStack stackInHand = bot.getInventory().getItemInMainHand().isEmpty() ? bot.getInventory().getItemInOffHand() : bot.getInventory().getItemInMainHand();
                Material itemInHand = stackInHand.getType();
                var resPlayer = getBotOrOwnerResPlayer(event);
                if (resPlayer == null) return;
                if (itemInHand.isBlock() && !resPlayer.canPlaceBlock(block, false)) {
                    event.hardCancel();
                }
                if (!residence.getPermissions().playerHas(resPlayer, Flags.use, false)) {
                    event.hardCancel();
                }
            }
            case "attack" -> {
                Entity entity = Utils.getTargetedEntity(bot, 7);
                if (entity != null) {
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(entity.getLocation());
                    if (residence != null) {
                        var resPlayer = getBotOrOwnerResPlayer(event);
                        if (resPlayer != null && !resPlayer.canDamageEntity(entity, false)) {
                            event.hardCancel();
                        }
                    }
                }
            }
            case "use_to", "use_to_offhand" -> {
                Entity entity = Utils.getTargetedEntity(bot, 3);
                if (entity == null) return;
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(entity.getLocation());
                if (residence == null) return;
                var resPlayer = getBotOrOwnerResPlayer(event);
                if (resPlayer == null) return;
                ItemStack stackInHand = bot.getInventory().getItemInMainHand().isEmpty() ? bot.getInventory().getItemInOffHand() : bot.getInventory().getItemInMainHand();
                Material itemInHand = stackInHand.getType();
                switch (entity) {
                    case Shearable shearable when shearable.readyToBeSheared() && itemInHand == Material.SHEARS && !residence.getPermissions().playerHas(resPlayer, Flags.shear, false) ->
                        event.hardCancel();
                    case Animals animal when animal.isBreedItem(stackInHand) && !residence.getPermissions().playerHas(resPlayer, Flags.animalkilling, false) ->
                        event.hardCancel();
                    case Colorable ignored when isDye(itemInHand) && !residence.getPermissions().playerHas(resPlayer, Flags.dye, false) ->
                        event.hardCancel();
                    case CollarColorable ignored when isDye(itemInHand) && !residence.getPermissions().playerHas(resPlayer, Flags.dye, false) ->
                        event.hardCancel();
                    default -> {
                        if (!residence.getPermissions().playerHas(resPlayer, Flags.use, false)) {
                            event.hardCancel();
                        }
                    }
                }
            }
            case "drop" -> {
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(bot.getLocation());
                if (residence != null) {
                    var resPlayer = getBotOrOwnerResPlayer(event);
                    if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.itemdrop, false)) {
                        event.hardCancel();
                    }
                }
            }
            case "jump", "swim" -> {
                ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(bot.getLocation());
                if (residence != null) {
                    var resPlayer = getBotOrOwnerResPlayer(event);
                    if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.move, false)) {
                        event.hardCancel();
                    }
                }
            }
        }
    }

    private boolean isDye(Material material) {
        return switch (material) {
            case Material.BLACK_DYE, Material.BLUE_DYE, Material.BROWN_DYE,
                 Material.CYAN_DYE, Material.GRAY_DYE, Material.GREEN_DYE,
                 Material.LIGHT_BLUE_DYE, Material.LIGHT_GRAY_DYE, Material.LIME_DYE,
                 Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.PINK_DYE,
                 Material.PURPLE_DYE, Material.RED_DYE, Material.WHITE_DYE,
                 Material.YELLOW_DYE -> true;
            default -> false;
        };
    }

    @Nullable
    ResidencePlayer getBotOrOwnerResPlayer(BotActionExecuteEvent event) {
        var bot = event.getBot();
        return switch (Leavidence.config().check_mode) {
            case BOT -> ResidenceApi.getPlayerManager().getResidencePlayer(bot.getRealName());
            case OWNER -> {
                var creatorUuid = bot.getCreatePlayerUUID();
                var creatorPlayer = creatorUuid == null ? null : Bukkit.getPlayer(creatorUuid);
                yield creatorPlayer == null ? null : ResidenceApi.getPlayerManager().getResidencePlayer(creatorPlayer.getName());
            }
        };
    }
}
