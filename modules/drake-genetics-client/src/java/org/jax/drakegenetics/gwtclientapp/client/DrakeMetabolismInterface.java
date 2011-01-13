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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class DrakeMetabolismInterface
{
    private final DrakeGeneticsServiceAsync drakeGeneticsService;
    private final TabPanel panel;
    
    private final Map<String,String> metabolites;
    private final Map<String,String> diets;
    

    /**
     * Constructor
     * @param drakeGeneticsService  the service interface
     * @param panel                 the panel to update
     * @param drake                 the drake which is the subject
     */
    public DrakeMetabolismInterface(DrakeGeneticsServiceAsync drakeGeneticsService, 
            TabPanel panel)
    {
        this.drakeGeneticsService = drakeGeneticsService;
        this.panel = panel;
        this.metabolites = new HashMap<String,String>();
        this.diets = new HashMap<String, String>();
        this.metabolites.put("Blood Glucose","B gluc");
        this.metabolites.put("Blood Ketones","B ket");
        this.metabolites.put("Blood Free Fatty Acids","B ffa");
        this.metabolites.put("Blood Glucagon","B glucgn");
        this.metabolites.put("Blood Insulin","B ins");
        this.metabolites.put("Liver Fatty Acid (palmitate)","L palm");
        this.metabolites.put("Liver Glucose","L gluc");
        this.metabolites.put("Liver Glycogen","L glycgn");
        
        this.diets.put("Standard","normal");
        this.diets.put("High in Sugar","high glucose");
        
        
    }
    
    
    public void runTest(Drake specimen, String diet, List<String> mets)
    {
        //Image spinnerImage = new Image("/images/drakeSpinner.gif");
        //panel.add(spinnerImage);
        //panel.layout(true);
        final Drake subject = specimen;
        final List<String> metabolites = mets;
        
        Map<String, String> phenome = subject.getPhenome();
        
        this.drakeGeneticsService.getMetabolicTestResults(
                phenome.get("Diabetes Predisposition"), diet, 
                new AsyncCallback<Map<String, double[]>>() {
                    public void onSuccess(Map<String, double[]> results) {
                        DrakeMetabolismInterface.this.testSucceeded(subject,
                                results, metabolites);
                    }
                        
                    public void onFailure(Throwable caught) {
                        DrakeMetabolismInterface.this.testFailed(caught);
                    }
                });
    }
        
    
    private void testSucceeded(Drake drake, Map<String, double[]> results,
            List<String> metabolites)
    {
        // Code up call to Keith's code to get plots, and add them
        // to tab panel
        GWT.log("in testSucceeded for Drake " + drake.toString());
        GWT.log(results.toString());
        GWT.log(metabolites.toString());
    }
    
    
    private void testFailed(Throwable caught)
    {
        caught.printStackTrace();
        GWT.log(caught.getMessage());
    }
}
