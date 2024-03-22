package Main;

import SetupWorld.Setup;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("play").setExecutor(this);
    }
    @Override
    public void onDisable() {
    }
}