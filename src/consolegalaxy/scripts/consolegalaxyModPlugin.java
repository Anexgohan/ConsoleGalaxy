package consolegalaxy.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import consolegalaxy.campaign.listeners.LocationChangeListener;
import consolegalaxy.campaign.listeners.EntityDiscoveryListener;
import consolegalaxy.campaign.listeners.PlanetaryConditionsListener;
import consolegalaxy.campaign.listeners.PlanetDiscoveryListener;
import org.json.JSONObject;


public class consolegalaxyModPlugin extends BaseModPlugin {

    private static boolean settingsAlreadyRead = false;
    public static final String ID = "consolegalaxy";
    public static final String SETTINGS_PATH = "data/config/settings.json";

    public static boolean enableChatterCompanion; // unused
    public static int MAX_TABLE_WIDTH;
    // Explore
    public static boolean excludeClaimedSystems;
    public static boolean playerSurveyAll;
    public static int playerPlanetCount;
    public static boolean hasGate;
    public static int playerStableLocCount;
    public static int playerJumpPointsCount;
    public static boolean filterByHazard;
    public static int maxHazard;
    // SystemInRange
    public static double playerLightYearDistance;
    public static int playerPlanetCountForSystemInRange;

    @Override
    public void onGameLoad(boolean newGame) {
        try {
            if (!settingsAlreadyRead) {
//                JSONObject cfg = Global.getSettings().getMergedJSONForMod(SETTINGS_PATH, ID);
                JSONObject cfg = Global.getSettings().loadJSON(SETTINGS_PATH, ID);

                enableChatterCompanion = cfg.getBoolean("enableChatterCompanion");

                excludeClaimedSystems = cfg.getBoolean("excludeClaimedSystems");
                playerSurveyAll = cfg.getBoolean("playerSurveyAll");
                playerPlanetCount = cfg.getInt("playerPlanetCount");
                hasGate = cfg.getBoolean("hasGate");
                playerStableLocCount = cfg.getInt("playerStableLocCount");
                playerJumpPointsCount = cfg.getInt("playerJumpPointsCount");
                filterByHazard = cfg.getBoolean("filterByHazard");
                maxHazard = cfg.getInt("maxHazard");

                playerLightYearDistance = (double) cfg.getInt("playerLightYearDistance");
                playerPlanetCountForSystemInRange = cfg.getInt("playerPlanetCountForSystemInRange");

                MAX_TABLE_WIDTH = cfg.getInt("MAX_TABLE_WIDTH");

                settingsAlreadyRead = true;
            }
        } catch (Exception e) {
            String stackTrace = "";
            for(int i = 0; i < e.getStackTrace().length; i++) {
                StackTraceElement ste = e.getStackTrace()[i];
                stackTrace += "    " + ste.toString() + System.lineSeparator();
            }
            Global.getLogger(consolegalaxyModPlugin.class).error(e.getMessage() + System.lineSeparator() + stackTrace);
        }
    }
}
