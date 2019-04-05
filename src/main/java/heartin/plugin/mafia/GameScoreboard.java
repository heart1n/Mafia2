package heartin.plugin.mafia;


import nemo.mc.packet.Packet;
import nemo.mc.scoreboard.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

final class GameScoreboard {

    private static final String OBJECTIVE_NAME = "MafiaGame";
    private final GameProcess process;
    private final Scoreboard scoreboard;
    private final Score score;
    private final Team mafia;
    private final Team citizen;
    private final Team police;
    private final Team doctor;

    private String displayNameForCitizen;
    private String displayNameForMafia;
    private String displayNameForPolice;
    private String displayNameForDoctor;


    GameScoreboard(GameProcess process) {
        this.process = process;

        ScoreboardManager manager = ScoreboardManager.getInstance();
        Scoreboard scoreboard = manager.newScoreboard();
        Objective objective = scoreboard.registerObjective(OBJECTIVE_NAME);
        objective.setDisplayName(this.displayNameForCitizen = this.displayNameForMafia = this.displayNameForPolice = this.displayNameForDoctor =  "§bMafia Games");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreboard = scoreboard;
        this.score = objective.registerScore("직업 :     ");


        Team team = scoreboard.registerTeam("Citizen");
        team.setPrefix("§r");
        this.citizen = team;

        team = scoreboard.registerTeam("Mafia");
        team.setPrefix("§c");
        this.mafia = team;

        team = scoreboard.registerTeam("Police");
        team.setPrefix("§b");
        this.police = team;

        team = scoreboard.registerTeam("Doctor");
        team.setPrefix("§a");
        this.doctor = team;
    }


    void setDisplayName(String displayName)
    {
        this.displayNameForCitizen = this.displayNameForMafia = this.displayNameForPolice =  (this.displayNameForDoctor = displayName);

        Collection<? extends GamePlayer> onlineGamePlayers = this.process.getPlayerManager().getOnlinePlayers();

        if (onlineGamePlayers.size() > 0)
        {
            Packet packet = Packet.SCOREBAORD.scoreboardDisplayName("MafiaGame", displayName);

            for (GamePlayer gamePlayer : onlineGamePlayers)
                packet.send(gamePlayer.getPlayer());
        }
    }

    void setDisplayNameForCitizen(String displayName) {
        this.displayNameForCitizen = displayName;

        Set<? extends GamePlayer> onlineCitizen = this.process.getPlayerManager().getOnlineCitizen();

        if (onlineCitizen.size() > 0) {
            Packet packet = Packet.SCOREBAORD.scoreboardDisplayName("MafiaGame", displayName);

            for (GamePlayer gamePlayer : onlineCitizen)
                packet.send(gamePlayer.getPlayer());
        }
    }

    void setDisplayNameForMafia(String displayName) {
        this.displayNameForMafia = displayName;

        Set<? extends GamePlayer> onlineMafia = this.process.getPlayerManager().getOnlineMafia();

        if (onlineMafia.size() > 0) {
            Packet packet = Packet.SCOREBAORD.scoreboardDisplayName("MafiaGame", displayName);

            for (GamePlayer gamePlayer : onlineMafia)
                packet.send(gamePlayer.getPlayer());
        }
    }

    void setDisplayNameforDoctor(String displayName) {
        this.displayNameForDoctor = displayName;

        Set<? extends GamePlayer> onlineDoctor = this.process.getPlayerManager().getOnlineDoctor();

        if (onlineDoctor.size() > 0) {
            Packet packet = Packet.SCOREBAORD.scoreboardDisplayName("MafiaGame", displayName);

            for (GamePlayer gamePlayer : onlineDoctor)
                packet.send(gamePlayer.getPlayer());
        }
    }

    void setDisplayNameForPolice(String displayName) {
        this.displayNameForPolice = displayName;

        Set<? extends GamePlayer> onlinePolice = this.process.getPlayerManager().getOnlinePolice();

        if (onlinePolice.size() > 0) {
            Packet packet = Packet.SCOREBAORD.scoreboardDisplayName("MafiaGame", displayName);

            for (GamePlayer gamePlayer : onlinePolice)
                packet.send(gamePlayer.getPlayer());
        }
    }

    void setCitizen(String entry)
    {
        this.citizen.addEntry(entry);
    }

    void setMafia(String entry)
    {
        this.mafia.addEntry(entry);
    }

    void setPolice(String entry)
    {
        this.police.addEntry(entry);
    }

    void setDoctor(String entry)
    {
        this.doctor.addEntry(entry);
    }

    void setMedium(String entry)
    {

    }
    void setSoldier(String entry)
    {

    }

    void setSpy(String entry)
    {

    }

    void setScore(int score)
    {
        this.score.setScore(score);
    }


    void registerPlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();

        this.scoreboard.registerPlayer(player);

        Packet.SCOREBAORD.scoreboardDisplayName("Citizen", this.displayNameForCitizen).send(player);
        Packet.SCOREBAORD.scoreboardDisplayName("Mafia", this.displayNameForMafia).send(player);
        Packet.SCOREBAORD.scoreboardDisplayName("Doctor", this.displayNameForDoctor).send(player);
        Packet.SCOREBAORD.scoreboardDisplayName("Police", this.displayNameForPolice).send(player);
    }

    void unregisterPlayer(GamePlayer gamePlayer) {
        this.scoreboard.unregisterPlayer(gamePlayer.getPlayer());
    }

    void clear() {
        this.scoreboard.clear();
    }


}
