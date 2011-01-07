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
import org.jax.drakegenetics.shareddata.client.DrakeSpeciesSingleton;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DrakeBreedingInterface
{
    private final DrakeGeneticsServiceAsync drakeGeneticsService;
    private final ContentPanel panel;

    /**
     * Constructor
     * @param drakeGeneticsService  the service interface
     * @param panel                 the panel to update
     */
    public DrakeBreedingInterface(DrakeGeneticsServiceAsync drakeGeneticsService, 
            ContentPanel panel)
    {
        this.drakeGeneticsService = drakeGeneticsService;
        this.panel = panel;
    }
    
    /**
     * Initialize
     */
    public void init()
    {
        DiploidGenome female = new DiploidGenome(
                "P1_M", "P1_P", true, DrakeSpeciesSingleton.getInstance());
        DiploidGenome male = new DiploidGenome(
                "P2_M", "P2_P", false, DrakeSpeciesSingleton.getInstance());
        this.drakeGeneticsService.breedPair(
                female,
                male,
                new AsyncCallback<List<DiploidGenome>>()
                {
                    public void onSuccess(List<DiploidGenome> offspring)
                    {
                        DrakeBreedingInterface.this.f1BreedingSucceeded(offspring);
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeBreedingInterface.this.breedingFailed(caught);
                    }
                });
    }
    
    public void breed(DiploidGenome female, DiploidGenome male)
    {
        //Image spinnerImage = new Image("/images/drakeSpinner.gif");
        //panel.add(spinnerImage);
        //panel.layout(true);

        this.drakeGeneticsService.breedPair(
                female,
                male,
                new AsyncCallback<List<DiploidGenome>>()
                {
                    public void onSuccess(List<DiploidGenome> offspring)
                    {
                        DrakeBreedingInterface.this.f1BreedingSucceeded(offspring);
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeBreedingInterface.this.breedingFailed(caught);
                    }
                });
    }
    
    private void f1BreedingSucceeded(List<DiploidGenome> offspring)
    {
        DiploidGenome nextFemale = null;
        DiploidGenome nextMale = null;
        
        StringBuilder offspringText = new StringBuilder("<pre>");
        offspringText.append("F1s\n");
        offspringText.append("=========\n");
        offspringText.append(offspring.size());
        offspringText.append(" offspring\n");
        for(DiploidGenome currGenome : offspring)
        {
            offspringText.append(currGenome);
            offspringText.append("\n\n");
            if(!currGenome.isAneuploid())
            {
                if(currGenome.isMale())
                {
                    nextMale = currGenome;
                }
                else
                {
                    nextFemale = currGenome;
                }
            }
        }
        
        if(nextFemale != null && nextMale != null)
        {
            offspringText.append("MALE AND FEMALE PAIR FOR F2\n");
            offspringText.append("=========\n");
            offspringText.append(nextFemale);
            offspringText.append(nextMale);
            
            this.drakeGeneticsService.breedPair(
                    nextFemale,
                    nextMale,
                    new AsyncCallback<List<DiploidGenome>>()
                    {
                        /**
                         * {@inheritDoc}
                         */
                        public void onSuccess(List<DiploidGenome> result)
                        {
                            DrakeBreedingInterface.this.f2BreedingSucceeded(result);
                        }
                        
                        /**
                         * {@inheritDoc}
                         */
                        public void onFailure(Throwable caught)
                        {
                            DrakeBreedingInterface.this.breedingFailed(caught);
                        }
                    });
        }
        else
        {
            offspringText.append("NO VALID PAIRING FOR F2\n");
            offspringText.append("=========\n");
        }
        
        offspringText.append("</pre>");
        
        this.panel.removeAll();
        this.panel.add(new HTML(offspringText.toString()));
        GWT.log(offspringText.toString());
        this.panel.layout(true);
    }

    private void f2BreedingSucceeded(List<DiploidGenome> offspring)
    {
        StringBuilder offspringText = new StringBuilder("<pre>");
        offspringText.append("F2s\n");
        offspringText.append("=========\n");
        offspringText.append(offspring.size());
        offspringText.append(" offspring\n");
        for(DiploidGenome currGenome : offspring)
        {
            offspringText.append(currGenome);
            offspringText.append("\n\n");
        }
        
        offspringText.append("</pre>");
        
        this.panel.add(new HTML(offspringText.toString()));
        GWT.log(offspringText.toString());
        this.panel.layout(true);
    }

    private void breedingFailed(Throwable caught)
    {
        caught.printStackTrace();
        this.panel.add(new Label(caught.getMessage()));
        this.panel.layout(true);
    }
}
