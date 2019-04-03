package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GamePlugin;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandGameStart extends CommandComponent {

    public CommandGameStart() {

        super(null, "게임을 시작합니다", "mafia.start");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {
        if (GamePlugin.getInstance().processStart()) {
            sender.sendMessage("게임을 시작합니다.");
        } else {
            sender.sendMessage("게임이 진행 중 입니다.");
        }

        return true;
    }
}
