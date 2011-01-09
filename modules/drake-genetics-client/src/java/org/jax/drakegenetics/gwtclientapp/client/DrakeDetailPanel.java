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


import java.util.Map;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The DrakeDetailPanel is the class for displaying details about a specific
 * instance of a Drake.
 * 
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class DrakeDetailPanel implements DrakeReceiver {

    private Label failMessage = null;
    private ContentPanel detailPanel = new ContentPanel();
    // The  drake being displayed
    private Drake drake;
    // Image for display 
    private Image drakeImage;
    // The panel where the image is displayed
    private ContentPanel drakeImagePanel = new ContentPanel();
    
    private final TextBox drakeNameLabel = new TextBox();
    private final TextBox drakeGenderLabel = new TextBox();
    private final TextArea genomeTextArea = new TextArea();
    private final TextArea phenomeTextArea = new TextArea();
    //private final Label drakePhenomeLabel = new Label();

    public DrakeDetailPanel(ContentPanel fp, 
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        this.detailPanel = fp;

        detailPanel.setHeaderVisible(false);
        detailPanel.setBodyStyle("backgroundColor: #ede9e3");
        detailPanel.setWidth(544);
        //detailPanel.setHeight(100);
        detailPanel.setHeight(220);
        detailPanel.setBodyStyle("backgroundColor: #ffffff");

        
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setWidth(544);
        //top.setHeight(100);
        hp1.setHeight(220);
        
        drakeImagePanel.setLayout(new BorderLayout());
        drakeImagePanel.setHeaderVisible(false);
        drakeImagePanel.setSize(220, 220);

        hp1.add(drakeImagePanel);
        
        VerticalPanel vp1 = new VerticalPanel();
        
        /*SelectionListener<ButtonEvent> BreedingButtonListener = 
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                DiploidGenome fg = female.getDiploidgenome();
                DiploidGenome mg = female.getDiploidgenome();
                
                breedingInterface.breed(fg, mg);
                Info.display("Breeding...", "Breeding "
                        + female.toString() + " X " + male.toString());

            }
        };*/
        //Button breedButton = new Button("Breed", BreedingButtonListener);
        Button breedButton = new Button("Make Available for Breeding");

        vp1.add(breedButton);
        
        HorizontalPanel namePanel = new HorizontalPanel();
        Label name = new Label("Name: ");
        namePanel.add(name);
        drakeNameLabel.setReadOnly(true);
        drakeNameLabel.setWidth("10");
        namePanel.add(drakeNameLabel);
        //vp1.add(namePanel);
        
        HorizontalPanel genderPanel = new HorizontalPanel();
        Label gender = new Label("Sex: ");
        namePanel.add(gender);
        drakeGenderLabel.setReadOnly(true);
        drakeGenderLabel.setWidth("10");
        namePanel.add(drakeGenderLabel);
        //genderPanel.add(gender);
        //genderPanel.add(drakeGenderLabel);
        //vp1.add(genderPanel);
        vp1.add(namePanel);
 
        Label genome = new Label("Diploid Genome: ");
        vp1.add(genome);
        genomeTextArea.setWidth(315);
        genomeTextArea.setHeight(75);
        genomeTextArea.setReadOnly(true);
        vp1.add(genomeTextArea);

        Label phenome = new Label("Phenotype: ");
        vp1.add(phenome);
        phenomeTextArea.setWidth(315);
        phenomeTextArea.setHeight(50);
        phenomeTextArea.setReadOnly(true);
        vp1.add(phenomeTextArea);
 
        hp1.add(vp1);
        
        detailPanel.add(hp1);
        
    }
    
    public void sendDrake(Drake d) {
        this.drake = d;
        this.drakeImage = d.getLargeimage();
        this.drakeImagePanel.add(drakeImage);
        this.drakeImagePanel.layout(true);
        this.drakeNameLabel.setText(" " + d.getName());
        String sex = " Female";
        if (d.getGender().equals("M"))
            sex = " Male";
        this.drakeGenderLabel.setText(sex);
        this.genomeTextArea.setValue(d.getDiploidgenome().toString());
        Map<String,String> phenome = d.getPhenome();
        if (phenome != null) {
            GWT.log(phenome.toString());
            this.phenomeTextArea.setValue(phenome.toString());
        }
    }
    
}
