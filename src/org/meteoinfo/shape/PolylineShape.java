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
package org.meteoinfo.shape;

import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointD;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Poyline shape class
 * 
 * @author Yaqiang Wang
 */
public class PolylineShape extends Shape {
    // <editor-fold desc="Variables">

    private List<? extends PointD> _points;
    private List<? extends Polyline> _polylines;
    /**
     * Value
     */
    public double value;
    /**
     * Part number
     */
    private int _numParts;
    /**
     * Part array
     */
    public int[] parts;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public PolylineShape() {
        this.setShapeType(ShapeTypes.Polyline);
        _points = new ArrayList<PointD>();
        _numParts = 1;
        parts = new int[1];
        parts[0] = 0;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get points
     * 
     * @return point list
     */
    @Override
    public List<? extends PointD> getPoints() {
        return _points;
    }

    /**
     * Set points
     * 
     * @param points point list
     */
    @Override
    public void setPoints(List<? extends PointD> points) {
        _points = points;
        this.setExtent(MIMath.getPointsExtent(_points));
        updatePolyLines();
    }
    
    /**
     * Get part number
     * @return Part number
     */
    public int getPartNum(){
        return this._numParts;
    }
    
    /**
     * Set part number
     * @param value Part number
     */
    public void setPartNum(int value){
        this._numParts = value;
    }
    
    /**
     * Get point number
     * @return Point number
     */
    public int getPointNum(){
        return this._points.size();
    }

    /**
     * Get polylines
     * 
     * @return polyline list
     */
    public List<? extends Polyline> getPolylines() {
        return _polylines;
    }

    public void setPolylines(List<? extends Polyline> polylines) {
        _polylines = polylines;
        updatePartsPoints();
    }

    /**
     * Get length
     * 
     * @return length
     */
    public double getLength() {
        double length = 0.0;
        double dx, dy;
        for (Polyline aPL : _polylines) {
            for (int i = 0; i < aPL.getPointList().size() - 1; i++) {
                dx = aPL.getPointList().get(i + 1).X - aPL.getPointList().get(i).X;
                dy = aPL.getPointList().get(i + 1).Y - aPL.getPointList().get(i).Y;
                length += Math.sqrt(dx * dx + dy * dy);
            }
        }

        return length;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    private void updatePolyLines() {
        List<Polyline> polylines = new ArrayList<Polyline>();
        if (_numParts == 1) {
            Polyline aPolyLine = new Polyline();
            aPolyLine.setPointList(_points);
            polylines.add(aPolyLine);
        } else {
            PointD[] Pointps;
            Polyline aPolyLine = null;
            int numPoints = this.getPointNum();
            for (int p = 0; p < _numParts; p++) {
                if (p == _numParts - 1) {
                    Pointps = new PointD[numPoints - parts[p]];
                    for (int pp = parts[p]; pp < numPoints; pp++) {
                        Pointps[pp - parts[p]] = _points.get(pp);
                    }
                } else {
                    Pointps = new PointD[parts[p + 1] - parts[p]];
                    for (int pp = parts[p]; pp < parts[p + 1]; pp++) {
                        Pointps[pp - parts[p]] = _points.get(pp);
                    }
                }

                aPolyLine = new Polyline();
                aPolyLine.setPointList(Arrays.asList(Pointps));
                polylines.add(aPolyLine);
            }
        }
        
        _polylines = polylines;
    }

    private void updatePartsPoints() {
        _numParts = 0;
        List<PointD> points = new ArrayList<PointD>();
        List<Integer> partList = new ArrayList<Integer>();
        for (int i = 0; i < _polylines.size(); i++) {
            _numParts += 1;
            partList.add(points.size());
            points.addAll(_polylines.get(i).getPointList());
        }
        _points = points;
        parts = new int[partList.size()];
        for (int i = 0; i < partList.size(); i++) {
            parts[i] = partList.get(i);
        }
        this.setExtent(MIMath.getPointsExtent(_points));
    }

    /**
     * Clone
     * 
     * @return PolylineShape
     */
    @Override
    public Object clone() {
        PolylineShape aPLS = new PolylineShape();
        aPLS.value = value;
        aPLS.setExtent(this.getExtent());
        aPLS._numParts = _numParts;
        aPLS.parts = (int[]) parts.clone();
        aPLS.setPoints(_points);
        aPLS.setVisible(this.isVisible());
        aPLS.setSelected(this.isSelected());
        aPLS.setLegendIndex(this.getLegendIndex());

        return aPLS;
    }

    /**
     * Value clone
     * @return PolylineShape
     */
    public PolylineShape valueClone() {
        PolylineShape aPLS = new PolylineShape();
        aPLS.value = value;
        aPLS.setVisible(this.isVisible());
        aPLS.setSelected(this.isSelected());
        aPLS.setLegendIndex(this.getLegendIndex());

        return aPLS;
    }
    // </editor-fold>
}
