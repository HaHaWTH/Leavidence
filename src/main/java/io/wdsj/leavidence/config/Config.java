package io.wdsj.leavidence.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import io.wdsj.leavidence.enums.CheckMode;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Config {

    private final ConfigFile config;
    private final Plugin plugin;
    public final CheckMode check_mode;
    public final boolean check_creation;
    public final String create_failed_message;
    public Config(Plugin plugin, File dataFolder) throws Exception {
        this.plugin = plugin;
        // Load config.yml with ConfigMaster
        this.config = ConfigFile.loadConfig(new File(dataFolder, "config.yml"));
        config.set("plugin-version", "1.1");

        // Pre-structure to force order
        structureConfig();

        this.check_mode = CheckMode.fromString(getString("plugin.check-mode", CheckMode.BOT.toString(),
                """
                        Which entity permission should be checked when bot executing actions.
                        Available options: BOT, OWNER"""));
        this.check_creation = getBoolean("plugin.check-creation", true,
                """
                        Whether to check for permission when creating a bot.
                        If true, the bot will not be created if the player does not have permission to create it.
                        If false, the bot will be created even if the player does not have permission to create it.""");

        this.create_failed_message = getString("plugin.create-failed-message", "<red>You need move flag to create a bot in this residence!</red>",
                """
                        The message that will be sent to the player when the bot creation fails.""");
    }

    public void saveConfig() {
        try {
            config.save();
        } catch (Exception e) {
            this.plugin.getLogger().severe("Failed to save config file: " + e.getMessage());
        }
    }

    private void structureConfig() {
        createTitledSection("Plugin general setting", "plugin");
    }

    public void createTitledSection(String title, String path) {
        config.addSection(title);
        config.addDefault(path, null);
    }

    public boolean getBoolean(String path, boolean def, String comment) {
        config.addDefault(path, def, comment);
        return config.getBoolean(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, def);
    }

    public String getString(String path, String def, String comment) {
        config.addDefault(path, def, comment);
        return config.getString(path, def);
    }

    public String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    public double getDouble(String path, double def, String comment) {
        config.addDefault(path, def, comment);
        return config.getDouble(path, def);
    }

    public double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, def);
    }

    public int getInt(String path, int def, String comment) {
        config.addDefault(path, def, comment);
        return config.getInteger(path, def);
    }

    public int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInteger(path, def);
    }

    public long getLong(String path, long def, String comment) {
        config.addDefault(path, def, comment);
        return config.getLong(path, def);
    }

    public long getLong(String path, long def) {
        config.addDefault(path, def);
        return config.getLong(path, def);
    }

    public List<String> getList(String path, List<String> def, String comment) {
        config.addDefault(path, def, comment);
        return config.getStringList(path);
    }

    public List<String> getList(String path, List<String> def) {
        config.addDefault(path, def);
        return config.getStringList(path);
    }

    public ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue) {
        config.addDefault(path, null);
        config.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> config.addExample(path+"."+string, object));
        return config.getConfigSection(path);
    }

    public ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue, String comment) {
        config.addDefault(path, null, comment);
        config.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> config.addExample(path+"."+string, object));
        return config.getConfigSection(path);
    }

    public void addComment(String path, String comment) {
        config.addComment(path, comment);
    }
}
