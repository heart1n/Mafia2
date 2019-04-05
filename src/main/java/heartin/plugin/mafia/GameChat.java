package heartin.plugin.mafia;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameChat {

    private GameProcess process;

    public static Map<GamePlayer, Enum> playerChat = new HashMap();

    public GamePlayer setMafiaChat(Player player)
    {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);
        playerChat.put(gamePlayer, ChatMode.MAFIA);

        return gamePlayer;
    }

    public static enum ChatMode
    {
        GENERAL,
        MAFIA,
        DEATH;
    }
}
