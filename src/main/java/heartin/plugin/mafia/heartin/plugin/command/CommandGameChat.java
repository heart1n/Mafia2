package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GameChat;
import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GamePlugin;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGameChat extends CommandComponent {

    public CommandGameChat() {

        super(null, "채팅바꾸기", "mafia.chat");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        Player player = (Player) sender;



            GameProcess process = GamePlugin.getInstance().getProcess();

            for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia()) {
                gamePlayer.getPlayer().sendMessage("난 마피아다");

                //GamePlayer mafia = (GamePlayer) gamePlayer;

                // if (mafia.isOnline()) {
                //      process.getChat().setMafiaChat(player);
                //  } else {
                //     player.sendMessage("당신은 마피아가 아닙니다.");
                //  }
            }

            //GamePlayer mafia = (GamePlayer) process.getPlayerManager().getOnlineMafia();

            // if (mafia.isOnline()) {
            //      process.getChat().setMafiaChat(player);
            //  } else {
            //     player.sendMessage("당신은 마피아가 아닙니다.");
            //  }

            return true;
        }
    }
