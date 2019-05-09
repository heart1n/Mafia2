package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

import java.util.HashMap;
import java.util.Map;

public class Ability {


    private GamePlayer gamePlayer;
    private final String abilityName;
    private final String[] description;
    private Type type;

    public Map<GamePlayer, Boolean> soldier = new HashMap();


    public Ability(GamePlayer gamePlayer, String abilityName, Type type, String[] decription) {

        this.gamePlayer = gamePlayer;
        this.abilityName = abilityName;
        this.description = decription;
        this.type = type;
    }

    public String[] description() {
        return description;
    }

    public Enum getAbilityType() {
        return this.type;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public GamePlayer getAbilityPlayer() {
        return gamePlayer;
    }

    public Enum setAbilityType(GamePlayer gamePlayer, Type type) {
        this.gamePlayer = gamePlayer;
        this.type = type;

        return type;
    }

    public GamePlayer setAttack(GamePlayer gamePlayer) {

        soldier.put(gamePlayer, false);

        return gamePlayer;
    }

    public GamePlayer addAttack(GamePlayer gamePlayer) {
        soldier.put(gamePlayer, true);

        return gamePlayer;
    }

    public GamePlayer getMafiaPlayer(GamePlayer gamePlayer)
    {
        this.type = Type.MAFIA;

        if (this.type == Type.MAFIA)
        {
            gamePlayer.getPlayer().sendMessage("");
        }

        return gamePlayer;
    }


    public static enum Type {
        MAFIA,
        DOCTOR,
        POLICE,
        SPY,
        SOLDIER,
        POLITICIAN,
        MEDIUM,
        CITIZEN;
    }
}
