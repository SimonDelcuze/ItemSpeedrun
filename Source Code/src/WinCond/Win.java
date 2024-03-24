package WinCond;

import SetupWorld.Setup;
import Start.Start;
import org.bukkit.*;
import Scoreboard.timer.Timer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import static Scoreboard.timer.Timer.sec;
import static Scoreboard.timer.Timer.min;

import java.util.Set;


public class Win {
    public void checkPlayersForDiamondSword() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().contains(Setup.itemID)) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.getInventory().clear();
                    Location spawnLocation = p.getWorld().getHighestBlockAt(0, 0).getLocation();
                    p.teleport(spawnLocation);
                    p.sendTitle(ChatColor.YELLOW + "The item has been found", "");
                    p.sendMessage("§c§l" + player.getName() + " has won in " + min + "m " + sec + "s");
                }
                break;
            }
        }
    }
}
