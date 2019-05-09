package heartin.plugin.mafia;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameChat {

    private GameProcess process;

    public static Map<GamePlayer, ChatMode> playerChat = new HashMap();
    private Map<GamePlayer, Boolean> mafiaChat = new HashMap();


    public GameChat(GameProcess process) {
        this.process = process;
    }

    public GamePlayer changePlayerChat(Player player) {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);
        ChatMode mode = this.playerChat.get(gamePlayer);

        if (mafiaChat.get(gamePlayer)) {
            mode = mode == null || mode == ChatMode.GENERAL ? ChatMode.MAFIA : ChatMode.GENERAL;

            playerChat.put(gamePlayer, mode);

            gamePlayer.getPlayer().sendMessage(Message.SYSTEM + "§e채팅모드 §c: §r§l" + mode);
        } else {
            player.sendMessage(Message.SYSTEM + "마피아만 사용할 수 있습니다.");
        }


        return gamePlayer;
    }

    public GamePlayer setMafiachat(GamePlayer gamePlayer) {
        this.mafiaChat.put(gamePlayer, true);

        return gamePlayer;
    }

    public void clearChat() {
        this.mafiaChat.clear();
        this.playerChat.clear();
    }

    public GamePlayer setGeneralChat(Player player) {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        this.playerChat.put(gamePlayer, ChatMode.GENERAL);
        this.mafiaChat.put(gamePlayer, false);

        return gamePlayer;
    }

    public static enum ChatMode {
        GENERAL,
        MAFIA,
        DEATH;
    }
}
