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
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

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
    
    private final Text name = new Text();
    private final Text gender = new Text();
    private final Text color = new Text();
    private final Text armor = new Text();
    private final Text tail = new Text();
    private final Text eye = new Text();
    private final Text nicked = new Text();
    private final Text breath = new Text();
    private final Text imgnm = new Text();
    
    
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
        vp1.setSpacing(1);
        
        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setSpacing(2);
        Label nameLabel = new Label("Name: ");
        namePanel.add(nameLabel);
        this.name.setWidth(150);
        namePanel.add(name);
        vp1.add(namePanel);
        
        HorizontalPanel genderPanel = new HorizontalPanel();
        genderPanel.setSpacing(2);
        Label genderLabel = new Label("Sex: ");
        genderPanel.add(genderLabel);
        gender.setWidth(75);
        genderPanel.add(gender);
        vp1.add(genderPanel);
        
        HorizontalPanel colorPanel = new HorizontalPanel();
        colorPanel.setSpacing(2);
        Label colorLabel = new Label("Scale Color: ");
        colorPanel.add(colorLabel);
        this.color.setWidth(150);
        colorPanel.add(color);
        vp1.add(colorPanel);
        
        HorizontalPanel armorPanel = new HorizontalPanel();
        armorPanel.setSpacing(2);
        Label armorLabel = new Label("Armor: ");
        armorPanel.add(armorLabel);
        armor.setWidth(150);
        armorPanel.add(armor);
        vp1.add(armorPanel);

        HorizontalPanel tailPanel = new HorizontalPanel();
        tailPanel.setSpacing(2);
        Label tailLabel = new Label("Tail Morphology: ");
        tailPanel.add(tailLabel);
        this.tail.setWidth(150);
        tailPanel.add(tail);
        vp1.add(tailPanel);
        
        HorizontalPanel eyePanel = new HorizontalPanel();
        eyePanel.setSpacing(2);
        Label eyeLabel = new Label("Eye Color: ");
        eyePanel.add(eyeLabel);
        eye.setWidth(150);
        eyePanel.add(eye);
        vp1.add(eyePanel);
        
        HorizontalPanel nickedPanel = new HorizontalPanel();
        nickedPanel.setSpacing(2);
        Label nickedLabel = new Label("Eye Morphology: ");
        nickedPanel.add(nickedLabel);
        this.nicked.setWidth(150);
        nickedPanel.add(nicked);
        vp1.add(nickedPanel);
        
        HorizontalPanel breathPanel = new HorizontalPanel();
        breathPanel.setSpacing(2);
        Label breathLabel = new Label("Breath: ");
        breathPanel.add(armorLabel);
        breath.setWidth(150);
        breathPanel.add(breath);
        vp1.add(breathPanel);
 
        HorizontalPanel imgnmPanel = new HorizontalPanel();
        imgnmPanel.setSpacing(2);
        Label imgnmLabel = new Label("imgnm: ");
        imgnmPanel.add(imgnmLabel);
        imgnm.setWidth(150);
        imgnmPanel.add(imgnm);
        vp1.add(imgnmPanel);
        /*
         * SelectionListener<ButtonEvent> BreedingButtonListener = new
         * SelectionListener<ButtonEvent>() {
         * 
         * @Override public void componentSelected(ButtonEvent ce) {
         * DiploidGenome fg = female.getDiploidgenome(); DiploidGenome mg =
         * female.getDiploidgenome();
         * 
         * breedingInterface.breed(fg, mg); Info.display("Breeding...",
         * "Breeding " + female.toString() + " X " + male.toString());
         * 
         * } };
         */
        // Button breedButton = new Button("Breed", BreedingButtonListener);
        Button breedButton = new Button("Make Available for Breeding");

        vp1.add(breedButton);
        
        hp1.add(vp1);
        
        detailPanel.add(hp1);
        
    }
    
    public void sendDrake(Drake d) {
        this.drake = d;
        this.drakeImage = d.getLargeimage();
        this.imgnm.setText(this.drakeImage.getUrl());
        this.drakeImagePanel.add(drakeImage);
        this.drakeImagePanel.layout(true);
        this.name.setText(d.getName());
        String sex = "Female";
        if (d.getGender().equals("M"))
            sex = "Male";
        this.gender.setText(sex);
        Map<String,String> phenome = d.getPhenome();
        if (phenome != null) {
            if (phenome.containsKey("Lethal")) {
                color.setText("");
                armor.setText("");
                tail.setText("");
                eye.setText("");
                nicked.setText("");
                breath.setText("");
            }
            else {
                color.setText(phenome.get("Scale Color"));
                armor.setText(phenome.get("Armor"));
                tail.setText(phenome.get("Tail Morphology"));
                eye.setText(phenome.get("Eye Color"));
                nicked.setText(phenome.get("Eye Morphology"));
                breath.setText(phenome.get("Breath"));
            }
        }
    }
    
}
