package SetupWorld;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;

public class Setup extends JavaPlugin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            long seed = new Random().nextLong();
            String worldName = "world_" + seed;
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.seed(seed);
            player.sendMessage("Création d'un nouveau monde");
            World newWorld = player.getServer().createWorld(worldCreator);
            if (newWorld != null) {
                player.teleport(newWorld.getSpawnLocation());
            } else {
                player.sendMessage("Une erreur est survenue lors de la création du monde.");
            }
            return true;
        } else {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return false;
        }
    }
}
