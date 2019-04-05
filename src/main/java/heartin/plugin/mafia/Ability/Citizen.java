package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Citizen extends Ability {

    private static String[] description = {""};

    public Citizen(GamePlayer gamePlayer) {
        super(gamePlayer, "Citizen", Type.CITIZEN,  description);
    }

    public String[] getDescription(String[] description) {
        return description;
    }

}
