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

import java.util.List;

/**
 * Class for representing a drake's genome
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DrakeGenome extends DiploidGenome
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -2733549932765240875L;

    /**
     * Constructor
     */
    public DrakeGenome()
    {
    }

    /**
     * Constructor
     * @param maternalHaploid   see {@link #getMaternalHaploid()}
     * @param paternalHaploid   see {@link #getPaternalHaploid()}
     */
    public DrakeGenome(
            List<Chromosome> maternalHaploid,
            List<Chromosome> paternalHaploid)
    {
        super(maternalHaploid, paternalHaploid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpeciesGenomeDescription getSpeciesGenomeDescription()
    {
        return DrakeSpeciesSingleton.getInstance();
    }
}
