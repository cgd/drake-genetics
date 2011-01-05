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

import java.util.Map;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MetabolismService
{
    private final PhenoService phenoService;

    /**
     * @param phenoService
     */
    public MetabolismService(PhenoService phenoService)
    {
        this.phenoService = phenoService;
    }
    
    /**
     * Get the metabolic test results 
     * @param genome
     *          the genome object
     * @param diet
     *          the diet consumed by the drake
     * @return
     *          the metabolic test results time course. the key is the
     *          name of the metabolite and the value is the metabolite measures
     *          over time
     */
    public Map<String, double[]> getMetabolicTestResults(
            DiploidGenome genome,
            String diet)
    {
        Map<String, String> phenome = this.phenoService.getPhenome(genome);
        
        return this.getMetabolicTestResults(phenome.get("Diabetes"), diet);
    }
    
    /**
     * Get the metabolic test results 
     * @param predispForDiabetes
     *          the categorical "phenotype" that tells us if we have a predisposition
     *          to diabetes or not.
     *          
     *          TODO really this is an abuse of the word phenotype since it
     *          refers to a predisposition. This should be resolved in some
     *          future iteration 
     * @param diet
     *          the diet consumed by the drake
     * @return
     *          the metabolic test results time course. the key is the
     *          name of the metabolite and the value is the metabolite measures
     *          over time
     */
    public Map<String, double[]> getMetabolicTestResults(
            String predispForDiabetes,
            String diet)
    {
        // TODO please implement me :(
        return null;
    }
}
