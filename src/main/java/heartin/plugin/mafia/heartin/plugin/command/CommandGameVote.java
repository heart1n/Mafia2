package heartin.plugin.mafia.heartin.plugin.command;

import heartin.plugin.mafia.GamePlayer;
import heartin.plugin.mafia.GamePlugin;
import heartin.plugin.mafia.GameProcess;
import nemo.mc.command.ArgumentList;
import nemo.mc.command.bukkit.CommandComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGameVote extends CommandComponent {


   // public static Map<String, Integer> vote = new HashMap();


    public CommandGameVote() {
        super("<player>", "해당 플레이어에게 투표를 합니다", "mafia.vote", 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String componentLabel, ArgumentList args) {


        Player player = (Player) sender;

        GameProcess process = GamePlugin.getInstance().getProcess();
        GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);

        String name = gamePlayer.getName();
        name = args.next();

       /*     if (process.getVote().getVote(gamePlayer)) {

                if (!process.getPlayerManager().getGamePlayer(gamePlayer).isDead() // 클릭한 상대 호출
                ) {
                   // vote.put(name, Integer.valueOf((Integer) vote.get(name)).intValue() + 1);
                    process.getVote().removeVote(gamePlayer);
                    sender.sendMessage(name + "에게 투표를 합니다.");

                //    Bukkit.broadcastMessage(name + ": " + vote.get(name) + "표");

                } else {
                    sender.sendMessage("죽은 플레이어 입니다.");
                }
            } else {
                sender.sendMessage("투표권이 없습니다");
            }*/

        return true;
    }
}
