package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Ability {


    private final GamePlayer gamePlayer;
    private final String abilityName;
    private final int abilityCode;
    private final String[] description;
    private Type type;

    public Ability(GamePlayer gamePlayer, String abilityName, Type type, int abilityCode, String[] decription) {

        this.gamePlayer = gamePlayer;
        this.abilityName = abilityName;
        this.description = decription;
        this.type = type;
        this.abilityCode = abilityCode;
    }

    public void description()
    {
    }

    public static enum Type
    {
        MAFIA,
        CITIZEN;
    }
}
