package me.lukiiy.BetaDeaths;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class BetaDeaths extends JavaPlugin {
    private static BetaDeaths instance;

    public Configuration config;
    public boolean dcBridgeHook;

    private Map<Entity, Entity> lastDamager;

    @Override
    public void onEnable() {
        instance = this;
        setupConfig();

        lastDamager = new HashMap<>();
        PluginManager pl = getServer().getPluginManager();

        pl.registerEvent(Event.Type.ENTITY_DAMAGE, new Listener(), Event.Priority.Lowest, this);
        pl.registerEvent(Event.Type.ENTITY_DEATH, new Listener(), Event.Priority.Normal, this);

        getCommand("betadeaths").setExecutor(new ReloadCMD());

        if (Utils.getMCVersion(getServer().getVersion()) > 173) {
            getServer().getLogger().warning("This plugin will be disabled due to death messages being added in b1.8.");
            pl.disablePlugin(this);
            return;
        }

        dcBridgeHook = getConfiguration().getBoolean("hooks.dcBridge", false) && pl.isPluginEnabled("DiscordBridge");
    }

    @Override
    public void onDisable() {}

    public static BetaDeaths getInstance() {
        return instance;
    }

    // Config
    public void setupConfig() {
        config = getConfiguration();
        config.load();

        String m = "msgs."; // Death Messages
        config.getString(m + "contact", "(victim) was pricked to death");
        config.getString(m + "attack", "(victim) was slain by (damager)");
        config.getString(m + "attack_projectile", "(victim) was shot by (damager)");
        config.getString(m + "fall", "(victim) fell from a high place");
        config.getString(m + "hard_fall", "(victim) hit the ground too hard");
        config.getString(m + "fire", "(victim) went up in flames");
        config.getString(m + "burn", "(victim) burned to death");
        config.getString(m + "lava", "(victim) tried to swim in lava");
        config.getString(m + "void", "(victim) fell out of the world");
        config.getString(m + "suicide", "(victim) was killed");
        config.getString(m + "drown", "(victim) drowned");
        config.getString(m + "lightning", "(victim) was struck by lightning");
        config.getString(m + "suffocation", "(victim) suffocated in a wall");
        config.getString(m + "explosion", "(victim) was blown up by (damager)");
        config.getString(m + "explosion_block", "(victim) blew up");
        config.getString(m + "void", "(victim) fell out of the world");
        config.getString(m + "default_cause", "(victim) died");

        String e = "entity."; // Entity names
        config.getString(e + "chicken", "Chicken");
        config.getString(e + "cow", "Cow");
        config.getString(e + "creeper", "Creeper");
        config.getString(e + "ghast", "Ghast");
        config.getString(e + "giant", "Giant");
        config.getString(e + "monster", "Monster");
        config.getString(e + "pig", "Pig");
        config.getString(e + "pigzombie", "PigZombie");
        config.getString(e + "sheep", "Chicken");
        config.getString(e + "skeleton", "Skeleton");
        config.getString(e + "slime", "Slime");
        config.getString(e + "spider", "Spider");
        config.getString(e + "squid", "Squid");
        config.getString(e + "zombie", "Zombie");
        config.getString(e + "wolf", "Wolf");

        // Booleans
        config.getBoolean("broadcastTamedMobsDeaths", true);
        config.getBoolean("onlyPlayers", false);
        config.getBoolean("hooks.dcBridge", true);

        config.save();
    }
    
    // Entity Damage By Entity Cache
    public Entity getEntityLastDamager(Entity entity) {
        return lastDamager.get(entity);
    }

    public void setEntityLastDamager(Entity entity, Entity damager) {
        lastDamager.put(entity, damager);
    }
}