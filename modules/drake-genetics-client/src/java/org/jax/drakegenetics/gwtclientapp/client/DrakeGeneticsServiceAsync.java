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
import java.util.Map;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.LibraryNode;

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

    /**
     * For fetching the phenotype of a diploid genome
     * @param genome
     *          the diploid genome for which you want a phenotype
     * @param callback
     *          callback containing the phenotype mapping for this genome.
     */
    public void getPhenome(DiploidGenome genome, 
            AsyncCallback<Map<String, String>> callback);

    /**
     * Get the metabolic test results 
     * @param predispForDiabetes
     *          the value of the diabetes predisposition "phenotype"
     * @param diet
     *          the diet consumed by the drake
     * @param callback
     *          callback containing the metabolic test results time course.
     *          the key is the name of the metabolite and the value is the
     *          metabolite measures over time
     */
    public void getMetabolicTestResults(
            String predispForDiabetes,
            String diet,
            AsyncCallback<Map<String, double[]>> callback);
    
    /**
     * For fetching the tree that represents the Library
     * 
     * @param callback 
     *          callback containing tree structure that represents all the 
     *          publications in the Library
     */
    public void getLibrary(AsyncCallback<LibraryNode> callback);

    /**
     * For fetching the tree that represents the Help document structure
     * 
     * @param callback 
     *          callback containing tree structure that represents all the help 
     *          documents 
     */
    public void getHelp(AsyncCallback<LibraryNode> callback);

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
     * @param callback 
     *          callback containing specific journal article as a string.  
     *          expected that these are html based documents for formatting 
     *          purposes. 
     */
    public void getPublication(String journal, String volume, String article,
            AsyncCallback<String> callback);

    /**
     * For fetching a specific publication from the library. 
     * 
     * @param documentTreePath
     *          A list representing the path in the tree structure to the
     *          specific publication.
     * @param callback 
     *          callback containing specific publication as a URL.  
     *          Expected that these are html based documents for formatting 
     *          purposes. 
     */
    public void getPublication(List<String> documentTreePath,
            AsyncCallback<String> callback);

    /**
     * For fetching a specific document from the user help.  
     * 
     * @param documentTreePath
     *          A list representing the path in the tree structure to the
     *          specific help page.
     * @param callback 
     *          callback containing specific help document as a URL.  
     *          Expected that these are html based documents for formatting 
     *          purposes. 
     */
    public void getHelpDocument(List<String> documentTreePath,
            AsyncCallback<String> callback);

}
