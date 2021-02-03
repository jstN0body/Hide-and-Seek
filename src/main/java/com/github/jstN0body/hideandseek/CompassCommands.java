package com.github.jstN0body.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommands implements CommandExecutor {

    private final HideAndSeekPlugin plugin;

    /**
     * Registers the /compass command, runs the code in the onCommand method.
     */
    public CompassCommands(HideAndSeekPlugin plugin) {
        Bukkit.getPluginCommand("compass").setExecutor(this);

        this.plugin = plugin;
    }

    /***
     * The code that is run when the /compass command is called in-game or from the console.
     * @return true if the command was passed successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("give") && args.length == 3) {
            Player hunter = Bukkit.getPlayer(args[1]);
            Player hider = Bukkit.getPlayer(args[2]);
            try {
                TrackingCompass compass = new TrackingCompass(hunter, hider);
                compass.registerListener(plugin);
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Are you entering valid player names?");
            }
            return true;
        } else if (subCommand.equalsIgnoreCase("update") && args.length == 2) {
            long delay = Long.parseLong(args[1]);

            Bukkit.getScheduler().cancelTasks(plugin);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                for (TrackingCompass compass : TrackingCompass.COMPASSES) {
                    compass.update();
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "Compasses Updated!");
            }, 0, delay);
            Bukkit.broadcastMessage(ChatColor.GREEN + "Compass update delay set to " + (delay / 20) + " seconds.");
            return true;
        }
        return false;
    }
}
