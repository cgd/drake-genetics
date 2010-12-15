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

import java.util.ArrayList;
import java.util.List;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.Tree;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT service interface for the drake genetics.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
@RemoteServiceRelativePath("drakegeneticsservice")
public interface DrakeGeneticsService extends RemoteService
{
    /**
     * For breeding a pair of drakes
     * @param maternalGenome
     *          the mothers genetic info
     * @param paternalGenome
     *          the fathers genetic info
     * @return
     *          the child genomes
     */
    public List<DiploidGenome> breedPair(
            DiploidGenome maternalGenome,
            DiploidGenome paternalGenome);

    /**
     * For fetching the tree that represents the Library
     * 
     * @return
     *          a tree structure that represents all the publications in
     *          the Library
     */
    public Tree<String> getLibrary();

    /**
     * For fetching the tree that represents the Help document structure
     * 
     * @return
     *          a tree structure that represents all the help documents 
     */
    public Tree<String> getHelp();

    /**
     * For fetching a specific publication from the library.  
     * 
     * @param journal
     *          The name of the Journal - should be the root level for a 
     *          publication in the Library tree structure
     * @param volume
     *          Represents the specific volume of aJournal - should be the 
     *          second level of the Library tree structure
     * @param article
     *          The name of the specific article to be returned - should be the 
     *          third and final level of the Library tree structure
     * @return
     *          a specific journal article as a string.  expected that these
     *          are html based documents for formatting purposes. 
     */
    public String getPublication(String journal, String volume, String article);

    /**
     * For fetching a specific document from the user help.  
     * 
     * @param documentTreePath
     *          A list representing the path in the tree structure to the
     *          specific help page.
     * @return
     *          a specific help document as a string.  Expected that these
     *          are html based documents for formatting purposes. 
     */
    public String getHelpDocument(List<String> documentTreePath);

}
