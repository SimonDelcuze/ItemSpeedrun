package SetupWorld;

import Main.main;
import Scoreboard.timer.Timer;
import Scoreboard.scoreboard;
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
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import static Scoreboard.timer.Timer.sec;
import static Scoreboard.timer.Timer.min;

public class Setup implements CommandExecutor
{

    private main plugin;
    public static Material itemID = Material.DIAMOND;

    private Material[] items = {
            Material.DIAMOND, Material.GOLD_INGOT, Material.IRON_INGOT,
            Material.EMERALD, Material.COAL, Material.DIAMOND_SWORD, Material.FERMENTED_SPIDER_EYE, Material.GOLDEN_APPLE, Material.GOLDEN_BOOTS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET, Material.GOLDEN_LEGGINGS, Material.GOLDEN_SHOVEL, Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_HORSE_ARMOR, Material.GOLDEN_CARROT, Material.GOLDEN_HELMET, Material.GOLDEN_LEGGINGS, Material.GOLDEN_SHOVEL, Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_HORSE_ARMOR, Material.GOLDEN_CARROT, Material.BROWN_MUSHROOM, Material.COBBLESTONE_STAIRS, Material.DIRT, Material.MELON, Material.ENDER_CHEST, Material.JUKEBOX, Material.COOKIE, Material.ACACIA_DOOR,
            Material.GRAVEL, Material.PAINTING, Material.LAPIS_ORE, Material.POISONOUS_POTATO, Material.TNT, Material.TORCH, Material.PAPER, Material.HOPPER, Material.BLAZE_POWDER, Material.BLAZE_ROD, Material.NETHER_STAR, Material.NETHER_WART, Material.NETHER_BRICK, Material.NETHER_BRICK, Material.NETHER_BRICK_SLAB, Material.EMERALD_BLOCK
    };

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
            if (biomeAtSpawn != Biome.OCEAN && biomeAtSpawn != Biome.RIVER && biomeAtSpawn != Biome.DEEP_OCEAN && biomeAtSpawn != Biome.COLD_OCEAN
                    && biomeAtSpawn != Biome.FROZEN_OCEAN && biomeAtSpawn != Biome.LUKEWARM_OCEAN && biomeAtSpawn != Biome.WARM_OCEAN && biomeAtSpawn != Biome.DEEP_WARM_OCEAN
                    && biomeAtSpawn != Biome.DEEP_LUKEWARM_OCEAN && biomeAtSpawn != Biome.DEEP_COLD_OCEAN && biomeAtSpawn != Biome.DEEP_FROZEN_OCEAN) {
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
        sec = 0;
        min = 0;
        plugin.resetGame();
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return false;
        }
        Player player = (Player) sender;
        World newWorld = generateWorld(sender);
        Random random = new Random();
        itemID = items[random.nextInt(items.length)];
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("Trouvez un " + itemID.toString() + " pour gagner!");
            org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
            board.getTeam("item").setSuffix(" " + Setup.itemID.name());
        }
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
