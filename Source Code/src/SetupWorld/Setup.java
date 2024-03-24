    package SetupWorld;

    import Main.main;
    import Scoreboard.timer.Timer;
    import Scoreboard.scoreboard;
    import org.bukkit.*;
    import org.bukkit.command.Command;
    import org.bukkit.command.CommandExecutor;
    import org.bukkit.command.CommandSender;
    import org.bukkit.entity.Player;
    import org.bukkit.event.player.PlayerRespawnEvent;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Random;
    import org.bukkit.block.Biome;
    import org.bukkit.event.Listener;
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
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.inventory.InventoryClickEvent;
    import org.bukkit.inventory.Inventory;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.inventory.meta.ItemMeta;
    import org.bukkit.Material;
    import org.bukkit.entity.Player;
    import org.bukkit.Bukkit;
    import org.bukkit.ChatColor;
    import static Scoreboard.timer.Timer.sec;
    import static Scoreboard.timer.Timer.min;

    public class Setup implements CommandExecutor, Listener
    {

        private main plugin;
        private static long lastPlayCommandTime = 0;
        private String lastWorldName = null;

        private final long cooldown = 30000;
        private Map<Player, String> gameLengths = new HashMap<>();
        public static Material itemID = Material.DIAMOND;

        private Material[] shortitem = {
                Material.STONE_AXE, Material.GRAVEL, Material.COBBLESTONE, Material.DIRT, Material.SAND, Material.COAL, Material.PAPER, Material.BONE, Material.FLINT,
                Material.BOOK, Material.BOOKSHELF, Material.BRICK, Material.BUCKET, Material.CAKE, Material.CARROT, Material.CHEST, Material.CLAY, Material.BREAD,
                Material.CACTUS, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.COOKED_RABBIT
        };
        private Material[] miditem = {
                Material.IRON_AXE, Material.IRON_INGOT, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_SWORD,
                Material.GOLDEN_CARROT, Material.COCOA, Material.EGG, Material.ENDER_PEARL, Material.PUMPKIN, Material.PUMPKIN_PIE, Material.RABBIT_FOOT, Material.MINECART, Material.SUGAR,
                Material.WRITTEN_BOOK, Material.REDSTONE_ORE, Material.REDSTONE_BLOCK
        };
        private Material[] longitem = {
                Material.DIAMOND_SWORD, Material.DIAMOND, Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS, Material.DIAMOND_PICKAXE,
                Material.NETHER_BRICK, Material.BLAZE_POWDER, Material.NETHERRACK, Material.NETHER_STAR, Material.DRAGON_EGG, Material.ENDER_CHEST, Material.BEACON, Material.EMERALD,
                Material.GOLDEN_APPLE, Material.JUKEBOX, Material.PRISMARINE, Material.SEA_LANTERN, Material.SPONGE
        };

        public Setup(main plugin) {
            this.plugin = plugin;
        }
        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent event)
        {
            if (lastWorldName != null) {
                World respawnWorld = Bukkit.getWorld(lastWorldName);
                if (respawnWorld != null) {
                    Location respawnLocation = respawnWorld.getSpawnLocation();
                    event.setRespawnLocation(respawnLocation);
                }
            }
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
                sender.sendMessage(ChatColor.RED + "This comamnd can only be done by a player.");
                return null;
            }
            Player player = (Player) sender;
            if (lastWorldName != null && Bukkit.getWorld(lastWorldName) != null)
                Bukkit.getServer().unloadWorld(Bukkit.getWorld(lastWorldName), true);
            World newWorld = null;
            boolean acceptableBiome = false;
            long seed;
            WorldCreator worldCreator;
            Biome biomeAtSpawn;

            for (Player p : Bukkit.getOnlinePlayers())
                p.sendTitle(ChatColor.YELLOW + "Loading map...", "", 10, 300, 10);

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
            lastWorldName = newWorld.getName();

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
        public void openGameLengthInventory(Player player) {
            Inventory inventory = Bukkit.createInventory(null, 9, "Choose Game Length");

            ItemStack paper1 = new ItemStack(Material.PAPER);
            ItemMeta meta1 = paper1.getItemMeta();
            meta1.setDisplayName("Short Game");
            paper1.setItemMeta(meta1);

            ItemStack paper2 = new ItemStack(Material.PAPER);
            ItemMeta meta2 = paper2.getItemMeta();
            meta2.setDisplayName("Medium Game");
            paper2.setItemMeta(meta2);

            ItemStack paper3 = new ItemStack(Material.PAPER);
            ItemMeta meta3 = paper3.getItemMeta();
            meta3.setDisplayName("Long Game");
            paper3.setItemMeta(meta3);

            inventory.setItem(3, paper1);
            inventory.setItem(4, paper2);
            inventory.setItem(5, paper3);

            player.openInventory(inventory);
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            Random random = new Random();

            if (event.getView().getTitle().equals("Choose Game Length")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PAPER) {
                    Player player = (Player) event.getWhoClicked();
                    String gameLength = event.getCurrentItem().getItemMeta().getDisplayName();
                    switch (gameLength) {
                        case "Short Game":
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                itemID = shortitem[random.nextInt(shortitem.length)];

                            }
                            break;
                        case "Medium Game":
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                itemID = miditem[random.nextInt(miditem.length)];
                            }
                            break;
                        case "Long Game":
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                itemID = longitem[random.nextInt(longitem.length)];
                            }
                            break;
                    }
                    player.closeInventory();
                }
                sec = 0;
                min = 0;
                plugin.resetGame();
                Player player = (Player) event.getWhoClicked();
                World newWorld = generateWorld(player);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage("§c§l" + "Find a(n) " + itemID.toString() + " to win!");
                    org.bukkit.scoreboard.Scoreboard board = p.getScoreboard();
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
                for (Player p : Bukkit.getOnlinePlayers())
                    p.sendMessage(ChatColor.GREEN + "The game starts, do /ready when you want to start !");
            }
        }


        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This comamnd can only be done by a player.");
                return false;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayCommandTime < cooldown) {
                sender.sendMessage(ChatColor.RED + "Wait before using this command again.");
                return true;
            }
            lastPlayCommandTime = currentTime;
            Player player = (Player) sender;
            if (!gameLengths.containsKey(player)) {
                openGameLengthInventory(player);
                return true;
            }
            return false;
        }
    }

