package com.github.jstN0body.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrackingCompass implements Listener {

    public static final List<TrackingCompass> COMPASSES = new ArrayList<>();

    private final ItemStack compassItem = new ItemStack(Material.COMPASS);
    private final CompassMeta meta = (CompassMeta) compassItem.getItemMeta();

    private Player holder;
    private final Player trackedPlayer;

    /**
     * @param holder the player to give the compass to.
     * @param trackedPlayer the player that the compass to track.
     */
    public TrackingCompass(@NotNull Player holder, @NotNull Player trackedPlayer) {
        this.holder = holder;
        this.trackedPlayer = trackedPlayer;

        meta.setLodestoneTracked(false);
        meta.setLodestone(this.trackedPlayer.getLocation());
        meta.setDisplayName(ChatColor.RED + this.trackedPlayer.getName());
        compassItem.setItemMeta(meta);

        holder.getInventory().addItem(compassItem);

        TrackingCompass.COMPASSES.add(this);
    }

    /**
     * Registers this class as a listener for this plugin.
     * @param plugin instance of JavaPlugin, the main class extends this
     */
    public void registerListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Sets the compass to track the death location of the tracked player when they die.
     * @param event (automatically filled by the event handler)
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getUniqueId().equals(trackedPlayer.getUniqueId())) {
            TrackingCompass.COMPASSES.remove(this);
            updateHolder();
            int index = holder.getInventory().first(compassItem);
            meta.setLodestone(event.getEntity().getLocation());
            compassItem.setItemMeta(meta);
            holder.getInventory().setItem(index, compassItem);
            holder.sendMessage(ChatColor.LIGHT_PURPLE + "Your target, " + trackedPlayer.getName() + ", has died. Now tracking their grave.");
        }
    }

    /**
     * Locates the holder of this tracking compass.
     * @return true if the holder was found.
     */
    private boolean updateHolder() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().contains(compassItem)) {
                holder = player;
                return true;
            }
        }
        holder = null;
        return false;
    }

    /**
     * Updates the location that the compass is tracking.
     */
    public void update() {
        if (updateHolder()) {
            int index = holder.getInventory().first(compassItem);
            meta.setLodestone(this.trackedPlayer.getLocation());
            compassItem.setItemMeta(meta);
            holder.getInventory().setItem(index, compassItem);
            return;
        }
        System.out.println("Holder of " + this.toString() + " is null");
    }

    @Override
    public String toString() {
        return "Compass tracking: " + trackedPlayer.getName();
    }
}
