package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;

public class Politician extends Ability{


    private static String[] description = {"당신은 마피아 입니다"};

    public Politician(GamePlayer gamePlayer) {

        super(gamePlayer, "정치인", Type.POLITICIAN, description);
    }


    public String[] description()
    {
        return description;
    }


    public Enum type()
    {
        return getAbilityType();
    }
}
