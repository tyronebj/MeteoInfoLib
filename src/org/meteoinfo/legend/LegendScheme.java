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
package org.meteoinfo.legend;

import org.meteoinfo.data.meteodata.DrawType2D;
import org.meteoinfo.drawing.MarkerType;
import org.meteoinfo.drawing.PointStyle;
import org.meteoinfo.global.colors.ColorUtil;
import org.meteoinfo.shape.ShapeTypes;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static org.meteoinfo.shape.ShapeTypes.Point;
import static org.meteoinfo.shape.ShapeTypes.PointM;
import static org.meteoinfo.shape.ShapeTypes.PointZ;
import static org.meteoinfo.shape.ShapeTypes.Polygon;
import static org.meteoinfo.shape.ShapeTypes.PolygonM;
import static org.meteoinfo.shape.ShapeTypes.PolygonZ;
import static org.meteoinfo.shape.ShapeTypes.Polyline;
import static org.meteoinfo.shape.ShapeTypes.PolylineM;
import static org.meteoinfo.shape.ShapeTypes.PolylineZ;

/**
 * Legend scheme class
 *
 * @author Yaqiang Wang
 */
public class LegendScheme {
    // <editor-fold desc="Variables">

    private String fieldName = "";
    private LegendType legendType = LegendType.SingleSymbol;
    private ShapeTypes shapeType;
    private BreakTypes breakType;
    private List<ColorBreak> legendBreaks;
    private boolean hasNoData;
    private double minValue;
    private double maxValue;
    private double undef;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     *
     * @param aShapeType
     */
    public LegendScheme(ShapeTypes aShapeType) {
        shapeType = aShapeType;        
        legendBreaks = new ArrayList<ColorBreak>();
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get field name
     *
     * @return The field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Set field name
     *
     * @param fn The field name
     */
    public void setFieldName(String fn) {
        fieldName = fn;
    }

    /**
     * Get legend type
     *
     * @return The legend type
     */
    public LegendType getLegendType() {
        return legendType;
    }

    /**
     * Set legend type
     *
     * @param lt The legend type
     */
    public void setLegendType(LegendType lt) {
        legendType = lt;
    }

    /**
     * Get shape type
     *
     * @return The shape type
     */
    public ShapeTypes getShapeType() {
        return shapeType;
    }

    /**
     * Set shape type
     *
     * @param st The shape type
     */
    public void setShapeType(ShapeTypes st) {
        shapeType = st;
    }

    /**
     * Get break type
     *
     * @return The break type
     */
    public BreakTypes getBreakType() {
        BreakTypes breakType = BreakTypes.ColorBreak;
        switch (this.shapeType) {
            case Point:
            case PointM:
            case PointZ:
                breakType = BreakTypes.PointBreak;
                break;
            case Polyline:
            case PolylineM:
            case PolylineZ:
                breakType = BreakTypes.PolylineBreak;
                break;
            case Polygon:
            case PolygonM:
            case PolygonZ:
                breakType = BreakTypes.PolygonBreak;
                break;
        }
        return breakType;
    }

    /**
     * Set break type
     *
     * @param bt The break type
     */
    public void setBreakType(BreakTypes bt) {
        breakType = bt;
    }

    /**
     * Get legend breaks
     *
     * @return The legend breaks
     */
    public List<ColorBreak> getLegendBreaks() {
        return legendBreaks;
    }

    /**
     * Set legend breaks
     *
     * @param breaks The legend breaks
     */
    public void setLegendBreaks(List<ColorBreak> breaks) {
        legendBreaks = breaks;
    }

    /**
     * Get if has no data
     *
     * @return If has no data
     */
    public boolean getHasNoData() {
        return hasNoData;
    }

    /**
     * Set if has no data
     *
     * @param istrue If has no data
     */
    public void setHasNoData(boolean istrue) {
        hasNoData = istrue;
    }

    /**
     * Get minimum value
     *
     * @return Minimum value
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Set minimum value
     *
     * @param min
     */
    public void setMinValue(double min) {
        minValue = min;
    }

    /**
     * Get maximum value
     *
     * @return Maximum value
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Set maximum value
     *
     * @param max Maximum value
     */
    public void setMaxValue(double max) {
        maxValue = max;
    }

    /**
     * Get undefine value
     *
     * @return Undefine value
     */
    public double getUndefValue() {
        return undef;
    }

    /**
     * Set undefine value
     *
     * @param uv Undefine value
     */
    public void setUndefValue(double uv) {
        undef = uv;
    }

    /**
     * Get legend break number
     *
     * @return Legend break number
     */
    public int getBreakNum() {
        return legendBreaks.size();
    }

    /**
     * Get visible legend breaks number
     *
     * @return The visible legend breaks number
     */
    public int getVisibleBreakNum() {
        int n = 0;
        for (ColorBreak aCB : this.legendBreaks) {
            if (aCB.isDrawShape()) {
                n += 1;
            }
        }

        return n;
    }

    /**
     * Get visible break number
     *
     * @return Visible break number
     */
    public int VisibleBreakNum() {
        int n = 0;
        for (ColorBreak aCB : legendBreaks) {
            if (aCB.isDrawShape()) {
                n += 1;
            }
        }

        return n;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    /// <summary>

    /**
     * Get color list
     *
     * @return Color list
     */
    public List<Color> getColors() {
        List<Color> colors = new ArrayList<Color>();
        for (int i = 0; i < legendBreaks.size(); i++) {
            colors.add(legendBreaks.get(i).getColor());
        }

        return colors;
    }

    /**
     * Judge if shape type is consistent with draw type
     *
     * @param drawTyp Draw type
     * @return Boolean
     */
    public boolean isConsistent(DrawType2D drawTyp) {
        switch (shapeType) {
            case Point:
            case PointZ:
            case StationModel:
            case WeatherSymbol:
            case WindArraw:
            case WindBarb:
                switch (drawTyp) {
                    case Grid_Point:
                    case Station_Info:
                    case Station_Model:
                    case Station_Point:
                    case Traj_Point:
                    case Traj_StartPoint:
                    case Weather_Symbol:
                    case Barb:
                        return true;
                    default:
                        return false;
                }
            case Polyline:
            case PolylineZ:
                switch (drawTyp) {
                    case Contour:
                    case Streamline:
                    case Traj_Line:
                        return true;
                    default:
                        return false;
                }
            case Polygon:
                switch (drawTyp) {
                    case Shaded:
                        return true;
                    default:
                        return false;
                }
            case Image:
                switch (drawTyp) {
                    case Raster:
                        return true;
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    /**
     * Export to xml document
     *
     * @param doc xml document
     * @param parent Parent xml element
     */
    public void exportToXML(Document doc, Element parent) {
        Element root = doc.createElement("LegendScheme");
        Attr fieldNameAttr = doc.createAttribute("FieldName");
        Attr legendTypeAttr = doc.createAttribute("LegendType");
        Attr shapeTypeAttr = doc.createAttribute("ShapeType");
        Attr breakNumAttr = doc.createAttribute("BreakNum");
        Attr hasNoDataAttr = doc.createAttribute("HasNoData");
        Attr minValueAttr = doc.createAttribute("MinValue");
        Attr maxValueAttr = doc.createAttribute("MaxValue");
        Attr undefAttr = doc.createAttribute("UNDEF");

        fieldNameAttr.setValue(this.fieldName);
        legendTypeAttr.setValue(this.legendType.toString());
        shapeTypeAttr.setValue(this.shapeType.toString());
        breakNumAttr.setValue(String.valueOf(this.getBreakNum()));
        hasNoDataAttr.setValue(String.valueOf(this.hasNoData));
        minValueAttr.setValue(String.valueOf(this.minValue));
        maxValueAttr.setValue(String.valueOf(this.maxValue));
        undefAttr.setValue(String.valueOf(this.undef));

        root.setAttributeNode(fieldNameAttr);
        root.setAttributeNode(legendTypeAttr);
        root.setAttributeNode(shapeTypeAttr);
        root.setAttributeNode(breakNumAttr);
        root.setAttributeNode(hasNoDataAttr);
        root.setAttributeNode(minValueAttr);
        root.setAttributeNode(maxValueAttr);
        root.setAttributeNode(undefAttr);

        Element breaks = doc.createElement("Breaks");
        Element brk;
        Attr caption;
        Attr startValue;
        Attr endValue;
        Attr color;
        Attr drawShape;
        Attr size;
        Attr style;
        Attr outlineColor;
        Attr drawOutline;
        Attr drawFill;
        switch (this.shapeType) {
            case Point:
            case PointZ:
                Attr isNoData;
                for (ColorBreak aCB : this.legendBreaks) {
                    PointBreak aPB = (PointBreak) aCB;
                    brk = doc.createElement("Break");
                    caption = doc.createAttribute("Caption");
                    startValue = doc.createAttribute("StartValue");
                    endValue = doc.createAttribute("EndValue");
                    color = doc.createAttribute("Color");
                    drawShape = doc.createAttribute("DrawShape");
                    outlineColor = doc.createAttribute("OutlineColor");
                    size = doc.createAttribute("Size");
                    style = doc.createAttribute("Style");
                    drawOutline = doc.createAttribute("DrawOutline");
                    drawFill = doc.createAttribute("DrawFill");
                    isNoData = doc.createAttribute("IsNoData");
                    Attr markerType = doc.createAttribute("MarkerType");
                    Attr fontName = doc.createAttribute("FontName");
                    Attr charIndex = doc.createAttribute("CharIndex");
                    Attr imagePath = doc.createAttribute("ImagePath");
                    Attr angle = doc.createAttribute("Angle");

                    caption.setValue(aPB.getCaption());
                    startValue.setValue(String.valueOf(aPB.getStartValue()));
                    endValue.setValue(String.valueOf(aPB.getEndValue()));
                    color.setValue(ColorUtil.toHexEncoding(aPB.getColor()));
                    drawShape.setValue(String.valueOf(aPB.isDrawShape()));
                    outlineColor.setValue(ColorUtil.toHexEncoding(aPB.getOutlineColor()));
                    size.setValue(String.valueOf(aPB.getSize()));
                    style.setValue(aPB.getStyle().toString());
                    drawOutline.setValue(String.valueOf(aPB.getDrawOutline()));
                    drawFill.setValue(String.valueOf(aPB.getDrawFill()));
                    isNoData.setValue(String.valueOf(aPB.isNoData()));
                    markerType.setValue(aPB.getMarkerType().toString());
                    fontName.setValue(aPB.getFontName());
                    charIndex.setValue(String.valueOf(aPB.getCharIndex()));
                    imagePath.setValue(aPB.getImagePath());
                    angle.setValue(String.valueOf(aPB.getAngle()));

                    brk.setAttributeNode(caption);
                    brk.setAttributeNode(startValue);
                    brk.setAttributeNode(endValue);
                    brk.setAttributeNode(color);
                    brk.setAttributeNode(drawShape);
                    brk.setAttributeNode(outlineColor);
                    brk.setAttributeNode(size);
                    brk.setAttributeNode(style);
                    brk.setAttributeNode(drawOutline);
                    brk.setAttributeNode(drawFill);
                    brk.setAttributeNode(isNoData);
                    brk.setAttributeNode(markerType);
                    brk.setAttributeNode(fontName);
                    brk.setAttributeNode(charIndex);
                    brk.setAttributeNode(imagePath);
                    brk.setAttributeNode(angle);

                    breaks.appendChild(brk);
                }
                break;
            case Polyline:
            case PolylineZ:
                for (ColorBreak aCB : this.legendBreaks) {
                    PolylineBreak aPLB = (PolylineBreak) aCB;
                    brk = doc.createElement("Break");
                    caption = doc.createAttribute("Caption");
                    startValue = doc.createAttribute("StartValue");
                    endValue = doc.createAttribute("EndValue");
                    color = doc.createAttribute("Color");
                    drawShape = doc.createAttribute("DrawShape");
                    size = doc.createAttribute("Size");
                    style = doc.createAttribute("Style");
                    Attr drawSymbol = doc.createAttribute("DrawSymbol");
                    Attr symbolSize = doc.createAttribute("SymbolSize");
                    Attr symbolStyle = doc.createAttribute("SymbolStyle");
                    Attr symbolColor = doc.createAttribute("SymbolColor");
                    Attr symbolInterval = doc.createAttribute("SymbolInterval");

                    caption.setValue(aPLB.getCaption());
                    startValue.setValue(String.valueOf(aPLB.getStartValue()));
                    endValue.setValue(String.valueOf(aPLB.getEndValue()));
                    color.setValue(ColorUtil.toHexEncoding(aPLB.getColor()));
                    drawShape.setValue(String.valueOf(aPLB.isDrawShape()));
                    size.setValue(String.valueOf(aPLB.getSize()));
                    style.setValue(aPLB.getStyle().toString());
                    drawSymbol.setValue(String.valueOf(aPLB.getDrawSymbol()));
                    symbolSize.setValue(String.valueOf(aPLB.getSymbolSize()));
                    symbolStyle.setValue(aPLB.getSymbolStyle().toString());
                    symbolColor.setValue(ColorUtil.toHexEncoding(aPLB.getSymbolColor()));
                    symbolInterval.setValue(String.valueOf(aPLB.getSymbolInterval()));

                    brk.setAttributeNode(caption);
                    brk.setAttributeNode(startValue);
                    brk.setAttributeNode(endValue);
                    brk.setAttributeNode(color);
                    brk.setAttributeNode(drawShape);
                    brk.setAttributeNode(size);
                    brk.setAttributeNode(style);
                    brk.setAttributeNode(drawSymbol);
                    brk.setAttributeNode(symbolSize);
                    brk.setAttributeNode(symbolStyle);
                    brk.setAttributeNode(symbolColor);
                    brk.setAttributeNode(symbolInterval);

                    breaks.appendChild(brk);
                }
                break;
            case Polygon:
                Attr outlineSize;
                for (ColorBreak aCB : this.legendBreaks) {
                    PolygonBreak aPGB = (PolygonBreak) aCB;
                    brk = doc.createElement("Break");
                    caption = doc.createAttribute("Caption");
                    startValue = doc.createAttribute("StartValue");
                    endValue = doc.createAttribute("EndValue");
                    color = doc.createAttribute("Color");
                    drawShape = doc.createAttribute("DrawShape");
                    outlineColor = doc.createAttribute("OutlineColor");
                    drawOutline = doc.createAttribute("DrawOutline");
                    drawFill = doc.createAttribute("DrawFill");
                    outlineSize = doc.createAttribute("OutlineSize");
                    //Attr usingHatchStyle = doc.createAttribute("UsingHatchStyle");
                    //style = doc.createAttribute("Style");
                    Attr backColor = doc.createAttribute("BackColor");

                    caption.setValue(aPGB.getCaption());
                    startValue.setValue(String.valueOf(aPGB.getStartValue()));
                    endValue.setValue(String.valueOf(aPGB.getEndValue()));
                    color.setValue(ColorUtil.toHexEncoding(aPGB.getColor()));
                    drawShape.setValue(String.valueOf(aPGB.isDrawShape()));
                    outlineColor.setValue(ColorUtil.toHexEncoding(aPGB.getOutlineColor()));
                    drawOutline.setValue(String.valueOf(aPGB.getDrawOutline()));
                    drawFill.setValue(String.valueOf(aPGB.getDrawFill()));
                    outlineSize.setValue(String.valueOf(aPGB.getOutlineSize()));
                    //usingHatchStyle.InnerText = aPGB.UsingHatchStyle.ToString();
                    //style.InnerText = aPGB.Style.ToString();
                    backColor.setValue(ColorUtil.toHexEncoding(aPGB.getBackColor()));

                    brk.setAttributeNode(caption);
                    brk.setAttributeNode(startValue);
                    brk.setAttributeNode(endValue);
                    brk.setAttributeNode(color);
                    brk.setAttributeNode(drawShape);
                    brk.setAttributeNode(outlineColor);
                    brk.setAttributeNode(drawOutline);
                    brk.setAttributeNode(drawFill);
                    brk.setAttributeNode(outlineSize);
                    //brk.setAttributeNode(usingHatchStyle);
                    //brk.setAttributeNode(style);
                    brk.setAttributeNode(backColor);

                    breaks.appendChild(brk);
                }
                break;
            case Image:
                for (ColorBreak aCB : this.legendBreaks) {
                    brk = doc.createElement("Break");
                    caption = doc.createAttribute("Caption");
                    startValue = doc.createAttribute("StartValue");
                    endValue = doc.createAttribute("EndValue");
                    color = doc.createAttribute("Color");
                    isNoData = doc.createAttribute("IsNoData");

                    caption.setValue(aCB.getCaption());
                    startValue.setValue(String.valueOf(aCB.getStartValue()));
                    endValue.setValue(String.valueOf(aCB.getEndValue()));
                    color.setValue(ColorUtil.toHexEncoding(aCB.getColor()));
                    isNoData.setValue(String.valueOf(aCB.isNoData()));

                    brk.setAttributeNode(caption);
                    brk.setAttributeNode(startValue);
                    brk.setAttributeNode(endValue);
                    brk.setAttributeNode(color);
                    brk.setAttributeNode(isNoData);

                    breaks.appendChild(brk);
                }
                break;
        }

        root.appendChild(breaks);
        parent.appendChild(root);
    }

    /**
     * Export to xml file
     *
     * @param aFile xml file path
     * @throws ParserConfigurationException
     */
    public void exportToXMLFile(String aFile) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("MeteoInfo");
        File af = new File(aFile);
        Attr fn = doc.createAttribute("File");
        Attr type = doc.createAttribute("Type");
        fn.setValue(af.getName());
        type.setValue("LegendScheme");
        root.setAttributeNode(fn);
        root.setAttributeNode(type);
        doc.appendChild(root);

        exportToXML(doc, root);

        //Save to file
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            //transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            PrintWriter pw = new PrintWriter(new FileOutputStream(aFile));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
        } catch (TransformerException mye) {
        } catch (IOException exp) {
        }
    }

    /**
     * Import legend scheme from XML node
     *
     * @param LSNode xml node
     */
    public void importFromXML(Node LSNode) {
        importFromXML(LSNode, true);
    }

    /**
     * Import legend scheme from xml node
     *
     * @param LSNode xml node
     * @param keepShape if keep the legend shape type
     */
    public void importFromXML(Node LSNode, boolean keepShape) {
        legendBreaks = new ArrayList<ColorBreak>();

        if (LSNode.getAttributes().getNamedItem("FieldName") != null) {
            fieldName = LSNode.getAttributes().getNamedItem("FieldName").getNodeValue();
        }
        legendType = LegendType.valueOf(LSNode.getAttributes().getNamedItem("LegendType").getNodeValue());
        ShapeTypes aShapeType = ShapeTypes.valueOf(LSNode.getAttributes().getNamedItem("ShapeType").getNodeValue());

        //BreakNum = Convert.ToInt32(LSNode.Attributes["BreakNum"].InnerText);
        hasNoData = Boolean.parseBoolean(LSNode.getAttributes().getNamedItem("HasNoData").getNodeValue());
        minValue = Double.parseDouble(LSNode.getAttributes().getNamedItem("MinValue").getNodeValue());
        maxValue = Double.parseDouble(LSNode.getAttributes().getNamedItem("MaxValue").getNodeValue());
        undef = Double.parseDouble(LSNode.getAttributes().getNamedItem("UNDEF").getNodeValue());

        if (!keepShape) {
            shapeType = aShapeType;
        }
        boolean sameShapeType = (shapeType == aShapeType);
        importBreaks(LSNode, sameShapeType);
    }

    private void importBreaks(Node parent, boolean sameShapeType) {
        Node breaksNode = ((Element)parent).getElementsByTagName("Breaks").item(0);

        NodeList breaks = ((Element)breaksNode).getElementsByTagName("Break");
        if (sameShapeType) {
            switch (shapeType) {
                case Point:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PointBreak aPB = new PointBreak();
                        try {
                            aPB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aPB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aPB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aPB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                            aPB.setDrawShape(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                            aPB.setDrawFill(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawFill").getNodeValue()));
                            aPB.setDrawOutline(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawOutline").getNodeValue()));
                            aPB.setNoData(Boolean.parseBoolean(brk.getAttributes().getNamedItem("IsNoData").getNodeValue()));
                            aPB.setOutlineColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("OutlineColor").getNodeValue()));
                            aPB.setSize(Float.parseFloat(brk.getAttributes().getNamedItem("Size").getNodeValue()));
                            aPB.setStyle(PointStyle.valueOf(brk.getAttributes().getNamedItem("Style").getNodeValue()));
                            aPB.setMarkerType(MarkerType.valueOf(brk.getAttributes().getNamedItem("MarkerType").getNodeValue()));
                            aPB.setFontName(brk.getAttributes().getNamedItem("FontName").getNodeValue());
                            aPB.setCharIndex(Integer.parseInt(brk.getAttributes().getNamedItem("CharIndex").getNodeValue()));
                            aPB.setImagePath(brk.getAttributes().getNamedItem("ImagePath").getNodeValue());
                            aPB.setAngle(Float.parseFloat(brk.getAttributes().getNamedItem("Angle").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPB);
                        }
                    }
                    break;
                case Polyline:
                case PolylineZ:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PolylineBreak aPLB = new PolylineBreak();
                        try {
                            aPLB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aPLB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aPLB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aPLB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                            aPLB.setDrawPolyline(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                            aPLB.setSize(Float.parseFloat(brk.getAttributes().getNamedItem("Size").getNodeValue()));
                            aPLB.setStyle(LineStyles.valueOf(brk.getAttributes().getNamedItem("Style").getNodeValue()));
                            aPLB.setDrawSymbol(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawSymbol").getNodeValue()));
                            aPLB.setSymbolSize(Float.parseFloat(brk.getAttributes().getNamedItem("SymbolSize").getNodeValue()));
                            aPLB.setSymbolStyle(PointStyle.valueOf(brk.getAttributes().getNamedItem("SymbolStyle").getNodeValue()));
                            aPLB.setSymbolColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("SymbolColor").getNodeValue()));
                            aPLB.setSymbolInterval(Integer.parseInt(brk.getAttributes().getNamedItem("SymbolInterval").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPLB);
                        }
                    }
                    break;
                case Polygon:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PolygonBreak aPGB = new PolygonBreak();
                        try {
                            aPGB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aPGB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aPGB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aPGB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                            aPGB.setDrawShape(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                            aPGB.setDrawFill(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawFill").getNodeValue()));
                            aPGB.setDrawOutline(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawOutline").getNodeValue()));
                            aPGB.setOutlineSize(Float.parseFloat(brk.getAttributes().getNamedItem("OutlineSize").getNodeValue()));
                            aPGB.setOutlineColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("OutlineColor").getNodeValue()));
                            //aPGB.UsingHatchStyle = bool.Parse(brk.Attributes["UsingHatchStyle"].InnerText);
                            //aPGB.Style = (HatchStyle) Enum.Parse(typeof(HatchStyle), brk.Attributes["Style"].InnerText, true);
                            aPGB.setBackColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("BackColor").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPGB);
                        }
                    }
                    break;
                case Image:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        ColorBreak aCB = new ColorBreak();
                        try {
                            aCB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aCB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aCB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aCB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                            aCB.setDrawShape(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aCB);
                        }
                    }
                    break;
            }
        } else {
            switch (shapeType) {
                case Point:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PointBreak aPB = new PointBreak();
                        try {
                            aPB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aPB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aPB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aPB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                            aPB.setDrawShape(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPB);
                        }
                    }
                    break;
                case Polyline:
                case PolylineZ:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PolylineBreak aPLB = new PolylineBreak();
                        try {
                            if (!"NoData".equals(brk.getAttributes().getNamedItem("Caption").getNodeValue())) {
                                aPLB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                                aPLB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                                aPLB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                                aPLB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                                aPLB.setDrawPolyline(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                            }
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPLB);
                        }
                    }
                    break;
                case Polygon:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        PolygonBreak aPGB = new PolygonBreak();
                        try {
                            if (!"NoData".equals(brk.getAttributes().getNamedItem("Caption").getNodeValue())) {
                                aPGB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                                aPGB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                                aPGB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                                aPGB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                                aPGB.setDrawShape(Boolean.parseBoolean(brk.getAttributes().getNamedItem("DrawShape").getNodeValue()));
                                aPGB.setDrawFill(true);
                            }
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aPGB);
                        }
                    }

                    break;
                case Image:
                    for (int i = 0; i < breaks.getLength(); i++) {
                        Node brk = breaks.item(i);
                        ColorBreak aCB = new ColorBreak();
                        try {
                            aCB.setCaption(brk.getAttributes().getNamedItem("Caption").getNodeValue());
                            aCB.setStartValue(brk.getAttributes().getNamedItem("StartValue").getNodeValue());
                            aCB.setEndValue(brk.getAttributes().getNamedItem("EndValue").getNodeValue());
                            aCB.setColor(ColorUtil.parseToColor(brk.getAttributes().getNamedItem("Color").getNodeValue()));
                        } catch (Exception e) {
                        } finally {
                            legendBreaks.add(aCB);
                        }
                    }
                    break;
            }
            //breakNum = LegendBreaks.Count;
        }
    }

    /**
     * Import legend scheme from XML file
     *
     * @param aFile File path
     */
    public void importFromXMLFile(String aFile) throws ParserConfigurationException, SAXException, IOException {
        importFromXMLFile(aFile, true);
    }

    /**
     * Import legend scheme from XML file
     *
     * @param aFile file path
     * @param keepShape If keep shape type
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void importFromXMLFile(String aFile, boolean keepShape) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(aFile);

        Element root = doc.getDocumentElement();
        Node LSNode;
        if ("MeteoInfo".equals(root.getNodeName())) {
            LSNode = root.getElementsByTagName("LegendScheme").item(0);
        } else {
            LSNode = root;
        }

        importFromXML(LSNode, keepShape);
    }

    /**
     * Import legend scheme from an image color palette file
     *
     * @param filePath File path
     */
    public void importFromPaletteFile_Unique(String filePath) {
        BufferedReader sr = null;
        try {
            File aFile = new File(filePath);
            sr = new BufferedReader(new FileReader(aFile));
            this.shapeType = ShapeTypes.Image;
            this.legendType = LegendType.UniqueValue;
            this.legendBreaks = new ArrayList<ColorBreak>();
            ColorBreak aCB;
            String[] dataArray;
            sr.readLine();
            String aLine = sr.readLine();
            while (aLine != null) {
                aLine = aLine.trim();
                if (aLine.isEmpty()){
                    aLine = sr.readLine();
                    continue;
                }
                dataArray = aLine.split("\\s+");
                Color aColor = new Color(Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[2]), Integer.parseInt(dataArray[1]));
                aCB = new ColorBreak();
                aCB.setColor(aColor);
                aCB.setStartValue(dataArray[0]);
                aCB.setEndValue(dataArray[0]);
                aCB.setCaption(String.valueOf(aCB.getStartValue()));
                this.legendBreaks.add(aCB);

                aLine = sr.readLine();
            }
            sr.close();
        } catch (IOException ex) {
            Logger.getLogger(LegendScheme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                sr.close();
            } catch (IOException ex) {
                Logger.getLogger(LegendScheme.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Import legend scheme from an image color palette file
     *
     * @param filePath File path
     */
    public void importFromPaletteFile_Graduated(String filePath) {
        BufferedReader sr = null;
        try {
            File aFile = new File(filePath);
            sr = new BufferedReader(new FileReader(aFile));
            this.shapeType = ShapeTypes.Image;
            this.legendType = LegendType.GraduatedColor;
            this.legendBreaks = new ArrayList<ColorBreak>();
            List<Color> colorList = new ArrayList<Color>();
            List<Integer> values = new ArrayList<Integer>();
            ColorBreak aCB;
            String[] dataArray;
            sr.readLine();
            String aLine = sr.readLine();
            while (aLine != null) {
                dataArray = aLine.split("\\s+");
                Color aColor = new Color(Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[2]), Integer.parseInt(dataArray[1]));
                if (colorList.isEmpty()) {
                    colorList.add(aColor);
                } else {
                    if (!colorList.contains(aColor)) {
                        aCB = new ColorBreak();
                        aCB.setColor(aColor);
                        aCB.setStartValue(Collections.min(values));
                        aCB.setEndValue(Collections.max(values));
                        if (String.valueOf(aCB.getStartValue()).equals(String.valueOf(aCB.getEndValue()))) {
                            aCB.setCaption(String.valueOf(aCB.getStartValue()));
                        } else {
                            if (this.legendBreaks.isEmpty()) {
                                aCB.setCaption("< " + String.valueOf(aCB.getEndValue()));
                            } else {
                                aCB.setCaption(String.valueOf(aCB.getStartValue()) + " - " + String.valueOf(aCB.getEndValue()));
                            }
                        }
                        this.legendBreaks.add(aCB);

                        values.clear();
                        colorList.add(aColor);
                    }
                }
                values.add(Integer.parseInt(dataArray[0]));

                aLine = sr.readLine();
            }
            sr.close();
            aCB = new ColorBreak();
            aCB.setColor(colorList.get(colorList.size() - 1));
            aCB.setStartValue(Collections.min(values));
            aCB.setEndValue(Collections.max(values));
            aCB.setCaption("> " + String.valueOf(aCB.getStartValue()));
            this.legendBreaks.add(aCB);
        } catch (IOException ex) {
            Logger.getLogger(LegendScheme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                sr.close();
            } catch (IOException ex) {
                Logger.getLogger(LegendScheme.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Clone
     *
     * @return Legend scheme
     */
    @Override
    public Object clone() {
        LegendScheme bLS = new LegendScheme(shapeType);
        bLS.setFieldName(fieldName);
        //bLS.breakNum = breakNum;
        bLS.setHasNoData(hasNoData);
        bLS.setLegendType(legendType);
        bLS.setMinValue(minValue);
        bLS.setMaxValue(maxValue);
        bLS.setUndefValue(undef);
        for (ColorBreak aCB : legendBreaks) {
            bLS.getLegendBreaks().add((ColorBreak) aCB.clone());
        }

        return bLS;
    }
    // </editor-fold>
}
