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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software. If not, see <http://www.gnu.org/licenses/>.
*/
package org.jax.drakegenetics.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jax.drakegenetics.shareddata.client.Chromosome;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.Gene;

/**
 *
 * @author gbeane
 */
public class PhenoService {

    private GeneLookup geneLookup;
    private GenotypeService genotypeService;


    /**
     *
     * @param geneLookup
     * @param genotypeService
     */
    public PhenoService(GeneLookup geneLookup, GenotypeService genotypeService)
    {
        this.geneLookup = geneLookup;
        this.genotypeService = genotypeService;
    }


    /**
     * 
     * @param genome
     * @return
     */
    public Map<String, String> getPhenome(DiploidGenome genome)
    {
        Map<String, String> phenome = new HashMap<String, String>();

        Map<String, List<String>> alleles = getAlleles(genome);

        try {
            phenome.put("Metabolic", getMetabolicPhenotype(alleles));
            phenome.put("Eye Color", getEyeColor(alleles));
            phenome.put("Eye Morphology", getEyeMorphology(alleles));
            phenome.put("Scale Color", getScaleColor(alleles));
            phenome.put("Tail Morphology", getTailMorphology(alleles));

            //TODO add other phenotypes to phenome
        }
        catch (LethalAlleleCombination e) {
            phenome.clear();
            phenome.put("Lethal", "true");
        }
        return phenome;
    }

    /**
     * get the alleles for this genome
     * @param genome
     * @return a map of gene symbols to list of alleles for that gene
     */
    private Map<String, List<String>> getAlleles(DiploidGenome genome)
    {
        Map<String, List<String>> allAlleles = new HashMap<String, List<String>>();
        List<Chromosome> maternalHaploid = genome.getMaternalHaploid();
        List<Chromosome> paternalHaploid = genome.getPaternalHaploid();

        for (Gene gene : geneLookup.getGenes()) {
            List<String> alleles = new ArrayList<String>();


            for (Chromosome c : maternalHaploid) {
                if (c.getChromosomeName().equals(gene.getChromosomeName())) {
                    alleles.add(genotypeService.getAllele(
                            c.getHaplotypeAtPosition(gene.getStartPosition()),
                            gene.getSymbol()));
                }
            }

            for (Chromosome c : paternalHaploid) {
                if (c.getChromosomeName().equals(gene.getChromosomeName())) {
                    alleles.add(genotypeService.getAllele(
                            c.getHaplotypeAtPosition(gene.getStartPosition()),
                            gene.getSymbol()));
                }
            }

            allAlleles.put(gene.getSymbol(), alleles);

        }

        return allAlleles;
    }

    /**
     * 
     * @param alleles
     * @return
     */
    private String getMetabolicPhenotype(Map<String, List<String>> alleles)
    {

        List<String> bogBreathAlleles = alleles.get("Otc");

        /*
         * Bog/Bog - healthy
         * Bog/bog - healthy
         * bog/bog - bog breath
         * Bog/Y - healthy
         * bog/Y - bog breath
        */

        /* if the first allele in the list is "bog" ... */
        if (bogBreathAlleles.get(0).equals("bog")) {
            /* if there is only one copy of the gene, or the second copy is also
             * "bog" then the phenotype is "bog breath"
             */
            if (bogBreathAlleles.size() == 1
                    || (bogBreathAlleles.size() == 2 && bogBreathAlleles.get(1).equals("bog"))) {
                return "bog breath";
            }
        }

        // every other combination is healty
        return "healthy";
    }

    /**
     *
     * @param alleles
     * @return
     */
    private String getEyeColor(Map<String, List<String>> alleles)
    {
        List<String> flameAlleles = alleles.get("Xdh");

        /*
         * F/F - red eye
         * F/f - gold eye
         * f/f - white eye
         */

        if (flameAlleles.get(0).equals("F") && flameAlleles.get(1).equals("F")) {
            // F/F
            return "red";
        }
        else if (flameAlleles.get(0).equals("f") && flameAlleles.get(1).equals("f")) {
            // f/f
            return "white";
        }
        // F/f
        return "gold";
    }

    private String getEyeMorphology(Map<String, List<String>> alleles) throws LethalAlleleCombination
    {
        List<String> nickAlleles = alleles.get("Pax6");

        /*
         * N/N - lethal
         * N/n - nicked iris
         * n/n - normal eye
         */

        if(nickAlleles.get(0).equals("N") && nickAlleles.get(1).equals("N")) {
                // N/N
                throw new LethalAlleleCombination();
        }
        else if (nickAlleles.get(0).equals("n") && nickAlleles.get(1).equals("n")) {
                // n/n
                return "normal eye";
        }
        
        // N/n
        return "nicked iris";

    }

    /**
     * 
     * @param alleles
     * @return
     */
    private String getTailMorphology(Map<String, List<String>> alleles)
    {
        List<String> tailAlleles = alleles.get("Dll3");

        /*
         * T/T - long tail with barb
         * T/t - long tail with barb
         * t/t - short tail, no barb
         */

        if (tailAlleles.get(0).equals("t") && tailAlleles.get(1).equals("t")) {
            return "short no barb";
        }

        return "long with barb";
    }

    private String getArmor(Map<String, List<String>> alleles) {
        
        List<String> armorAlleles = alleles.get("Eda");

        /*
         * A1/A1 - five lateral plates
         * A1/A2 - three lateral plates
         * A2/A2 - one lateral plate
         */

        if (armorAlleles.get(0).equals("A1") && armorAlleles.get(1).equals("A1")) {
            return "five lateral plates";
        }
        else if (armorAlleles.get(0).equals("A2") && armorAlleles.get(1).equals("A2")) {
            return "one lateral plate";
        }

        return "three lateral plates";
    }

    /**
    *
    * @param alleles
    * @return
    */
    private String getScaleColor(Map<String, List<String>> alleles)
    {
        //TODO
        return "Frost";
    }

    /*
     * private class used to bail out of phenotyping if we find a lethal
     * combination of alleles
     */
    private class LethalAlleleCombination extends Exception {

    }

}
