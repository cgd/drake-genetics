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
import java.util.List;
import java.util.Set;

/**
 * class representing a diploid genome
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class DiploidGenome implements Serializable
{
    private static final long serialVersionUID = -5461239317917973856L;

    private List<Chromosome> maternalHaploid;
    private List<Chromosome> paternalHaploid;
    
    /**
     * Constructor
     */
    public DiploidGenome()
    {
    }
    
    /**
     * Constructor
     * @param maternalHaploid   see {@link #getMaternalHaploid()}
     * @param paternalHaploid   see {@link #getPaternalHaploid()}
     */
    public DiploidGenome(List<Chromosome> maternalHaploid,
            List<Chromosome> paternalHaploid)
    {
        this.maternalHaploid = maternalHaploid;
        this.paternalHaploid = paternalHaploid;
    }

    /**
     * Getter for the maternal haploid (the chromosomes contributed from the
     * mother's egg)
     * @return the maternalHaploid
     */
    public List<Chromosome> getMaternalHaploid()
    {
        return this.maternalHaploid;
    }
    
    /**
     * Setter for the maternale haploid.
     * @param maternalHaploid the maternalHaploid to set
     */
    public void setMaternalHaploid(List<Chromosome> maternalHaploid)
    {
        this.maternalHaploid = maternalHaploid;
    }
    
    /**
     * Getter for the paternal haploid (the chromosomes contributed from the
     * fathers sperm)
     * @return the paternalHaploid
     */
    public List<Chromosome> getPaternalHaploid()
    {
        return this.paternalHaploid;
    }
    
    /**
     * Setter for the paternal haploid
     * @param paternalHaploid the paternalHaploid to set
     */
    public void setPaternalHaploid(List<Chromosome> paternalHaploid)
    {
        this.paternalHaploid = paternalHaploid;
    }
    
    /**
     * Get all chromosomes matching the given names
     * @param chromosomeNamesToMatch
     *          the chromosome names that we will look for
     * @return
     *          any matches (string case is ignored)
     */
    public List<Chromosome> getChromosomeMatches(Set<String> chromosomeNamesToMatch)
    {
        ArrayList<Chromosome> matches = new ArrayList<Chromosome>(
                this.maternalHaploid.size() * 2);
        Chromosome.addChromosomeMatches(
                this.getMaternalHaploid(),
                chromosomeNamesToMatch,
                matches);
        return matches;
    }
    
    /**
     * Determines if there is any aneuploidy in this genome.
     * @return  true if there is any aneuploidy
     */
    public boolean isAneuploid()
    {
        return this.getSpeciesGenomeDescription().isAneuploid(this);
    }
    
    /**
     * Get the species description for this genome
     * @return  the species description
     */
    public abstract SpeciesGenomeDescription getSpeciesGenomeDescription();
}
