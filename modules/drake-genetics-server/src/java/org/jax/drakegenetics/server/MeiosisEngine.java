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

import java.util.List;

import org.jax.drakegenetics.shareddata.client.Chromosome;
import org.jax.drakegenetics.shareddata.client.DiploidGenome;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MeiosisEngine
{
    /**
     * Performs meiosis with the given genome and returns the resulting four
     * gametes.
     * @param genome
     *          the genome to perform meiosis on
     * @return
     *          the resulting gametes. This array will always have a length of
     *          four but since the java designers decided they don't care about
     *          tuples I'm returning an array
     */
    public List<Chromosome>[] performMeiosis(DiploidGenome genome)
    {
        // TODO implement me
        return null;
    }
}
