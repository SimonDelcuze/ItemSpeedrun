package Start;

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

public class Start implements CommandExecutor {

    public main plugin;

    public Start(main plugin) {
        this.plugin = plugin;
    }

    public void showTimer(Player player)
    {
        new BukkitRunnable() {
            int counter = 5;
            ChatColor[] colors = {
                    ChatColor.RED,
                    ChatColor.GOLD,
                    ChatColor.YELLOW,
                    ChatColor.GREEN,
                    ChatColor.AQUA,
                    ChatColor.BLUE,
                    ChatColor.LIGHT_PURPLE
            };

            public void run() {
                if (counter > 0) {
                    ChatColor color = colors[counter % colors.length];
                    player.sendTitle(color + "" + ChatColor.BOLD + counter, "");
                    counter--;
                } else {
                    ChatColor color = colors[0];
                    player.sendTitle(color + "" + ChatColor.BOLD + "Go!", "");
                    cancel();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (plugin.isPlayerReady(p)) {
                                    setupGamePlayer(p);
                                }
                            }
                        }
                    }.runTaskLater(Bukkit.getPluginManager().getPlugin("plugin"), 20L);
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("plugin"), 0L, 20L);
    }

    public void setupGamePlayer(Player player) {
        World world = player.getWorld();
        Location spawnLocation = world.getHighestBlockAt(0, 0).getLocation().add(0.5, -49, 0.5);
        player.teleport(spawnLocation);
        player.getInventory().clear();
        ItemStack steaks = new ItemStack(Material.COOKED_BEEF, 64);
        player.getInventory().addItem(steaks);
        player.performCommand("trackingcompass");
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        world.setDifficulty(Difficulty.HARD);
        world.setPVP(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This comamnd can only be done by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (plugin.isGameStarted()) {
            if (!plugin.isPlayerReady(player)) {
                plugin.setPlayerReady(player);
                sender.sendMessage(ChatColor.GREEN + "You are now ready!");
                if (plugin.getReadyCount() == Bukkit.getOnlinePlayers().size()) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        showTimer(p);
                    }
                    plugin.setGameStarted(false);
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + "You are already ready!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The game has not started yet, do /play to start the game.");
        }
        return true;
    }
}
