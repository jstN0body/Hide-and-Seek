package com.github.jstN0body.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {

    public PlayerDeathListener(HideAndSeekPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Spawns a chest when a player dies and places all their compasses inside. Sets player to spectator mode.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().getLocation().getBlock().setType(Material.CHEST);
        Chest chest = (Chest) event.getEntity().getLocation().getBlock().getState();
        List<ItemStack> compasses = new ArrayList<>();
        for (ItemStack item : event.getDrops()) {
            if (item.getType() == Material.COMPASS) {
                compasses.add(item);
            }
        }
        chest.getInventory().addItem(compasses.toArray(new ItemStack[0]));
        event.getDrops().removeAll(compasses);
        event.getEntity().setGameMode(GameMode.SPECTATOR);
    }
}
