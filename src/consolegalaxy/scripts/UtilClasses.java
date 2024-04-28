package consolegalaxy.scripts;

import org.lwjgl.util.vector.Vector2f;

import java.util.*;

public class UtilClasses {

    public static class Planet{
        protected String type;
        protected String name;
        protected String faction;
        protected int hazard;
        protected List<String> conditions =  new ArrayList<String>();

        public Planet(){
        }

        public Planet(String name, String type, String faction, int hazard, List<String> conditions){
            this.name = name;
            this.type = type;
            this.faction = faction;
            this.hazard = hazard;
            this.conditions = conditions;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getFaction() {
            return faction;
        }

        public int getHazard() {
            return hazard;
        }

        public String getHazardAsString() {
            return hazard+"%";
        }

        public List<String> getConditions() {
            return conditions;
        }
        public String getConditionsAsString(){
            return conditions.toString();
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFaction(String faction) {
            this.faction = faction;
        }

        public void setHazard(int hazard) {
            this.hazard = hazard;
        }

        public void setConditions(List<String> conditions) {
            this.conditions = conditions;
        }

        public void addCondition(String condition) {
            this.conditions.add(condition);
        }
    }

    public static class SolarSystem {
        protected String constellation;
        protected String systemName;
        protected String starInfo;
        protected String pulsarBlackhole = "";
        protected String starTypes = "";
        protected Vector2f coords;
        protected int nrOfStars = 0;
        protected int planetCount = 0;
        protected int jumpPointCount = 0;
        protected int stableLocCount = 0;
        protected List<Planet> planets = new ArrayList<Planet>();
        public Map<String,Integer> allPalnetConditions = new HashMap<String,Integer>();
        protected String gate = "NO";
        protected double result; // setter used in method `SystemInRange.findCloseSystems()`

        public SolarSystem() {
        }

        public SolarSystem(String constellation, String systemName, String systemInfo, String pulsarBlackhole, String gate, Vector2f coords, int planetCount) {
            this.constellation = constellation;
            this.systemName = systemName;
            this.starInfo = systemInfo;
            this.pulsarBlackhole = pulsarBlackhole;
            this.gate = gate;
            this.coords = coords;
            this.planetCount = planetCount;
        }

        public void fillAllPlanetConditions(){
            for (Planet p : this.planets){
                for (String c : p.conditions){
                    if (this.allPalnetConditions.get(c) == null) {
                        // create if it doesn't exist
                        this.allPalnetConditions.put(c, 1);
                    }
                    // update if it exists
                    this.allPalnetConditions.put(c, this.allPalnetConditions.get(c)+1);
                }
            }
        }

        public String getConstellation() {
            return constellation;
        }

        public String getStarInfo() {
            return starInfo;
        }

        public String getSystemName() {
            return systemName;
        }

        public String getPulsarBlackhole() {
            return pulsarBlackhole;
        }

        public String getGate() {
            return gate;
        }

        public int getNrOfStars() {
            return nrOfStars;
        }

        public int getJumpPointCount() {
            return jumpPointCount;
        }

        public int getStableLocCount() {
            return stableLocCount;
        }

        public String getStarTypes() {
            return starTypes;
        }

        public List<Planet> getPlanets() {
            return planets;
        }

        public Vector2f getCoords() {
            return coords;
        }

        public int getPlanetCount() {
            return planetCount;
        }

        public void setResult(double result) {
            this.result = result;
        }

        public double getResult() {
            return result;
        }

        public String getAllPalnetConditionsAsString() {
            return Arrays.toString(allPalnetConditions.keySet().toArray());
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public void setStarInfo(String starInfo) {
            this.starInfo = starInfo;
        }

        public void setStarTypes(String starTypes) {
            this.starTypes += "/"+starTypes;
        }

        public void setPulsarBlackhole(String pulsarBlackhole) {
            this.pulsarBlackhole = pulsarBlackhole;
        }

        public void setNrOfStars(int nrOfStars) {
            this.nrOfStars = nrOfStars;
        }

        public void setCoords(Vector2f coords) {
            this.coords = coords;
        }

        public void setPlanetCount(int planetCount) {
            this.planetCount = planetCount;
        }

        public void setPlanets(Planet planet) {
            this.planets.add(planet);
        }

        public void setGate(String gate) {
            this.gate = gate;
        }

        public void setJumpPointCount(int jumpPointCount) {
            this.jumpPointCount = jumpPointCount;
        }

        public void setStableLocCount(int stableLocCount) {
            this.stableLocCount = stableLocCount;
        }
    }
}
