package heartin.plugin.mafia.Ability;

import heartin.plugin.mafia.GamePlayer;
import nemo.mc.ChatType;
import nemo.mc.packet.Packet;
import nemo.mc.text.TextComponent;
import nemo.mc.text.TextComponentBuilder;
import org.bukkit.entity.Player;

public class Citizen extends Ability {

    private static String[] description = {""};

    public Citizen(GamePlayer gamePlayer) {
        super(gamePlayer, "Citizen", Type.CITIZEN,  description);
    }

    public String[] getDescription(String[] description) {
        return description;
    }


    public static void sendCitizen(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: 시민");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

}
