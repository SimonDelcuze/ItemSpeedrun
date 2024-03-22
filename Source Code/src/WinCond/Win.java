package WinCond;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class Win {
    public void checkPlayersForDiamondSword() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().contains(Material.DIAMOND_SWORD)) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.getInventory().clear();
                    Location spawnLocation = p.getWorld().getHighestBlockAt(0, 0).getLocation();
                    p.teleport(spawnLocation);
                    p.sendMessage(player.getName() + " has won!");
                }
                break;
            }
        }
    }
}
