package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GameProcess;

import java.util.HashMap;
import java.util.Map;

public class Soldier extends Ability{

    private GameProcess process;

    private static String[] description = {""};


    public Soldier(GamePlayer gamePlayer)
    {
        super(gamePlayer, "군인", Type.SOLDIER, description);
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
