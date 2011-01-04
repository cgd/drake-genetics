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
import org.jax.drakegenetics.shareddata.client.Gene;

/**
 *
 * @author gbeane
 */
public class GeneLookup {

    private Map<String, Gene> nameToGene;
    private Map<String, Gene> symbolToGene;

    /**
     * Constructor
     */
    public GeneLookup()
    {
        nameToGene = new HashMap<String, Gene>();
        symbolToGene = new HashMap<String, Gene>();

        init();
    }

    private void init()
    {
        //hard coded in some Genes for now...
        Gene gene;
        List<String> alleles;

        alleles = new ArrayList<String>();
        alleles.add("B");
        alleles.add("b");
        gene = new Gene("Brown", "Tyrp1", "X", 20.0, 17596, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("Bog");
        alleles.add("bog");
        gene = new Gene("Bog Breath", "Otc", "X", 22.0, 68719, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("D");
        alleles.add("d");
        alleles.add("dl");
        gene = new Gene("Dilute", "Myo5a", "X", 40.0, 152673, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("Tr");
        alleles.add("tr");
        gene = new Gene("Transformer", "Ar", "X", 53.0, 173494, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("T");
        alleles.add("t");
        gene = new Gene("Tail", "Dll3", "X", 60.0, 10584, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("Mt");
        alleles.add("M");
        alleles.add("m");
        gene = new Gene("Metalic", "M", "2", 20.0, 259610, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("F");
        alleles.add("f");
        gene = new Gene("Flame", "Xdh", "2", 50.0, 66274, alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("C");
        alleles.add("c");
        gene = new Gene("Colorless", "Tyr", "3", 50.0, 64572 , alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("N");
        alleles.add("n");
        gene = new Gene("Nick", "Pax6", "3", 70.0, 29514 , alleles);
        addGene(gene);

        alleles = new ArrayList<String>();
        alleles.add("A1");
        alleles.add("A2");
        gene = new Gene("Armor", "Eda", "3", 90.0, 425156 , alleles);
        addGene(gene);
        
    }

    /**
     * add a Gene to the internal directory
     * @param gene
     */
    public void addGene(Gene gene)
    {
        nameToGene.put(gene.getName(), gene);
        symbolToGene.put(gene.getSymbol(), gene);
    }

    /**
     * Set the genes referenced by this GeneLookup
     * @param genes
     */
    public void setGenes(List<Gene> genes)
    {
        nameToGene.clear();
        symbolToGene.clear();
        for (Gene gene : genes) {
            addGene(gene);
        }
    }

    /**
     * Lookup a Gene by the gene name
     * @param name of Gene to lookup
     * @return Gene with this name
     */
    public Gene getGeneByName(String name)
    {
        return nameToGene.get(name);
    }

    /**
     * Lookup a Gene by gene symbol
     * @param symbol the gene symbol to lookup
     * @return Gene with this gene symbol
     */
    public Gene getGeneBySymbol(String symbol)
    {
        return symbolToGene.get(symbol);
    }

    /**
     * Get a list of all Genes
     * @return a List of all Genes
     */
    public List<Gene> getGenes()
    {
        return new ArrayList<Gene>(nameToGene.values());
    }
}
