package Main;

import Scoreboard.timer.Timer;
import SetupWorld.Setup;
import Start.Start;
import WinCond.Win;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import SetupWorld.Setup;
import Scoreboard.scoreboard;

public class main extends JavaPlugin {
    private Set<UUID> readyPlayers = new HashSet<>();
    private boolean gameStarted = false;
    private static main instance;

    @Override
    public void onEnable() {
        Setup setup = new Setup(this);
        Start start = new Start(this);
        Win winInstance = new Win(this);
        Timer timer = new Timer();

        getServer().getPluginManager().registerEvents(setup, this);
        this.getCommand("play").setExecutor(setup);
        this.getCommand("ready").setExecutor(start);

        new BukkitRunnable() {
            @Override
            public void run() {
                winInstance.checkPlayersForDiamondSword();
            }
        }.runTaskTimer(this, 0L, 20L);
        instance = this;
        getServer().getPluginManager().registerEvents(new events.evenements(), this);
        timer.runTaskTimer(main.getInstance(), 0, 20);

    }

    public static main getInstance() {
        return instance;
    }

    public void resetGame() {
        readyPlayers.clear();
        gameStarted = false;
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
