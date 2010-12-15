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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * This is the main entry point for the GWT query application. This gets
 * loaded up as JavaScript into the query page.
 */
public class DrakeGeneticsEntryPoint implements EntryPoint
{
    private DrakeGeneticsServiceAsync drakeGeneticsService;
    
    /**
     * {@inheritDoc}
     */
    public void onModuleLoad()
    {

        this.drakeGeneticsService = GWT.create(DrakeGeneticsService.class);
        DrakeQuestBaseInterface drakeQuestBaseInterface = 
        	new DrakeQuestBaseInterface(this.drakeGeneticsService, 
        			RootPanel.get("drakeQuestContainer"));
        drakeQuestBaseInterface.init();
        
        DrakeBreedingInterface drakeBreedingInterface = new DrakeBreedingInterface(
                this.drakeGeneticsService,
                RootPanel.get("drakeBreedingContainer"));
        drakeBreedingInterface.init();
    }
    
}
