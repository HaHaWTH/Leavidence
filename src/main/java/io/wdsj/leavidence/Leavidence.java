package io.wdsj.leavidence;

import io.wdsj.leavidence.config.Config;
import io.wdsj.leavidence.listener.BotActionListener;
import io.wdsj.leavidence.listener.BotSpawnListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

public final class Leavidence extends JavaPlugin {
    private static Logger logger;
    private File dataFolder;
    private static Config config;
    @Override
    public void onEnable() {
        dataFolder = getDataFolder();
        logger = getSLF4JLogger();
        reloadConfiguration();
        Metrics metrics = new Metrics(this, 21521);
        getServer().getPluginManager().registerEvents(new BotActionListener(), this);
        getServer().getPluginManager().registerEvents(new BotSpawnListener(), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        logger.info("Leavidence is disabled.");
    }

    public static Config config() {
        return config;
    }

    public static Logger logger() {
        return logger;
    }

    public void createDirectory(File dir) throws IOException {
        try {
            Files.createDirectories(dir.toPath());
        } catch (FileAlreadyExistsException e) { // Thrown if dir exists but is not a directory
            if (dir.delete()) createDirectory(dir);
        }
    }

    public void reloadConfiguration() {
        try {
            createDirectory(dataFolder);
            config = new Config(this, dataFolder);
            config.saveConfig();
        } catch (Throwable t) {
            logger.error("Failed while loading config!", t);
        }
    }
}
