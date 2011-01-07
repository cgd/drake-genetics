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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract class representing a diploid genome with maternal and paternal DNA
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DiploidGenome implements Serializable
{
    private static final long serialVersionUID = -5461239317917973856L;

    private List<Chromosome> maternalHaploid;
    private List<Chromosome> paternalHaploid;
    private SpeciesGenomeDescription speciesGenomeDescription;
    
    /**
     * Constructor
     */
    public DiploidGenome()
    {
    }
    
    /**
     * Constructor. NOTE: it is expected that the chromosomes given will be
     * pre-sorted using {@link ChromosomeNameComparator}
     * @param maternalHaploid
     *          see {@link #getMaternalHaploid()}
     * @param paternalHaploid
     *          see {@link #getPaternalHaploid()}
     * @param speciesGenomeDescription
     *          see {@link #getSpeciesGenomeDescription()}
     */
    public DiploidGenome(
            List<Chromosome> maternalHaploid,
            List<Chromosome> paternalHaploid,
            SpeciesGenomeDescription speciesGenomeDescription)
    {
        this.maternalHaploid = maternalHaploid;
        this.paternalHaploid = paternalHaploid;
        this.speciesGenomeDescription = speciesGenomeDescription;
    }
    
    /**
     * Creates a "pure" inbred genome from the given haplotype and description
     * @param haplotype
     *          the inbred haplotype
     * @param isFemale
     *          determines if this is a male or female inbred
     * @param speciesGenomeDescription
     *          the genome description
     */
    public DiploidGenome(
            String haplotype,
            boolean isFemale,
            SpeciesGenomeDescription speciesGenomeDescription)
    {
        this(haplotype, haplotype, isFemale, speciesGenomeDescription);
    }

    /**
     * Creates an "F1" genome where the maternal and paternal haplotypes each
     * appear with a single copy.
     * @param maternalHaplotype
     *          the maternal haplotype to use
     * @param paternalHaplotype
     *          the paternal haplotype to use
     * @param isFemale
     *          determines if this is a male or female
     * @param speciesGenomeDescription
     *          the genome description
     */
    public DiploidGenome(
            String maternalHaplotype,
            String paternalHaplotype,
            boolean isFemale,
            SpeciesGenomeDescription speciesGenomeDescription)
    {
        this.maternalHaploid = new ArrayList<Chromosome>();
        this.paternalHaploid = new ArrayList<Chromosome>();
        this.speciesGenomeDescription = speciesGenomeDescription;
        
        Map<String, ChromosomeDescription> chrDescs = speciesGenomeDescription.getChromosomeDescriptions();
        ArrayList<String> sortedChrNames = new ArrayList<String>(chrDescs.keySet());
        Collections.sort(sortedChrNames, new ChromosomeNameComparator());
        for(String chrName : sortedChrNames)
        {
            // create the maternal and paternal chromosomes
            Chromosome currMaternalChr = new Chromosome();
            currMaternalChr.setChromosomeName(chrName);
            ArrayList<CrossoverPoint> maternalCrossovers = new ArrayList<CrossoverPoint>(1);
            maternalCrossovers.add(new CrossoverPoint(maternalHaplotype, 0.0));
            currMaternalChr.setCrossovers(maternalCrossovers);
            
            Chromosome currPaternalChr = new Chromosome();
            currPaternalChr.setChromosomeName(chrName);
            ArrayList<CrossoverPoint> paternalCrossovers = new ArrayList<CrossoverPoint>(1);
            paternalCrossovers.add(new CrossoverPoint(paternalHaplotype, 0.0));
            currPaternalChr.setCrossovers(paternalCrossovers);
            
            // TODO do we need to worry about "M" chromosomes?
            if(chrName.equals("X"))
            {
                this.maternalHaploid.add(currMaternalChr);
                if(isFemale)
                {
                    this.paternalHaploid.add(new Chromosome(currPaternalChr));
                }
            }
            else if(chrName.equals("Y"))
            {
                if(!isFemale)
                {
                    this.paternalHaploid.add(currPaternalChr);
                }
            }
            else
            {
                // it's an autosome
                this.maternalHaploid.add(currMaternalChr);
                this.paternalHaploid.add(currPaternalChr);
            }
        }
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
     * Setter for the maternal haploid. NOTE: it is expected that the
     * chromosomes given will be pre-sorted using
     * {@link ChromosomeNameComparator}
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
     * Setter for the paternal haploid. NOTE: it is expected that the
     * chromosomes given will be pre-sorted using
     * {@link ChromosomeNameComparator}
     * @param paternalHaploid the paternalHaploid to set
     */
    public void setPaternalHaploid(List<Chromosome> paternalHaploid)
    {
        this.paternalHaploid = paternalHaploid;
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
    public SpeciesGenomeDescription getSpeciesGenomeDescription()
    {
        return this.speciesGenomeDescription;
    }
    
    /**
     * Setter for the species description
     * @param speciesGenomeDescription
     *          the species description to set
     */
    public void setSpeciesGenomeDescription(
            SpeciesGenomeDescription speciesGenomeDescription)
    {
        this.speciesGenomeDescription = speciesGenomeDescription;
    }
    
    /**
     * Determine if this is a male
     * @return  true if this is a male
     */
    public boolean isMale()
    {
        // TODO check requirements for what should be done in case of XXY, 0Y, 0X etc
        for(Chromosome chr: this.paternalHaploid)
        {
            if(chr.getChromosomeName().equals("Y"))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Diploid Genome: ");
        sb.append("Sex = ");
        sb.append(this.isMale() ? "male" : "female");
        sb.append(", aneuploid = " + this.isAneuploid() + "\n");
        
        sb.append("  Species: " + this.speciesGenomeDescription.getName() + "\n");
        sb.append("  Maternal Chromosomes: count = " + this.maternalHaploid.size() + "\n");
        appendChromosomeStrings(sb, this.maternalHaploid);
        sb.append("  Paternal Chromosomes: count = " + this.paternalHaploid.size() + "\n");
        appendChromosomeStrings(sb, this.paternalHaploid);
        
        return sb.toString();
    }
    
    private static void appendChromosomeStrings(StringBuilder sb, List<Chromosome> chromosomes)
    {
        for(Chromosome chromosome: chromosomes)
        {
            sb.append("    ");
            sb.append(chromosome);
            sb.append('\n');
        }
    }
}
