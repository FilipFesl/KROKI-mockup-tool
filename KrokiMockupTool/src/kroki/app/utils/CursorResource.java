package kroki.app.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

/**
 * Class used to get images representing different cursors
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class CursorResource {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("kroki.resources.KrokiMockupToolApp");
    private static HashMap<String, BufferedImage> cursorMap = new HashMap<String, BufferedImage>();

    public static Image getCursorResource(String key) {
        BufferedImage value = null;
        if (cursorMap.containsKey(key)) {
            value = cursorMap.get(key);
        } else {
            String url = resourceBundle.getString(key);
            if (url != null) {
                BufferedImage temp = null;
                try {
                    temp = ImageIO.read(CursorResource.class.getResource(url));
                    value = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics2D = value.createGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    graphics2D.drawImage(temp, 0, 0, temp.getWidth(), temp.getHeight(), null);
                    graphics2D.dispose();
                    cursorMap.put(key, value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
