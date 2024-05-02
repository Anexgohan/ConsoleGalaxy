package consolegalaxy.scripts;

import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.util.Pair;
import org.lazywizard.console.Console;
import org.lwjgl.util.vector.Vector2f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Utils {

    public static void showNotification(String msg) {
        Console.showMessage("Test notification: " + msg);
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static String centerText(String text, int len){
        // Credits : https://stackoverflow.com/a/12088891
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2f);
        float start = mid - (len / 2f);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }

    public static Pair<String,Integer> getStringTypeOfSystemAndStarNumber(StarSystemGenerator.StarSystemType systemType){

        Pair<String,Integer> result;

        switch (systemType) {
            case BINARY_FAR:
            case BINARY_CLOSE:
                result = new Pair<String,Integer>("[binary]", 2);
                break;
            case TRINARY_2FAR:
            case TRINARY_2CLOSE:
            case TRINARY_1CLOSE_1FAR:
                result =  new Pair<String,Integer>("[trinary]", 3);
                break;
            case SINGLE:
                result =  new Pair<String,Integer>("[single]", 1);
                break;
            case NEBULA:
                result =  new Pair<String,Integer>("[nebula]", 0);
                break;
            default:
                result =  new Pair<String,Integer>("[???]", 0);
        }
        return result;
    }
}
