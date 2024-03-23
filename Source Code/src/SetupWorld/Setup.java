package SetupWorld;

import Main.main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Random;
import org.bukkit.block.Biome;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.Potion;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.Bukkit;

public class Setup implements CommandExecutor
{

    private main plugin;

    public Setup(main plugin) {
        this.plugin = plugin;
    }
    public void setupPlayerInSpawn(Player player)
    {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
    }

    public World generateWorld(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande ne peut être exécutée que par un joueur.");
            return null;
        }
        Player player = (Player) sender;
        World newWorld = null;
        boolean acceptableBiome = false;
        long seed;
        WorldCreator worldCreator;
        Biome biomeAtSpawn;

        player.sendTitle(ChatColor.YELLOW + "Loading map...", "");
        while (!acceptableBiome) {
            seed = new Random().nextLong();
            worldCreator = new WorldCreator("world_" + seed);
            worldCreator.seed(seed);
            newWorld = player.getServer().createWorld(worldCreator);
            biomeAtSpawn = newWorld.getBiome(0, 0);
            if (biomeAtSpawn != Biome.OCEAN && biomeAtSpawn != Biome.RIVER && biomeAtSpawn != Biome.DEEP_OCEAN) {
                acceptableBiome = true;
            } else {
                player.getServer().unloadWorld(newWorld, false);
            }
        }
        player.sendTitle("", "");
        newWorld.setSpawnLocation(0, newWorld.getHighestBlockYAt(0, 0) + 1, 0);

        return newWorld;
    }

    public void setDifficulty(Difficulty difficulty)
    {
        for (World world : Bukkit.getWorlds())
            world.setDifficulty(difficulty);
    }

    @SuppressWarnings("deprecation")
    public void buildSpawn(Location spawnLocation) {
        World world = spawnLocation.getWorld();
        int radius = 10;
        int height = 5;
        Random random = new Random();

        Material[] glassTypes = {
                Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS,
                Material.PINK_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS
        };

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = -radius; z <= radius; z++) {
                    boolean isEdge = x == -radius || x == radius || z == -radius || z == radius || y == 0;
                    Location blockLocation = spawnLocation.clone().add(x, y, z);
                    Block block = world.getBlockAt(blockLocation);

                    if (isEdge) {
                        Material selectedGlass = glassTypes[random.nextInt(glassTypes.length)];
                        BlockData glassBlockData = selectedGlass.createBlockData();
                        block.setBlockData(glassBlockData);
                    }
                }
            }
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return false;
        }
        Player player = (Player) sender;
        World newWorld = generateWorld(sender);
        Location spawnLocation = newWorld.getHighestBlockAt(newWorld.getSpawnLocation()).getLocation().add(0, 50, 0);
        Location spawnPlayer = newWorld.getHighestBlockAt(newWorld.getSpawnLocation()).getLocation().add(0, 51, 0);

        newWorld.setPVP(false);
        buildSpawn(spawnLocation);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(spawnPlayer);
            Location playerLocation = player.getLocation();
            setupPlayerInSpawn(p);
        }
        newWorld.setDifficulty(Difficulty.PEACEFUL);
        plugin.setGameStarted(true);
        sender.sendMessage(ChatColor.GREEN + "Le jeu commence! Utilisez /ready quand vous êtes prêts.");
        return true;
    }
}
