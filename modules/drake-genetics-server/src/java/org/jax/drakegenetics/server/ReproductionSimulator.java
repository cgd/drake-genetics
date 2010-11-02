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
package org.jax.drakegenetics.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jax.drakegenetics.shareddata.client.Chromosome;
import org.jax.drakegenetics.shareddata.client.ChromosomeDescription;
import org.jax.drakegenetics.shareddata.client.CrossoverPoint;
import org.jax.drakegenetics.shareddata.client.DiploidGenome;

/**
 * Contains algorithms for simulating reproduction.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ReproductionSimulator
{
    private final Random rand;
    private static final double PROB_AUTOSOMAL_NONDISJUNCTION = 0.002;
    private static final double PROB_SEX_NONDISJUNCTION = 0.01;
    
    /**
     * Constructor
     */
    public ReproductionSimulator()
    {
        this(new Random());
    }
    
    /**
     * Constructor
     * @param rand  the random number generator to use
     */
    public ReproductionSimulator(Random rand)
    {
        this.rand = rand;
    }

    /**
     * Performs meiosis with the given genome and returns the resulting four
     * gametes.
     * @param genome
     *          the genome to perform meiosis on
     * @return
     *          the resulting gametes. This array will always have a length of
     *          four but since the java designers decided they don't care about
     *          tuples I'm returning an array
     */
    @SuppressWarnings("unchecked")
    public List<Chromosome>[] performMeiosis(DiploidGenome genome)
    {
        if(genome.isAneuploid())
        {
            // all aneuploids are infertile
            return null;
        }
        
        Map<String, ChromosomeDescription> chrDescMap =
            genome.getSpeciesGenomeDescription().getChromosomeDescriptions();
        List<Chromosome> maternalHaploid = genome.getMaternalHaploid();
        List<Chromosome> paternalHaploid = genome.getPaternalHaploid();
        
        int chrCount = maternalHaploid.size();
        assert paternalHaploid.size() == chrCount;
        
        List<Chromosome>[] gametes = new ArrayList[] {
                new ArrayList<Chromosome>(chrCount),
                new ArrayList<Chromosome>(chrCount),
                new ArrayList<Chromosome>(chrCount),
                new ArrayList<Chromosome>(chrCount)};
        
        // we model crossover as a process which is independent for each
        // chromosome, so we can work on each chromosome separately
        for(int chrIndex = 0; chrIndex < chrCount; chrIndex++)
        {
            // we will generate 4 chromosomes in total for the gametes using
            // 2 copies of maternal DNA and 2 copies of paternal DNA
            ArrayList<Chromosome> maternalCopies = new ArrayList<Chromosome>(2);
            ArrayList<Chromosome> paternalCopies = new ArrayList<Chromosome>(2);
            for(int i = 0; i < 2; i++)
            {
                maternalCopies.add(new Chromosome(maternalHaploid.get(chrIndex)));
                paternalCopies.add(new Chromosome(paternalHaploid.get(chrIndex)));
            }
            
            // we don't do crossover in the case of the Y chromosome
            if(!paternalHaploid.get(chrIndex).getChromosomeName().equals("Y"))
            {
                ChromosomeDescription chrDesc = chrDescMap.get(
                        maternalHaploid.get(chrIndex).getChromosomeName());
                this.maybeCrossover(chrDesc, maternalCopies, paternalCopies);
            }
            
            double probNonDisj = maternalHaploid.get(chrIndex).isSexChromosome() ?
                    PROB_SEX_NONDISJUNCTION :
                    PROB_AUTOSOMAL_NONDISJUNCTION;
            if(this.rand.nextDouble() < probNonDisj)
            {
                // pairing up maternal and paternal ensures that chromosomes
                // will be non-sister at the centromere
                gametes[0].add(maternalCopies.get(0));
                gametes[0].add(paternalCopies.get(0));
                gametes[1].add(maternalCopies.get(1));
                gametes[1].add(paternalCopies.get(1));
            }
            else
            {
                // normal meiosis has occurred
                gametes[0].add(maternalCopies.get(0));
                gametes[1].add(maternalCopies.get(1));
                gametes[2].add(paternalCopies.get(0));
                gametes[3].add(paternalCopies.get(1));
            }
            
            this.shuffle(Arrays.asList(gametes));
        }
        
        return gametes;
    }
    
    /**
     * Shuffle a list
     * @param <E>   the type of elements in the list
     * @param list  the list to shuffle
     */
    private <E> void shuffle(List<E> list)
    {
        int size = list.size();
        for(int i = 0; i < size; i++)
        {
            int swapIndex = this.rand.nextInt(size);
            if(swapIndex != i)
            {
                E tmp = list.get(i);
                list.set(i, list.get(swapIndex));
                list.set(swapIndex, tmp);
            }
        }
    }
    
    /**
     * The per-chromosome crossover algorithm.
     * @param chrDesc
     *          the chromosome description shared by all chromosomes being
     *          crossed (we obviously don't want to cross different chromosome
     *          numbers)
     * @param maternalChrs
     *          the maternal chromosomes. must be the same length as paternal
     *          and either 1 or 2
     * @param paternalChrs
     *          the paternal chromosomes. must be the same length as maternal
     *          and either 1 or 2
     */
    private void maybeCrossover(
            ChromosomeDescription chrDesc,
            List<Chromosome> maternalChrs,
            List<Chromosome> paternalChrs)
    {
        int size = maternalChrs.size();
        assert size == paternalChrs.size() && (size == 1 || size == 2);
        
        for(int i = 0; i < size; i++)
        {
            int[] crossoverSegments = this.getCrossoverSegments(chrDesc);
            for(int segIndex : crossoverSegments)
            {
                Chromosome mChr = maternalChrs.get(0);
                Chromosome pChr = paternalChrs.get(0);
                
                // randomly place the crossover somewhere in the given 10cM segment
                double crossoverLocation = (segIndex + this.rand.nextDouble()) * 10.0;
                
                // split the chromosomes at the crossover point
                List<CrossoverPoint> mCrossovers = mChr.getCrossovers();
                int mCrossoverIndex = ReproductionSimulator.findCrossoverIndex(
                        mCrossovers,
                        crossoverLocation);
                List<CrossoverPoint> mPrefix = new ArrayList<CrossoverPoint>(mCrossovers.subList(
                        0,
                        mCrossoverIndex));
                List<CrossoverPoint> mSuffix = mCrossovers.subList(
                        mCrossoverIndex,
                        mCrossovers.size());
                
                List<CrossoverPoint> pCrossovers = pChr.getCrossovers();
                int pCrossoverIndex = ReproductionSimulator.findCrossoverIndex(
                        pCrossovers,
                        crossoverLocation);
                List<CrossoverPoint> pPrefix = new ArrayList<CrossoverPoint>(pCrossovers.subList(
                        0,
                        pCrossoverIndex));
                List<CrossoverPoint> pSuffix = pCrossovers.subList(
                        pCrossoverIndex,
                        pCrossovers.size());
                
                // splice in the new crossover points
                CrossoverPoint lastMCrossover = mPrefix.get(mPrefix.size() - 1);
                CrossoverPoint lastPCrossover = pPrefix.get(pPrefix.size() - 1);
                mPrefix.add(new CrossoverPoint(
                        lastPCrossover.getDistalHaplotypeId(),
                        crossoverLocation));
                pPrefix.add(new CrossoverPoint(
                        lastMCrossover.getDistalHaplotypeId(),
                        crossoverLocation));
                
                // keep the centromeres intact
                if(crossoverLocation < chrDesc.getCentromerePositionCm())
                {
                    mChr.setCrossovers(pPrefix);
                    mChr.getCrossovers().addAll(mSuffix);
                    
                    pChr.setCrossovers(mPrefix);
                    pChr.getCrossovers().addAll(pSuffix);
                }
                else
                {
                    mChr.setCrossovers(mPrefix);
                    mChr.getCrossovers().addAll(pSuffix);
                    
                    pChr.setCrossovers(pPrefix);
                    pChr.getCrossovers().addAll(mSuffix);
                }
                
                // shuffling keeps crossover pairings independent
                this.shuffle(maternalChrs);
                this.shuffle(paternalChrs);
            }
        }
    }

    /**
     * Determines where the new crossover should be inserted in the given list
     * of crossovers.
     * @param crossovers    the list we're going to insert a new crossover in
     * @param newCrossover  the new crossover location in cM
     * @return              the insertion index
     */
    private static int findCrossoverIndex(
            List<CrossoverPoint> crossovers,
            double newCrossover)
    {
        int size = crossovers.size();
        
        // we start at 1 because the new crossover should never be less than
        // 0 and all chromosomes start at 0
        int i = 1;
        for( ; i < size; i++)
        {
            if(newCrossover < crossovers.get(i).getCentimorganPosition())
            {
                break;
            }
        }
        
        return i;
    }
    
    /**
     * Randomly chooses between 0 and 3 crossover indices
     * @param chrDesc
     *          the description of the chromosome which may undergo crossover
     * @return
     *          the crossovers (may be empty)
     */
    private int[] getCrossoverSegments(ChromosomeDescription chrDesc)
    {
        int segments = chrDesc.getNumTenCmSegments();
        List<Integer> crossoverIndexList = new ArrayList<Integer>(segments);
        for(int i = 0; i < segments; i++)
        {
            // give each segment a 0.1 chance of crossover
            if(this.rand.nextDouble() < 0.1)
            {
                crossoverIndexList.add(i);
            }
        }
        
        // shuffle the crossover indices. This is necessary because crossover
        // order will affect the resulting chromosome
        this.shuffle(crossoverIndexList);
        
        // we want to return a maximum of 3 crossovers
        int[] crossoverIndices = new int[Math.min(3, crossoverIndexList.size())];
        for(int i = 0; i < crossoverIndices.length; i++)
        {
            crossoverIndices[i] = crossoverIndexList.get(i);
        }
        
        return crossoverIndices;
    }
}
