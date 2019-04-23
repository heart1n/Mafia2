package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameChat {

    private GameProcess process;

    public static Map<GamePlayer, ChatMode> playerChat = new HashMap();

    public GameChat(GameProcess process) {
        this.process = process;
    }

    public GamePlayer setMafiaChat(Player player) {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        ChatMode mode = this.playerChat.get(gamePlayer);

        mode = mode == null || mode == ChatMode.GENERAL ? ChatMode.MAFIA : ChatMode.GENERAL;

        playerChat.put(gamePlayer, mode);

        gamePlayer.getPlayer().sendMessage("§e채팅모드 §c: §r§l" + mode);

        return gamePlayer;
    }

    public GamePlayer setGeneralChat(Player player) {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        playerChat.put(gamePlayer, ChatMode.GENERAL);

        return gamePlayer;
    }

    public static enum ChatMode {
        GENERAL,
        MAFIA,
        DEATH;
    }
}
