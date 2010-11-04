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

import java.util.List;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async interface for {@link DrakeGeneticsService}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface DrakeGeneticsServiceAsync
{
    /**
     * For breeding a pair of drakes
     * @param maternalGenome
     *          the mothers genetic info
     * @param paternalGenome
     *          the fathers genetic info
     * @param callback 
     *          callback containing the child genomes
     */
    public void breedPair(
            DiploidGenome maternalGenome,
            DiploidGenome paternalGenome,
            AsyncCallback<List<DiploidGenome>> callback);
}
