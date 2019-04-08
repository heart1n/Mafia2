package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.Ability.Ability;
import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GamePlugin;
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

        GameProcess process = GamePlugin.getInstance().getProcess();

        Ability mafia = process.getPlayerManager().getMafia(player);

        if (mafia.abilityType() == Ability.Type.MAFIA) {

            process.getChat().setMafiaChat(player);
        } else {
            player.sendMessage("당신은 마피아가 아닙니다.");
        }

        return true;
    }
}