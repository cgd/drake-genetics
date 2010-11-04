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
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Constains metadata describing the structure of a particular species' genome.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SpeciesGenomeDescription implements Serializable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -4935021442131156304L;
    
    private String name;
    private Map<String, ChromosomeDescription> chromosomeDescriptions;
    private int autosomeCount;
    
    /**
     * Constructor
     */
    public SpeciesGenomeDescription()
    {
    }
    
    /**
     * Constructor
     * @param name
     *          the species name
     * @param chromosomeDescriptions
     *          the chromosome descriptions
     */
    public SpeciesGenomeDescription(
            String name,
            Map<String, ChromosomeDescription> chromosomeDescriptions)
    {
        this.name = name;
        this.chromosomeDescriptions = chromosomeDescriptions;
        this.autosomeCount = 0;
        for(String chrId : chromosomeDescriptions.keySet())
        {
            if(!ChromosomeDescription.isSexChromosome(chrId))
            {
                this.autosomeCount++;
            }
        }
    }
    
    /**
     * Getter for the species name
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Setter for the name
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Getter for the chromosome descriptions
     * @return the chromosome descriptions
     */
    public Map<String, ChromosomeDescription> getChromosomeDescriptions()
    {
        return this.chromosomeDescriptions;
    }
    
    /**
     * Setter for the chromosome descriptions
     * @param chromosomeDescriptions the chromosome descriptions to set
     */
    public void setChromosomeDescriptions(Map<String, ChromosomeDescription> chromosomeDescriptions)
    {
        this.chromosomeDescriptions = chromosomeDescriptions;
    }
    
    /**
     * Determine if the given genome is aneuploid
     * @param genome
     *          the genome to test for aneuploidy
     * @return
     *          true iff it's an aneuploid
     */
    public boolean isAneuploid(DiploidGenome genome)
    {
        return
            this.isHaploidAneuploid(genome.getMaternalHaploid()) ||
            this.isHaploidAneuploid(genome.getPaternalHaploid());
    }
    
    /**
     * Determines if there is any aneuploidy in the given haploid chromosome
     * given the autosomes that we expect (we also expect a single sex
     * chromosome)
     * @param haploidChromosomes
     *          the haploid chromosomes
     * @return  true if there is any aneuploidy
     */
    public boolean isHaploidAneuploid(Collection<Chromosome> haploidChromosomes)
    {
        boolean foundSexChromosome = false;
        Set<String> autosomesFound = new HashSet<String>();
        for(Chromosome chromosome : haploidChromosomes)
        {
            String name = chromosome.getChromosomeName();
            if(ChromosomeDescription.isSexChromosome(name))
            {
                if(foundSexChromosome)
                {
                    // two sex chromosomes should not be in a normal haploid
                    return true;
                }
                
                foundSexChromosome = true;
            }
            else
            {
                if(autosomesFound.contains(name))
                {
                    // two copies of the same autosome should not be in a normal haploid
                    return true;
                }
                
                autosomesFound.add(name);
            }
        }
        
        // a normal haploid must contain all autosomes and one of "X" or "Y"
        return autosomesFound.size() != this.autosomeCount || !foundSexChromosome;
    }
}
