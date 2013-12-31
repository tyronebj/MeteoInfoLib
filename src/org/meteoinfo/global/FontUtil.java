 /* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteoinfo.global;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.meteoinfo.drawing.Draw;
import org.meteoinfo.legend.MapFrame;

/**
 *
 * @author yaqiang
 */
public class FontUtil {

    /**
     * Get all available fonts (system fonts, weather font and custom fonts)
     *
     * @return
     */
    public static List<Font> getAllFonts() {
        List<Font> fontList = new ArrayList<Font>();

        //Weather font
        Font weatherFont = getWeatherFont();
        if (weatherFont != null) {
            fontList.add(weatherFont);
        }

        //System fonts
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = gEnv.getAllFonts();
        for (int i = 0; i < fonts.length; i++) {
            fontList.add(fonts[i]);
        }

        //Custom fonts
        String fn = GlobalUtil.getAppPath(MapFrame.class);
        fn = fn.substring(0, fn.lastIndexOf("/"));
        String path = fn + File.separator + "font";
        File pathDir = new File(path);
        if (pathDir.isDirectory()) {
            
        }

        return fontList;
    }
    
    /**
     * Register weather font
     */
    public static void registerWeatherFont(){
        Font weatherFont = getWeatherFont();
        if (weatherFont != null) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(weatherFont);
        }
    }

    /**
     * Get weather symbol font
     *
     * @return Weather symbol font
     */
    public static Font getWeatherFont() {
        Font font = null;
        InputStream is = Draw.class.getResourceAsStream("/org/meteoinfo/resources/WeatherSymbol.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException ex) {
            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
        }

        return font;
    }
}
