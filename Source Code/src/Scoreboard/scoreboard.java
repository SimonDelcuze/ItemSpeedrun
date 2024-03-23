package Scoreboard;

import Main.main;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import Scoreboard.timer.Timer;
import SetupWorld.Setup;

import java.io.Console;

public class scoreboard {

    public static void SetScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("test", "dummy");
        obj.setDisplayName("§3§lItem §7§lSpeedRun");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score space = obj.getScore(" ");
        space.setScore(6);

        Score space4 = obj.getScore("§c");
        space4.setScore(5);

        Team timer = obj.getScoreboard().registerNewTeam("timer");
        timer.addEntry("§dTimer §7-§c ");
        timer.setSuffix("" + Timer.timer);
        obj.getScore("§dTimer §7-§c ").setScore(4);


        Team itemid = obj.getScoreboard().registerNewTeam("item");
        itemid.addEntry("§dItem §7-§c ");
        itemid.setSuffix("");
        obj.getScore("§dItem §7-§c ").setScore(3);

        Score space2 = obj.getScore("  ");
        space2.setScore(2);
        Score space3 = obj.getScore("   ");
        space3.setScore(1);

        Score play = obj.getScore("§3Start §7- §c/play");
        play.setScore(0);

        player.setScoreboard(board);
    }

    public static void updatetimer(Player player){
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        board.getTeam("timer").setSuffix("" + Timer.timer);
    }
    public static void updateitem(Player player){
        System.out.println("" + Setup.itemID.name());
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        board.getTeam("item").setSuffix(" " + Setup.itemID.name());
    }
}
