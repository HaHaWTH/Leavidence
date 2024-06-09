package io.wdsj.leavidence;

import io.wdsj.leavidence.listener.BotListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Leavidence extends JavaPlugin {
    private static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        Metrics metrics = new Metrics(this, 21521);
        getServer().getPluginManager().registerEvents(new BotListener(), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        logger.info("Leavidence is disabled.");
    }
}
