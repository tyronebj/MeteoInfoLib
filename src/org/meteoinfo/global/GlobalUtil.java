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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Template
 *
 * @author Yaqiang Wang
 */
public class GlobalUtil {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Get file extension
     *
     * @param filePath The file path
     * @return File extension
     */
    public static String getFileExtension(String filePath) {
        String extension = "";
        String fn = new File(filePath).getName();
        if (fn.indexOf(".") >= 0) {
            String ext = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase().trim();
            try {
                extension = ext;
            } catch (IllegalArgumentException e) {
            }
        }

        return extension;
    }

    /**
     * Get root path
     *
     * @param aFile The file
     * @return Root path
     */
    public static String getPathRoot(File aFile) {
        File path = aFile.getParentFile();
        String pathRoot = path.toString();
        while (path != null) {
            path = path.getParentFile();
            if (path != null) {
                pathRoot = path.toString();
            }
        }

        return pathRoot;
    }

    /**
     * Get relative path of the file using project file path
     *
     * @param fileName File path
     * @param projFile Project file path
     * @return Relative path
     */
    public static String getRelativePath(String fileName, String projFile) {
        String RelativePath = "";
        File aFile = new File(fileName);
        File pFile = new File(projFile);


        if (!getPathRoot(aFile).toLowerCase().equals(getPathRoot(pFile).toLowerCase())) {
            RelativePath = fileName;
        } else {
            List<String> aList = new ArrayList<String>();
            aList.add(aFile.getAbsolutePath());
            do {
                aList.add("");
                File tempFile = new File(aList.get(aList.size() - 2));
                if (tempFile.exists() && tempFile.getParent() != null) {
                    aList.set(aList.size() - 1, tempFile.getParent());
                } else {
                    break;
                }
            } while (!"".equals(aList.get(aList.size() - 1)));

            List<String> bList = new ArrayList<String>();
            bList.add(pFile.getAbsolutePath());
            do {
                bList.add("");
                File tempFile = new File(bList.get(bList.size() - 2));
                if (tempFile.exists() && tempFile.getParent() != null) {
                    bList.set(bList.size() - 1, tempFile.getParent());
                } else {
                    break;
                }
            } while (!"".equals(bList.get(bList.size() - 1)));

            boolean ifExist = false;
            int offSet;
            for (int i = 0; i < aList.size(); i++) {
                for (int j = 0; j < bList.size(); j++) {
                    if (aList.get(i).equals(bList.get(j))) {
                        for (int k = 1; k < j; k++) {
                            RelativePath = RelativePath + ".." + File.separator;
                        }
                        if (aList.get(i).endsWith(File.separator)) {
                            offSet = 0;
                        } else {
                            offSet = 1;
                        }
                        RelativePath = RelativePath + fileName.substring(aList.get(i).length() + offSet);
                        ifExist = true;
                        break;
                    }
                }
                if (ifExist) {
                    break;
                }
            }
        }

        if ("".equals(RelativePath)) {
            RelativePath = fileName;
        }
        return RelativePath;
    }

    /**
     * Convert Image to BufferedImage
     *
     * @param image The Image
     * @return The BufferedImage
     */
    public static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }

    /**
     * Make a color of a image transparent
     *
     * @param im The image
     * @param color The color
     * @return Result image
     */
    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * Get application path by a class
     *
     * @param cls Class
     * @return Application path
     */
    public static String getAppPath(Class cls) {
        if (cls == null) {
            throw new java.lang.IllegalArgumentException("The parameter can not be null!");
        }

        ClassLoader loader = cls.getClassLoader();
        //Get class name
        String clsName = cls.getName() + ".class";
        //Get package
        Package pack = cls.getPackage();
        String path = "";
        if (pack != null) {
            String packName = pack.getName();
            //Judge if is Java base class to avoid this condition
            if (packName.startsWith("java.") || packName.startsWith("javax.")) {
                throw new java.lang.IllegalArgumentException("Dont use Java system class!");
            }
            //Remove package name from the class name
            clsName = clsName.substring(packName.length() + 1);
            //Convert package name to path if the package has simple name
            if (packName.indexOf(".") < 0) {
                path = packName + "/";
            } else {    //Convert package name to path
                int start = 0;
                int end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }

        //Get resource
        java.net.URL url = loader.getResource(path + clsName);
        //Get path
        String realPath = url.getPath();
        //Remove "file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1) {
            realPath = realPath.substring(pos + 5);
        }
        //Remove class info
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);
        //Remove JAR package name
        if (realPath.endsWith("!")) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        }

        //Get Chinese path by decode
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return realPath;
    }

    /**
     * String pad left
     *
     * @param str The string
     * @param length Pad length
     * @param ch Pad char
     * @return Padded string
     */
    public static String padLeft(String str, int length, char ch) {
        for (int i = str.length(); i < length; i++) {
            str = ch + str;
        }

        return str;
    }

    /**
     * String pad right
     *
     * @param str The string
     * @param length Pad length
     * @param ch Pad char
     * @return Padded string
     */
    public static String padRight(String str, int length, char ch) {
        for (int i = str.length(); i < length; i++) {
            str = str + ch;
        }

        return str;
    }

    /**
     * Deep clone object
     *
     * @param oldObj Old object
     * @return Cloned object
     * @throws Exception
     */
    public static Object deepCopy(Object oldObj) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream(); // A
            oos = new ObjectOutputStream(bos); // B
            // serialize and pass the object
            oos.writeObject(oldObj);   // C
            oos.flush();               // D
            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray()); // E
            ois = new ObjectInputStream(bin);                  // F
            // return the new object
            return ois.readObject(); // G
        } catch (Exception e) {
            System.out.println("Exception in ObjectCloner = " + e);
            throw (e);
        } finally {
            oos.close();
            ois.close();
        }
    }

    /**
     * Deep clone a BufferedIamge
     *
     * @param bi Original image
     * @return Cloned image
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * Get default font name
     * 
     * @return Default font name
     */
    public static String getDefaultFontName(){
        String[] fontnames = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        List<String> fns = Arrays.asList(fontnames);
        String fn = "宋体";
        if (!fns.contains(fn))
            fn = "Arial";
        
        if (!fns.contains(fn))
            fn = fontnames[0];
        
        return fn;
    }
    // </editor-fold>
}
