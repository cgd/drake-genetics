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

import com.extjs.gxt.ui.client.Style.VerticalAlignment;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * The BreedingForm is the class for managing the widgets related to 
 * display of the breeding pair, the action of calling the breeding interface,
 * and displaying the progeny of the cross.
 * 
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class BreedingForm implements DrakeReceiver {

    private Label failMessage = null;
    private ContentPanel formPanel = new ContentPanel();
    // Once the detail panel is working we have this so we can send it
    // drakes for detail display
    private ContentPanel detailPanel = new ContentPanel();
    // The female drake being bred
    private Drake female;
    // Image for display in the panel which displays the "pair"
    private Image femaleImage;
    // The male drake being bred
    private Drake male;
    // Image for display in the panel which displays the "pair"
    private Image maleImage;
    // The panel where the female image is displayed
    private ContentPanel femalePanel = new ContentPanel();
    // The panel where the male image is displayed
    private ContentPanel malePanel = new ContentPanel();

    public BreedingForm(ContentPanel fp, 
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        this.formPanel = fp;

        formPanel.setHeaderVisible(false);
        formPanel.setBodyStyle("backgroundColor: #ede9e3");
        formPanel.setWidth(544);
        //formPanel.setHeight(350);
        formPanel.setHeight(450);
        
        VerticalPanel vp1 = new VerticalPanel();
        HorizontalPanel top = new HorizontalPanel();
        top.setVerticalAlign(VerticalAlignment.MIDDLE);
        top.setWidth(400);
        top.setHeight(150);
        ContentPanel bottom = new ContentPanel();
        bottom.setHeaderVisible(false);
        vp1.add(top);
        vp1.add(bottom);
        
        final DrakeBreedingInterface breedingInterface = 
            new DrakeBreedingInterface(drakeGeneticsService,  
                bottom);

        ContentPanel breedingPair = new ContentPanel();
        breedingPair.setPosition(10, 10);
        breedingPair.setHeading("Breeding Pair");
        breedingPair.setSize(160, 100);

        femalePanel.setLayout(new BorderLayout());
        femalePanel.setHeaderVisible(false);
        femalePanel.setSize(80, 80);
        malePanel.setLayout(new BorderLayout());
        malePanel.setHeaderVisible(false);
        malePanel.setSize(80, 80);

        HorizontalPanel thePair = new HorizontalPanel();
        thePair.add(femalePanel);
        thePair.add(malePanel);
        breedingPair.add(thePair);
        
        
        SelectionListener<ButtonEvent> BreedingButtonListener = 
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                DiploidGenome fg = female.getDiploidgenome();
                DiploidGenome mg = female.getDiploidgenome();
                
                breedingInterface.breed(fg, mg);
                Info.display("Breeding...", "Breeding "
                        + female.toString() + " X " + male.toString());

            }
        };
        Button breedButton = new Button("Breed", BreedingButtonListener);


        
        top.add(breedingPair);
        top.add(breedButton);
        
        formPanel.add(vp1);
        
    }
    
    public void sendDrake(Drake d) {
        if (d.getGender().equals("F")) {
            this.female = d;
            this.femaleImage = d.getSmallimage();
            this.femalePanel.add(femaleImage);
            this.femalePanel.layout(true);

        } else {
            this.male = d;
            this.maleImage = d.getSmallimage();
            this.malePanel.add(maleImage);
            this.malePanel.layout(true);
        }

    }
    
}
