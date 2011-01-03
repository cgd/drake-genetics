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
import java.util.List;
import java.util.Map;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;

/**
 *
 * @author gbeane
 */
public class PhenoService {

    private GeneLookup geneLookup;

    public PhenoService(GeneLookup geneLookup)
    {
        this.geneLookup = geneLookup;
    }


    public Map<String, String> getPhenome (DiploidGenome genome)
    {
        Map<String, String> phenome = new HashMap<String, String>();



        return phenome;
    }

    private Map<String, List<String>> getAlleles(DiploidGenome genome)
    {
        Map<String, List<String>> alleles = new HashMap<String, List<String>>();

        return alleles;
    }

}
