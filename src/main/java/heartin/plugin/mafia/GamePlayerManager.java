package heartin.plugin.mafia;

import com.sun.org.apache.xpath.internal.operations.Bool;
import heartin.plugin.mafia.Ability.*;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

public final class GamePlayerManager {

    private final GameProcess process;
    private final Map<GamePlayer, Enum> playerChat;
    private final Map<UUID, GamePlayer> playersByUniqueId;
    private final Map<Player, GamePlayer> playersByPlayer;
    private final Map<GamePlayer, Ability> playerAbility;
    private final Set<GamePlayer> onlineCitizen;
    private final Set<GamePlayer> onlineMafia;
    private final Set<GamePlayer> onlineDoctor;
    private final Set<GamePlayer> onlinePolice;
    private final Set<GamePlayer> onlineMedium;
    private final Set<GamePlayer> onlineSoldier;
    private final Set<GamePlayer> onlineSpy;
    private Set<GamePlayer> unmodifiableMafia;
    private Set<GamePlayer> unmodifiableDoctor;
    private Set<GamePlayer> unmodifiablePolice;
    private Set<GamePlayer> unmodifiableCitizen;
    private Set<GamePlayer> unmodifiableSpy;
    private Set<GamePlayer> unmodifiableSoldier;
    private Set<GamePlayer> unmodifiableMedium;
    private Collection<GamePlayer> unmodifiablePlayers;
    private Collection<GamePlayer> unmodifiableOnlinePlayers;

    GamePlayerManager(GameProcess process) {
        this.process = process;

        Collection<? extends Player> players = process.getPlugin().getServer().getOnlinePlayers();
        int size = players.size();

        Map playersByUniqueId = new HashMap(size);
        Map playersByPlayer = new IdentityHashMap(size);
        Map playerAbility = new HashMap(size);
        Map playerChat = new HashMap();


        for (Player player : players) {
            GameMode mode = player.getGameMode();

          /* if ((mode == GameMode.CREATIVE) || (mode == GameMode.SPECTATOR)) {
                continue;
            } */
            GamePlayer gamePlayer = new GamePlayer(this, player);

            playersByUniqueId.put(gamePlayer.getUniqueId(), gamePlayer);
            playersByPlayer.put(player, gamePlayer);
        }

        if (playersByUniqueId.size() < 4) {
            throw new IllegalArgumentException("게임의 필요한 인원이 부족합니다 (최소 4명)");
        }
        this.playersByUniqueId = playersByUniqueId;
        this.playersByPlayer = playersByPlayer;
        this.playerChat = playerChat;
        this.playerAbility = playerAbility;
        this.onlineCitizen = new HashSet(size);
        this.onlineMafia = new HashSet(size);
        this.onlineDoctor = new HashSet(size);
        this.onlinePolice = new HashSet(size);
        this.onlineMedium = new HashSet(size);
        this.onlineSoldier = new HashSet(size);
        this.onlineSpy = new HashSet(size);
    }

    void registerGamePlayer(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        if (gamePlayer != null) {
            gamePlayer.setPlayer(player);
        }
    }

    void unregisterGamePlayer(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByPlayer.remove(player);

        if (gamePlayer != null) {
            gamePlayer.removePlayer();
            onlineSpy.remove(gamePlayer);
            onlineSoldier.remove(gamePlayer);
            onlineMedium.remove(gamePlayer);
            onlineCitizen.remove(gamePlayer);
            onlineDoctor.remove(gamePlayer);
            onlineMafia.remove(gamePlayer);
            onlinePolice.remove(gamePlayer);
        }
    }

    public GamePlayer setMafiaChat(Player player) {
        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        if (!playerChat.values().contains(GameChat.ChatMode.MAFIA)) {
            playerChat.put(gamePlayer, GameChat.ChatMode.MAFIA);
        } else {
            playerChat.remove(gamePlayer, GameChat.ChatMode.MAFIA);
            playerChat.put(gamePlayer, GameChat.ChatMode.GENERAL);
        }

        return gamePlayer;
    }

    public GamePlayer setMafia(Player player) {

        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Mafia(gamePlayer));
        this.onlineMafia.add(gamePlayer);
        //  process.getScoreboard().setMafia(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineMafia() {

        Set mafia = this.unmodifiableMafia;

        if (mafia == null) {
            this.unmodifiableMafia = (mafia = Collections.unmodifiableSet(this.onlineMafia));
        }

        return mafia;
    }

    public GamePlayer setCitizen(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Citizen(gamePlayer));
        this.onlineCitizen.add(gamePlayer);
        //   process.getScoreboard().setCitizen(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineCitizen() {

        Set citizen = this.unmodifiableCitizen;

        if (citizen == null) {
            this.unmodifiableCitizen = (citizen = Collections.unmodifiableSet(this.onlineCitizen));
        }
        return citizen;
    }


    public GamePlayer setDoctor(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Doctor(gamePlayer));
        this.onlineDoctor.add(gamePlayer);
        //  process.getScoreboard().setDoctor(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineDoctor() {

        Set doctor = this.unmodifiableDoctor;

        if (doctor == null) {
            this.unmodifiableDoctor = (doctor = Collections.unmodifiableSet(this.onlineDoctor));
        }
        return doctor;
    }

    public GamePlayer setPolice(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Police(gamePlayer));
        this.onlinePolice.add(gamePlayer);
        //  process.getScoreboard().setPolice(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlinePolice() {

        Set police = this.unmodifiablePolice;

        if (police == null) {
            this.unmodifiablePolice = (police = Collections.unmodifiableSet(this.onlinePolice));
        }
        return police;
    }

    public GamePlayer setSpy(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Spy(gamePlayer));
        this.onlineSpy.add(gamePlayer);
        //  process.getScoreboard().setSpy(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineSpy() {

        Set spy = this.unmodifiableSpy;

        if (spy == null) {
            this.unmodifiableSpy = (spy = Collections.unmodifiableSet(this.onlineSpy));
        }
        return spy;
    }

    public GamePlayer setSoldier(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Citizen(gamePlayer));
        this.onlineSoldier.add(gamePlayer);
        // process.getScoreboard().setSoldier(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineSoldier() {

        Set soldier = this.unmodifiableSoldier;

        if (soldier == null) {
            this.unmodifiableSoldier = (soldier = Collections.unmodifiableSet(this.onlineSoldier));
        }
        return soldier;
    }

    public GamePlayer setMedium(Player player) {
        GamePlayer gamePlayer = (GamePlayer) this.playersByUniqueId.get(player.getUniqueId());

        this.playerAbility.put(gamePlayer, new Citizen(gamePlayer));
        this.onlineMedium.add(gamePlayer);
        // process.getScoreboard().setMedium(gamePlayer.getName());

        return gamePlayer;
    }

    public Set<GamePlayer> getOnlineMedium() {

        Set medium = this.unmodifiableMedium;

        if (medium == null) {
            this.unmodifiableMedium = (medium = Collections.unmodifiableSet(this.onlineMedium));
        }
        return medium;
    }

    public GamePlayer getGamePlayer(Player player) {

        return (GamePlayer) this.playersByPlayer.get(player);
    }

    public GamePlayer getGamePlayer(UUID uniqueId) {
        return this.playersByUniqueId.get(uniqueId);
    }

    private Collection<GamePlayer> players;

    public Collection<GamePlayer> getPlayers() {
        Collection<GamePlayer> players = this.players;

        if (players == null)
            this.players = players = Collections.unmodifiableCollection(this.playersByUniqueId.values());

        return players;
    }

    private Collection<GamePlayer> onlinePlayers;

    public Collection<GamePlayer> getOnlinePlayers() {
        Collection<GamePlayer> onlinePlayers = this.onlinePlayers;

        if (onlinePlayers == null)
            this.onlinePlayers = onlinePlayers = Collections.unmodifiableCollection(playersByPlayer.values());

        return onlinePlayers;
    }
}
