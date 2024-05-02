package consolegalaxy.campaign.listeners;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.listeners.CurrentLocationChangedListener;
import org.apache.log4j.Logger;
import consolegalaxy.scripts.Utils;

public class LocationChangeListener implements CurrentLocationChangedListener {
    private static final Logger log = Global.getLogger(LocationChangeListener.class);

    public void reportCurrentLocationChanged(LocationAPI prev, LocationAPI curr) {
        Utils.showNotification("Current location: "+curr.getLocation().toString());
//        Utils.tryCreateCryosleeperReports(curr.getEntitiesWithTag(Tags.CRYOSLEEPER), log, true);
//        Utils.tryCreateUnsearchedRuinsReports(curr.getEntitiesWithTag(Tags.PLANET), log, true);
//        Utils.tryCreateSalvageableReports(curr.getEntitiesWithTag(Tags.SALVAGEABLE), log, false);
    }
}
