package events;

import Scoreboard.timer.Timer;
import Main.main;
import org.bukkit.event.Listener;
import Scoreboard.scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class evenements implements Listener{

    @EventHandler
    public void Join(PlayerJoinEvent event){
        Player player = event.getPlayer();
        scoreboard.SetScoreboard(player);
    }
}
