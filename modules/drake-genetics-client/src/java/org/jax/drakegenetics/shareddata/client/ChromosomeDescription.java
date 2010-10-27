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
 * Class used to describe the structure of a chromosome for a particular
 * species/chromosome number. See {@link Chromosome} for working with
 * genetic composition data.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ChromosomeDescription
{
    private int numTenCmSegments;
    private int centromerePositionCm;
    private String chromosomeName;
    
    /**
     * Constructor
     * @param numTenCmSegments see {@link #getNumTenCmSegments()}
     * @param centromerePositionCm see {@link #getCentromerePositionCm()}
     * @param chromosomeName see {@link #getChromosomeName()}
     */
    public ChromosomeDescription(
            int numTenCmSegments,
            int centromerePositionCm,
            String chromosomeName)
    {
        if(centromerePositionCm > numTenCmSegments  * 10.0)
        {
            throw new IllegalArgumentException(
                    "the given centromere position extends beyond the end " +
                    "of the chromosome");
        }
        
        this.numTenCmSegments = numTenCmSegments;
        this.centromerePositionCm = centromerePositionCm;
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
     * Determines the centromere's position in centimorgans
     * @return the position
     */
    public int getCentromerePositionCm()
    {
        return this.centromerePositionCm;
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
