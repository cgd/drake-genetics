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
package org.jax.drakegenetics.shareddata.client;

import java.io.Serializable;

/**
 * Represents a particular crossover.
 * @author  <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CrossoverPoint implements Serializable
{
    private static final long serialVersionUID = -6036262571418761352L;
    
    private String distalHaplotypeId;
    private double centimorganPosition;

    /**
     * Constructor
     */
    public CrossoverPoint()
    {
    }
    
    /**
     * Constructor
     * @param distalHaplotypeId see {@link #getDistalHaplotypeId()}
     * @param centimorganPosition see {@link #getCentimorganPosition()}
     */
    public CrossoverPoint(String distalHaplotypeId, double centimorganPosition)
    {
        this.distalHaplotypeId = distalHaplotypeId;
        this.centimorganPosition = centimorganPosition;
    }

    /**
     * Constructor
     * @param crossoverPoint    the crossover point
     */
    public CrossoverPoint(CrossoverPoint crossoverPoint)
    {
        this.distalHaplotypeId = crossoverPoint.distalHaplotypeId;
        this.centimorganPosition = crossoverPoint.centimorganPosition;
    }
    
    /**
     * Getter for the distal haplotype ID
     * @return the distalHaplotypeId
     */
    public String getDistalHaplotypeId()
    {
        return this.distalHaplotypeId;
    }
    
    /**
     * @param distalHaplotypeId the distalHaplotypeId to set
     */
    public void setDistalHaplotypeId(String distalHaplotypeId)
    {
        this.distalHaplotypeId = distalHaplotypeId;
    }
    
    /**
     * @return the centimorganPosition
     */
    public double getCentimorganPosition()
    {
        return this.centimorganPosition;
    }
    
    /**
     * @param centimorganPosition the centimorganPosition to set
     */
    public void setCentimorganPosition(double centimorganPosition)
    {
        this.centimorganPosition = centimorganPosition;
    }
}