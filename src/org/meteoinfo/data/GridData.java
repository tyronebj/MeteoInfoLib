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
package org.meteoinfo.data;

import org.meteoinfo.geoprocess.GeoComputation;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.meteoinfo.data.meteodata.GridDataSetting;
import org.meteoinfo.data.meteodata.InterpolationMethods;
import org.meteoinfo.layer.VectorLayer;
import org.meteoinfo.projection.ProjectionInfo;
import org.meteoinfo.projection.ProjectionManage;
import org.meteoinfo.projection.Reproject;
import org.meteoinfo.shape.PolygonShape;
import org.meteoinfo.shape.ShapeTypes;

/**
 *
 * @author yaqiang
 */
public class GridData {
    // <editor-fold desc="Variables">
    /// <summary>
    /// Grid data
    /// </summary>

    public double[][] data;
    /// <summary>
    /// x coordinate array
    /// </summary>
    public double[] xArray;
    /// <summary>
    /// y coordinate array
    /// </summary>
    public double[] yArray;
    /// <summary>
    /// Undef data
    /// </summary>
    public double missingValue;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public GridData() {
        missingValue = -9999;
    }

    /**
     * Constructor
     *
     * @param aGridData The grid data
     */
    public GridData(GridData aGridData) {
        xArray = aGridData.xArray.clone();
        yArray = aGridData.yArray.clone();
        missingValue = aGridData.missingValue;
        data = new double[yArray.length][xArray.length];
    }

    /**
     * Constructor
     *
     * @param xStart xArray start
     * @param xDelt xArray delt
     * @param xNum xArray number
     * @param yStart yArray start
     * @param yDelt yArray delt
     * @param yNum yArray number
     */
    public GridData(double xStart, double xDelt, int xNum, double yStart, double yDelt, int yNum) {
        xArray = new double[xNum];
        yArray = new double[yNum];
        for (int i = 0; i < xNum; i++) {
            xArray[i] = xStart + xDelt * i;
        }
        for (int i = 0; i < yNum; i++) {
            yArray[i] = yStart + yDelt * i;
        }

        missingValue = -9999;
        data = new double[yNum][xNum];
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get xArray number
     *
     * @return xArray number
     */
    public int getXNum() {
        return xArray.length;
    }

    /**
     * Get yArray number
     *
     * @return yArray number
     */
    public int getYNum() {
        return yArray.length;
    }

    /**
     * Get xArray delt
     *
     * @return xArray delt
     */
    public double getXDelt() {
        return xArray[1] - xArray[0];
    }

    /**
     * Get yArray delt
     *
     * @return yArray delt
     */
    public double getYDelt() {
        return yArray[1] - yArray[0];
    }

    /**
     * Get Extent
     *
     * @return Extent
     */
    public Extent getExtent() {
        Extent extent = new Extent();
        extent.minX = xArray[0];
        extent.maxX = xArray[xArray.length - 1];
        extent.minY = yArray[0];
        extent.maxY = yArray[yArray.length - 1];

        return extent;
    }

    /**
     * Get if the data is global
     *
     * @return If the data is global
     */
    public boolean isGlobal() {
        boolean isGlobal = false;
        if (MIMath.doubleEquals(xArray[getXNum() - 1] + getXDelt() - xArray[0], 360.0)) {
            isGlobal = true;
        }

        return isGlobal;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    // <editor-fold desc="Operation">
    /**
     * Add operation with another grid data
     *
     * @param bGrid The grid data
     * @return Added grid data
     */
    public GridData add(GridData bGrid) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)
                        || MIMath.doubleEquals(bGrid.data[i][j], bGrid.missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] + bGrid.data[i][j];
                }
            }
        }

        return cGrid;
    }

    /**
     * Add operation with a double value
     *
     * @param value Double value
     * @return Added grid data
     */
    public GridData add(double value) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] + value;
                }
            }
        }

        return cGrid;
    }

    /**
     * Subtraction operation with another grid data
     *
     * @param bGrid The grid data
     * @return Subtracted grid data
     */
    public GridData subtract(GridData bGrid) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)
                        || MIMath.doubleEquals(bGrid.data[i][j], bGrid.missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] - bGrid.data[i][j];
                }
            }
        }

        return cGrid;
    }

    /**
     * Subtraction operation with a double value
     *
     * @param value The double value
     * @return Subtracted grid data
     */
    public GridData subtract(double value) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] - value;
                }
            }
        }

        return cGrid;
    }

    /**
     * Multiply operation with another grid data
     *
     * @param bGrid The grid data
     * @return Result grid data
     */
    public GridData multiply(GridData bGrid) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)
                        || MIMath.doubleEquals(bGrid.data[i][j], bGrid.missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] * bGrid.data[i][j];
                }
            }
        }

        return cGrid;
    }

    /**
     * Multiply operation with a double value
     *
     * @param value Double value
     * @return Result grid data
     */
    public GridData multiply(double value) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] * value;
                }
            }
        }

        return cGrid;
    }

    /**
     * Divide operation with another grid data
     *
     * @param bGrid The grid data
     * @return Result grid data
     */
    public GridData divide(GridData bGrid) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)
                        || MIMath.doubleEquals(bGrid.data[i][j], bGrid.missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] / bGrid.data[i][j];
                }
            }
        }

        return cGrid;
    }

    /**
     * Divide operation with a double value
     *
     * @param value Double value
     * @return Result grid data
     */
    public GridData divide(double value) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    cGrid.data[i][j] = missingValue;
                } else {
                    cGrid.data[i][j] = data[i][j] / value;
                }
            }
        }

        return cGrid;
    }

    /**
     * Regrid data with double linear interpolation method
     *
     * @param gridData Result grid data
     */
    public void regrid(GridData gridData) {
        for (int i = 0; i < gridData.getYNum(); i++) {
            for (int j = 0; j < gridData.getXNum(); j++) {
                gridData.data[i][j] = toStation(gridData.xArray[j], gridData.yArray[i]);
            }
        }
    }

    /**
     * Interpolate grid data to a station point
     *
     * @param x X coordinate of the station
     * @param y Y coordinate of the station
     * @return Interpolated value
     */
    public double toStation(double x, double y) {
        double iValue = missingValue;
        if (x < xArray[0] || x > xArray[this.getXNum() - 1] || y < yArray[0] || y > yArray[this.getYNum() - 1]) {
            return iValue;
        }

        //Get x/y index
        double DX = this.getXDelt();
        double DY = this.getYDelt();
        int xIdx = 0, yIdx = 0;
        xIdx = (int) ((x - xArray[0]) / DX);
        yIdx = (int) ((y - yArray[0]) / DY);
        if (xIdx == this.getXNum() - 1) {
            xIdx = this.getXNum() - 2;
        }

        if (yIdx == this.getYNum() - 1) {
            yIdx = this.getYNum() - 2;
        }

        int i1 = yIdx;
        int j1 = xIdx;
        int i2 = i1 + 1;
        int j2 = j1 + 1;
        double a = data[i1][j1];
        double b = data[i1][j2];
        double c = data[i2][j1];
        double d = data[i2][j2];
        List<Double> dList = new ArrayList<Double>();
        if (!MIMath.doubleEquals(a, missingValue)) {
            dList.add(a);
        }
        if (!MIMath.doubleEquals(b, missingValue)) {
            dList.add(b);
        }
        if (!MIMath.doubleEquals(c, missingValue)) {
            dList.add(c);
        }
        if (!MIMath.doubleEquals(d, missingValue)) {
            dList.add(d);
        }

        if (dList.isEmpty()) {
            return iValue;
        } else if (dList.size() == 1) {
            iValue = dList.get(0);
        } else if (dList.size() <= 3) {
            double aSum = 0;
            for (double dd : dList) {
                aSum += dd;
            }
            iValue = aSum / dList.size();
        } else {
            double x1val = a + (c - a) * (y - yArray[i1]) / DY;
            double x2val = b + (d - b) * (y - yArray[i1]) / DY;
            iValue = x1val + (x2val - x1val) * (x - xArray[j1]) / DX;
        }

        return iValue;
    }

    /**
     * Interpolate grid data to station data
     *
     * @param stData Station data
     * @return Interpolated station data
     */
    public StationData toStation(StationData stData) {
        StationData nstData = new StationData(stData);
        for (int i = 0; i < nstData.getStNum(); i++) {
            nstData.setValue(i, this.toStation(nstData.getX(i), nstData.getY(i)));
        }

        return nstData;
    }

    /**
     * Set grid values by anthor grid data - overlay
     *
     * @param bGrid The grid data
     * @return Result grid data
     */
    public GridData setValue(GridData bGrid) {
        Extent aExtent = this.getExtent();
        Extent bExtent = bGrid.getExtent();
        if (!MIMath.isExtentCross(aExtent, bExtent)) {
            return this;
        }

        int xNum = this.getXNum();
        int yNum = this.getYNum();
        double axdelt = this.getXDelt();
        double aydelt = this.getYDelt();
        int sXidx = 0;
        int eXidx = xNum - 1;
        int sYidx = 0;
        int eYidx = yNum - 1;
        if (aExtent.minX < bExtent.minX) {
            sXidx = (int) ((bExtent.minX - aExtent.minX) / axdelt);
        }
        if (aExtent.maxX > bExtent.maxX) {
            eXidx = (int) ((bExtent.maxX - aExtent.minX) / axdelt);
        }
        if (aExtent.minY < bExtent.minY) {
            sYidx = (int) ((bExtent.minY - aExtent.minY) / aydelt);
        }
        if (aExtent.maxY > bExtent.maxY) {
            eYidx = (int) ((bExtent.maxY - aExtent.minY) / aydelt);
        }

        GridData cGrid = (GridData) this.clone();
        int xidx, yidx;
        double bxdelt = bGrid.getXDelt();
        double bydelt = bGrid.getYDelt();
        for (int i = sYidx; i <= eYidx; i++) {
            for (int j = sXidx; j <= eXidx; j++) {
                xidx = (int) ((xArray[j] - bGrid.xArray[0]) / bxdelt);
                if (xidx < 0 || xidx >= bGrid.getXNum()) {
                    continue;
                }
                yidx = (int) ((yArray[i] - bGrid.yArray[0]) / bydelt);
                if (yidx < 0 || yidx >= bGrid.getYNum()) {
                    continue;
                }
                cGrid.data[i][j] = bGrid.data[yidx][xidx];
            }
        }

        return cGrid;
    }

    /**
     * Set grid values by anthor grid data - overlay
     *
     * @param bGrid The grid data
     * @param useMissingData if set missing data
     * @return Result grid data
     */
    public GridData setValue(GridData bGrid, boolean useMissingData) {
        Extent aExtent = this.getExtent();
        Extent bExtent = bGrid.getExtent();
        if (!MIMath.isExtentCross(aExtent, bExtent)) {
            return this;
        }

        int xNum = this.getXNum();
        int yNum = this.getYNum();
        double axdelt = this.getXDelt();
        double aydelt = this.getYDelt();
        int sXidx = 0;
        int eXidx = xNum - 1;
        int sYidx = 0;
        int eYidx = yNum - 1;
        if (aExtent.minX < bExtent.minX) {
            sXidx = (int) ((bExtent.minX - aExtent.minX) / axdelt);
        }
        if (aExtent.maxX > bExtent.maxX) {
            eXidx = (int) ((bExtent.maxX - aExtent.minX) / axdelt);
        }
        if (aExtent.minY < bExtent.minY) {
            sYidx = (int) ((bExtent.minY - aExtent.minY) / aydelt);
        }
        if (aExtent.maxY > bExtent.maxY) {
            eYidx = (int) ((bExtent.maxY - aExtent.minY) / aydelt);
        }

        GridData cGrid = (GridData) this.clone();
        int xidx, yidx;
        double bxdelt = bGrid.getXDelt();
        double bydelt = bGrid.getYDelt();
        for (int i = sYidx; i <= eYidx; i++) {
            for (int j = sXidx; j <= eXidx; j++) {
                xidx = (int) ((xArray[j] - bGrid.xArray[0]) / bxdelt);
                if (xidx < 0 || xidx >= bGrid.getXNum()) {
                    continue;
                }
                yidx = (int) ((yArray[i] - bGrid.yArray[0]) / bydelt);
                if (yidx < 0 || yidx >= bGrid.getYNum()) {
                    continue;
                }
                if (useMissingData) {
                    if (MIMath.doubleEquals(bGrid.data[yidx][xidx], bGrid.missingValue)) {
                        continue;
                    }
                }
                cGrid.data[i][j] = bGrid.data[yidx][xidx];
            }
        }

        return cGrid;
    }

    /**
     * Set constant value to all grid cells
     *
     * @param aValue The value
     * @return Result grid data
     */
    public GridData setValue(double aValue) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData(this);
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                cGrid.data[i][j] = aValue;
            }
        }

        return cGrid;
    }

    /**
     * Merge grid values by anthor grid data - the two grids should have same
     * extent replace missing value with valid data
     *
     * @param bGrid The grid data
     * @return Result grid data
     */
    public GridData merge(GridData bGrid) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = (GridData) this.clone();
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)
                        && (!MIMath.doubleEquals(bGrid.data[i][j], bGrid.missingValue))) {
                    cGrid.data[i][j] = bGrid.data[i][j];
                }
            }
        }

        return cGrid;
    }

    /**
     * Calculate summary value
     *
     * @return Summary value
     */
    public double sum() {
        double sum = 0;
        for (int i = 0; i < this.getYNum(); i++) {
            for (int j = 0; j < this.getXNum(); j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    continue;
                }

                sum += data[i][j];
            }
        }

        return sum;
    }

    /**
     * Calculate average value
     *
     * @return Average value
     */
    public double average() {
        double ave = 0;
        int vdNum = 0;
        for (int i = 0; i < this.getYNum(); i++) {
            for (int j = 0; j < this.getXNum(); j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    continue;
                }

                ave += data[i][j];
                vdNum += 1;
            }
        }

        ave = ave / vdNum;

        return ave;
    }

    /**
     * Calculate average data array by another grid data and threshold values
     *
     * @param bGrid The grid data
     * @param tValues Threshold values
     * @return Average values
     */
    public double[] average(GridData bGrid, double[] tValues) {
        int vn = tValues.length;
        int n = vn + 1;
        double[] aves = new double[n];
        int[] vdNum = new int[n];
        int k;
        double v, bv;
        for (int i = 0; i < this.getYNum(); i++) {
            for (int j = 0; j < this.getXNum(); j++) {
                v = data[i][j];
                bv = bGrid.data[i][j];
                if (MIMath.doubleEquals(v, missingValue)) {
                    continue;
                }

                if (MIMath.doubleEquals(bv, bGrid.missingValue)) {
                    continue;
                }

                if (bv >= tValues[vn - 1]) {
                    k = vn;
                } else {
                    for (k = 0; k < vn; k++) {
                        if (bv < tValues[k]) {
                            break;
                        }
                    }
                }
                aves[k] += v;
                vdNum[k] += 1;
            }
        }

        for (k = 0; k < n; k++) {
            if (vdNum[k] > 0) {
                aves[k] = aves[k] / vdNum[k];
            } else {
                aves[k] = missingValue;
            }
        }

        return aves;
    }

    /**
     * Simple statistics of the grid data
     *
     * @return Result array - average, standard deviation
     */
    public double[] statistics() {
        double theMean, theSqDev, theSumSqDev, theVariance, theStdDev, theValue;

        theMean = average();
        theSumSqDev = 0;
        int vdNum = 0;
        for (int i = 0; i < this.getYNum(); i++) {
            for (int j = 0; j < this.getXNum(); j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    continue;
                }

                theValue = data[i][j];
                theSqDev = (theValue - theMean) * (theValue - theMean);
                theSumSqDev = theSqDev + theSumSqDev;
                vdNum += 1;
            }
        }

        theVariance = theSumSqDev / (vdNum - 1);
        theStdDev = Math.sqrt(theVariance);
        double sem = theStdDev / Math.sqrt(vdNum);

        return new double[]{theMean, theStdDev, sem};
    }
    // </editor-fold>
    // <editor-fold desc="Functions">
    /**
     * Calculate abstract grid data
     * @return Result grid data
     */
    public GridData abs(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.abs(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate anti-cosine grid data
     * @return Result grid data
     */
    public GridData acos(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.acos(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate anti-sine grid data
     * @return Result grid data
     */
    public GridData asin(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.asin(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate anti-tangent grid data
     * @return Result grid data
     */
    public GridData atan(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.atan(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate cosine grid data
     * @return Result grid data
     */
    public GridData cos(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.cos(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate sine grid data
     * @return Result grid data
     */
    public GridData sin(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.sin(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate tangent grid data
     * @return Result grid data
     */
    public GridData tan(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.abs(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate e raised specific power value of grid data
     * @return Result grid data
     */
    public GridData exp(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.exp(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate power grid data
     * @param p Power value
     * @return Result grid data
     */
    public GridData pow(double p){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.pow(data[i][j], p);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate square root grid data
     * @return Result grid data
     */
    public GridData sqrt(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.sqrt(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate logrithm grid data
     * @return Result grid data
     */
    public GridData log(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.log(data[i][j]);
            }
        }
        
        return gridData;
    }
    
    /**
     * Calculate base 10 logrithm grid data
     * @return Result grid data
     */
    public GridData log10(){
        GridData gridData = new GridData(this);
        for (int i = 0; i < gridData.getYNum(); i++){
            for (int j = 0; j < gridData.getXNum(); j++){
                if (MIMath.doubleEquals(data[i][j], missingValue))
                    gridData.data[i][j] = missingValue;
                else
                    gridData.data[i][j] = Math.abs(data[i][j]);
            }
        }
        
        return gridData;
    }
    // </editor-fold>
    // <editor-fold desc="Convert data">

    /**
     * Extend the grid data to global by add a new column data
     */
    public void extendToGlobal() {
        int yNum = getYNum();
        int xNum = getXNum();
        double[][] newGriddata = new double[yNum][xNum + 1];
        double[] newX = new double[xNum + 1];
        int i, j;
        for (i = 0; i < xNum; i++) {
            newX[i] = xArray[i];
        }

        newX[xNum] = newX[xNum - 1] + getXDelt();
        for (i = 0; i < yNum; i++) {
            for (j = 0; j < xNum; j++) {
                newGriddata[i][j] = data[i][j];
            }
            newGriddata[i][xNum] = newGriddata[i][0];
        }

        data = newGriddata;
        xArray = newX;
    }

    // </editor-fold>
    // <editor-fold desc="Modify">
    /**
     * Extract grid data by extent
     *
     * @param extent Extent
     * @return Extracted grid data
     */
    public GridData extract(Extent extent) {
        return extract(extent.minX, extent.maxX, extent.minY, extent.maxY);
    }

    /**
     * Extract grid data by extent
     *
     * @param sX Start X
     * @param eX End X
     * @param sY Start Y
     * @param eY End Y
     * @return Grid data
     */
    public GridData extract(double sX, double eX, double sY, double eY) {
        if (eX <= sX || eY <= sY) {
            return null;
        }

        int xNum = this.getXNum();
        int yNum = this.getYNum();
        if (sX >= xArray[xNum - 2] || sY >= yArray[yNum - 2]) {
            return null;
        }

        GridData aGridData = new GridData();
        aGridData.missingValue = missingValue;
        int sXidx = 0, eXidx = xNum - 1, sYidx = 0, eYidx = yNum - 1;

        //Get start x
        int i;
        if (sX <= xArray[0]) {
            sXidx = 0;
        } else {
            for (i = 0; i < xNum; i++) {
                if (sX <= xArray[i]) {
                    sXidx = i;
                    break;
                }
            }
        }

        //Get end x            
        if (eX >= xArray[xNum - 1]) {
            eXidx = xNum - 1;
        } else {
            for (i = 0; i < xNum; i++) {
                if (eX <= xArray[i]) {
                    eXidx = i;
                    break;
                }
            }
        }

        //Get star y            
        if (sY <= yArray[0]) {
            sYidx = 0;
        } else {
            for (i = 0; i < yNum; i++) {
                if (sY <= yArray[i]) {
                    sYidx = i;
                    break;
                }
            }
        }

        //Get end y            
        if (eY >= yArray[yNum - 1]) {
            eYidx = yNum - 1;
        } else {
            for (i = 0; i < yNum; i++) {
                if (eY <= yArray[i]) {
                    eYidx = i;
                    break;
                }
            }
        }

        int newXNum = eXidx - sXidx + 1;
        double[] newX = new double[newXNum];
        for (i = sXidx; i <= eXidx; i++) {
            newX[i - sXidx] = xArray[i];
        }

        int newYNum = eYidx - sYidx + 1;
        double[] newY = new double[newYNum];
        for (i = sYidx; i <= eYidx; i++) {
            newY[i - sYidx] = yArray[i];
        }

        aGridData.xArray = newX;
        aGridData.yArray = newY;

        double[][] newData = new double[newYNum][newXNum];
        for (i = sYidx; i <= eYidx; i++) {
            for (int j = sXidx; j <= eXidx; j++) {
                newData[i - sYidx][j - sXidx] = data[i][j];
            }
        }
        aGridData.data = newData;

        return aGridData;
    }

    /**
     * Extract grid data by extent index
     *
     * @param sXIdx Start x index
     * @param sYIdx Start y index
     * @param xNum X number
     * @param yNum Y number
     * @return Extracted grid data
     */
    public GridData extract(int sXIdx, int sYIdx, int xNum, int yNum) {
        GridData aGridData = new GridData();
        aGridData.missingValue = missingValue;
        int eXIdx = sXIdx + xNum - 1, eYIdx = sYIdx + yNum - 1;
        double[] newX = new double[xNum];
        int i;
        for (i = sXIdx; i <= eXIdx; i++) {
            newX[i - sXIdx] = xArray[i];
        }

        double[] newY = new double[yNum];
        for (i = sYIdx; i <= eYIdx; i++) {
            newY[i - sYIdx] = yArray[i];
        }

        aGridData.xArray = newX;
        aGridData.yArray = newY;

        double[][] newData = new double[yNum][xNum];
        for (i = sYIdx; i <= eYIdx; i++) {
            for (int j = sXIdx; j <= eXIdx; j++) {
                newData[i - sYIdx][j - sXIdx] = data[i][j];
            }
        }
        aGridData.data = newData;

        return aGridData;
    }
    
    /**
     * Set missing value - bigger or smaller than the given value
     * 
     * @param value The given value
     * @param isBigger Is bigger or not
     */
    public void setMissingValue(double value, boolean isBigger){
        int xnum = this.getXNum();
        int ynum = this.getYNum();
        for (int i = 0; i < ynum; i++){
            for (int j = 0; j < xnum; j++){
                if (isBigger){
                    if (this.data[i][j] > value)
                        this.data[i][j] = this.missingValue;
                }
                else {
                    if (this.data[i][j] < value)
                        this.data[i][j] = this.missingValue;
                }
            }
        }
    }

    /**
     * Skip the grid data by two dimension skip factor
     *
     * @param skipI
     * @param skipJ
     * @return
     */
    public GridData skip(int skipI, int skipJ) {
        int yNum = (getYNum() + skipI - 1) / skipI;
        int xNum = (getXNum() + skipJ - 1) / skipJ;
        int i, j, idxI, idxJ;

        GridData gdata = new GridData();
        gdata.missingValue = missingValue;
        gdata.xArray = new double[xNum];
        gdata.yArray = new double[yNum];
        gdata.data = new double[yNum][xNum];

        for (i = 0; i < yNum; i++) {
            idxI = i * skipI;
            gdata.yArray[i] = yArray[idxI];
        }
        for (j = 0; j < xNum; j++) {
            idxJ = j * skipJ;
            gdata.xArray[j] = xArray[idxJ];
        }
        for (i = 0; i < yNum; i++) {
            idxI = i * skipI;
            for (j = 0; j < xNum; j++) {
                idxJ = j * skipJ;
                gdata.data[i][j] = data[idxI][idxJ];
            }
        }

        return gdata;
    }
    // </editor-fold>
    // <editor-fold desc="Others">

    /**
     * Save as Surfer ASCII data file
     *
     * @param aFile File path
     */
    public void saveAsSurferASCIIFile(String aFile) {
        try {
            BufferedWriter sw = new BufferedWriter(new FileWriter(new File(aFile)));
            sw.write("DSAA");
            sw.newLine();
            sw.write(String.valueOf(this.getXNum()) + " " + String.valueOf(this.getYNum()));
            sw.newLine();
            sw.write(String.valueOf(xArray[0]) + " " + String.valueOf(xArray[xArray.length - 1]));
            sw.newLine();
            sw.write(String.valueOf(yArray[0]) + " " + String.valueOf(yArray[yArray.length - 1]));
            sw.newLine();
            double[] maxmin = new double[2];
            getMaxMinValue(maxmin);
            sw.write(String.valueOf(maxmin[1]) + " " + String.valueOf(maxmin[0]));
            double value;
            String aLine = "";
            for (int i = 0; i < this.getYNum(); i++) {
                for (int j = 0; j < this.getXNum(); j++) {
                    if (MIMath.doubleEquals(data[i][j], missingValue)) {
                        value = -9999.0;
                    } else {
                        value = data[i][j];
                    }

                    if (j == 0) {
                        aLine = String.valueOf(value);
                    } else {
                        aLine = aLine + " " + String.valueOf(value);
                    }
                }
                sw.newLine();
                sw.write(aLine);
            }
            sw.flush();
            sw.close();
        } catch (IOException ex) {
            Logger.getLogger(GridData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save as station data file
     *
     * @param filePath File Path
     * @param fieldName Field name
     */
    public void saveAsStationData(String filePath, String fieldName) {
        try {
            BufferedWriter sw = new BufferedWriter(new FileWriter(new File(filePath)));
            sw.write("Id,X,Y," + fieldName);
            String aLine = "";
            int id = 1;
            for (int i = 0; i < this.getYNum(); i++) {
                for (int j = 0; j < this.getXNum(); j++) {
                    if (!MIMath.doubleEquals(data[i][j], missingValue)) {
                        aLine = String.valueOf(id) + "," + String.valueOf(xArray[j]) + "," + String.valueOf(yArray[i]) + "," + String.valueOf(data[i][j]);
                        sw.newLine();
                        sw.write(aLine);
                    }
                }
            }
            sw.flush();
            sw.close();
        } catch (IOException ex) {
            Logger.getLogger(GridData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get maximum and minimum values
     *
     * @param maxmin Max/Min array
     * @return If has undefine data
     */
    public boolean getMaxMinValue(double[] maxmin) {
        double max = 0;
        double min = 0;
        int vdNum = 0;
        boolean hasUndef = false;
        for (int i = 0; i < getYNum(); i++) {
            for (int j = 0; j < getXNum(); j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    hasUndef = true;
                    continue;
                }

                if (vdNum == 0) {
                    min = data[i][j];
                    max = min;
                } else {
                    if (min > data[i][j]) {
                        min = data[i][j];
                    }
                    if (max < data[i][j]) {
                        max = data[i][j];
                    }
                }
                vdNum += 1;
            }
        }

        maxmin[0] = max;
        maxmin[1] = min;
        return hasUndef;
    }

    /**
     * Get maximum and minimum values
     *
     * @return Max/Min array
     */
    public double[] getMaxMinValue() {
        double max = 0;
        double min = 0;
        int vdNum = 0;
        boolean hasUndef = false;
        for (int i = 0; i < getYNum(); i++) {
            for (int j = 0; j < getXNum(); j++) {
                if (MIMath.doubleEquals(data[i][j], missingValue)) {
                    hasUndef = true;
                    continue;
                }

                if (vdNum == 0) {
                    min = data[i][j];
                    max = min;
                } else {
                    if (min > data[i][j]) {
                        min = data[i][j];
                    }
                    if (max < data[i][j]) {
                        max = data[i][j];
                    }
                }
                vdNum += 1;
            }
        }

        return new double[]{max, min};
    }

    /**
     * Get grid data setting
     *
     * @return Grid data setting
     */
    public GridDataSetting getGridDataSetting() {
        GridDataSetting gDataSet = new GridDataSetting();
        gDataSet.dataExtent = (Extent) this.getExtent().clone();
        gDataSet.xNum = this.getXNum();
        gDataSet.yNum = this.getYNum();
        return gDataSet;
    }

    /**
     * Mask out grid data with a polygon shape
     *
     * @param aPGS The polygon shape
     * @return Maskouted grid data
     */
    public GridData maskout(PolygonShape aPGS) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            if (yArray[i] >= aPGS.getExtent().minY && yArray[i] <= aPGS.getExtent().maxY) {
                for (int j = 0; j < xNum; j++) {
                    if (xArray[j] >= aPGS.getExtent().minX && xArray[j] <= aPGS.getExtent().maxX) {
                        if (GeoComputation.pointInPolygon(aPGS, new PointD(xArray[j], yArray[i]))) {
                            cGrid.data[i][j] = data[i][j];
                        } else {
                            cGrid.data[i][j] = missingValue;
                        }
                    } else {
                        cGrid.data[i][j] = missingValue;
                    }
                }
            } else {
                for (int j = 0; j < xNum; j++) {
                    cGrid.data[i][j] = missingValue;
                }
            }
        }

        return cGrid;
    }

    /**
     * Mask out grid data with polygon shapes
     *
     * @param polygons The polygon shapes
     * @return Maskouted grid data
     */
    public GridData maskout(List<PolygonShape> polygons) {
        int xNum = this.getXNum();
        int yNum = this.getYNum();

        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                if (GeoComputation.pointInPolygons(polygons, new PointD(xArray[j], yArray[i]))) {
                    cGrid.data[i][j] = data[i][j];
                } else {
                    cGrid.data[i][j] = missingValue;
                }
            }
        }

        return cGrid;
    }

    /**
     * Mask out grid data with a polygon layer
     *
     * @param maskLayer The polygon layer
     * @return Maskouted grid data
     */
    public GridData maskout(VectorLayer maskLayer) {
        if (maskLayer.getShapeType() != ShapeTypes.Polygon) {
            return this;
        }

        int xNum = this.getXNum();
        int yNum = this.getYNum();
        GridData cGrid = new GridData();
        cGrid.xArray = xArray;
        cGrid.yArray = yArray;
        cGrid.data = new double[yNum][xNum];
        cGrid.missingValue = missingValue;

        for (int i = 0; i < yNum; i++) {
            if (yArray[i] >= maskLayer.getExtent().minY && yArray[i] <= maskLayer.getExtent().maxY) {
                for (int j = 0; j < xNum; j++) {
                    if (xArray[j] >= maskLayer.getExtent().minX && xArray[j] <= maskLayer.getExtent().maxX) {
                        if (GeoComputation.pointInPolygonLayer(maskLayer, new PointD(xArray[j], yArray[i]), false)) {
                            cGrid.data[i][j] = data[i][j];
                        } else {
                            cGrid.data[i][j] = missingValue;
                        }
                    } else {
                        cGrid.data[i][j] = missingValue;
                    }
                }
            } else {
                for (int j = 0; j < xNum; j++) {
                    cGrid.data[i][j] = missingValue;
                }
            }
        }

        return cGrid;
    }

    /**
     * Mask out grid data by a mask grid data
     *
     * @param maskGrid Mask grid data
     * @return Result grid data
     */
    public GridData maskout(GridData maskGrid) {
        GridData cGrid = new GridData(this);
        int xnum = this.getXNum();
        int ynum = this.getYNum();
        for (int i = 0; i < ynum; i++) {
            for (int j = 0; j < xnum; j++) {
                if (maskGrid.data[i][j] >= 0) {
                    cGrid.data[i][j] = data[i][j];
                } else {
                    cGrid.data[i][j] = missingValue;
                }
            }
        }

        return cGrid;
    }

    /**
     * Project grid data
     *
     * @param fromProj From projection
     * @param toProj To projection
     * @return Porjected grid data
     */
    public GridData project(ProjectionInfo fromProj, ProjectionInfo toProj) {
        Extent aExtent;
        int xnum = this.getXNum();
        int ynum = this.getYNum();
        if (this.isGlobal() || this.xArray[xnum - 1] - this.xArray[0] == 360) {
            aExtent = ProjectionManage.getProjectionGlobalExtent(toProj);
        } else {
            aExtent = ProjectionManage.getProjectionExtent(fromProj, toProj, this.xArray, this.yArray);
        }

        double xDelt = (aExtent.maxX - aExtent.minX) / (xnum - 1);
        double yDelt = (aExtent.maxY - aExtent.minY) / (ynum - 1);
        double[] newX = new double[xnum];
        double[] newY = new double[ynum];
        for (int i = 0; i < newX.length; i++) {
            newX[i] = aExtent.minX + i * xDelt;
        }

        for (int i = 0; i < newY.length; i++) {
            newY[i] = aExtent.minY + i * yDelt;
        }

        GridData pGridData = project(fromProj, toProj, newX, newY);

        return pGridData;
    }

    /**
     * Project grid data
     *
     * @param fromProj From projection info
     * @param toProj To projection info
     * @param newX New xArray coordinates
     * @param newY New yArray coordinates
     * @return Projected grid data
     */
    public GridData project(ProjectionInfo fromProj, ProjectionInfo toProj, double[] newX, double[] newY) {
        double[][] newdata = new double[newY.length][newX.length];
        int i, j, xIdx, yIdx;
        double x, y;

        double[][] points = new double[1][];
        for (i = 0; i < newY.length; i++) {
            for (j = 0; j < newX.length; j++) {
                points[0] = new double[]{newX[j], newY[i]};
                try {
                    Reproject.reprojectPoints(points, toProj, fromProj, 0, 1);
                    x = points[0][0];
                    y = points[0][1];

                    if (x < xArray[0] || x > xArray[xArray.length - 1]) {
                        newdata[i][j] = missingValue;
                    } else if (y < yArray[0] || y > yArray[yArray.length - 1]) {
                        newdata[i][j] = missingValue;
                    } else {
                        xIdx = (int) ((x - xArray[0]) / getXDelt());
                        yIdx = (int) ((y - yArray[0]) / getYDelt());
                        newdata[i][j] = data[yIdx][xIdx];
                    }
                } catch (Exception e) {
                    newdata[i][j] = missingValue;
                    j++;
                    continue;
                }
            }
        }

        GridData gData = new GridData(this);
        gData.data = newdata;
        gData.xArray = newX;
        gData.yArray = newY;

        return gData;
    }

    /**
     * Project grid data
     *
     * @param fromProj From projection info
     * @param toProj To projection info
     * @param newX New xArray coordinates
     * @param newY New yArray coordinates
     * @param interpMethod Interpolation method
     * @return Projected grid data
     */
    public GridData project(ProjectionInfo fromProj, ProjectionInfo toProj, double[] newX, double[] newY,
            InterpolationMethods interpMethod) {
        switch (interpMethod) {
            case Bilinear:
                return project_Bilinear(fromProj, toProj, newX, newY);
            default:
                return project_Bilinear(fromProj, toProj, newX, newY);
        }
    }

    /**
     * Project grid data to station data
     *
     * @param fromProj From projection info
     * @param toProj To projection info
     * @param stData Station data
     * @param interpMethod Interpolation method
     * @return Projected station data
     */
    public StationData project(ProjectionInfo fromProj, ProjectionInfo toProj, StationData stData,
            InterpolationMethods interpMethod) {
        switch (interpMethod) {
            case Bilinear:
                return project_Bilinear(fromProj, toProj, stData);
            default:
                return project_Bilinear(fromProj, toProj, stData);
        }
    }

    private GridData project_Bilinear(ProjectionInfo fromProj, ProjectionInfo toProj, double[] newX, double[] newY) {
        //PointD[][] pos = new PointD[newY.length][newX.length];
        double[][] newdata = new double[newY.length][newX.length];
        int i, j;
        double x, y;

        double[][] points = new double[1][];
        for (i = 0; i < newY.length; i++) {
            for (j = 0; j < newX.length; j++) {
                points[0] = new double[]{newX[j], newY[i]};
                try {
                    Reproject.reprojectPoints(points, toProj, fromProj, 0, 1);
                    x = points[0][0];
                    y = points[0][1];

                    if (x < xArray[0] || x > xArray[xArray.length - 1]) {
                        newdata[i][j] = missingValue;
                    } else if (y < yArray[0] || y > yArray[yArray.length - 1]) {
                        newdata[i][j] = missingValue;
                    } else {
                        newdata[i][j] = this.toStation(x, y);
                    }
                } catch (Exception e) {
                    newdata[i][j] = missingValue;
                    j++;
                    continue;
                }
            }
        }

        GridData gData = new GridData(this);
        gData.data = newdata;
        gData.xArray = newX;
        gData.yArray = newY;

        return gData;
    }

    private StationData project_Bilinear(ProjectionInfo fromProj, ProjectionInfo toProj, StationData stData) {
        int i;
        double x, y;
        StationData nsData = new StationData(stData);
        nsData.missingValue = missingValue;

        double[][] points = new double[1][];
        for (i = 0; i < stData.getStNum(); i++) {
            points[0] = new double[]{stData.getX(i), stData.getY(i)};
            try {
                Reproject.reprojectPoints(points, toProj, fromProj, 0, 1);
                x = points[0][0];
                y = points[0][1];

                if (x < xArray[0] || x > xArray[xArray.length - 1]) {
                    nsData.setValue(i, missingValue);
                } else if (y < yArray[0] || y > yArray[yArray.length - 1]) {
                    nsData.setValue(i, missingValue);
                } else {
                    nsData.setValue(i, this.toStation(x, y));
                }
            } catch (Exception e) {
                nsData.setValue(i, missingValue);
                i++;
                continue;
            }
        }

        return nsData;
    }

    /**
     * Clone
     *
     * @return Grid data object
     */
    @Override
    public Object clone() {
        GridData newGriddata = new GridData(this);
        newGriddata.data = data.clone();

        return newGriddata;
    }

    /**
     * yArray reverse to the grid data
     */
    public void yReverse() {
        int xNum = getXNum();
        int yNum = getYNum();
        double[][] newdata = new double[yNum][xNum];
        for (int i = 0; i < yNum; i++) {
            for (int j = 0; j < xNum; j++) {
                newdata[yNum - i - 1][j] = data[i][j];
            }
        }
        data = newdata;
    }
    // </editor-fold>
    // </editor-fold>
}
