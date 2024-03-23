package Scoreboard.timer;

import SetupWorld.Setup;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import Scoreboard.scoreboard;
import Main.main;
import org.bukkit.plugin.java.JavaPlugin;
public class Timer extends BukkitRunnable {

    public static int sec = 0;
    public static int min = 0;
    public static String timer;
    @Override
    public void run() {
        if (main.getInstance().getReadyCount() == Bukkit.getOnlinePlayers().size()) {
            sec++;
            if (sec == 60) {
                sec = 0;
                min++;
            }
        }
        timer = min + "m " + sec + "s";
        for (Player players : Bukkit.getOnlinePlayers()) {
            scoreboard.updatetimer(players);
        }
    }

}
