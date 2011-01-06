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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gbeane
 */
public class GenotypeService {

    // map haplotype -> Gene Symbol -> Allele
    private Map<String, Map<String, String>> haplotypeToAlleles;

    public GenotypeService()
    {
        haplotypeToAlleles = new HashMap<String, Map<String, String>>();
    }

    public String getAllele(String haplotype, String geneSymbol) {
        return haplotypeToAlleles.get(haplotype).get(geneSymbol);
    }

}
