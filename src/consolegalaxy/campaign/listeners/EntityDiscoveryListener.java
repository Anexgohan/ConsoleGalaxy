package consolegalaxy.campaign.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.DiscoverEntityListener;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.apache.log4j.Logger;

import consolegalaxy.scripts.Utils;

public class EntityDiscoveryListener implements DiscoverEntityListener {
    private static final Logger log = Global.getLogger(EntityDiscoveryListener.class);

    @Override
    public void reportEntityDiscovered(SectorEntityToken entity) {
        if (entity.hasTag(Tags.CRYOSLEEPER) || entity.hasTag(Tags.CORONAL_TAP)){
            Utils.showNotification(entity.getFullName());
        } else {
            Utils.showNotification("Discovered: " + entity.getFullName());
        }
    }

}
