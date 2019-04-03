package heartin.plugin.mafia;

import org.bukkit.event.Listener;

public class GameListener  implements Listener {

    private final GameProcess process;

    GameListener(GameProcess process)
    {
        this.process = process;
    }
}
