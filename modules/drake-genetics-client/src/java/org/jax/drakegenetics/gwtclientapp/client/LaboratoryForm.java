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
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * The LaboratoryForm is the class for managing the widgets related to 
 * display of the Laboratory experiments, like metabolism experiments (currently
 * metabolism will be our only experiement), the action of calling the 
 * metabolism interface, and displaying the results of experiements.
 * 
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class LaboratoryForm implements DrakeReceiver {

    private Label failMessage = null;
    private ContentPanel formPanel = new ContentPanel();
    // The drake for experimentation
    private Drake specimen;
    // Image for display in the panel which displays the specimen
    private Image specimenImage = new Image();
    // The panel where the specimen image is displayed
    private ContentPanel specimenPanel = new ContentPanel();
    // panels which hold the experiment result image panels
    private final List<ContentPanel> plotPanels = new ArrayList<ContentPanel>();
    // list of results from a experiements
    //private final List<???> results = new ArrayList<???>();

    public LaboratoryForm(ContentPanel fp, DrakeDetailPanel dp,
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        this.formPanel = fp;
        final DrakeDetailPanel detailPanel = dp;

        final TabPanel tabPanel = new TabPanel();
        tabPanel.setSize(538,323);
        tabPanel.setTabScroll(true);
        final DrakeMetabolismInterface metabolismInterface = 
            new DrakeMetabolismInterface(drakeGeneticsService, tabPanel);
        
        formPanel.setHeaderVisible(false);
        formPanel.setBodyStyle("backgroundColor: #ede9e3");
        formPanel.setWidth(544);
        formPanel.setHeight(450);
        
        VerticalPanel vp1 = new VerticalPanel();
        formPanel.add(vp1);
        
        HorizontalPanel top = new HorizontalPanel();
        vp1.add(top);
        top.setWidth(544);
        top.setHeight(150);
        top.setVerticalAlign(VerticalAlignment.TOP);
        
        specimenPanel.setHeading("Subject");
        specimenPanel.setSize(78, 100);

        specimenPanel.setLayout(new BorderLayout());
        specimenPanel.sinkEvents(Event.ONCLICK);
        specimenPanel.addListener(Events.OnClick,
                new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (specimen != null) {
                    detailPanel.sendDrake(specimen);
                    
                }
            }
        });

        top.add(specimenPanel);

        FormData formData; 
        formData = new FormData("-20");  
        VerticalPanel controls = new VerticalPanel();
        controls.setStyleAttribute("backgroundColor", "#ede9e3");
        controls.setSpacing(2);  
        controls.setBorders(false);

        FormPanel simple = new FormPanel();
        simple.setHeaderVisible(false);
        simple.setBorders(false);
        simple.setWidth(455);
        simple.setHeight(145);
        
        List<Diet> list = new ArrayList<Diet>();  
        Diet standard = new Diet("Standard", "Standard");
        list.add(standard);
        Diet glucose = new Diet("High in Sugar","High in Sugar");
        list.add(glucose);
        
        final ListStore<Diet> store = new ListStore<Diet>();  
        store.add(list);  
      
        final ComboBox<Diet> diets = new ComboBox<Diet>();  
        diets.setFieldLabel("Diet: ");  
        diets.setDisplayField("displayname");  
        diets.setName("name");  
        diets.setValueField("name");  
        diets.setForceSelection(true);  
        diets.setStore(store);  
        diets.setTriggerAction(TriggerAction.ALL);
        diets.setEditable(false);
        diets.setValue(standard);
        simple.add(diets, formData);
        
        final CheckBox check1 = new CheckBox();
        check1.setBoxLabel("Blood Glucose");
        check1.setValue(true);  
      
        final CheckBox check2 = new CheckBox();
        check2.setBoxLabel("Blood Ketones");
        check2.setValue(true);  

        final CheckBox check3 = new CheckBox();
        check3.setBoxLabel("Blood Free Fatty Acids");
        check3.setValue(true);  
      
        final CheckBox check4 = new CheckBox();
        check4.setBoxLabel("Liver Glycogen");
        check4.setValue(true);  
      
        final CheckBox check5 = new CheckBox();
        check5.setBoxLabel("Blood Glucagon");
        check5.setValue(true);  
      
        final CheckBox check6 = new CheckBox();
        check6.setBoxLabel("Blood Insulin");
        check6.setValue(true);  
      
        final CheckBox check7 = new CheckBox();
        check7.setBoxLabel("Liver Glucose");
        check7.setValue(true);  
      
        final CheckBox check8 = new CheckBox();
        check8.setBoxLabel("Liver Fatty Acid (palmitate)");
        check8.setValue(true);  
      
        final CheckBoxGroup checkGroup1 = new CheckBoxGroup();  
        checkGroup1.setFieldLabel("Measure");  
        checkGroup1.add(check1);  
        checkGroup1.add(check2);  
        checkGroup1.add(check3);  
        simple.add(checkGroup1, formData); 

        final CheckBoxGroup checkGroup2 = new CheckBoxGroup();  
        checkGroup2.add(check4);  
        checkGroup2.add(check5);  
        checkGroup2.add(check6);  
        simple.add(checkGroup2, formData); 

        final CheckBoxGroup checkGroup3 = new CheckBoxGroup();  
        checkGroup3.add(check7);  
        checkGroup3.add(check8);  
        simple.add(checkGroup3, formData); 

        HorizontalPanel buttons = new HorizontalPanel();
        SelectionListener<ButtonEvent> RunTestButtonListener = 
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (specimen == null) {
                    MessageBox mb = MessageBox.alert("Select Drake", 
                            "Must select a drake before running test!", 
                            new Listener<MessageBoxEvent> () {
                                public void handleEvent(MessageBoxEvent e) {
                                    GWT.log("do nothing");
                                }
                    });
                } else {
                    Diet diet = diets.getValue();
                    if (diet == null) {
                        MessageBox mb = MessageBox.alert("Select Diet", 
                                "Must select a diet before " +
                                "running test!", 
                                new Listener<MessageBoxEvent> () {
                                    public void handleEvent(MessageBoxEvent e) {
                                        GWT.log("do nothing");
                                    }
                        });
                    } else {
                        GWT.log(diet.toString());
                        GWT.log("Selected diet = " + diet.getName());
                        List<String> metabolites = new ArrayList<String>();
                        for (CheckBox c : checkGroup1.getValues()) {
                            metabolites.add(c.getBoxLabel());
                        }
                        for (CheckBox c : checkGroup2.getValues()) {
                            metabolites.add(c.getBoxLabel());
                        }
                        for (CheckBox c : checkGroup3.getValues()) {
                            metabolites.add(c.getBoxLabel());
                        }
                        if (metabolites.size() > 0) {
                            GWT.log(metabolites.toString());
                            metabolismInterface.runTest(specimen, 
                                    diet.getName(),
                                    metabolites);
                            Info.display("Running Tests...",
                                    "Running Metabolism test...");
                        } else {
                            MessageBox mb = MessageBox.alert("Select Metabolite", 
                                    "Must select something to measure before " +
                                    "running test!", 
                                    new Listener<MessageBoxEvent> () {
                                        public void handleEvent(MessageBoxEvent e) {
                                            GWT.log("do nothing");
                                        }
                            });
                            
                        }
                    }

                }
            }
        };
        Button runTestButton = new Button("Run Tests", RunTestButtonListener);
        buttons.add(runTestButton);
        //simple.add(runTestButton);
        
        Button clearTabsButton = new Button("Clear Tabs", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                tabPanel.removeAll();
            }
        });
        buttons.add(clearTabsButton);
        simple.add(buttons);

        controls.add(simple);
        
        top.add(controls);
        
        ContentPanel bottom = new ContentPanel();
        vp1.add(bottom);
        bottom.setBodyStyle("backgroundColor: #ede9e3");
        bottom.setHeaderVisible(false);
        bottom.setWidth(540);
        bottom.setHeight(325);
        
        bottom.add(tabPanel);
        
        
        
    }
    
    public void sendDrake(Drake d) {
        this.specimen = d;
        this.specimenImage = d.getSmallimage();
        this.specimenPanel.removeAll();
        this.specimenPanel.add(specimenImage);
        this.specimenPanel.setTitle(specimen.getName());
        this.specimenPanel.layout(true);
    }
    
}
