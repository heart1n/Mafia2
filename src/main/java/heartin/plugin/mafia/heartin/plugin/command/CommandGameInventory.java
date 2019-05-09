package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.Ability.Ability;
import heartin.plugin.mafia.GameInventory;
import heartin.plugin.mafia.GamePlugin;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGameInventory extends CommandComponent {


    private GameProcess process;


    public CommandGameInventory() {

        super(null, "채팅바꾸기", "mafia.chat");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        Player player = (Player) sender;

        GameProcess process = GamePlugin.getInstance().getProcess();

        process.getInventory().show(process.getPlayerManager().getGamePlayer(player));

        return true;
    }

}
