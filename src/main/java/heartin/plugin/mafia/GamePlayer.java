package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private Ability ability;
    private final GamePlayerManager manager;
    private final UUID uniqueId;
    private final String name;
    private Player player;
    private boolean dead;

    GamePlayer(GamePlayerManager manager, Player player) {
        this.manager = manager;
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();

        setPlayer(player);
    }

    void setPlayer(Player player) {

        this.player = player;
    }

    void removePlayer() {

        this.player = null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUniqueId() {

        return this.uniqueId;
    }

    public String getName() {

        return this.name;
    }

    public boolean isDead() {
        return this.dead;
    }

    void setDead() {
        this.dead = true;
    }

    public boolean isOnline() {

        return this.player != null;
    }
}
