/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart.plot;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.meteoinfo.chart.ChartLegend;
import org.meteoinfo.chart.ChartText;
import org.meteoinfo.chart.axis.TimeAxis;
import org.meteoinfo.data.Dataset;
import org.meteoinfo.data.XYDataset;
import org.meteoinfo.data.XYErrorSeriesData;
import org.meteoinfo.data.XYListDataset;
import org.meteoinfo.data.XYSeriesData;
import org.meteoinfo.drawing.Draw;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointF;
import org.meteoinfo.global.colors.ColorUtil;
import org.meteoinfo.legend.ColorBreak;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.PointBreak;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.legend.PolylineBreak;
import org.meteoinfo.shape.ShapeTypes;

/**
 *
 * @author yaqiang
 */
public final class XY1DPlot extends XYPlot {

    // <editor-fold desc="Variables">
    private XYListDataset dataset;
    private ChartPlotMethod chartPlotMethod;
    private List<SeriesLegend> seriesLegends;
    private boolean useBreak2D;
    private float barWidth;
    private boolean autoBarWidth;

    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public XY1DPlot() {
        super();

        this.dataset = new XYListDataset();
        this.chartPlotMethod = ChartPlotMethod.LINE;
        this.useBreak2D = false;
        this.seriesLegends = new ArrayList<>();
        this.barWidth = 0.8f;
        this.autoBarWidth = true;
    }

    /**
     * Constructor
     *
     * @param dateset Dataset
     */
    public XY1DPlot(XYDataset dateset) {
        this();
        this.setDataset(dateset);
    }

    /**
     * Constructor
     *
     * @param orientation Plot orientation
     * @param dateset Dataset
     */
    public XY1DPlot(PlotOrientation orientation, XYDataset dateset) {
        this();
        this.setPlotOrientation(orientation);
        this.setDataset(dateset);
    }

    /**
     * Constructor
     *
     * @param isTime If x axis is time
     * @param dateset Dataset
     */
    public XY1DPlot(boolean isTime, XYDataset dateset) {
        this();
        if (isTime) {
            this.setXAxis(new TimeAxis("X", true));
        }
        //this.getXAxis().setTimeAxis(isTime);
        this.setDataset(dateset);
    }

    /**
     * Constructor
     *
     * @param isTime If x axis is time
     * @param cpMethod Plot method
     * @param dateset Dataset
     */
    public XY1DPlot(boolean isTime, ChartPlotMethod cpMethod, XYDataset dateset) {
        this();
        if (isTime) {
            this.setXAxis(new TimeAxis("X", true));
        }
        //this.getXAxis().setTimeAxis(isTime);
        this.setChartPlotMethod(cpMethod);
        this.setDataset(dateset);
    }

    /**
     * Constructor
     *
     * @param isTime If x axis is time
     * @param orientation Plot orientation
     * @param dateset Dataset
     */
    public XY1DPlot(boolean isTime, PlotOrientation orientation, XYDataset dateset) {
        this();
        if (isTime) {
            this.setXAxis(new TimeAxis("X", true));
        }
        //this.getXAxis().setTimeAxis(isTime);
        this.setPlotOrientation(orientation);
        this.setDataset(dateset);
    }

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    @Override
    public XYDataset getDataset() {
        return dataset;
    }

    @Override
    public void setDataset(Dataset value) {
        dataset = (XYListDataset) value;
        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
        this.updateSeriesLegend();
    }

    private void updateSeriesLegend() {
        //this.seriesLegends.clear();
        int si = this.seriesLegends.size();
        if (si > dataset.getSeriesCount()) {
            si = 0;
        }
        for (int i = si; i < dataset.getSeriesCount(); i++) {
            switch (this.chartPlotMethod) {
                case LINE:
                case LINE_POINT:
                    PolylineBreak plb = new PolylineBreak();
                    if (this.chartPlotMethod == ChartPlotMethod.LINE) {
                        plb.setDrawSymbol(false);
                    } else {
                        plb.setDrawSymbol(true);
                    }
                    plb.setColor(ColorUtil.getCommonColor(i));
                    plb.setCaption(dataset.getSeriesKey(i));
                    seriesLegends.add(new SeriesLegend(plb));
                    break;
                case POINT:
                    PointBreak pb = new PointBreak();
                    pb.setColor(ColorUtil.getCommonColor(i));
                    pb.setCaption(dataset.getSeriesKey(i));
                    seriesLegends.add(new SeriesLegend(pb));
                    break;
                case BAR:
                    PolygonBreak pgb = new PolygonBreak();
                    pgb.setColor(ColorUtil.getCommonColor(i));
                    pgb.setCaption(dataset.getSeriesKey(i));
                    seriesLegends.add(new SeriesLegend(pgb));
                    break;
            }
        }
    }

    /**
     * Get chart plot method
     *
     * @return Chart plot method
     */
    public ChartPlotMethod getChartPlotMethod() {
        return this.chartPlotMethod;
    }

    /**
     * Set chart plot method
     *
     * @param value Chart plot method
     */
    public void setChartPlotMethod(ChartPlotMethod value) {
        this.chartPlotMethod = value;
        if (this.dataset != null) {
            this.updateSeriesLegend();
        }
    }

    @Override
    public PlotType getPlotType() {
        return PlotType.XY;
    }

    /**
     * Get Series legend breaks
     *
     * @return Series legend breaks
     */
    public List<SeriesLegend> getLegendBreaks() {
        return this.seriesLegends;
    }

//    /**
//     * Get point breaks
//     *
//     * @return Point breaks
//     */
//    public PointBreak[] getPointBreaks() {
//        return this.pointBreaks;
//    }
    /**
     * If use item 2D point breaks
     *
     * @return Boolean
     */
    public boolean isUseBreak2D() {
        return this.useBreak2D;
    }

    /**
     * Set if use item 2D point breaks
     *
     * @param value Boolean
     */
    public void setUseBeak2D(boolean value) {
        this.useBreak2D = value;
    }

    /**
     * Get bar width ratio
     *
     * @return Bar width ratio
     */
    public float getBarWidth() {
        return this.barWidth;
    }

    /**
     * Set bar width ratio
     *
     * @param value Bar width ratio
     */
    public void setBarWidth(float value) {
        this.barWidth = value;
    }

    /**
     * Get if automatically decide bar width
     *
     * @return Boolean
     */
    public boolean isAutoBarWidth() {
        return this.autoBarWidth;
    }

    /**
     * Set if automatically decide bar height
     *
     * @param value Boolean
     */
    public void setAutoBarWidth(boolean value) {
        this.autoBarWidth = value;
    }

    // </editor-fold>
    // <editor-fold desc="Methods">    
    /**
     * Add a series data
     *
     * @param seriesKey Series key
     * @param xvs X value array
     * @param yvs Y value array
     */
    public void addSeries(String seriesKey, double[] xvs, double[] yvs) {
        ((XYListDataset) this.dataset).addSeries(seriesKey, xvs, yvs);
        PolylineBreak plb = new PolylineBreak();
        plb.setColor(ColorUtil.getCommonColor(this.dataset.getSeriesCount()));
        plb.setCaption(seriesKey);
        seriesLegends.add(new SeriesLegend(plb));

        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    /**
     * Remove last series
     */
    public void removeLastSeries() {
        XYListDataset ds = (XYListDataset) this.dataset;
        ds.removeSeries(dataset.getSeriesCount() - 1);
        this.seriesLegends.remove(this.seriesLegends.size() - 1);

        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    @Override
    void drawGraph(Graphics2D g, Rectangle2D area) {
        AffineTransform oldMatrix = g.getTransform();
        Rectangle oldRegion = g.getClipBounds();
        g.setClip(area);
        g.translate(area.getX(), area.getY());

        //Draw background
        if (this.isDrawBackground()) {
            g.setColor(this.getBackground());
            g.fill(new Rectangle2D.Double(0, 0, area.getWidth(), area.getHeight()));
        }

        double[] xy;
        for (int i = 0; i < this.dataset.getSeriesCount(); i++) {
            XYSeriesData sdata = this.dataset.getSeriesData(i);
            int len = sdata.dataLength();
            PointF[] points = new PointF[len];
            List<Integer> mvIdx = sdata.getMissingValueIndex();
            if (this.getPlotOrientation() == PlotOrientation.VERTICAL) {
                for (int j = 0; j < len; j++) {
                    xy = this.projToScreen(sdata.getX(j), sdata.getY(j), area);
                    points[j] = new PointF((float) xy[0], (float) xy[1]);
                }
            } else {
                for (int j = 0; j < len; j++) {
                    xy = this.projToScreen(sdata.getY(j), sdata.getX(j), area);
                    points[j] = new PointF((float) xy[0], (float) xy[1]);
                }
            }
            if (this.getYAxis().isInverse()) {
                PointF[] npoints = new PointF[len];
                PointF p;
                float y;
                for (int j = 0; j < len; j++) {
                    p = points[len - j - 1];
                    y = (float) area.getHeight() - p.Y;
                    npoints[j] = new PointF(p.X, y);
                }
                points = npoints;
                if (!mvIdx.isEmpty()) {
                    for (int j = 0; j < mvIdx.size(); j++) {
                        mvIdx.set(j, len - mvIdx.get(j) - 1);
                    }
                }
            }
            if (this.getXAxis().isInverse()) {
                PointF[] npoints = new PointF[len];
                PointF p;
                float x;
                for (int j = 0; j < len; j++) {
                    p = points[len - j - 1];
                    x = (float) area.getWidth() - p.X;
                    npoints[j] = new PointF(x, p.Y);
                }
                points = npoints;
                if (!mvIdx.isEmpty()) {
                    for (int j = 0; j < mvIdx.size(); j++) {
                        mvIdx.set(j, len - mvIdx.get(j) - 1);
                    }
                }
            }

            SeriesLegend slegend = this.seriesLegends.get(i);
            if (slegend.isLine()) {
                if (mvIdx.isEmpty()) {
                    Draw.drawPolyline(points, (PolylineBreak) slegend.getLegendBreak(), g);
                } else {
                    Draw.drawPolyline(points, (PolylineBreak) slegend.getLegendBreak(), g, mvIdx);
                }
            } else if (slegend.isPoint()) {
                if (slegend.isMutiple()) {
                    for (int j = 0; j < len; j++) {
                        if (!mvIdx.contains(j)) {
                            Draw.drawPoint(points[j], (PointBreak) slegend.getLegendBreak(j), g);
                        }
                    }
                } else {
                    for (int j = 0; j < len; j++) {
                        if (!mvIdx.contains(j)) {
                            Draw.drawPoint(points[j], (PointBreak) slegend.getLegendBreak(), g);
                        }
                    }
                }
            } else if (slegend.isPolygon()) {
                if (slegend.getPlotMethod() == ChartPlotMethod.BAR) {
                    float width = this.barWidth;
                    if (this.autoBarWidth) {
                        if (points.length > 1) {
                            width = (float) ((points[1].X - points[0].X) * 0.5) / this.dataset.getSeriesCount();
                        } else {
                            width = (float) (area.getWidth() / 10) / this.dataset.getSeriesCount();
                        }
                        float height;
                        for (int j = 0; j < len; j++) {
                            if (!mvIdx.contains(j)) {
                                height = (float) (area.getHeight() - points[j].Y);
                                Draw.drawBar(new PointF(points[j].X - width * this.dataset.getSeriesCount() / 2
                                        + i * width, (float) area.getHeight()), width, height, (PolygonBreak) slegend.getLegendBreak(), g, false, 5);
                            }
                        }
                    } else {
                        width = (float) this.projXLength(width, area);
                        float height;
                        for (int j = 0; j < len; j++) {
                            if (!mvIdx.contains(j)) {
                                height = (float) (area.getHeight() - points[j].Y);
                                Draw.drawBar(new PointF(points[j].X, (float) area.getHeight()), width, height, (PolygonBreak) slegend.getLegendBreak(), g, false, 5);
                            }
                        }
                    }
                }
            }

            //Draw error bar
            if (sdata instanceof XYErrorSeriesData) {
                XYErrorSeriesData esdata = (XYErrorSeriesData) sdata;
                g.setColor(slegend.getLegendBreak().getColor());
                PointF p;
                double error;
                double elen = 6;
                if (esdata.getYerror() != null) {
                    if (slegend.getPlotMethod() == ChartPlotMethod.BAR) {
                        g.setColor(slegend.getErrorColor());
                        float width = this.barWidth;
                        if (this.autoBarWidth) {
                            if (points.length > 1) {
                                width = (float) ((points[1].X - points[0].X) * 0.5) / this.dataset.getSeriesCount();
                            } else {
                                width = (float) (area.getWidth() / 10) / this.dataset.getSeriesCount();
                            }
                            float x;
                            for (int j = 0; j < len; j++) {
                                if (!mvIdx.contains(j)) {
                                    p = points[j];
                                    error = esdata.getYerror(j);
                                    error = this.projYLength(error, area);
                                    x = p.X - width * this.dataset.getSeriesCount() / 2
                                            + i * width + width / 2;
                                    g.draw(new Line2D.Double(x, p.Y - error, x, p.Y + error));
                                    g.draw(new Line2D.Double(x - (elen * 0.5), p.Y - error, x + (elen * 0.5), p.Y - error));
                                    g.draw(new Line2D.Double(x - (elen * 0.5), p.Y + error, x + (elen * 0.5), p.Y + error));
                                }
                            }
                        } else {
                            width = (float) this.projXLength(width, area);
                            float x;
                            for (int j = 0; j < len; j++) {
                                if (!mvIdx.contains(j)) {
                                    p = points[j];
                                    error = esdata.getYerror(j);
                                    error = this.projYLength(error, area);
                                    x = p.X + width / 2;
                                    g.draw(new Line2D.Double(x, p.Y - error, x, p.Y + error));
                                    g.draw(new Line2D.Double(x - (elen * 0.5), p.Y - error, x + (elen * 0.5), p.Y - error));
                                    g.draw(new Line2D.Double(x - (elen * 0.5), p.Y + error, x + (elen * 0.5), p.Y + error));
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < len; j++) {
                            if (!mvIdx.contains(j)) {
                                p = points[j];
                                error = esdata.getYerror(j);
                                error = this.projYLength(error, area);
                                g.draw(new Line2D.Double(p.X, p.Y - error, p.X, p.Y + error));
                                g.draw(new Line2D.Double(p.X - (elen * 0.5), p.Y - error, p.X + (elen * 0.5), p.Y - error));
                                g.draw(new Line2D.Double(p.X - (elen * 0.5), p.Y + error, p.X + (elen * 0.5), p.Y + error));
                            }
                        }
                    }
                }
                if (esdata.getXerror() != null) {
                    for (int j = 0; j < len; j++) {
                        if (!mvIdx.contains(j)) {
                            p = points[j];
                            error = esdata.getXerror(j);
                            error = this.projXLength(error, area);
                            g.draw(new Line2D.Double(p.X - error, p.Y, p.X + error, p.Y));
                            g.draw(new Line2D.Double(p.X - error, p.Y - (elen * 0.5), p.X - error, p.Y + (elen * 0.5)));
                            g.draw(new Line2D.Double(p.X + error, p.Y - (elen * 0.5), p.X + error, p.Y + (elen * 0.5)));
                        }
                    }
                }
            }
        }

        g.setClip(oldRegion);

        //Draw texts
        for (ChartText text : this.getTexts()) {
            xy = this.projToScreen(text.getX(), text.getY(), area);
            float x = (float) xy[0];
            float y = (float) xy[1];
            g.setFont(text.getFont());
            g.setColor(text.getColor());
            //Dimension dim = Draw.getStringDimension(text.getText(), g);
            //y -= dim.height * 2 / 3;
            Draw.drawString(g, text.getText(), x, y);
        }        

        g.setTransform(oldMatrix);
    }

    /**
     * Get a item point break
     *
     * @param seriesIdx Series index
     * @param itemIdx Item index
     * @return Item point break;
     */
    public PointBreak getItemPointBreak(int seriesIdx, int itemIdx) {
        return (PointBreak) this.seriesLegends.get(seriesIdx).getLegendBreak(itemIdx);
    }

    /**
     * Set item point break
     *
     * @param seriesIdx Series index
     * @param itemIdx Item index
     * @param pb Item point break
     */
    public void setItemPointBreak(int seriesIdx, int itemIdx, PointBreak pb) {
        this.seriesLegends.get(seriesIdx).setLegendBreak(itemIdx, pb);
    }

    /**
     * Get legend break
     *
     * @param seriesIdx Series index
     * @return Legend break
     */
    public ColorBreak getLegendBreak(int seriesIdx) {
        return this.seriesLegends.get(seriesIdx).getLegendBreak();
    }

    /**
     * Set legend break
     *
     * @param seriesIdx Series index
     * @param cb Legend break
     */
    public void setLegendBreak(int seriesIdx, ColorBreak cb) {
        this.seriesLegends.get(seriesIdx).setLegendBreak(cb);
    }

    /**
     * Set series legend
     *
     * @param seriesIdx Series index
     * @param sLegend SeriesLegend
     */
    public void setLegendBreak(int seriesIdx, SeriesLegend sLegend) {
        this.seriesLegends.set(seriesIdx, sLegend);
    }

//    /**
//     * Get point break
//     *
//     * @param seriesIdx Series index
//     * @return Point break
//     */
//    public PointBreak getPointBreak(int seriesIdx) {
//        return this.pointBreaks[seriesIdx];
//    }
//
//    /**
//     * Set point break
//     *
//     * @param seriesIdx Series index
//     * @param pb Point break
//     */
//    public void setPointBreak(int seriesIdx, PointBreak pb) {
//        this.pointBreaks[seriesIdx] = pb;
//    }
//
//    /**
//     * Get polygon break
//     *
//     * @param seriesIdx Series index
//     * @return Polygon break
//     */
//    public PolygonBreak getPolygonBreak(int seriesIdx) {
//        return this.polygonBreaks[seriesIdx];
//    }
//
//    /**
//     * Set polygon break
//     *
//     * @param seriesIdx Series index
//     * @param pgb Polygon break
//     */
//    public void setPolygonBreak(int seriesIdx, PolygonBreak pgb) {
//        this.polygonBreaks[seriesIdx] = pgb;
//    }
    private double getBarXInterval(int idx) {
        double[] xvalues = this.dataset.getXValues(idx);
        if (xvalues.length == 1) {
            if (xvalues[0] == 0) {
                return 1;
            } else {
                return xvalues[0] / 10;
            }
        } else {
            return xvalues[1] - xvalues[0];
        }
    }

    private int getBarIndex() {
        int idx = -1;
        for (int i = 0; i < this.seriesLegends.size(); i++) {
            if (this.seriesLegends.get(i).getPlotMethod() == ChartPlotMethod.BAR) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    /**
     * Get auto extent
     *
     * @return Auto extent
     */
    @Override
    public Extent getAutoExtent() {
        Extent extent = dataset.getDataExtent();
        int barIdx = this.getBarIndex();
        if (barIdx >= 0) {
            double dx = getBarXInterval(barIdx);
            extent.minX -= dx;
            extent.maxX += dx;
        }
        double[] xValues;
        if (this.getXAxis() instanceof TimeAxis) {
            //if (this.getXAxis().isTimeAxis()) {
            xValues = (double[]) MIMath.getIntervalValues(extent.minX, extent.maxX, false).get(0);
            xValues[0] = extent.minX;
            xValues[xValues.length - 1] = extent.maxX;
        } else {
            xValues = (double[]) MIMath.getIntervalValues(extent.minX, extent.maxX, true).get(0);
        }
        double[] yValues = (double[]) MIMath.getIntervalValues(extent.minY, extent.maxY, true).get(0);
        if (this.getPlotOrientation() == PlotOrientation.VERTICAL) {
            return new Extent(xValues[0], xValues[xValues.length - 1], yValues[0], yValues[yValues.length - 1]);
        } else {
            return new Extent(yValues[0], yValues[yValues.length - 1], xValues[0], xValues[xValues.length - 1]);
        }
    }

    /**
     * Set auto extent
     */
    public void setAutoExtent() {
        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    /**
     * Get legend scheme
     *
     * @return Legend scheme
     */
    public LegendScheme getLegendScheme() {
//        LegendScheme ls = null;
//        switch (this.chartPlotMethod) {
//            case LINE:
//            case LINE_POINT:
//                ls = new LegendScheme(ShapeTypes.Polyline);
//                ls.getLegendBreaks().addAll(Arrays.asList(this.lineBreaks));
//                break;
//            case POINT:
//                ls = new LegendScheme(ShapeTypes.Point);
//                ls.getLegendBreaks().addAll(Arrays.asList(this.pointBreaks));
//                break;
//            case BAR:
//                ls = new LegendScheme(ShapeTypes.Polygon);
//                ls.getLegendBreaks().addAll(Arrays.asList(this.polygonBreaks));
//                break;
//        }
        ShapeTypes stype = ShapeTypes.Polyline;
        LegendScheme ls = new LegendScheme(stype);
        for (SeriesLegend slegend : this.seriesLegends) {
            ls.getLegendBreaks().add(slegend.getLegendBreak());
        }
        return ls;
    }

    @Override
    public void updateLegendScheme() {
        if (this.getLegend() == null) {
            this.setLegend(new ChartLegend(this.getLegendScheme()));
        } else {
            this.getLegend().setLegendScheme(this.getLegendScheme());
        }
    }

    // </editor-fold>   
}
