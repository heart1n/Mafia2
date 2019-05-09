package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

import java.util.HashMap;
import java.util.Map;

public class Ability {


    private GamePlayer gamePlayer;
    private final String abilityName;
    private final String[] description;
    private Type type;

    public Map<GamePlayer, Integer> soldier =  new HashMap();


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

    public GamePlayer getAbilityPlayer()
    {
        return gamePlayer;
    }

    public Enum setAbilityType(GamePlayer gamePlayer, Type type)
    {
        this.gamePlayer = gamePlayer;
        this.type = type;

        return type;
    }

    public GamePlayer setAttack(GamePlayer gamePlayer)
    {

        soldier.put(gamePlayer, Integer.valueOf(0));
        soldier.put(gamePlayer, Integer.valueOf((Integer) soldier.get(gamePlayer)).intValue() + 1);

        return gamePlayer;
    }


    public static enum Type {
        MAFIA,
        DOCTOR,
        POLICE,
        SPY,
        SOLDIER,
        MEDIUM,
        CITIZEN;
    }
}
