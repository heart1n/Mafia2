package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandGameChat extends CommandComponent {

    private GameProcess process;

    public  CommandGameChat()
    {
        super(null, "채팅바꾸기" , "mafia.chat");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlineMafia())
        {

        }


        return super.onCommand(sender, command, label, componentLabel, args);
    }
}
