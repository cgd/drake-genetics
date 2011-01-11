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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;



/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MetabolismService
{

    /**
     * @param phenoService
     */
    public MetabolismService()
    {
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
        // for now these are lookups in a static table
        try {
            return readFile(predispForDiabetes, diet);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Look up static metabolite values
     * @param predispForDiabetes string value of the diabetes predisposition "phenotype"
     * @param diet string indicating diet: "high glucose" or "normal"
     * @return
     */
   
    private Map<String, double[]> readFile(String predispForDiabetes, String diet) throws IOException
    {
        //TODO as long as we're reading the metabolite values from static files
        //     maybe we should put this in a file in the metabolism/ directory
        //     rather than hard code it
        String[] metabolites = {"L g6p", "L glycgn", "L pep", "L pyr", "L lac",
        		"L oa_m", "L acet_m", "L citrate", "L aK", "L malate", "L oa_c",
        		"L acet_c", "L malonyl", "L palm", "L palmCoA", "L ket",
        		"L alan", "L cAMP", "L glutamate", "L co2", "B gluc", "B ins", 
        		"B glucgn", "B ffa", "B lac", "B ket", "B alan", "sink",
        		"F_g6p", "F_acyl", "F_tg", "F_ffa", "M_g6p", "M_glyc", "M_pyr", 
        		"M_lac", "M_ket", "M_alan", "glyc PP1", "glyc PP1 GPa", 
        		"glyc PKa", "glyc GPa", "glyc  GSa", "glyc R2C2", "glyc C", 
        		"glyc R2 C cAMP2", "glyc R2 cAMP4"};
        
        Map <String, double[]> metaboliteValues = new HashMap<String, double[]>();
        StringBuilder sb = new StringBuilder("/metabolism/");
        
        // append string for predisposition
        if (predispForDiabetes.equalsIgnoreCase("predisposition for diabetes")) {
        	sb.append("Predisp_");
        }
        else {
        	sb.append("NoPredisp_");
        }
        
        // append diet string
        if (diet.equalsIgnoreCase("high glucose")) {
            sb.append("HighGlucose");
        }
        else {
            sb.append("Normal");
        }
        
        //append filename
        sb.append(".txt");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(sb.toString())));
        String line;
        
        
        int lineNum = 0;
        while ((line = br.readLine()) != null) {
            double[] values;

            // skip header
            if (lineNum++ == 0) {
                continue;
            }

            String[] tokens = line.split("\t");
            values = new double[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                values[i] = new Double(tokens[i]);
            }

            metaboliteValues.put(metabolites[lineNum - 1], values);
        }
        
    	
    	return metaboliteValues;
    }
}
