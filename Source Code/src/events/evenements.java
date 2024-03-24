package events;

import Scoreboard.timer.Timer;
import Main.main;
import org.bukkit.event.Listener;
import Scoreboard.scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import static Scoreboard.timer.Timer.sec;
import static Scoreboard.timer.Timer.min;

public class evenements implements Listener{

    @EventHandler
    public void Join(PlayerJoinEvent event){
        sec = 0;
        min = 0;
        Player player = event.getPlayer();
        scoreboard.SetScoreboard(player);
    }
}
