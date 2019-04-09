package heartin.plugin.mafia;

import heartin.plugin.mafia.heartin.plugin.command.CommandGameVote;
import nemo.mc.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class GameScheduler implements Runnable {

    public static BossBar remainbar = Bukkit.createBossBar("§c남은 시간 : §e<remainTime> §7초", BarColor.WHITE, BarStyle.SOLID, new BarFlag[]{BarFlag.PLAY_BOSS_MUSIC});

    final GameProcess process;
    private GameTask task;

    Map<GamePlayer, Integer> voteMax = new HashMap();

    GameScheduler(GameProcess process) {
        this.process = process;
        this.task = new SelectAbility();
    }

    public void run() {

        this.task = this.task.run();
    }

    private class SelectAbility implements GameTask {


        public SelectAbility() {

        }

        public GameTask run() {

            GameProcess process = GameScheduler.this.process;

            List<GamePlayer> citizens = new ArrayList(process.getPlayerManager().getOnlinePlayers());
            Random random = new Random();

            if (citizens.size() > 1) {

                GamePlayer mafia = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));
                GamePlayer doctor = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));
                GamePlayer police = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));

                if (mafia.isOnline()) {

                    Player player = mafia.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);

                    process.getPlayerManager().setMafia(gamePlayer);

                    player.sendMessage("" + process.getPlayerManager().getMafia(gamePlayer.getPlayer()).abilityType());
                }

                if (doctor.isOnline()) {
                    Player player = doctor.getPlayer();
                    process.getPlayerManager().setDoctor(player);
                }

                if (police.isOnline()) {
                    Player player = police.getPlayer();
                    process.getPlayerManager().setPolice(player);
                }

                for (GamePlayer gamePlayer : citizens) {
                    if (!gamePlayer.isOnline())
                        continue;
                    Player player = gamePlayer.getPlayer();

                    process.getPlayerManager().setCitizen(player);
                }
            } else {
                System.out.println("==============================");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

                Player player = gamePlayer.getPlayer();

                process.getChat().setGeneralChat(player);
                player.sendMessage("GeneralChatJoin" + process.getChat().playerChat.values().size());

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 10), true);
                Packet titlePacket = Packet.TITLE.compound("§6마피아 게임을 시작합니다", "§7직업을 확인해주세요.", 5, 60, 10);
                titlePacket.send(player);

                process.getScoreboard().registerPlayer(gamePlayer);


                player.sendMessage("잠시 후 게임이 시작됩니다.");
                player.sendMessage("=======================================================");
                player.sendMessage("§bonline Citizen " + process.getPlayerManager().getOnlineCitizen().size());
            }

            return new WaitTask();
        }
    }

    private class WaitTask implements GameTask {

        private int remainTicks = GameConfig.waitTicks;

        WaitTask() {
            updateTime();
        }

        @Override
        public GameTask run() {

            if (--this.remainTicks > 0) {
                updateTime();

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineCitizen()) {
                    Message.sendCitizen(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                    Message.sendDoctor(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                    Message.sendPolice(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                    Message.sendMafia(gamePlayer);
                }
                return this;
            }

            Bukkit.broadcastMessage("end WaitTask");

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                Packet titlePacket = Packet.TITLE.compound("§6아침이 되었습니다.", "§7대화를 진행하세요.", 5, 60, 10);
                titlePacket.send(player);
            }

            return new DayTask();
        }

        private void updateTime() {
            int remainTicks = this.remainTicks;

            if (remainTicks > 100) {
                if (remainTicks % 2 == 0) {
                    int seconds = remainTicks / 20;

                    for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                        Player player = gamePlayer.getPlayer();

                        remainbar.addPlayer(player);
                    }

                    remainbar.setColor(BarColor.BLUE);
                    remainbar.setTitle(String.format("§d남은시간§r§l %02d:%02d§r ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format("§d§l남은시간 §f§l%02d:%02d§r  ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            } else {
                remainTicks += 19;

                if (remainTicks % 5 == 0) {
                    char color = remainTicks / 5 % 2 == 0 ? 'f' : 'c';
                    int seconds = remainTicks / 20;

                    remainbar.setTitle(String.format("§d남은시간 §%c§l%02d:%02d§r ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§l남은시간 §%c§l%02d:%02d§r  ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            }
        }
    }

    private class DayTask implements GameTask {

        private int remainTicks = GameConfig.dayTicks;

        DayTask() {

            updateTime();
        }

        public GameTask run() {

            if (--this.remainTicks > 0) {
                updateTime();

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineCitizen()) {
                    Message.sendCitizen(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                    Message.sendDoctor(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                    Message.sendPolice(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                    Message.sendMafia(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getDeathPlayers()) {
                    Message.sendGhost(gamePlayer);
                }

                return this;
            }
            // Task run

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

                Player player = gamePlayer.getPlayer();

                process.getPlayerManager().setVote(gamePlayer);
                player.sendMessage("투표권 지급");

                Packet titlePacket = Packet.TITLE.compound("§6투표시간이 되었습니다", "§7투표를 진행해주세요.", 5, 60, 10);
                titlePacket.send(player);
            }
            Bukkit.broadcastMessage("end DayTask");


            // GameProcess process = GameScheduler.this.process;
            //     process.getPlugin().processStop();

            return new VoteTask();
        }

        private void updateTime() {
            int remainTicks = this.remainTicks;

            if (remainTicks > 100) {
                if (remainTicks % 2 == 0) {
                    int seconds = remainTicks / 20;

                    for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                        Player player = gamePlayer.getPlayer();

                        remainbar.addPlayer(player);
                    }

                    remainbar.setColor(BarColor.PINK);
                    remainbar.setTitle(String.format("§d남은시간§r§l %02d:%02d§r ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format("§d§l남은시간 §f§l%02d:%02d§r  ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            } else {
                remainTicks += 19;


                if (remainTicks % 5 == 0) {
                    char color = remainTicks / 5 % 2 == 0 ? 'f' : 'c';
                    int seconds = remainTicks / 20;

                    remainbar.setTitle(String.format("§d남은시간 §%c§l%02d:%02d§r ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§l남은시간 §%c§l%02d:%02d§r  ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            }
        }
    }

    private class VoteTask implements GameTask {
        private int remainTicks = GameConfig.voteTicks;

        VoteTask() {
            updateTime();
        }

        public GameTask run() {
            if (--this.remainTicks > 0) {
                updateTime();

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineCitizen()) {
                    Message.sendCitizen(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                    Message.sendDoctor(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                    Message.sendPolice(gamePlayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                    Message.sendMafia(gamePlayer);
                }
                return this;
            }
            //task run

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                process.getPlayerManager().cleartVote(gamePlayer);

                Packet titlePacket = Packet.TITLE.compound("§6투표시간이 종료되었습니다", "§7아무말아무말", 5, 60, 10);
                titlePacket.send(player);
            }

            Bukkit.broadcastMessage("end VoteTask");

            GameProcess process = GameScheduler.this.process;
            // process.getPlugin().processStop();

            return new VoteKillTask();
        }

        private void updateTime() {
            int remainTicks = this.remainTicks;

            if (remainTicks > 100) {
                if (remainTicks % 2 == 0) {
                    int seconds = remainTicks / 20;

                    for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                        Player player = gamePlayer.getPlayer();

                        remainbar.addPlayer(player);
                    }

                    remainbar.setTitle(String.format("§d투표시간§r§l %02d:%02d§r ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§l투표시간 §f§l%02d:%02d§r  ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            } else {
                remainTicks += 19;

                if (remainTicks % 5 == 0) {
                    char color = remainTicks / 5 % 2 == 0 ? 'f' : 'c';
                    int seconds = remainTicks / 20;

                    remainbar.setTitle(String.format("§d투표시간 §%c§l%02d:%02d§r ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format(" §d§l투표시간 §%c§l%02d:%02d§r  ", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            }
        }
    }

    private class VoteKillTask implements GameTask {

        VoteKillTask() {
        }

        public GameTask run() {

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineCitizen()) {
                Message.sendCitizen(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                Message.sendDoctor(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                Message.sendPolice(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                Message.sendMafia(gamePlayer);
            }
            //task run


            Collection vote = CommandGameVote.vote.values();
            Integer max = (Integer) Collections.max(vote);

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                if (CommandGameVote.vote.get(gamePlayer.getName()) == max) {
                    voteMax.put(gamePlayer, max);
                }
            }
            Set<String> user = CommandGameVote.vote.keySet();
            Set<GamePlayer> topuser = voteMax.keySet();

            Bukkit.broadcastMessage("==================투표=====================");
            for (String str : user) {
                Bukkit.broadcastMessage(str + " : " + CommandGameVote.vote.get(str) + " 표");
            }

            if (voteMax.size() >= 2) {
                Bukkit.broadcastMessage("최다 득표가 2명 이상입니다");
                return new VoteTask();

            }

            for (GamePlayer str : topuser) {
                Bukkit.broadcastMessage("================최다득=======================");
                Bukkit.broadcastMessage(str.getName() + " : " + voteMax.get(str) + " 표");

                str.getPlayer().setHealth(0D);
                Bukkit.broadcastMessage(str.getName() + "사망");
            }


            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                Packet titlePacket = Packet.TITLE.compound("§6투표시간이 종료되었습니다", "§7아무말아무말", 5, 60, 10);
                titlePacket.send(player);
            }

            Bukkit.broadcastMessage("end VoteTask");

            GameProcess process = GameScheduler.this.process;
            // process.getPlugin().processStop();

            return new DayTask();
        }
    }

    private class NightTask implements GameTask {

        private int remainTicks = GameConfig.nightTicks;

        NightTask() {

        }

        public GameTask run() {

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineCitizen()) {
                Message.sendCitizen(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                Message.sendDoctor(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                Message.sendPolice(gamePlayer);
            }
            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                Message.sendMafia(gamePlayer);
            }
            //task run


            return new DayTask();
        }
    }


}