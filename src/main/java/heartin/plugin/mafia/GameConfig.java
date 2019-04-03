package heartin.plugin.mafia;

import org.bukkit.configuration.file.FileConfiguration;

public class GameConfig {
    public static int dayTicks;
    public static int nightTicks;
    public static int mafiaNumber;
    public static int citizenNumber;
    public static int selectTicks;


    static void load(FileConfiguration config) {

        selectTicks = config.getInt("select-ticks");
        dayTicks = config.getInt("day-ticks");
        nightTicks = config.getInt("night-ticks");
        mafiaNumber = config.getInt("mafia-number");
        citizenNumber = config.getInt("citizen-number");

    }

    private GameConfig() {
    }
}
