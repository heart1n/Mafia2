package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Soldier extends Ability{

    private static String[] description = {""};

    public Soldier(GamePlayer gamePlayer) {
        super(gamePlayer, "Soldier", Type.CITIZEN, description);
    }

    public String[] getDescription(String[] description)
    {
        return description;
    }

    public String getAbilityName(String abilityName)
    {
        return abilityName;
    }

}
