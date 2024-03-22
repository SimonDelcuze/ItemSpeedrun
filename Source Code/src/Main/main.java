package Main;

import SetupWorld.Setup;
import WinCond.Win;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("play").setExecutor(new Setup());
        Win winInstance = new Win();
        new BukkitRunnable() {
            @Override
            public void run() {
                winInstance.checkPlayersForDiamondSword();
            }
        }.runTaskTimer(this, 0L, 20L);
    }
    @Override
    public void onDisable() {
    }
}