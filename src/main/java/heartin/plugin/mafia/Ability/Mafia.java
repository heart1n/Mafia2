package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Mafia extends Ability {

    private static String[] description = {"당신은 마피아 입니다"};

    public Mafia(GamePlayer gamePlayer) {

        super(gamePlayer, "Mafia", Type.MAFIA, description);
    }


    public String[] description()
    {
        return description;
    }


    public Enum type()
    {
        return abilityType();
    }

}
