package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameVote {


    public Map<String, Integer> vote = new HashMap();
    public Map<GamePlayer, Integer> doctor = new HashMap();
    private final Map<GamePlayer, Boolean> voteCheckPlayer;
    private GameProcess process;

    public GameVote(GameProcess process) {
        this.process = process;
        Map voteCheckPlayer = new HashMap();

        this.voteCheckPlayer = voteCheckPlayer;
    }

    public GamePlayer setMafiaVote(GamePlayer gamePlayer) {

        Ability ability = process.getPlayerManager().getAbility(gamePlayer);

        if (ability.abilityType() == Ability.Type.MAFIA) {
            this.voteCheckPlayer.put(gamePlayer, true);
        }
        return gamePlayer;
    }

    public GamePlayer setVote(GamePlayer gamePlayer) {

        this.voteCheckPlayer.put(gamePlayer, true);
        this.vote.put(gamePlayer.getName(), Integer.valueOf(0));

        return gamePlayer;
    }

    public GamePlayer removeVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.remove(gamePlayer);
        this.voteCheckPlayer.put(gamePlayer, false);

        return gamePlayer;
    }

    public Boolean getVote(GamePlayer gamePlayer) {
        Boolean bool = this.voteCheckPlayer.get(gamePlayer);

        return bool;
    }

    public GamePlayer clearVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.clear();

        return gamePlayer;
    }

    public GamePlayer setResurrection(GamePlayer gamePlayer) {
        Ability ability = process.getPlayerManager().getAbility(gamePlayer);
        if (ability.abilityType() == Ability.Type.DOCTOR) {

            this.doctor.put(gamePlayer, Integer.valueOf(0));

        }


        return gamePlayer;
    }
}
