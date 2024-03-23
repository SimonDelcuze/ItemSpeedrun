package Main;

import SetupWorld.Setup;
import Start.Start;
import WinCond.Win;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;

public class main extends JavaPlugin {
    private Set<UUID> readyPlayers = new HashSet<>();
    private boolean gameStarted = false;

    @Override
    public void onEnable() {
        Setup setup = new Setup(this);
        Start start = new Start(this);
        Win winInstance = new Win();

        this.getCommand("play").setExecutor(setup);
        this.getCommand("ready").setExecutor(start);

        new BukkitRunnable() {
            @Override
            public void run() {
                winInstance.checkPlayersForDiamondSword();
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    public void setPlayerReady(Player player) {
        readyPlayers.add(player.getUniqueId());
    }

    public boolean isPlayerReady(Player player) {
        return readyPlayers.contains(player.getUniqueId());
    }

    public int getReadyCount() {
        return readyPlayers.size();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }

    @Override
    public void onDisable() {
    }
}
