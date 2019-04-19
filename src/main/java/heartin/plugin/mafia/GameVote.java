package heartin.plugin.mafia;

import heartin.plugin.mafia.heartin.plugin.command.CommandGameVote;

import java.util.HashMap;
import java.util.Map;

public class GameVote {


    private final Map<GamePlayer, Boolean> voteCheckPlayer;
    private GameProcess process;

    public GameVote(GameProcess process)
    {
        this.process = process;
        Map voteCheckPlayer = new HashMap();

        this.voteCheckPlayer = voteCheckPlayer;
    }


    public GamePlayer setVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.put(gamePlayer, true);

        CommandGameVote.vote.put(gamePlayer.getName(), Integer.valueOf(0));

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

    public GamePlayer cleartVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.clear();

        return gamePlayer;
    }

}
