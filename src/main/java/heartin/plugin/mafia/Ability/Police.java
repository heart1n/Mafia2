package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public final class Police extends Ability {

    private static String[] description = {""};

    public Police(GamePlayer gamePlayer) {

        super(gamePlayer, "Police", Type.CITIZEN, description);
    }

    public String[] getDescription(String[] description)

    {
        return description;
    }

}
