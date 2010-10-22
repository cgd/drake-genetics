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


/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ChromosomeDescription
{
    private int numTenCmSegments;
    private int centromereSegmentIndex;
    private String chromosomeName;
    
    /**
     * Constructor
     * @param numTenCmSegments see {@link #getNumTenCmSegments()}
     * @param centromereSegmentIndex see {@link #getCentromereSegmentIndex()}
     * @param chromosomeName see {@link #getChromosomeName()}
     */
    public ChromosomeDescription(
            int numTenCmSegments,
            int centromereSegmentIndex,
            String chromosomeName)
    {
        if(centromereSegmentIndex >= numTenCmSegments)
        {
            throw new IllegalArgumentException(
                    "centromereSegmentIndex must be less than numTenCmSegments");
        }
        
        this.numTenCmSegments = numTenCmSegments;
        this.centromereSegmentIndex = centromereSegmentIndex;
        this.chromosomeName = chromosomeName;
    }

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
     * Determines which 10 cM segment contains the centromere.
     * @return the centromere segment index
     */
    public int getCentromereSegmentIndex()
    {
        return this.centromereSegmentIndex;
    }
    
    /**
     * Getter for the name of this chromosome. Eg: "19" "X" "Y" ...
     * @return the chromosome name
     */
    public String getChromosomeName()
    {
        return this.chromosomeName;
    }
    
    /**
     * Determine if this is a sex chromosome
     * @return  true if this is a sex chromosome
     */
    public boolean isSexChromosome()
    {
        return isSexChromosome(this.chromosomeName);
    }
    
    /**
     * Determine if the given name is an X or Y chromosome
     * @param chromosomeName
     *          the chromosome name
     * @return
     *          true iff it's an "X" or "Y"
     */
    public static boolean isSexChromosome(String chromosomeName)
    {
        return chromosomeName.equals("X") || chromosomeName.equals("Y");
    }
}
