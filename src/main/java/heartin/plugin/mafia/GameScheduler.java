package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import nemo.mc.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class GameScheduler implements Runnable {
    final GameProcess process;
    private GameTask task;

    private HashMap<GamePlayer, Boolean> AbilitySelect = new HashMap<>();

    GameScheduler(GameProcess process) {
        this.process = process;
        this.task = new SelectAbility();
    }

    public void run() {

        this.task = this.task.run();
    }

    private class DayTask implements GameTask {

        private int remainTicks = GameConfig.dayTicks;

        DayTask() {
            updateTime();
        }

        public GameTask run() {

            if (--this.remainTicks > 0) {
                updateTime();

                //Bukkit.broadcastMessage("remainTicks: " + remainTicks);
                return this;
            }
            Bukkit.broadcastMessage("test task1");


            GameProcess process = GameScheduler.this.process;

            process.getPlugin().processStop();
            return this;
        }

        private void updateTime() {
            int remainTicks = this.remainTicks;

            if (remainTicks > 100) {
                if (remainTicks % 2 == 0) {
                    int seconds = remainTicks / 20;

                    //  GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§lNemo §f§l%02d:%02d§r  ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            } else {
                remainTicks += 19;

                if (remainTicks % 5 == 0) {
                    char color = remainTicks / 5 % 2 == 0 ? 'f' : 'c';
                    int seconds = remainTicks / 20;

                    //GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§lNemo §%c§l%02d:%02d§r  ", new Object[] { Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60) }));
                }
            }
        }
    }
    /*
     *      /game start - > SelectAbility Task Run
     *
     * */
    private class SelectAbility implements GameTask {

        private int remainTicks = GameConfig.selectTicks;

        public SelectAbility() {
            updateTime();
        }

        public GameTask run() {

            GameProcess process = GameScheduler.this.process;

            List<GamePlayer> citizens = new ArrayList(process.getPlayerManager().getOnlinePlayers());
            Random random = new Random();

            GamePlayer mafia = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));
            GamePlayer doctor = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));
            GamePlayer police = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));

            if (mafia.isOnline()) {
                Player player = mafia.getPlayer();
                process.getPlayerManager().setMafia(player);

                player.sendMessage("마피아");
            }

            if (doctor.isOnline()) {
                Player player = doctor.getPlayer();
                process.getPlayerManager().setMafia(player);

                player.sendMessage("의사");
            }

            if (police.isOnline()) {
                Player player = police.getPlayer();
                process.getPlayerManager().setPolice(player);

                player.sendMessage("경찰");
            }

            for (GamePlayer gamePlayer : citizens) {
                if (!gamePlayer.isOnline())
                    continue;
                Player player = gamePlayer.getPlayer();

                process.getPlayerManager().setCitizen(player);

                Packet titlePacket = Packet.TITLE.compound("§6난 시민이다", "§7시민이당당", 5, 60, 10);
                titlePacket.send(player);
                player.sendMessage("시민");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

                Player player = gamePlayer.getPlayer();

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 10), true);
              //  Packet titlePacket = Packet.TITLE.compound("§6Title Messasge", "§7SubTitle Message", 5, 60, 10);
               // titlePacket.send(player);
            }

            return new DayTask();
        }

        private void updateTime() {
            int remainTicks = this.remainTicks;

            if (remainTicks > 100) {
                if (remainTicks % 2 == 0) {
                    int seconds = remainTicks / 20;
                    //  GameScheduler.this.process.getScoreboard().setDisplayName(String.format("GameTime", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds & 60)}));
                }
            }
        }
    }
}