package com.github.jstN0body.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class MinigamePrepCommands implements CommandExecutor {

    public MinigamePrepCommands() {
        Bukkit.getPluginCommand("hideandseek").setExecutor(this);
    }

    /**
     * Spreads all players around the center of the world, sets gamerules and time, clears inventories, sets spawns, and makes everyone glow.
     * @return true if the command is passed successfully.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (args[0].equalsIgnoreCase("prepare")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers 0 0 2500 3000 false @a");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear @a");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set day");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setBedSpawnLocation(player.getLocation(), true);
                player.sendMessage(ChatColor.BLUE + "Your spawn has been set at " +
                        player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ());
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999, 1, true, false, false));
            }
            return true;
        }
        return false;
    }
}
