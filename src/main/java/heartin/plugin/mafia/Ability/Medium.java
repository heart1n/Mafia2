package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Medium extends Ability{

    private static String[] description = {""};

    public Medium(GamePlayer gamePlayer) {

        super(gamePlayer, "Soldier", Type.CITIZEN, description);
    }

    public String[] getDescription(String[] description) {

        return description;
    }
}
