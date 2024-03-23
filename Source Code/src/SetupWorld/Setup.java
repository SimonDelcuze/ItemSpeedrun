package SetupWorld;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

import org.bukkit.block.Biome;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.bukkit.plugin.java.JavaPlugin;

public class Setup implements CommandExecutor
{

    public void setupPlayer(Player player)
    {
        player.getInventory().clear();

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.getInventory().clear();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = new ItemStack(Material.COOKED_BEEF, 64);
        inventory.addItem(item);
    }

    //Build an invisible box around the player
    public void encasePlayer(Player player, Material material)
    {
        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        playerLocation.setX(playerLocation.getBlockX() + 0.5);
        playerLocation.setY(playerLocation.getBlockY());
        playerLocation.setZ(playerLocation.getBlockZ() + 0.5);
        int radius = 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= 4; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((y == 1 || y == 2 || y == 3) && (x == 0 || x == 1 || x == -1) && (z == 0 || z == 1 || z == -1)) {
                        Location blockLocation = playerLocation.clone().add(x, y, z);
                        world.getBlockAt(blockLocation).setType(Material.AIR);
                        continue;
                    }
                    Location blockLocation = playerLocation.clone().add(x, y, z);
                    world.getBlockAt(blockLocation).setType(material);
                }
            }
        }
    }

    public void showTimer(Player player)
    {
        new BukkitRunnable() {
            int counter = 5;
            public void run() {
                if (counter > 0) {
                    player.sendTitle("", ChatColor.LIGHT_PURPLE + "" + counter);
                    counter--;
                } else {
                    player.sendTitle("", ChatColor.LIGHT_PURPLE + "Go!");
                    cancel();
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("plugin"), 10, 20L);
    }

    public void removeCage(Player player, long delay, JavaPlugin plugin, Location playerLocation) {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = player.getWorld();
                int radius = 2;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = 0; y <= 4; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Location blockLocation = playerLocation.clone().add(x, y, z);
                            world.getBlockAt(blockLocation).setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskLater(plugin, delay * 20L);
    }

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
            newWorld.setPVP(false);
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.teleport(spawnLocation);
                Location playerLocation = player.getLocation();
                setupPlayer(p);
                encasePlayer(p, Material.GLASS);
                playerLocation.setX(playerLocation.getBlockX() + 0.5);
                playerLocation.setY(playerLocation.getBlockY() + 1);
                playerLocation.setZ(playerLocation.getBlockZ() + 0.5);
                p.teleport(playerLocation);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            showTimer(p);
        }
        Location spawnLocation = newWorld.getHighestBlockAt(newWorld.getSpawnLocation()).getLocation().add(0, 1, 0);
        removeCage(player, 6L, (JavaPlugin) Bukkit.getPluginManager().getPlugin("plugin"), spawnLocation);
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        }
        newWorld.setPVP(true);
        seed = newWorld.getSeed();
        World world = Bukkit.getWorld("world_" + seed);
        world.setDifficulty(Difficulty.HARD);
        return true;
    }
}
