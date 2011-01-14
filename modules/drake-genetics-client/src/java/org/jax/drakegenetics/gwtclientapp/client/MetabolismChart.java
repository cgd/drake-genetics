/*
 * Copyright (c) 2010 The Jackson Laboratory
 * 
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jax.drakegenetics.gwtclientapp.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ImageLineChart;
import com.google.gwt.visualization.client.visualizations.ImageLineChart.Options;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MetabolismChart extends Composite
{
    private static boolean visAPILoaded = false;
    private AbsolutePanel absolutePositionsPanel;
    private ImageLineChart lineChart = null;
    
    private String chartTitle;
    private Map<String, double[]> metabolismData;
    
    private int pixelHeight = -1;
    private int pixelWidth = -1;
    
    /**
     * Default constructor. If you have already loaded the chart library use
     * the other constructor.
     */
    public MetabolismChart()
    {
        this(false);
    }
    
    /**
     * Constructor
     * @param visAPIPreloaded
     *          set to true if the visualization API has already been loaded
     *          with LineChart (otherwise this constructor will do it)
     */
    public MetabolismChart(boolean visAPIPreloaded)
    {
        this.absolutePositionsPanel = new AbsolutePanel();
        this.initWidget(this.absolutePositionsPanel);
        
        if(visAPIPreloaded)
        {
            MetabolismChart.visAPILoaded = true;
        }
        
        // TODO we have a race condition here if multiple charts are constructed
        //      before initialization has a chance to complete
        if(!MetabolismChart.visAPILoaded)
        {
            Runnable loadLineChartCallback = new Runnable()
            {
                public void run()
                {
                    MetabolismChart.this.visualizationAPILoaded();
                }
            };
            VisualizationUtils.loadVisualizationApi(loadLineChartCallback, ImageLineChart.PACKAGE);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPixelSize(int width, int height)
    {
        super.setPixelSize(width, height);
        
        this.pixelHeight = height;
        this.pixelWidth = width;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(String width, String height)
    {
        super.setSize(width, height);
        
        this.pixelHeight = -1;
        this.pixelWidth = -1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeight(String height)
    {
        super.setHeight(height);
        
        this.pixelHeight = -1;
        this.pixelWidth = -1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setWidth(String width)
    {
        super.setWidth(width);
        
        this.pixelHeight = -1;
        this.pixelWidth = -1;
    }
    
    private void visualizationAPILoaded()
    {
        MetabolismChart.visAPILoaded = true;
        if(this.metabolismData != null)
        {
            this.updateChart();
        }
    }
    
    /**
     * Draw the metabolism chart for the given parameters
     * @param chartTitle
     *          this will be used as the chart title
     * @param metabolismData
     *          the metabolismData to set
     */
    public void drawChart(String chartTitle, Map<String, double[]> metabolismData)
    {
        this.chartTitle = chartTitle;
        this.metabolismData = metabolismData;
        if(MetabolismChart.visAPILoaded)
        {
            this.updateChart();
        }
    }
    
    /**
     * Getter for the chart title
     * @return the chart title
     */
    public String getChartTitle()
    {
        return this.chartTitle;
    }
    
    /**
     * Getter for the metabolism data
     * @return the metabolism data
     */
    public Map<String, double[]> getMetabolismData()
    {
        return this.metabolismData;
    }
    
    /**
     * Repaints the chart using the existing title and data
     */
    public void redrawChart()
    {
        if(this.metabolismData != null && MetabolismChart.visAPILoaded)
        {
            this.updateChart();
        }
    }
    
    private void updateChart()
    {
        this.absolutePositionsPanel.clear();
        this.lineChart = null;
        
        if(this.metabolismData != null)
        {
            this.lineChart = new ImageLineChart(
                    this.metabolismDataToDataTable(),
                    this.createLineChartOptions());
            if(this.pixelHeight >= 1 && this.pixelWidth >= 1)
            {
                this.lineChart.setPixelSize(this.pixelWidth, this.pixelHeight);
            }
            else
            {
                this.lineChart.setPixelSize(
                        this.absolutePositionsPanel.getOffsetWidth(),
                        this.absolutePositionsPanel.getOffsetHeight());
            }
            this.absolutePositionsPanel.add(this.lineChart, 0, 0);
        }
    }
    
    private Options createLineChartOptions()
    {
        Options options = Options.create();
        
        if(this.pixelHeight >= 1 && this.pixelWidth >= 1)
        {
            options.setWidth(this.pixelWidth);
            options.setHeight(this.pixelHeight);
        }
        else
        {
            options.setWidth(this.absolutePositionsPanel.getOffsetWidth());
            options.setHeight(this.absolutePositionsPanel.getOffsetHeight());
        }
        
        options.setTitle(this.chartTitle);
        
        return options;
    }
    
    private DataTable metabolismDataToDataTable()
    {
        DataTable data = DataTable.create();
        
        data.addColumn(ColumnType.STRING, "x");
        
        int dataRowCount = -1;
        for(double[] metaValues : this.metabolismData.values())
        {
            if(dataRowCount == -1)
            {
                dataRowCount = metaValues.length;
            }
            else if(dataRowCount != metaValues.length)
            {
                throw new IllegalStateException(
                        "all metabolite arrays should be the same length");
            }
        }
        
        List<String> metaboliteNames = new ArrayList<String>(this.metabolismData.keySet());
        Collections.sort(metaboliteNames);
        for(String metabolite: metaboliteNames)
        {
            data.addColumn(ColumnType.NUMBER, metabolite);
        }
        
        data.addRows(dataRowCount);
        for(int row = 0; row < dataRowCount; row++)
        {
            data.setValue(row, 0, Integer.toString(row));
            for(int col = 0; col < metaboliteNames.size(); col++)
            {
                data.setValue(
                        row,
                        col + 1,
                        this.metabolismData.get(metaboliteNames.get(col))[row]);
            }
        }
                
        return data;
    }
}
