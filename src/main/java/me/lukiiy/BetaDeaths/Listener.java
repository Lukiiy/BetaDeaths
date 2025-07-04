package me.lukiiy.BetaDeaths;

import me.lukiiy.discordBridge.DiscordBridge;
import me.lukiiy.discordBridge.api.serialize.DSerial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class Listener extends EntityListener {
    public void onEntityDamage(EntityDamageEvent ev) {
        if (!(ev instanceof EntityDamageByEntityEvent) || !(isEntityCacheable(ev.getEntity()))) return;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;

        BetaDeaths.getInstance().setEntityLastDamager(event.getEntity(), event.getDamager());
    }

    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        
        if (!isEntityCacheable(entity)) return;
        if (entity instanceof Tameable && !((Tameable) entity).isTamed()) return;

        BetaDeaths instance = BetaDeaths.getInstance();
        String entityName = formattedEntityName(entity);
        EntityDamageEvent.DamageCause cause = entity.getLastDamageCause().getCause();

        Entity lastDamager = instance.getEntityLastDamager(entity);
        String damagerName = "???";

        if (lastDamager != null) {
            if (lastDamager instanceof Projectile) lastDamager = ((Projectile) lastDamager).getShooter();

            if (lastDamager instanceof LivingEntity) {
                damagerName = formattedEntityName(lastDamager);

                if (lastDamager instanceof Creeper && cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) cause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
            }
        }

        String reason = "default_cause";
        switch (cause) {
            case CONTACT:
                reason = "contact";
                break;
            case ENTITY_ATTACK:
                reason = "attack";
                break;
            case FALL:
                reason = "fall";
                if (entity.getFallDistance() < 5f) reason = "hard_fall";
                break;
            case FIRE:
            case FIRE_TICK:
                if (entity.getWorld().getBlockAt(entity.getLocation()).getType() == Material.FIRE) reason = "fire";
                else reason = "burn";
                break;
            case LAVA:
                reason = "lava";
                break;
            case VOID:
                reason = "void";
                break;
            case SUICIDE:
                reason = "suicide";
                break;
            case DROWNING:
                reason = "drown";
                break;
            case LIGHTNING:
                reason = "lightning";
                break;
            case PROJECTILE:
                reason = "attack_projectile";
                break;
            case SUFFOCATION:
                reason = "suffocation";
                break;
            case BLOCK_EXPLOSION:
                reason = "explosion_block";
                break;
            case ENTITY_EXPLOSION:
                reason = "explosion";
                break;
        }

        Server server = Bukkit.getServer();
        
        String msg = instance.getConfiguration().getString("msgs." + reason, "")
                .replace("(victim)", entityName + "§f")
                .replace("(damager)", damagerName + "§f")
                .replace('&', '§');

        if (msg.isEmpty()) return;

        server.broadcastMessage(msg);
        server.getLogger().info(msg);

        if (BetaDeaths.getInstance().dcBridgeHook) DiscordBridge.getInstance().getContext().sendMessage(DSerial.toDiscord(msg));
    }

    private boolean isEntityCacheable(Entity entity) {
        return entity instanceof Player || entity instanceof Tameable;
    }

    public String formattedEntityName(Entity entity) {
        String className = Utils.getEntityName(entity);

        return entity instanceof Player ? ((Player) entity).getDisplayName() : BetaDeaths.getInstance().getConfiguration().getString("entity." + className.toLowerCase(), className);
    }
}
