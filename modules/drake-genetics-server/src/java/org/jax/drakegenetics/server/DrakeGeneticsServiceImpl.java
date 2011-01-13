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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jax.drakegenetics.gwtclientapp.client.DrakeGeneticsService;
import org.jax.drakegenetics.shareddata.client.DiploidGenome;
import org.jax.drakegenetics.shareddata.client.LibraryNode;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the GWT {@link DrakeGeneticsService} interface
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DrakeGeneticsServiceImpl extends RemoteServiceServlet implements DrakeGeneticsService
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -4876385760655645346L;
    //replace the next two constants with properties...for testing only
    private static final String LIBRARY_ROOT = "/Library/";
    private static final String HELP_ROOT = "/Help/";
    

    private ReproductionSimulator reproductionSimulator;
    private StaticDocumentLibrary libraryController;
    private StaticDocumentLibrary helpController;
    private PhenoService phenoService;
    private MetabolismService metoService;
    private Set<String> paths;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        this.reproductionSimulator = new ReproductionSimulator();
        this.libraryController = new StaticDocumentLibrary(
                DrakeGeneticsServiceImpl.LIBRARY_ROOT,
                config.getServletContext());
        this.helpController = new StaticDocumentLibrary(
                DrakeGeneticsServiceImpl.HELP_ROOT,
                config.getServletContext());
        this.phenoService = new PhenoService(
                new GeneLookup(),
                new GenotypeService());
        ServletContext context = config.getServletContext();
        this.paths = (Set<String>)context.getResourcePaths("/images/eyes");

        this.metoService = new MetabolismService();
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String, double[]> getMetabolicTestResults(
            String predispForDiabetes,
            String diet)
    {
        try
        {
            return this.metoService.getMetabolicTestResults(predispForDiabetes, diet);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<DiploidGenome> breedPair(
            DiploidGenome maternalGenome,
            DiploidGenome paternalGenome)
    {
        // TODO right now this is a simple pass through. Implement persistence etc.
        try
        {
            return this.reproductionSimulator.simulateReproduction(
                    maternalGenome,
                    paternalGenome);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    public Map<String, String> getPhenome(
            DiploidGenome genome)
    {
        // TODO right now this is a simple pass through. Implement persistence etc.
        try
        {
            return this.phenoService.getPhenome(genome);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public LibraryNode getLibrary()
    {
        try
        {
            return this.libraryController.getLibraryRoot();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public LibraryNode getHelp()
    {
        try
        {
            return this.helpController.getLibraryRoot();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getPublication(String journal, String volume, String article)
    {
        List<String> nodes = new ArrayList<String>();
        nodes.add(journal);
        nodes.add(volume);
        nodes.add(article);
        try
        {
            return this.libraryController.getDocumentURL(nodes);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getPublication(List<String> documentTreePath)
    {
        try
        {
            return this.libraryController.getDocumentURL(documentTreePath);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * {@inheritDoc}
     */
    public String getHelpDocument(List<String> documentTreePath)
    {
        try
        {
            return this.helpController.getDocumentURL(documentTreePath);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    public Boolean isValidDrakeImage(String url) {
        String webResourceName = url.startsWith("/") ? url : "/" + url;
        boolean valid = false;
        for (Iterator<String> i=paths.iterator(); i.hasNext();) {
            String path = i.next();
            if (webResourceName.equals(path)) {
                valid = true;
                break;
            }
        }
        return new Boolean(valid);

    }

}
