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

import java.io.Serializable;
import java.util.Comparator;

/**
 * A comparator for ordering chromosomes correctly according to their names.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ChromosomeNameComparator implements Comparator<String>, Serializable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -6904444115637206157L;

    /**
     * {@inheritDoc}
     */
    public int compare(String chrName1, String chrName2)
    {
        return chrToInt(chrName1) - chrToInt(chrName2);
    }
    
    private static int chrToInt(String chr)
    {
        // numbers come first followed by X, Y and M
        try
        {
            return Integer.parseInt(chr);
        }
        catch(NumberFormatException ex)
        {
            if(chr.equals("X"))
            {
                return Integer.MAX_VALUE - 2;
            }
            else if(chr.equals("Y"))
            {
                return Integer.MAX_VALUE - 1;
            }
            else if(chr.equals("M"))
            {
                return Integer.MAX_VALUE;
            }
            else
            {
                throw new IllegalArgumentException(
                        "\"" + chr + "\" is not a valid chromosome name");
            }
        }
    }
}
