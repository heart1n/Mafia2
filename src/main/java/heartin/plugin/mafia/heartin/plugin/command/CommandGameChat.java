package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GameChat;
import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandGameChat extends CommandComponent {

    private GameProcess process;
    public static Map<GamePlayer, Enum> playerChat = new HashMap();

    public CommandGameChat() {
        super(null, "채팅바꾸기", "mafia.chat");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        Player player = (Player) sender;

        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        if (!playerChat.containsValue(GameChat.ChatMode.MAFIA)) {

            if (process.getPlayerManager().getOnlineMafia().contains(gamePlayer)) {
                playerChat.put(gamePlayer, GameChat.ChatMode.MAFIA);
                player.sendMessage("마피아 채팅에 입장합니다.");
            } else {
                player.sendMessage("당신은 마피아가 아닙니다.");
            }
        } else {
            playerChat.remove(gamePlayer);
            player.sendMessage("마피아 채팅을 나갑니다");
        }


        return true;
    }
}
