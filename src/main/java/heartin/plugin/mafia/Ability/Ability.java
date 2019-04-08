package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Ability {


    private final GamePlayer gamePlayer;
    private final String abilityName;
    private final String[] description;
    private Type type;

    public Ability(GamePlayer gamePlayer, String abilityName, Type type,  String[] decription) {

        this.gamePlayer = gamePlayer;
        this.abilityName = abilityName;
        this.description = decription;
        this.type = type;
    }

    public String[] description()
    {
        return  description;
    }

    public Enum abilityType()
    {
        return this.type;
    }

    public static enum Type
    {
        MAFIA,
        CITIZEN;
    }
}
