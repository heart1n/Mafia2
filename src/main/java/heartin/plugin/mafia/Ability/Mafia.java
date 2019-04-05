package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Mafia extends Ability {

    private static String[] description = {""};

    public Mafia(GamePlayer gamePlayer) {

        super(gamePlayer, "Mafia", Type.MAFIA, description);
    }

    public String[] getDescription(String[] description)
    {
        return description;
    }

}
