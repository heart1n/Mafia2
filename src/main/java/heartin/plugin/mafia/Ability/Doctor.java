package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Doctor extends Ability {

    private static String[] description = {""};

    public Doctor(GamePlayer gamePlayer) {

        super(gamePlayer, "의사", Type.DOCTOR, description);
    }

    public String[] getDescription(String[] description)
    {
        return description;
    }

    public String getAbilityType(String abilityType)
    {
        return abilityType;
    }

}
