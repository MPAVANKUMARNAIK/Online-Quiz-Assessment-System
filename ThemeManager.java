import java.awt.*;

public class ThemeManager {
    public static boolean dark = false;

    public static Color bg(){
        return dark ? Color.DARK_GRAY : Color.WHITE;
    }
}