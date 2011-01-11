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

import com.extjs.gxt.ui.client.Style.VerticalAlignment;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
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
    // The female drake being bred
    private Drake female;
    // Image for display in the panel which displays the "pair"
    private Image femaleImage = new Image();
    // The male drake being bred
    private Drake male;
    // Image for display in the panel which displays the "pair"
    private Image maleImage = new Image();
    // The panel where the female image is displayed
    private ContentPanel femalePanel = new ContentPanel();
    // The panel where the male image is displayed
    private ContentPanel malePanel = new ContentPanel();
    // panels which hold the progeny image panels for each breeding
    private final List<ContentPanel> progenyPanels = new ArrayList<ContentPanel>();
    // list of progeny from a breeding
    private final List<Drake> progeny = new ArrayList<Drake>();

    public BreedingForm(ContentPanel fp, DrakeDetailPanel dp,
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        this.formPanel = fp;
        final DrakeDetailPanel detailPanel = dp;

        formPanel.setHeaderVisible(false);
        formPanel.setBodyStyle("backgroundColor: #ede9e3");
        formPanel.setWidth(544);
        //formPanel.setHeight(350);
        formPanel.setHeight(450);
        
        VerticalPanel vp1 = new VerticalPanel();
        HorizontalPanel top = new HorizontalPanel();
        top.setVerticalAlign(VerticalAlignment.MIDDLE);
        top.setWidth(400);
        top.setHeight(102);
        ContentPanel bottom = new ContentPanel();
        bottom.setHeaderVisible(false);
        vp1.add(top);
        vp1.add(bottom);
        
        final DrakeBreedingInterface breedingInterface = 
            new DrakeBreedingInterface(drakeGeneticsService, progenyPanels, 
                    progeny);

        ContentPanel breedingPair = new ContentPanel();
        breedingPair.setPosition(10, 10);
        breedingPair.setHeading("Breeding Pair");
        breedingPair.setSize(160, 100);

        femalePanel.setLayout(new BorderLayout());
        femalePanel.setHeaderVisible(false);
        femalePanel.setSize(80, 80);
        femalePanel.sinkEvents(Event.ONCLICK);
        femalePanel.addListener(Events.OnClick,
                new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (female != null) {
                    detailPanel.sendDrake(female);
                    
                }
            }
        });

        malePanel.setLayout(new BorderLayout());
        malePanel.setHeaderVisible(false);
        malePanel.setSize(80, 80);
        malePanel.sinkEvents(Event.ONCLICK);
        malePanel.addListener(Events.OnClick,
                new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (male != null) {
                    detailPanel.sendDrake(male);
                    
                }
            }
        });

        HorizontalPanel thePair = new HorizontalPanel();
        thePair.add(femalePanel);
        thePair.add(malePanel);
        breedingPair.add(thePair);
        

        SelectionListener<ButtonEvent> BreedingButtonListener = 
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //if ( ce.isShiftKey()) {
                    GWT.log("in shift " + ce.getKeyCode());
                //}
                
                breedingInterface.breed(female, male);
                Info.display("Breeding...", "Breeding "
                        + female.toString() + " X " + male.toString());

            }
        };
        Button breedButton = new Button("Breed", BreedingButtonListener);


        
        top.add(breedingPair);
        top.add(breedButton);
        
        ContentPanel progenyPanel = new ContentPanel();
        progenyPanel.setHeading("Progeny");
        progenyPanel.setSize(400, 340);

        //  Creating a panel with 4 rows and 5 columns (change to a grid later
        //  it might turn out better
        VerticalPanel rows = new VerticalPanel();
        for (int i=0; i<4; i++) {
            HorizontalPanel row = new HorizontalPanel();
            for (int j=0; j<5; j++ ) {
                final ContentPanel child = new ContentPanel();
                child.setTitle("" + ((i*5)+j));
                child.setLayout(new BorderLayout());
                child.setHeaderVisible(false);
                child.setSize(80, 80);
                child.sinkEvents(Event.ONCLICK);
                child.addListener(Events.OnClick,
                        new Listener<BaseEvent>() {
                    public void handleEvent(BaseEvent be) {
                        GWT.log("In event for selecting progeny panel " + 
                                be.toString());
                        // The panel name was set to a number above.
                        // We parse the panel title to get the number it will
                        // be in our progenyPanels list
                        int panelNumber = Integer.parseInt(child.getTitle());
                        if (progeny.size() > panelNumber) {
                            Drake drake = progeny.get(panelNumber);
                            detailPanel.sendDrake(drake);
                        }
                        GWT.log(child.getTitle());
                    }
                });
                row.add(child);
                progenyPanels.add(child);
            }
            rows.add(row);
        }
        progenyPanel.add(rows);

        bottom.add(progenyPanel);
        
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
