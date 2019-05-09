package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;

import java.util.HashMap;
import java.util.Map;

public class GameVote {


    public Map<String, Integer> vote = new HashMap();
    public Map<GamePlayer, Integer> mafiaVote = new HashMap();

    public Map<GamePlayer, Boolean> resurrection = new HashMap();
    public Map<GamePlayer, Boolean> arrest = new HashMap();
    public Map<GamePlayer, Boolean> spy = new HashMap();
    public Map<GamePlayer, Boolean> voteCheckPlayer = new HashMap();
    public Map<GamePlayer, Boolean> abilityCheckPlayer = new HashMap();


    private GameProcess process;

    public GameVote(GameProcess process) {
        this.process = process;

    }

    public GamePlayer setAbilityCheck(GamePlayer gamePlayer)
    {
        this.abilityCheckPlayer.put(gamePlayer, true);

        return gamePlayer;
    }

    public GamePlayer removeAbilityCheck(GamePlayer gamePlayer)
    {
        this.abilityCheckPlayer.put(gamePlayer, false);

        return gamePlayer;
    }

    public Boolean getAbilityCheck(GamePlayer gamePlayer)
    {

        Boolean bool = this.abilityCheckPlayer.get(gamePlayer);

        return bool;
    }

    public void clearAbilityCheck()
    {
        this.abilityCheckPlayer.clear();
    }

    public GamePlayer setMafiaVote(GamePlayer gamePlayer) {

        this.mafiaVote.put(gamePlayer, Integer.valueOf(0));

        return gamePlayer;
    }

    public GamePlayer removeMafiaVote(GamePlayer gamePlayer)
    {
        this.mafiaVote.remove(gamePlayer);
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

    public GamePlayer clearVote(GamePlayer gamePlayer) {
        this.voteCheckPlayer.clear();
        this.vote.clear();

        return gamePlayer;
    }

    public GamePlayer setResurrection(GamePlayer gamePlayer) {

        this.resurrection.put(gamePlayer, true);

        return gamePlayer;
    }

    public Boolean getResurrection(GamePlayer gamePlayer)
    {

        Boolean bool = this.resurrection.get(gamePlayer);

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

    public void clear()
    {
        clearAbilityCheck();
        clearResurrection();
        this.mafiaVote.clear();
    }


}
