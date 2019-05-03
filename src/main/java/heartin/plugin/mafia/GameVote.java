package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;

import java.util.HashMap;
import java.util.Map;

public class GameVote {


    public Map<String, Integer> vote = new HashMap();

    public Map<String, Boolean> resurrection = new HashMap();
    public Map<GamePlayer, Boolean> arrest = new HashMap();
    public Map<GamePlayer, Boolean> voteCheckPlayer = new HashMap();
    private GameProcess process;

    public GameVote(GameProcess process) {
        this.process = process;

    }

    public GamePlayer setMafiaVote(GamePlayer gamePlayer) {

        Ability ability = process.getPlayerManager().getAbility(gamePlayer);

        if (ability.abilityType() == Ability.Type.MAFIA) {
            this.voteCheckPlayer.put(gamePlayer, true);
        }

        return gamePlayer;
    }

    // voteCheckPlayer -> 투표권 true / false HashMap
    // vote -> 투표 set int value
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

  /*  public Boolean getVote(GamePlayer gamePlayer) {
        Boolean bool = this.voteCheckPlayer.get(gamePlayer);

        return bool;
    }*/

    public GamePlayer clearVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.clear();
        this.vote.clear();

        return gamePlayer;
    }

    public String setResurrection(String playerName) {

        this.resurrection.put(playerName, true);

        return playerName;
    }

    public Boolean getResurrction(String playerName)
    {
        Boolean bool = this.resurrection.get(playerName);

        return bool;
    }

    public void clearResurrection() {

        this.resurrection.clear();
    }

    public GamePlayer setArrest(GamePlayer gamePlayer)
    {
        this.arrest.put(gamePlayer, true);

        return gamePlayer;
    }

    public void clearArrest(GamePlayer gamePlayer) {

        this.arrest.clear();
        this.arrest.put(gamePlayer, false);
    }


}
