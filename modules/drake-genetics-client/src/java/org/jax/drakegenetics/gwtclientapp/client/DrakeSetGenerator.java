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

package org.jax.drakegenetics.gwtclientapp.client;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.DrakeSpeciesSingleton;

import com.google.gwt.user.client.ui.Image;

/**  This is a class solely for generating a demo set of drakes
 * for breeding and metabolism experiments.
 * 
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class DrakeSetGenerator  {


    public static Folder getTreeModel() {
        Image f_small_example = new Image("/images/eyes/SEF11520.jpg");
        Image f_large_example = new Image("/images/eyes/LEF11520.jpg");
        Image m_small_example = new Image("/images/eyes/SEM00500.jpg");
        Image m_large_example = new Image("/images/eyes/LEM00500.jpg");
        Folder[] folders = new Folder[] {
                new Folder("Females", new Drake[] { new Drake("P1", "F",
                        new DiploidGenome("P1_M", "P1_P", true,
                                DrakeSpeciesSingleton.getInstance()),
                        f_small_example, f_large_example), }),
                new Folder("Males", new Drake[] { new Drake("P2", "M",
                        new DiploidGenome("P2_M", "P2_P", false,
                                DrakeSpeciesSingleton.getInstance()),
                        m_small_example, m_large_example), }) };

        Folder root = new Folder("root");
        for (int i = 0; i < folders.length; i++) {
            root.add((Folder) folders[i]);
        }

        return root;
    }

}
