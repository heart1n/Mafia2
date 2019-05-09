package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public final class Police extends Ability {

    private static String[] description = {""};

    public Police(GamePlayer gamePlayer) {

        super(gamePlayer, "경찰", Type.POLICE, description);
    }

    public String[] getDescription(String[] description)

    {
        return description;
    }

}
