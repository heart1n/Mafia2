package heartin.plugin.mafia;

import nemo.mc.ChatType;
import nemo.mc.packet.Packet;
import nemo.mc.text.TextComponent;
import nemo.mc.text.TextComponentBuilder;
import org.bukkit.entity.Player;

public class Message {

    public static String SYSTEM = "§r[§bMessage§r]";

    public static void sendCitizen(GamePlayer gamePlayer) {

        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: 시민");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

    public static void sendMafia(GamePlayer gamePlayer)
    {
        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: §c마피아");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

    public static void sendDoctor(GamePlayer gamePlayer)
    {
        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: §a의사");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

    public static void sendPolice(GamePlayer gamePlayer)
    {
        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: §b경찰");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

    public static void sendGhost(GamePlayer gamePlayer)
    {
        Player player = gamePlayer.getPlayer();

        TextComponentBuilder builder = TextComponent.builder();
        builder.object().text("§6당신의 §r직업 §r: §b유령");

        Packet.INFO.chat(builder.build(), ChatType.GAME_INFO).send((Player) player);
    }

}
