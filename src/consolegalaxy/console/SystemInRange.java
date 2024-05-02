package consolegalaxy.console;

import java.text.DecimalFormat;
import java.util.*;
import java.lang.Math;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.Pair;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lwjgl.util.vector.Vector2f;
import consolegalaxy.scripts.UtilClasses.SolarSystem;
import consolegalaxy.scripts.Utils;
import consolegalaxy.scripts.consolegalaxyModPlugin;

public class SystemInRange implements BaseCommand {


    protected static final String[] searchableObjects = {Tags.CRYOSLEEPER, Tags.CORONAL_TAP, Tags.GATE};
    protected static boolean searchCryo = false;
    protected static boolean searchGate = false;
    protected static boolean searchShunt = false;
    protected static boolean foundSystemsOverlapping = false;

    // Formaters
    // TODO print planet count in separate row or separate line?
    protected static final String STRING_FORMAT_OBJECTS = "|%-40s|"; // force to the left
    protected static final int TITLE_CELL_SIZE = consolegalaxyModPlugin.MAX_TABLE_WIDTH;
    protected static final int CONSTELLATION_CELL_SIZE = 25;
    protected static final int STARSYSTEM_CELL_SIZE = 46;
    protected static final int PULSAR_BLACKHOLE_CELL_SIZE = 11;
    protected static final int GATE_CELL_SIZE = 7;
    protected static final int LY_CELL_SIZE = 15;
    protected static final int NR_HORIZONTAL_LINES= 4; // number of horizontal lines separating all arguments in the middle
    protected static final String STRING_FORMAT_CONSTELLATION = "|%-" + CONSTELLATION_CELL_SIZE + "s|"; // negative value forces to the left
    protected static final String STRING_FORMAT_SYSTEM = "%-" + STARSYSTEM_CELL_SIZE + "s|"; // negative value forces to the left
    protected static final String STRING_FORMAT_PULSAR_BLACKHOLE = "%" + PULSAR_BLACKHOLE_CELL_SIZE + "s|";
    protected static final String STRING_FORMAT_GATE = "%" + GATE_CELL_SIZE + "s|";
    protected static final String STRING_FORMAT_LY = "%" + LY_CELL_SIZE + "s|"; // positive value force to the right
    // if some information can't fit in a single cell, use a second row
    protected static final String STRING_FORMAT_ROW = STRING_FORMAT_CONSTELLATION + STRING_FORMAT_SYSTEM +
            STRING_FORMAT_PULSAR_BLACKHOLE + STRING_FORMAT_GATE + STRING_FORMAT_LY;

    protected static final String STRING_FORMAT_TITLE = "|%s|";
    protected static final String HEADERS = String.format("|%s|%s|%s|%s|%s|\n|%s|%s|%s|%s|%s|",
            Utils.centerText("Constellation", CONSTELLATION_CELL_SIZE),
            Utils.centerText("System", STARSYSTEM_CELL_SIZE), Utils.centerText("Pulsar/", PULSAR_BLACKHOLE_CELL_SIZE),
            Utils.centerText("Gate", GATE_CELL_SIZE), Utils.centerText("Light-years", LY_CELL_SIZE),
            Utils.centerText("", CONSTELLATION_CELL_SIZE),
            Utils.centerText("", STARSYSTEM_CELL_SIZE), Utils.centerText("Blackhole", PULSAR_BLACKHOLE_CELL_SIZE),
            Utils.centerText("", GATE_CELL_SIZE), Utils.centerText("", LY_CELL_SIZE));

    protected static DecimalFormat df = new DecimalFormat("#.## LY");
    protected static final String SEPARATOR = "--------------------------------------------------------------------------------------------------------------";

    protected static HashMap<String, List<SolarSystem>> cryosleeperSystems = new HashMap<String, List<SolarSystem>>();
    protected static HashMap<String, List<SolarSystem>> gateSystems = new HashMap<String, List<SolarSystem>>();
    protected static HashMap<String, List<SolarSystem>> hypershuntSystems = new HashMap<String, List<SolarSystem>>();

    protected static HashMap<String, SolarSystem> solarSystems = new HashMap<String, SolarSystem>();
    protected static HashMap<String, Vector2f> cryoCoordinates = new HashMap<String, Vector2f>();
    protected static HashMap<String, Vector2f> gateCoordinates = new HashMap<String, Vector2f>();
    protected static HashMap<String, Vector2f> shuntCoordinates = new HashMap<String, Vector2f>();


    public void findObject(StarSystemAPI star_system) {
        if (!star_system.isHyperspace()) {
//            Console.showMessage(star_system.getNameWithLowercaseType() + " " + star_system.isNebula());
            String constellation = "";
            List<SectorEntityToken> cryoEntity = star_system.getEntitiesWithTag(Tags.CRYOSLEEPER);
            List<SectorEntityToken> gateEntity = star_system.getEntitiesWithTag(Tags.GATE);
            List<SectorEntityToken> shuntEntity = star_system.getEntitiesWithTag(Tags.CORONAL_TAP);

            int nrOfStars = 0;
            String pulsar_blackhole = "";
            String starInfo = "";
            String hasGate = "";

            if (!cryoEntity.isEmpty() && searchCryo) {
                try {
                    constellation = star_system.getConstellation().getName();
                } catch (Exception e) {
                    constellation = "Core World";
                }
                cryoCoordinates.put(constellation + " - " + star_system.getNameWithLowercaseType(), star_system.getLocation());
                String firstPart = String.format(STRING_FORMAT_OBJECTS, "Found " + Tags.CRYOSLEEPER + " in " + constellation);
                Console.showMessage(firstPart + " - " + star_system.getNameWithLowercaseType() + " at " + star_system.getLocation());
            }

            if (!gateEntity.isEmpty()) {
                hasGate = "Y";
                if (searchGate){
                    try {
                        constellation = star_system.getConstellation().getName();
                    } catch (Exception e) {
                        constellation = "Core World";
                    }
                    gateCoordinates.put(constellation + " - " + star_system.getNameWithLowercaseType(), star_system.getLocation());
                    String firstPart = String.format(STRING_FORMAT_OBJECTS, "Found " + Tags.GATE + " in " + constellation);
                    Console.showMessage(firstPart + " - " + star_system.getNameWithLowercaseType() + " at " + star_system.getLocation());
                }
            }

            if (!shuntEntity.isEmpty() && searchShunt) {
                try {
                    constellation = star_system.getConstellation().getName();
                } catch (Exception e) {
                    constellation = "Core World";
                }
                shuntCoordinates.put(constellation + " - " + star_system.getNameWithLowercaseType(), star_system.getLocation());
                String firstPart = String.format(STRING_FORMAT_OBJECTS, "Found " + Tags.CORONAL_TAP + " in " + constellation);
                Console.showMessage(firstPart + " - " + star_system.getNameWithLowercaseType() + " at " + star_system.getLocation());
            }

            // Inserts the star system in a hashmap, regardless of objects found
            try {
                try {
                    constellation = star_system.getConstellation().getName();
                } catch (Exception e) {
                    constellation = "Core World";
                }

                Pair<String,Integer> result = Utils.getStringTypeOfSystemAndStarNumber(star_system.getType());
                nrOfStars = result.two;
                starInfo = result.one;

                // stars are also planets
                if (star_system.getPlanets().size() - nrOfStars >= consolegalaxyModPlugin.playerPlanetCountForSystemInRange) {
                    if (star_system.hasPulsar() && star_system.hasBlackHole()) {
                        pulsar_blackhole = "P/B";
                    } else if (star_system.hasBlackHole()) {
                        pulsar_blackhole = "B";
                    } else if (star_system.hasPulsar()){
                        pulsar_blackhole = "P";
                    } // default is blank

                    solarSystems.put(star_system.getName(), new SolarSystem(constellation, star_system.getName(),
                            starInfo, pulsar_blackhole, hasGate, star_system.getLocation(),
                            star_system.getPlanets().size() - nrOfStars));
                }
            } catch (Exception e) {
                Console.showMessage("Location that doesn't have coordinates: " + star_system.getName());
            }
        }
    }

    void findCloseSystems(HashMap<String, Vector2f> objectCoordinates, String searchFor) {

        for (Map.Entry<String, Vector2f> objEntity : objectCoordinates.entrySet()) {

            HashMap<String, List<SolarSystem>> localCryosleeperSystems = new HashMap<String, List<SolarSystem>>();
            HashMap<String, List<SolarSystem>> localHypershuntSystems = new HashMap<String, List<SolarSystem>>();
            HashMap<String, List<SolarSystem>> localGateSystems = new HashMap<String, List<SolarSystem>>();
            // Table
            Console.showMessage("\n\n");
            Console.showMessage(SEPARATOR);
            Console.showMessage(String.format(STRING_FORMAT_TITLE, Utils.centerText(objEntity.getKey() + " (" + searchFor + ")", TITLE_CELL_SIZE)));
            Console.showMessage(SEPARATOR);
            Console.showMessage(HEADERS);

            for (Map.Entry<String, SolarSystem> candidateSystem : solarSystems.entrySet()) {
                SolarSystem ss = candidateSystem.getValue();
                double result = checkDistance((Vector2f) objEntity.getValue(), (Vector2f) ss.getCoords());
                ss.setResult(result);
                if (result <= consolegalaxyModPlugin.playerLightYearDistance) {
                    if (searchFor.equalsIgnoreCase("cryosleeper")) {
                        if (localCryosleeperSystems.get(ss.getConstellation()) == null) {
                            localCryosleeperSystems.put(ss.getConstellation(), new ArrayList<SolarSystem>() {
                            });
                        }
                        localCryosleeperSystems.get(ss.getConstellation()).add(ss);

                    } else if (searchFor.equalsIgnoreCase("coronal_tap")) {
                        if (localHypershuntSystems.get(ss.getConstellation()) == null) {
                            localHypershuntSystems.put(ss.getConstellation(), new ArrayList<SolarSystem>() {
                            });
                        }
                        localHypershuntSystems.get(ss.getConstellation()).add(ss);
                    } else {
                        if (localGateSystems.get(ss.getConstellation()) == null) {
                            localGateSystems.put(ss.getConstellation(), new ArrayList<SolarSystem>() {
                            });
                        }
                        localGateSystems.get(ss.getConstellation()).add(ss);
                    }
                }
            }

            // copy local variables to global scope
            cryosleeperSystems.putAll(localCryosleeperSystems);
            hypershuntSystems.putAll(localHypershuntSystems);
            gateSystems.putAll(localGateSystems);

            // print per object systems
            if (searchFor.equalsIgnoreCase("cryosleeper")) {
                sortAndPrintOutput(localCryosleeperSystems);
            } else if (searchFor.equalsIgnoreCase("coronal_tap")) {
                sortAndPrintOutput(localHypershuntSystems);
            } else {
                sortAndPrintOutput(localGateSystems);
            }
        }
    }

    void findOverlappingSystems() {
        Map<String, List<SolarSystem>> sortedCryosleeperSystems = new TreeMap<String, List<SolarSystem>>(cryosleeperSystems);
        HashMap<String,List<SolarSystem>> tmp = new HashMap<String,List<SolarSystem>>();
        // Table
        Console.showMessage("\n\n");
        Console.showMessage(SEPARATOR);
        Console.showMessage(String.format(STRING_FORMAT_TITLE, Utils.centerText("Systems within range of both the Cryosleeper and the Coronal tap:", TITLE_CELL_SIZE)));
        Console.showMessage(SEPARATOR);
        Console.showMessage(HEADERS);
        // find intersecting SolarSystems between these two Maps
        for (Map.Entry<String, List<SolarSystem>> entry : sortedCryosleeperSystems.entrySet()) {
            if (hypershuntSystems.containsKey(entry.getKey())) {
                String key = entry.getKey(); // constellation name
                foundSystemsOverlapping = true;
                for (SolarSystem _system : entry.getValue()){
                    if (hypershuntSystems.get(key).contains(_system)){
                        if (tmp.get(key) == null) tmp.put(key, new ArrayList<SolarSystem>());
                        tmp.get(key).add(_system);
                    }
                }
            }
        }

        if (!foundSystemsOverlapping) {
            Console.showMessage(String.format("|%"+TITLE_CELL_SIZE+"s|", Utils.centerText("No system in range of both the Cryosleeper and the Coronal tap.", TITLE_CELL_SIZE)));
            Console.showMessage(SEPARATOR);
        } else {
            sortAndPrintOutput(tmp);
        }
    }


    double checkDistance(Vector2f coronalCoord, Vector2f systemCoord) {
        double ac = Math.abs(coronalCoord.y - systemCoord.y);
        double cb = Math.abs(coronalCoord.x - systemCoord.x);
        return Math.hypot(ac, cb) / 2000;
    }


    public static void sortAndPrintOutput(HashMap<String, List<SolarSystem>> map) {
        Map<String, List<SolarSystem>> sortedMapByConstellation = new TreeMap<String, List<SolarSystem>>(map);
        Map<String, SolarSystem> sortedSubMapByStarSystem = new TreeMap<String, SolarSystem>();
        Map<String,Map<String, SolarSystem>> completeSort = new TreeMap<String, Map<String, SolarSystem>>();
        if (!sortedMapByConstellation.isEmpty()) {
            // sort by star system names
            for (Map.Entry<String, List<SolarSystem>> _entry : sortedMapByConstellation.entrySet()) {
                for (SolarSystem _ss : _entry.getValue()) {
                    sortedSubMapByStarSystem.put(_ss.getSystemName(), _ss);
                }
                completeSort.put(_entry.getKey(), new TreeMap<String, SolarSystem>(sortedSubMapByStarSystem));
                sortedSubMapByStarSystem.clear();
            }
        } else {
            Console.showMessage(SEPARATOR);
            Console.showMessage(String.format("|%" + consolegalaxyModPlugin.MAX_TABLE_WIDTH + "s|",
                    Utils.centerText("Couldn't find any system matching the requirements. Try with different distance in LY or planet count.",
                            TITLE_CELL_SIZE)));
            Console.showMessage(SEPARATOR);
            return;
        }
        for (Map.Entry<String, Map<String, SolarSystem>> _entry : completeSort.entrySet()) {
            for (Map.Entry<String, SolarSystem> entry : _entry.getValue().entrySet()){
                Console.showMessage(SEPARATOR);
                SolarSystem _system = entry.getValue();
                String[] _rows = splitIntoRows(_system);
                if (!_rows[1].isEmpty()) {
                    Console.showMessage(_rows[0]);
                    Console.showMessage(_rows[1]);
                } else {
                    Console.showMessage(
                            String.format(STRING_FORMAT_ROW, _system.getConstellation(),
                                    _system.getSystemName() + " " + _system.getStarInfo() +
                                            " PLANETS=" + _system.getPlanetCount(),
                                            Utils.centerText(_system.getPulsarBlackhole(), PULSAR_BLACKHOLE_CELL_SIZE),
                                            Utils.centerText(_system.getGate(), GATE_CELL_SIZE),
                                    df.format(_system.getResult())
                            )
                    );
                }
            }
        }
        Console.showMessage(SEPARATOR); // closing separator between tables
    }


    public static String[] splitIntoRows(SolarSystem entry) {

        String _constellation = entry.getConstellation();
        String _systemName = entry.getSystemName();
        String _starInfo = entry.getStarInfo();
        String _pulsarBlackhole = entry.getPulsarBlackhole();
        String _hasGate = entry.getGate();
        int _planets = entry.getPlanetCount();
        double _ly = entry.getResult();

        // If text won't fit withing the cell size, make a second row
        String firstRowConstellation = "";
        String firstRowSystem = "";
        String secondRowConstellation = "";
        String secondRowSystem = "";
        String firstRow = "";
        String secondRow = "";
        String systemAndStar = _systemName + " " + _starInfo + " planets=" + _planets;

        if (_constellation.length() > CONSTELLATION_CELL_SIZE) {
//            String addDash = "";
//            if (_constellation.substring(CONSTELLATION_CELL_SIZE - 1, CONSTELLATION_CELL_SIZE).equalsIgnoreCase(" ") ||
//                    _constellation.substring(CONSTELLATION_CELL_SIZE - 2, CONSTELLATION_CELL_SIZE -1).equalsIgnoreCase(" ")){
//                addDash = "-";
//            }
            String addDash = _constellation.substring(CONSTELLATION_CELL_SIZE - 1, CONSTELLATION_CELL_SIZE).equalsIgnoreCase(" ") ? "" : "-";
            firstRowConstellation = _constellation.substring(0, CONSTELLATION_CELL_SIZE - 1) + addDash;
            secondRowConstellation = _constellation.substring(CONSTELLATION_CELL_SIZE - 1);
        } else {
            firstRowConstellation = _constellation;
        }

        if (systemAndStar.length() > STARSYSTEM_CELL_SIZE) {
            String addDash = systemAndStar.substring(STARSYSTEM_CELL_SIZE - 1, STARSYSTEM_CELL_SIZE).equalsIgnoreCase(" ") ? "" : "-";
            firstRowSystem = systemAndStar.substring(0, STARSYSTEM_CELL_SIZE - 1) + addDash;
            secondRowSystem = systemAndStar.substring(STARSYSTEM_CELL_SIZE - 1);
        } else {
            firstRowSystem = systemAndStar;
        }

        if (!secondRowConstellation.isEmpty() || !secondRowSystem.isEmpty()) {
            firstRow = String.format(STRING_FORMAT_ROW, firstRowConstellation, firstRowSystem,
                    Utils.centerText(_pulsarBlackhole, PULSAR_BLACKHOLE_CELL_SIZE),
                    Utils.centerText(_hasGate, GATE_CELL_SIZE),
                    df.format(_ly));
            secondRow = String.format(STRING_FORMAT_ROW, secondRowConstellation, secondRowSystem, "", "", "");
        }
        return new String[]{firstRow, secondRow};
    }

    @Override
    public CommandResult runCommand(String args, CommandContext context) {


        if (!context.isInCampaign()) {
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        if (args.isEmpty()) {
            Console.showMessage("Arguments required.");
            return CommandResult.BAD_SYNTAX;
        } else {
            final String[] tmp = args.toLowerCase().split(" ");

            if (tmp.length < 1) {
                Console.showMessage("You must pass in at least ONE of argument: name of the object(s): coronal_tap OR coronal_tap,cryosleeper.");
                return CommandResult.BAD_SYNTAX;
            }

            String[] objects = tmp[0].split(",");

            for (String _object : objects) {
                if (Arrays.asList(searchableObjects).contains(_object)) {
                    if (_object.equalsIgnoreCase(Tags.CRYOSLEEPER)) searchCryo = true;
                    if (_object.equalsIgnoreCase(Tags.GATE)) searchGate = true;
                    if (_object.equalsIgnoreCase(Tags.CORONAL_TAP)) searchShunt = true;
                } else {
                    Console.showMessage("Can't search for entity: " + tmp[0] + ". Try one or more of the following: " + Arrays.toString(searchableObjects));
                    return CommandResult.BAD_SYNTAX;
                }
            }

            if (tmp.length > 1){
                try {
                    consolegalaxyModPlugin.playerLightYearDistance = Double.parseDouble(tmp[1]);
                } catch (NumberFormatException e) {
                    Console.showMessage("You must type in an integer. You typed " + tmp[1]);
                    return CommandResult.BAD_SYNTAX;
                }
            }

            // optional argument planets=
            if (tmp.length == 3) {
                try {
                    consolegalaxyModPlugin.playerPlanetCountForSystemInRange = Integer.parseInt(tmp[2].substring(8)); //planets=1
                } catch (Exception e) {
                    Console.showMessage("You must enter a valid integer for planets: " + tmp[2].substring(6));
                    return CommandResult.BAD_SYNTAX;
                }
            }

            Console.showMessage("\n");
            Console.showMessage("Argumnets: " + Arrays.toString(tmp));
            for (StarSystemAPI ss : Global.getSector().getStarSystems()) {
                // finds entities and also takes no on all the coordinates of each star system
                findObject(ss);
            }
            if (searchShunt) findCloseSystems(shuntCoordinates, "coronal_tap");
            if (searchCryo) findCloseSystems(cryoCoordinates, "cryosleeper");
            if (searchGate) findCloseSystems(gateCoordinates, "inactive_gate");

            if (searchCryo && searchShunt) {
                findOverlappingSystems();
            }
            Console.showMessage(String.format("|%" + TITLE_CELL_SIZE + "s|", Utils.centerText(" ", TITLE_CELL_SIZE)));
            Console.showMessage(String.format("|%" + TITLE_CELL_SIZE + "s|", Utils.centerText("END OF QUERY", TITLE_CELL_SIZE)));
            Console.showMessage(String.format("|%" + TITLE_CELL_SIZE + "s|", Utils.centerText(" ", TITLE_CELL_SIZE)));
            Console.showMessage(SEPARATOR + "\n");
            // clear all variables
            cryosleeperSystems.clear();
            gateSystems.clear();
            hypershuntSystems.clear();
            solarSystems.clear();

            cryoCoordinates.clear();
            gateCoordinates.clear();
            shuntCoordinates.clear();

            searchCryo = false;
            searchGate = false;
            searchShunt = false;
            foundSystemsOverlapping = false;

            consolegalaxyModPlugin.playerLightYearDistance = 10;
            consolegalaxyModPlugin.playerPlanetCountForSystemInRange = 1;

            return CommandResult.SUCCESS;
        }
    }
}
