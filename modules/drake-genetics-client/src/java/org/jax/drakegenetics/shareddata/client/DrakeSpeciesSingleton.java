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

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DrakeSpeciesSingleton
{
    private static final SpeciesGenomeDescription drakeSpecies;
    
    static
    {
        Map<String, ChromosomeDescription> chromosomeDescriptions =
            new HashMap<String, ChromosomeDescription>();
        
        // TODO add specifics of drake genome here
        
        drakeSpecies = new SpeciesGenomeDescription(
                "Drake",
                chromosomeDescriptions);
    }
    
    /**
     * Get the singleton description for the drake species
     * @return  the description
     */
    public static SpeciesGenomeDescription getInstance()
    {
        return drakeSpecies;
    }
}
