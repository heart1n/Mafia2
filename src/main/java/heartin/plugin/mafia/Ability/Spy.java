package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Spy extends  Ability {

    private static String[] description = {""};

    public Spy(GamePlayer gamePlayer) {

        super(gamePlayer, "Spy", Type.MAFIA , description);
    }

    public String[] getDescription(String[] description) {

        return description;
    }
}
