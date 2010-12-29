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

import java.util.Map;
import org.jax.drakegenetics.shareddata.client.Gene;

/**
 *
 * @author gbeane
 */
public class GeneLookup {

    private Map<String, Gene> nameToGene;
    private Map<String, Gene> symbolToGene;

    public GeneLookup()
    {
        init();
    }

    private void init()
    {
        //TODO: going to hard code in some Genes and add them to the Maps here for testing...
    }

    public Gene getGeneByName(String name)
    {
        return nameToGene.get(name);
    }

    public Gene getGeneBySymbol(String symbol)
    {
        return symbolToGene.get(symbol);
    }
}
