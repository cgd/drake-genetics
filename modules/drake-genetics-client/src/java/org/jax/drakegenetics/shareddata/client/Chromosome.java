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
import java.util.List;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Chromosome implements Serializable
{
    private static final long serialVersionUID = 6237372279849745397L;
    
    private int numTenCmSegments;
    private int centromereSegmentIndex;
    private String chromosomeName;
    private List<CrossoverPoint> crossovers;
    private String proximalHaplotypeId;
    
    /**
     * Getter for the number of segments. Chromosomes are always a multiple of
     * 10 cM long.
     * @return the number of segments
     */
    public int getNumTenCmSegments()
    {
        return this.numTenCmSegments;
    }
    
    /**
     * Setter for the number of segments
     * @param numTenCmSegments the new number of segments
     */
    public void setNumTenCmSegments(int numTenCmSegments)
    {
        this.numTenCmSegments = numTenCmSegments;
    }
    
    /**
     * Determines which 10 cM segment contains the centromere.
     * @return the centromereSegmentIndex
     */
    public int getCentromereSegmentIndex()
    {
        return this.centromereSegmentIndex;
    }
    
    /**
     * Set the centromere containing the index. This number should be less
     * than {@link #numTenCmSegments}
     * @param centromereSegmentIndex the centromereSegmentIndex to set
     */
    public void setCentromereSegmentIndex(int centromereSegmentIndex)
    {
        this.centromereSegmentIndex = centromereSegmentIndex;
    }
    
    /**
     * Getter for the name of this chromosome. Eg: "19" "X" "Y" ...
     * @return the chromosomeName
     */
    public String getChromosomeName()
    {
        return this.chromosomeName;
    }
    
    /**
     * Setter for the name of this chromosome
     * @param chromosomeName the chromosomeName to set
     */
    public void setChromosomeName(String chromosomeName)
    {
        this.chromosomeName = chromosomeName;
    }
    
    /**
     * Get all of the crossover points. The list returned should be in order
     * from the most proximal crossover to the most distal
     * @return the crossovers
     */
    public List<CrossoverPoint> getCrossovers()
    {
        return this.crossovers;
    }
    
    /**
     * Set the crossover point. The given list must be ordered from the most
     * proximal to the most distal point
     * @param crossovers the crossovers to set
     */
    public void setCrossovers(List<CrossoverPoint> crossovers)
    {
        this.crossovers = crossovers;
    }
    
    /**
     * Getter for the most proximal haplotype ID. This is needed because the
     * data in {@link #getCrossovers()} only contains the haplotype ID distal
     * to the crossover 
     * @return the proximalHaplotypeId
     */
    public String getProximalHaplotypeId()
    {
        return this.proximalHaplotypeId;
    }
    
    /**
     * Setter for the most proximal haplotype ID.
     * @param proximalHaplotypeId the proximalHaplotypeId to set
     */
    public void setProximalHaplotypeId(String proximalHaplotypeId)
    {
        this.proximalHaplotypeId = proximalHaplotypeId;
    }
}
