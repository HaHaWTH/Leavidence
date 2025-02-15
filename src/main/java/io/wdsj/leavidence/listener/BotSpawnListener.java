package io.wdsj.leavidence.listener;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import io.wdsj.leavidence.Leavidence;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.leavesmc.leaves.event.bot.BotCreateEvent;

public class BotSpawnListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBotCreate(BotCreateEvent event) {
        if (!Leavidence.config().check_creation) return;
        if (!(event.getCreator() instanceof Player player)) return;
        var spawnLoc = event.getCreateLocation();
        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(spawnLoc);
        if (residence == null) return;
        var resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName());
        if (resPlayer != null && !residence.getPermissions().playerHas(resPlayer, Flags.move, false)) {
            event.setCancelled(true);
            player.sendMessage(MiniMessage.miniMessage().deserialize(Leavidence.config().create_failed_message));
        }
    }
}
