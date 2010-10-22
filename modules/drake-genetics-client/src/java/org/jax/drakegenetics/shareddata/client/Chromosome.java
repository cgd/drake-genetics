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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Chromosome implements Serializable
{
    private static final long serialVersionUID = 6237372279849745397L;
    
    private String chromosomeName;
    private List<CrossoverPoint> crossovers;
    private String proximalHaplotypeId;
    
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
    
    /**
     * Adds all of the chromosomes which match one of the given
     * names to the matches collection
     * @param chromosomesToCheck
     *          the chromosomes to scan
     * @param chromosomeNamesToMatch
     *          the chromosomes to try to match
     * @param matches
     *          the collection that we will add any matches to
     */
    public static void addChromosomeMatches(
            Collection<Chromosome> chromosomesToCheck,
            Set<String> chromosomeNamesToMatch,
            Collection<Chromosome> matches)
    {
        for(Chromosome chromosome : chromosomesToCheck)
        {
            for(String name : chromosomeNamesToMatch)
            {
                if(name.equals(chromosome.getChromosomeName()))
                {
                    matches.add(chromosome);
                    break;
                }
            }
        }
    }
    
    /**
     * Hash all of the given chromosomes by their chromosome name
     * @param chromosomes
     *          the chromosomes to hash
     * @param chromosomeHash
     *          the hash map to add chromosomes to
     */
    public static void hashChromosomes(
            Collection<Chromosome> chromosomes,
            Map<String, List<Chromosome>> chromosomeHash)
    {
        for(Chromosome chromosome: chromosomes)
        {
            List<Chromosome> chrList = chromosomeHash.get(chromosome.getChromosomeName());
            if(chrList == null)
            {
                chrList = new ArrayList<Chromosome>();
                chromosomeHash.put(chromosome.getChromosomeName(), chrList);
            }
            
            chrList.add(chromosome);
        }
    }
}
