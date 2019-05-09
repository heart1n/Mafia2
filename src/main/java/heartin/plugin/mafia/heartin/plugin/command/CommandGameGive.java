package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GamePlugin;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandGameGive extends CommandComponent {


    private GameProcess process;

    public CommandGameGive() {

        super(null, "막대기", "mafia.give");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {

        ItemStack itemStack = new ItemStack(Material.STICK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§6도구");
        itemStack.setItemMeta(itemMeta);


        Player player = (Player) sender;

        GameProcess process = GamePlugin.getInstance().getProcess();

        process.getInventory().show(process.getPlayerManager().getGamePlayer(player));

        for(GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers())
        {
            player.getInventory().addItem(itemStack);
        }



        return true;
    }
}
