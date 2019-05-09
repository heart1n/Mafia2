package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import heartin.plugin.mafia.Ability.Citizen;
import heartin.plugin.mafia.Ability.Soldier;
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
                GamePlayer spy = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));
                GamePlayer soldier = (GamePlayer) citizens.remove(random.nextInt(citizens.size()));

                if (mafia.isOnline()) {
                    Player player = mafia.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);
                    process.getPlayerManager().setMafia(gamePlayer);
                    process.getChat().setMafiachat(gamePlayer);
                }

                if (doctor.isOnline()) {
                    Player player = doctor.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);
                    process.getPlayerManager().setDoctor(gamePlayer);
                }

                if (police.isOnline()) {
                    Player player = police.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);
                    process.getPlayerManager().setPolice(gamePlayer);
                }

                if (spy.isOnline())
                {
                    Player player = spy.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);
                    process.getPlayerManager().setSpy(gamePlayer);
                }

                if(soldier.isOnline())
                {
                    Player player = soldier.getPlayer();
                    GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);
                    process.getPlayerManager().setSoldier(gamePlayer);
                }
            }

            for (GamePlayer gamePlayer : citizens) {
                if (!gamePlayer.isOnline())
                    continue;
                Player player = gamePlayer.getPlayer();
                process.getPlayerManager().setCitizen(gamePlayer);
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

                Player player = gamePlayer.getPlayer();

                process.getChat().setGeneralChat(player);
                process.getScoreboard().registerPlayer(gamePlayer);
                process.getVote().clear();

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 10), true);
                Packet titlePacket = Packet.TITLE.compound("§6마피아 게임을 시작합니다", "§7직업을 확인해주세요.", 5, 60, 10);
                titlePacket.send(player);

                player.sendMessage("잠시 후 게임이 시작됩니다.");
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
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy())
                {
                    Message.sendSpy(gamePlayer);
                }
                for (GamePlayer gameplayer : process.getPlayerManager().getOnlineSoldier())
                {
                    Message.sendSoldier(gameplayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getDeathPlayers()) {
                    Message.sendGhost(gamePlayer);
                }
                return this;
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                Packet titlePacket = Packet.TITLE.compound("§6아침이 되었습니다.", "§7대화를 진행하세요.", 5, 60, 10);
                titlePacket.send(player);
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineDoctor()) {
                gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "의사는 원하는 플레이어 1명을 마피아를 피해 살릴 수 있습니다.");
                process.getVote().setAbilityCheck(gamePlayer);
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
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy())
                {
                    Message.sendSpy(gamePlayer);
                }
                for (GamePlayer gameplayer : process.getPlayerManager().getOnlineSoldier())
                {
                    Message.sendSoldier(gameplayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getDeathPlayers()) {
                    Message.sendGhost(gamePlayer);
                }

                return this;
            }
            // Task run

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

                Player player = gamePlayer.getPlayer();

                process.getVote().setVote(gamePlayer);
                process.getVote().resurrection.put(gamePlayer, false);

                Packet titlePacket = Packet.TITLE.compound("§6투표시간이 되었습니다", "§7투표를 진행해주세요.", 5, 60, 10);
                titlePacket.send(player);
            }

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
                    remainbar.setTitle(String.format("§d남은시간 §r§l%02d:%02d§r ", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
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
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy())
                {
                    Message.sendSpy(gamePlayer);
                }
                for (GamePlayer gameplayer : process.getPlayerManager().getOnlineSoldier())
                {
                    Message.sendSoldier(gameplayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getDeathPlayers()) {
                    Message.sendGhost(gamePlayer);
                }
                return this;
            }
            //task run

            Collection<Integer> vote = process.getVote().vote.values();
            Integer max = (Integer) Collections.max(vote);

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                if (process.getVote().vote.get(gamePlayer.getName()) == max) {
                    voteMax.put(gamePlayer, max);
                }
            }
            Set<String> user = process.getVote().vote.keySet();
            Set<GamePlayer> topuser = voteMax.keySet();

            Bukkit.broadcastMessage("§6─────────── §b투표 결과 §6─────────────");
            for (String str : user) {
                Bukkit.broadcastMessage(str + " : " + process.getVote().vote.get(str) + " 표");
            }

            if (voteMax.size() >= 2) {
                Bukkit.broadcastMessage("같은 표가 있으므로 투표를 종료합니다.");

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                    Player player = gamePlayer.getPlayer();

                    process.getVote().clearVote(gamePlayer);

                    process.getVote().mafiaVote.put(gamePlayer, Integer.valueOf(0));

                    Packet titlePacket = Packet.TITLE.compound("§6투표시간이 종료되었습니다", "§7밤이 되었습니다..", 5, 60, 10);
                    titlePacket.send(player);
                }

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                    Player player = gamePlayer.getPlayer();

                    process.getVote().setAbilityCheck(gamePlayer);
                    process.getVote().setMafiaVote(gamePlayer);

                    gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "죽일 플레이어를 선택 해주세요.");
                }

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                    process.getVote().setAbilityCheck(gamePlayer);
                    process.getVote().setArrest(gamePlayer);

                    gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "마피아로 의심되는 플레이어를 선택 해주세요.");
                }

                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy()) {
                    process.getVote().setAbilityCheck(gamePlayer);

                    gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "선택한 플레이어의 직업을 알아낼 수 있습니다.");
                }

                return new NightTask();
            }
            for (GamePlayer str : topuser) {
                Bukkit.broadcastMessage("§6─────────── §b최다 득표 §6─────────────");
                Bukkit.broadcastMessage(str.getName() + " : " + voteMax.get(str) + " 표");

                str.getPlayer().setHealth(0D);
                Bukkit.broadcastMessage(str.getName() + "사망");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                process.getVote().clearVote(gamePlayer);
                process.getVote().mafiaVote.put(gamePlayer, Integer.valueOf(0));

                Packet titlePacket = Packet.TITLE.compound("§6투표시간이 종료되었습니다", "§7밤이 되었습니다..", 5, 60, 10);
                titlePacket.send(player);
                voteMax.clear();
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                Player player = gamePlayer.getPlayer();

                process.getVote().setAbilityCheck(gamePlayer);
                process.getVote().setMafiaVote(gamePlayer);

                gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "죽일 플레이어를 선택 해주세요.");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePolice()) {
                process.getVote().setAbilityCheck(gamePlayer);
                process.getVote().setArrest(gamePlayer);

                gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "마피아로 의심되는 플레이어를 선택 해주세요.");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy()) {
                process.getVote().setAbilityCheck(gamePlayer);

                gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "선택한 플레이어의 직업을 알아낼 수 있습니다.");
            }

            GameProcess process = GameScheduler.this.process;
            // process.getPlugin().processStop();

            return new NightTask();
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

    private class NightTask implements GameTask {

        private int remainTicks = GameConfig.nightTicks;

        NightTask() {
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
                for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineSpy())
                {
                    Message.sendSpy(gamePlayer);
                }
                for (GamePlayer gameplayer : process.getPlayerManager().getOnlineSoldier())
                {
                    Message.sendSoldier(gameplayer);
                }
                for (GamePlayer gamePlayer : process.getPlayerManager().getDeathPlayers()) {
                    Message.sendGhost(gamePlayer);
                }

                return this;
            }

            // Collection<Integer> vote = process.getVote().vote.values();
            //  Integer max = (Integer) Collections.max(vote);

            //  for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
            //     if (process.getVote().vote.get(gamePlayer.getName()) == max) {
            //         voteMax.put(gamePlayer, max);
            //      }
            //   }
            Set<String> user = process.getVote().vote.keySet();
            Set<GamePlayer> topuser = voteMax.keySet();


            Map<GamePlayer, Integer> mafiaVoteMax = new HashMap();

            Collection vote = process.getVote().mafiaVote.values();
            Integer max = (Integer) Collections.max(vote);


            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                if (process.getVote().mafiaVote.get(gamePlayer) == 1) {
                    mafiaVoteMax.put(gamePlayer, max);
                }
            }
            Set<GamePlayer> top = mafiaVoteMax.keySet();

            // gamePlayer 가 의사가 선택한 사람이라면 -> new task

            for (GamePlayer gamePlayer : top) {

                Ability ability = process.getPlayerManager().getAbility(gamePlayer);

                if (process.getVote().getResurrection(gamePlayer)) {
                    Bukkit.broadcastMessage(Message.SYSTEM + "의사의 도움으로 마피아로부터 도망갔습니다!");

                } else if (ability.getAbilityType() == Ability.Type.SOLDIER)
                {
                    Bukkit.broadcastMessage(Message.SYSTEM + "군인은 마피아의 공격을 한 번 견뎠습니다.");
                    if (ability.soldier.values().size() == 2)
                    {
                        gamePlayer.getPlayer().setHealth(0D);
                        Bukkit.broadcastMessage(Message.SYSTEM + "군인이 공격을 2번 맞아 죽었습니다.");
                    }
                    ability.setAttack(gamePlayer);
                }

                    gamePlayer.getPlayer().setHealth(0D);
                    Bukkit.broadcastMessage(Message.SYSTEM + "§6" + gamePlayer.getName() + " §r는 마피아한테 죽었습니다.");
            }

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {
                Player player = gamePlayer.getPlayer();

                Packet titlePacket = Packet.TITLE.compound("§6기나긴 밤이 지나갔습니다", "§7아침이 되었습니다", 5, 60, 10);
                titlePacket.send(player);

                process.getVote().clearAbilityCheck();
                process.getVote().clearVote(gamePlayer);
                process.getVote().clearArrest(gamePlayer);
                process.getVote().clearResurrection();
                mafiaVoteMax.clear();
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

                    remainbar.setTitle(String.format("§d마피아 투표§r§l %02d:%02d§r", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format("§d§l마피아 투표 §f§l%02d:%02d§r", new Object[]{Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            } else {
                remainTicks += 19;

                if (remainTicks % 5 == 0) {
                    char color = remainTicks / 5 % 2 == 0 ? 'f' : 'c';
                    int seconds = remainTicks / 20;

                    remainbar.setTitle(String.format("§d마피아 투표 §%c§l%02d:%02d§r", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                    GameScheduler.this.process.getScoreboard().setDisplayName(String.format("§d§l마피아 투표 §%c§l%02d:%02d§r", new Object[]{Character.valueOf(color), Integer.valueOf(seconds / 60), Integer.valueOf(seconds % 60)}));
                }
            }
        }
    }


}


