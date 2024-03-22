package SetupWorld;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.WorldCreator;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;

public class Setup implements CommandExecutor
{

    //Setup the player's inventory and health
    public void setupPlayer(Player player)
    {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.getInventory().clear();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = new ItemStack(Material.COOKED_BEEF, 64);
        inventory.addItem(item);
    }

    //Build an invisible box around the player
    public void encasePlayer(Player player)
    {
        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        playerLocation.setX(playerLocation.getBlockX() + 0.5);
        playerLocation.setY(playerLocation.getBlockY());
        playerLocation.setZ(playerLocation.getBlockZ() + 0.5);
        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= 3; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((y == 1 || y == 2) && x == 0 && z == 0) {
                        continue;
                    }
                    Location blockLocation = playerLocation.clone().add(x, y, z);
                    world.getBlockAt(blockLocation).setType(Material.BARRIER);
                }
            }
        }
    }

    // Load a new map for the player to play on
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return false;
        }
        Player player = (Player) sender;
        World newWorld = null;
        boolean acceptableBiome = false;
        long seed;
        WorldCreator worldCreator;
        Biome biomeAtSpawn;
        while (!acceptableBiome) {
            seed = new Random().nextLong();
            worldCreator = new WorldCreator("world_" + seed);
            worldCreator.seed(seed);
            newWorld = player.getServer().createWorld(worldCreator);
            biomeAtSpawn = newWorld.getBiome(0, newWorld.getHighestBlockYAt(0, 0));
            if (biomeAtSpawn != Biome.OCEAN && biomeAtSpawn != Biome.RIVER && biomeAtSpawn != Biome.DEEP_OCEAN) {
                acceptableBiome = true;
            } else {
                player.getServer().unloadWorld(newWorld, false);
            }
        }
        if (newWorld != null) {
            Location spawnLocation = newWorld.getHighestBlockAt(newWorld.getSpawnLocation()).getLocation().add(0, 1, 0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.teleport(spawnLocation);
                Location playerLocation = player.getLocation();
                setupPlayer(p);
                encasePlayer(p);
                playerLocation.setX(playerLocation.getBlockX() + 0.5);
                playerLocation.setY(playerLocation.getBlockY() + 1);
                playerLocation.setZ(playerLocation.getBlockZ() + 0.5);
                p.teleport(playerLocation);
            }
        }
        return true;
    }
}
