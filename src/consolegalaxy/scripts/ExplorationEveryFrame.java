package consolegalaxy.scripts;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
// TODO mozda za obavestenja poput misija
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import java.util.HashMap;
import java.util.HashSet;


public class ExplorationEveryFrame implements EveryFrameScript {

//    private static final Logger log = Global.getLogger(ExplorationEveryFrame.class);
    protected IntervalUtil interval = new IntervalUtil(1.0f, 3.0f);
    protected boolean notRunYet = true;
    protected boolean gameSpeedChanged = false;

    protected int counter = 0;
    protected SectorAPI sector;
    protected CampaignFleetAPI playerFleet;
    protected Vector2f proximityInSystem = new Vector2f(200f,200f);
    protected Vector2f proximityInHyperspace = new Vector2f(200f,200f);
    // coordinates, system name
    protected HashMap<Vector2f,String> systemLocation = new HashMap<Vector2f,String>();
    // system name, goodies
    protected  HashMap<String,String> bestCharacteristicOfSystem = new HashMap<String,String>();

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {

        interval.advance(amount);

        if (!Global.getSector().isInNewGameAdvance() && !Global.getSector().getCampaignUI().isShowingMenu() &&
                !Global.getSector().getCampaignUI().isShowingDialog() && interval.intervalElapsed()) {

            // TODO ne radi kako treba
            Utils.showNotification("Is time fast? " + Global.getSector().isFastForwardIteration());

            if (notRunYet) {
                // set up everything
                sector = Global.getSector();
                playerFleet = sector.getPlayerFleet();
                runAtStart();
                getAllLocationCoordinates();
                notRunYet = false;
            }

            CampaignFleetAPI playerFleet = sector.getPlayerFleet();

            if (playerFleet.isInHyperspace()){
                interval.forceIntervalElapsed();
                checkPlayerProximityToSystem();
            } else {
                interval.forceIntervalElapsed();
                checkPlayerProximityToPlanet();
            }

        }
    }

    public void runAtStart(){
        Vector2f playerLoc = sector.getPlayerFleet().getLocation();
        sector.getCampaignUI().addMessage(
                "Current player location: %s,%s",
                Misc.getTextColor(),
                String.valueOf(playerLoc.getX()), // highlight 1
                String.valueOf(playerLoc.getY()), // highlight 2
                Misc.getHighlightColor(),
                Misc.getHighlightColor()
        );
    }

    public void checkPlayerProximityToSystem(){
        Utils.showNotification("Player in hyperspace.");
    }

    public void checkPlayerProximityToPlanet(){
        Utils.showNotification("Player in star system.");
//        getAllPlanetDataInSystem();
        for (PlanetAPI planet : sector.getPlayerFleet().getStarSystem().getPlanets()){
            Utils.showNotification(planet.getFullName() + " - "+ planet.getTypeId());
        }
    }

    public void getAllLocationCoordinates() {
        //List<SectorEntityToken> jumpPoints = sector.getEntitiesWithTag(Tags.JUMP_POINT);
        for (StarSystemAPI starSystem : sector.getStarSystems()) {
            if (starSystem != null) {
                // ignores Core World and other non-procgen systems
                if (starSystem.getConstellation() != null) {
                    systemLocation.put(starSystem.getLocation(), starSystem.getNameWithLowercaseType());
                }
                getAllPlanetDataInSystem(starSystem);
            }
        }
    }

    public void getAllPlanetDataInSystem(StarSystemAPI starSystem){
        for (PlanetAPI planet : starSystem.getPlanets()) {
            if (!planet.isStar()) {
                HashSet<String> conditions = new HashSet<String>();
                for (MarketConditionAPI cond : planet.getMarket().getConditions()){
                    conditions.add(cond.getName());
                }
            }
        }
    }
}
