package consolegalaxy.campaign.listeners;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.listeners.SurveyPlanetListener;
import org.apache.log4j.Logger;

import consolegalaxy.scripts.Utils;

public class PlanetDiscoveryListener implements SurveyPlanetListener {

    private static final Logger log = Global.getLogger(PlanetDiscoveryListener.class);

    @Override
    public void reportPlayerSurveyedPlanet(PlanetAPI planet) {
        Utils.showNotification(planet.getFullName());
//        Utils.tryCreateUnsearchedRuinsReport(planet, log, true);
    }

}
