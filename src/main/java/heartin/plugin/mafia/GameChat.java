package heartin.plugin.mafia;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameChat {

    private GameProcess process;

    public static Map<GamePlayer, Enum> playerChat = new HashMap();

    public GameChat(GameProcess process)
    {
        this.process = process;
    }

    public GamePlayer setMafiaChat(Player player)
    {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        if (playerChat.containsValue(ChatMode.GENERAL))
        {
            playerChat.put(gamePlayer, ChatMode.MAFIA);
            gamePlayer.getPlayer().sendMessage("마피아 채팅에 입장합니다");
        }
        else
        {
            playerChat.remove(gamePlayer);
            gamePlayer.getPlayer().sendMessage("마피아 채팅을 나갑니다");
        }
        return gamePlayer;
    }

    public GamePlayer setGeneralChat(Player player)
    {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        playerChat.put(gamePlayer, ChatMode.GENERAL);

        return gamePlayer;
    }


    public static enum ChatMode
    {
        GENERAL,
        MAFIA,
        DEATH;
    }
}
