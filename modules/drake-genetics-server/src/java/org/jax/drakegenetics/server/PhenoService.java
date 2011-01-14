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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jax.drakegenetics.shareddata.client.Chromosome;
import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.Gene;
import org.jax.drakegenetics.shareddata.client.PhenoConstants;

/**
 *
 * @author gbeane
 */
public class PhenoService {

    private final GeneLookup geneLookup;
    private final GenotypeService genotypeService;
    
    /**
     * Constructor
     * @param geneLookup
     * @param genotypeService
     */
    public PhenoService(GeneLookup geneLookup, GenotypeService genotypeService)
    {
        this.geneLookup = geneLookup;
        this.genotypeService = genotypeService;
    }


    /**
     * Get all phenotypes for this genome.
     * @param genome
     * @return A Map of phenotypes to values. If the genome is inviable 
     * then a single special phenotype of "Lethal" will be returned with value
     * "true"
     */
    public Map<String, String> getPhenome(DiploidGenome genome)
    {
        Map<String, String> phenome = new HashMap<String, String>();

        Map<String, List<String>> alleles = getAlleles(genome);

        try {
            phenome.put(PhenoConstants.CAT_BREATH, getBreathPhenotype(alleles));
            phenome.put(PhenoConstants.CAT_EYE_COLOR, getEyeColor(alleles));
            phenome.put(PhenoConstants.CAT_EYE_MORPH, 
                    getEyeMorphology(alleles));
            phenome.put(PhenoConstants.CAT_SCALE_COLOR, getScaleColor(alleles));
            phenome.put(PhenoConstants.CAT_TAIL_MORPH, 
                    getTailMorphology(alleles));
            phenome.put(PhenoConstants.CAT_ARMOR, getArmor(alleles));
            phenome.put(PhenoConstants.CAT_SEX, getSex(genome, alleles));
            phenome.put(PhenoConstants.CAT_STERILITY, 
                    getSterility(genome, alleles));
            phenome.put(PhenoConstants.CAT_DB_PREDISP, 
                    getDiabetesPredisposition(alleles));
        }
        catch (LethalAlleleCombinationException e) {
            phenome.clear();
            phenome.put(PhenoConstants.CAT_LETHAL, PhenoConstants.LETHAL_TRUE);
        }
        return phenome;
    }

 
    /*   TODO really this is an abuse of the word phenotype since it
     *   refers to a predisposition. This should be resolved in some
     *   future iteration
     */ 
    private static String getDiabetesPredisposition(Map<String, List<String>> alleles)
    {
    	List<String> diabetesAlleles = alleles.get("Dia");
    	
    	if (diabetesAlleles.contains("Db")) {
    		return PhenoConstants.DIABETES_NOPREDISP;
    	}
    	return PhenoConstants.DIABETES_PREDISP;
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
                    String allele = genotypeService.getAllele(
                            c.getHaplotypeAtPosition(gene.getStartPosition()),
                            gene.getSymbol());
                    if (allele != null) {
                        alleles.add(allele);
                    }
                }
            }

            for (Chromosome c : paternalHaploid) {
                if (c.getChromosomeName().equals(gene.getChromosomeName())) {
                    String allele = genotypeService.getAllele(
                            c.getHaplotypeAtPosition(gene.getStartPosition()),
                            gene.getSymbol());
                    if (allele != null) {
                        alleles.add(allele);
                    }
                }
            }

            allAlleles.put(gene.getSymbol(), alleles);

        }

        return allAlleles;
    }

    /**
     * get the breath phenotype
     * @param alleles
     * @return string describing metabolic phenotype
     */
    private static String getBreathPhenotype(Map<String, List<String>> alleles)
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
        if (numMatches(bogBreathAlleles, "bog") == bogBreathAlleles.size()) {
            return PhenoConstants.BREATH_BOG;
        }

        // every other combination is healthy
        return PhenoConstants.BREATH_HEALTHY;
    }

    /**
     * get the eye color determined by this set of alleles
     * @param alleles all alleles for this genome
     * @return String describing eye color phenotype
     */
    private static String getEyeColor(Map<String, List<String>> alleles)
    {
        List<String> flameAlleles = alleles.get("Xdh");

        /*
         * F/F - red eye
         * F/f - gold eye
         * f/f - white eye
         */

        if (numMatches(flameAlleles, "F") == 2) {
            // F/F
            return PhenoConstants.COLOR_RED;
        }
        else if (numMatches(flameAlleles, "f") == 2) {
            // f/f
            return PhenoConstants.COLOR_WHITE;
        }
        // F/f
        return PhenoConstants.COLOR_GOLD;
    }

    /**
     * Get the eye morphology determined by this set of alleles
     * @param alleles all alleles for this genome
     * @return String describing the eye morphology
     * @throws org.jax.drakegenetics.server.PhenoService.LethalAlleleCombinationException
     */
    private String getEyeMorphology(Map<String, List<String>> alleles) throws LethalAlleleCombinationException
    {
        List<String> nickAlleles = alleles.get("Pax6");

        /*
         * N/N - lethal
         * N/n - nicked iris
         * n/n - normal eye
         */

        if(numMatches(nickAlleles, "N") == 2) {
                // N/N
                throw new LethalAlleleCombinationException();
        }
        else if (numMatches(nickAlleles, "n") == 2) {
                // n/n
                return PhenoConstants.EYE_MORPH_NORMAL;
        }
        
        // N/n
        return PhenoConstants.EYE_MORPH_NICK;

    }

    /**
     * get the tail morphology determined by this set of alleles
     * @param alleles all alleles for this genome
     * @return String describing the tail morphology
     */
    private static String getTailMorphology(Map<String, List<String>> alleles)
    {
        List<String> tailAlleles = alleles.get("Dll3");

        /*
         * T/T - long tail with barb
         * T/t - long tail with barb
         * t/t - short tail, no barb
         * 
         * t/Y - short tail, no barb 
         * T/Y - long tail with barb 
         */

        if (numMatches(tailAlleles, "t") == tailAlleles.size()) {
            return PhenoConstants.TAIL_MORPH_SHORT_NO_BARB;
        }

        return PhenoConstants.TAIL_MORPH_LONG_BARB;
    }

    /**
     * Get the armor phenotype determined by this set of alleles
     * @param alleles all alleles for this genome
     * @return String describing the armor phenotype
     */
    private static String getArmor(Map<String, List<String>> alleles) {
        
        List<String> armorAlleles = alleles.get("Eda");

        /*
         * A1/A1 - five lateral plates
         * A1/A2 - three lateral plates
         * A2/A2 - one lateral plate
         */

        if (numMatches(armorAlleles, "A1") == 2) {
            return PhenoConstants.ARMOR_PLATES_5;
        }
        else if (numMatches(armorAlleles, "A2") == 2) {
            return PhenoConstants.ARMOR_PLATES_1;
        }

        return PhenoConstants.ARMOR_PLATES_3;
    }

    /**
     * Get the sterility phenotype
     * @param alleles all alleles for this genome
     * @return String description of the sterility phenotype
     */
    private static String getSterility(DiploidGenome genome, Map<String, List<String>> alleles)
    {

        List<String> transformerAlleles = alleles.get("Ar");

        // all aneuploids are sterile
        if (genome.isAneuploid()) {
        	return PhenoConstants.STERILE_TRUE;
        }
        
        //check Transformer gene 
        
        /*
         * Tr/Tr - normal female
         * Tr/tr - normal female
         * Tr/Y - normal male
         * tr/Y - sex-reversed male (sterile female)
         * tr/tr - not possible
         */

        if (transformerAlleles.contains("Tr")) {
        	return PhenoConstants.STERILE_FALSE;
        }
        
        return PhenoConstants.STERILE_TRUE;
    }

    /**
     * Get the sex of the individual with this genome
     * @param genome
     * @return String description of the sex: (Scruffy) [male, female]
     * @throws org.jax.drakegenetics.server.PhenoService.LethalAlleleCombinationException
     */
    private String getSex(DiploidGenome genome, Map<String, List<String>> alleles) throws LethalAlleleCombinationException
    {
        List<String> transformerAlleles = alleles.get("Ar");
        
        int xCount = 0;
        int yCount = 0;

        List<Chromosome> maternalHaploid = genome.getMaternalHaploid();
        List<Chromosome> paternalHaploid = genome.getPaternalHaploid();

        for (Chromosome c : maternalHaploid) {
            if (c.getChromosomeName().equals("X")) {
                xCount++;
            }
  
        }

        for (Chromosome c : paternalHaploid) {
            if (c.getChromosomeName().equals("X")) {
                xCount++;
            }
            else if (c.getChromosomeName().equals("Y")) {
                yCount++;
            }
        }

        System.out.println("xCount " + xCount);
        System.out.println("yCount " + yCount);
        
        if (xCount == 0 || xCount == 3) {
            // YO or XXX
            throw new LethalAlleleCombinationException();
        }
        else if (xCount == 1 && yCount == 0) {
            // XO
            return PhenoConstants.SEX_F_SCRUFFY;
        }
        else if (xCount == 2 && yCount == 1) {
            // XXY
            return PhenoConstants.SEX_M_SCRUFFY;
        }
        else if (xCount ==1 && yCount == 1) {
            // XY
            if (transformerAlleles.get(0).equals("tr")) {
                // sex reversed male, phenotype will be sterile female
                return PhenoConstants.SEX_F;
            }
            return PhenoConstants.SEX_M;
        }

        // XY
        return PhenoConstants.SEX_F;

    }

    /**
    * get the scale color phenotype
    * @param alleles all alleles for a genome
    * @return String description of scale color
    */
    private String getScaleColor(Map<String, List<String>> alleles) 
        throws LethalAlleleCombinationException {
        
    	List<String> colorlessAlleles = alleles.get("Tyr");
        List<String> metalicAlleles = alleles.get("M");
        List<String> diluteAlleles = alleles.get("Myo5a");
        List<String> brownAlleles = alleles.get("Otc");

        System.out.println(colorlessAlleles.toString());
        System.out.println(metalicAlleles.toString());
        System.out.println(brownAlleles.toString());
        System.out.println(diluteAlleles.toString());
        

        // We've tried to collapse as many of these rules as possible
        // and also take advantage that combinations like B/Y dl/dl or B/b dl/Y are impossible


        // if c/c drake is either Frost or inviable
        if (numMatches(colorlessAlleles, "c") == 2) {           
            // lets check for all inviable combinations
            
            // c/c Mt/* B/* dl/dl || c/c Mt/* B/Y dl/Y
            if (metalicAlleles.contains("Mt") && brownAlleles.contains("B")
                && numMatches(diluteAlleles, "dl") == diluteAlleles.size()) {
                        throw new LethalAlleleCombinationException();
            }

            if (metalicAlleles.contains("M")) { 

                // c/c M/* B/* dl/dl || c/c M/* B/Y dl/Y
                if (brownAlleles.contains("B")
                        && numMatches(diluteAlleles, "dl") == diluteAlleles.size()) {
                    throw new LethalAlleleCombinationException();
                }
                // c/c M/* b/b dl/dl || c/c M/* b/Y dl/Y
                else if (numMatches(brownAlleles, "b") == brownAlleles.size()
                        && numMatches(diluteAlleles, "dl") == diluteAlleles.size()) {

                    throw new LethalAlleleCombinationException();
                }
            }

            // c/c m/m dl/dl || c/c m/m dl/Y (*/* for Brown)
            if (numMatches(metalicAlleles, "m") == 2 
                    && numMatches(diluteAlleles, "dl") == diluteAlleles.size()) {
                throw new LethalAlleleCombinationException();
            }

            // any other c/c is Frost
            return PhenoConstants.COLOR_FROST;
            
        }
        else { // C/*

            // C/* Mt/*
            if (metalicAlleles.contains("Mt")) {
                // check for lethal combinations B/* dl/dl || B/Y dl/Y
                if (brownAlleles.contains("B") 
                        && numMatches(diluteAlleles, "dl") == diluteAlleles.size()) {
                    throw new LethalAlleleCombinationException();
                }
                
                // everything else Mt/* is Tawny
                return PhenoConstants.COLOR_TAWNY;    
            }
            // C/* M/*
            else if (metalicAlleles.contains("M")) {

                // C/* M/* B/*
                if (brownAlleles.contains("B")) {
                    // C/* M/* B/* D/*
                    if (diluteAlleles.contains("D")) {
                        return PhenoConstants.COLOR_STEEL;
                    }
                    // C/* M/* B/* d/d || C/* M/* B/Y d/Y || C/* M/* B/* d/dl
                    else if (numMatches(diluteAlleles, "d") == diluteAlleles.size()
                            || (numMatches(diluteAlleles, "d") == 1
                                && numMatches(diluteAlleles, "dl") == 1)) {
                        return PhenoConstants.COLOR_ARGENT;
                    }
                }

                // C/* M/* b/b || C/* M/* b/Y
                if (numMatches(brownAlleles, "b") == brownAlleles.size()) {
                    // C/* M/* b/b D/* || C/* M/* b/Y D/*
                    if (diluteAlleles.contains("D")) {
                        return PhenoConstants.COLOR_COPPER;
                    }
                    // C/* M/* b/b d/d || C/* M/* b/Y d/Y ||  C/* M/* b/b d/dl
                    else if (numMatches(diluteAlleles, "d") == diluteAlleles.size()
                            || (numMatches(diluteAlleles, "d") == 1
                                && numMatches(diluteAlleles, "dl") == 1)) {
                        return PhenoConstants.COLOR_GOLD;
                    }
                }

                // everything else inviable
                throw new LethalAlleleCombinationException();
            }
            // C/* m/m
            else {
                // C/* m/m B/* || C/* m/m B/Y
                if (brownAlleles.contains("B")) {
                    // C/* m/m B/* D/* || C/* m/m B/Y D/Y
                    if (diluteAlleles.contains("D")) {
                        return PhenoConstants.COLOR_CHARCOAL;
                    }
                    // C/* m/m B/* d/d || C/* m/m B/Y d/Y || C/* m/m B/* d/dl
                    else if (numMatches(diluteAlleles, "d") == diluteAlleles.size()
                            || (numMatches(diluteAlleles, "d") == 1 && numMatches(diluteAlleles, "dl") == 1)) {
                         return PhenoConstants.COLOR_DUST;
                    }
                    // all other C/* m/m B/*
                    throw new LethalAlleleCombinationException();
                }

                //else must be  C/* m/m b/b || C/* m/m b/Y

                // C/* m/m b/b D/* || C/* m/m b/Y D/Y
                if (diluteAlleles.contains("D")) {
                    return PhenoConstants.COLOR_EARTH;
                }
                // C/* m/m b/b d/d || C/* m/m b/Y d/Y || C/* m/m b/b d/dl
                else if(numMatches(diluteAlleles, "d") == diluteAlleles.size()
                            || (numMatches(diluteAlleles, "d") == 1 && numMatches(diluteAlleles, "dl") == 1)) {
                    return PhenoConstants.COLOR_SAND;
                }

                throw new LethalAlleleCombinationException();
            }
        }
    }

    /*
     * private exception used to bail out of phenotyping if we find a lethal
     * combination of alleles
     */
    private class LethalAlleleCombinationException extends Exception {

        /**
        *
        */
        private static final long serialVersionUID = -774596453717935441L;

    }

    private static <E> int numMatches(Collection<E> items, E itemToCheck) {
        int count = 0;
        for(E item : items) {
            if(itemToCheck.equals(item)) {
                count++;
            }
        }
        return count;
    }


}
