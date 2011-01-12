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
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class DrakeBreedingInterface
{
    private final DrakeGeneticsServiceAsync drakeGeneticsService;
    private final List<ContentPanel> panels;
    private final List<Drake> drakes;
    
    private final Map<String,Integer> coatColors;
    private final Map<String,Integer> eyeColors;
    
    private boolean have_final_images = false;

    /**
     * Constructor
     * @param drakeGeneticsService  the service interface
     * @param panel                 the panel to update
     */
    public DrakeBreedingInterface(DrakeGeneticsServiceAsync drakeGeneticsService, 
            List<ContentPanel> panels, List<Drake> drakes)
    {
        this.drakeGeneticsService = drakeGeneticsService;
        this.panels = panels;
        this.drakes = drakes;
        this.coatColors = new HashMap<String,Integer>();
        this.eyeColors = new HashMap<String, Integer>();
        this.coatColors.put("Frost",new Integer(0));
        this.coatColors.put("Charcoal",new Integer(1));
        this.coatColors.put("Earth",new Integer(2));
        this.coatColors.put("Dust",new Integer(3));
        this.coatColors.put("Sand",new Integer(4));
        this.coatColors.put("Steel",new Integer(5));
        this.coatColors.put("Copper",new Integer(6));
        this.coatColors.put("Argent",new Integer(7));
        this.coatColors.put("Gold",new Integer(8));
        this.coatColors.put("Tawny",new Integer(9));
        
        this.eyeColors.put("white",new Integer(0));
        this.eyeColors.put("gold",new Integer(1));
        this.eyeColors.put("red",new Integer(2));
        
        
    }
    
    
    public void breed(Drake f, Drake m)
    {
        //Image spinnerImage = new Image("/images/drakeSpinner.gif");
        //panel.add(spinnerImage);
        //panel.layout(true);
        final Drake female = f;
        final Drake male = m;
        
        DiploidGenome femaleDG = female.getDiploidgenome();
        DiploidGenome maleDG = male.getDiploidgenome(); 
        
        // Before we breed we need to clear the drakes
        if (this.drakes.size() > 0) {
            this.drakes.removeAll(this.drakes);
            GWT.log("Size of drakes list is now: " + this.drakes.size());
        }
        
        if (f.getPhenome().get("Sterility").equals("true")) {
            // If mom is sterile, there will be no eggs
            for (int i = 0; i < 20; i++) {
                Drake drake = new Drake("No Progeny Found", 
                        new Image("/images/eye/do-not-symbol-small.jpg"), 
                        new Image("/images/eye/do-not-symbol-large.jpg"));
                this.drakes.add(drake);
                panels.get(i).removeAll();
                panels.get(i).add(drake.getSmallimage());
                panels.get(i).layout(true);
            }
        } else if (m.getPhenome().get("Sterility").equals("true")) {
            // If dad is sterile, there will be unfertilized eggs
            for (int i = 0; i < 20; i++) {
                Drake drake = new Drake("Egg Unfertilized", 
                        new Image("/images/eye/egg_small.jpg"), 
                        new Image("/images/eye/egg_large.jpg"));
                this.drakes.add(drake);
                panels.get(i).removeAll();
                panels.get(i).add(drake.getSmallimage());
                panels.get(i).layout(true);
            }
        } else {
            this.drakeGeneticsService.breedPair(
                    femaleDG,
                    maleDG,
                    new AsyncCallback<List<DiploidGenome>>()
                    {
                        public void onSuccess(List<DiploidGenome> offspring)
                        {
                            DrakeBreedingInterface.this.breedingSucceeded(female, 
                                    male, offspring);
                        }
                        
                        public void onFailure(Throwable caught)
                        {
                            DrakeBreedingInterface.this.breedingFailed(caught);
                        }
                    });
        }

    }
    
    private void breedingSucceeded(Drake mother, Drake father, List<DiploidGenome> offspring)
    {
        // which off-spring
        int counter = 0;
        for(DiploidGenome currGenome : offspring)
        {
            final int index = counter;
            String sex = "F";
            if (currGenome.isMale()) {
                sex = "M";
            }
            
            final Drake drake = new Drake("(" + mother.getName() + " x " + father.getName() + ")",
                    sex, currGenome);
            drake.setBreeder(false);
            this.drakes.add(drake);
            this.drakeGeneticsService.getPhenome(currGenome,
                    new AsyncCallback<Map<String, String>>() {
                        public void onSuccess(Map<String, String> phenome) {
                            GWT.log("Progeny Number: "+ index + "  " + phenome.toString());
                            drake.setPhenome(phenome);
                            setImages(drake);
                            panels.get(index).removeAll();
                            panels.get(index).add(drake.getSmallimage());
                            panels.get(index).layout(true);
                        }

                        public void onFailure(Throwable caught) {
                            caught.printStackTrace();
                            GWT.log(caught.getMessage());
                        }
                    });
            
            ++counter;
        }
    }
    
    private void setImages(Drake drake) {
        Map<String,String> phenome = drake.getPhenome();
        if (phenome.containsKey("Lethal")) {
            drake.setSmallimage(new Image("/images/eyes/egg_small.jpg"));
            drake.setLargeimage(new Image("/images/eyes/egg_large.jpg"));
            drake.setDrake(false);
            drake.setBreeder(false);
        } else {
            final Image sm_img = new Image();
            final Image lg_img = new Image();
            
            if (this.have_final_images) {
                
                int color = this.coatColors.get(phenome.get("Scale Color"));
                int eye = this.eyeColors.get(phenome.get("Eye Color"));
                int tail = 0;  // short no barb
                if (! phenome.get("Tail Morphology").equals("short no barb")) {
                    tail = 1;  // normal barbed tail
                }
                int armor = 0;
                if (phenome.get("Armor").equals("one lateral plate")) {
                    armor = 1;
                } else if (phenome.get("Armor").equals("two lateral plates")) {
                    armor = 2;
                } else if (phenome.get("Armor").equals("three lateral plates")) {
                    armor = 3;
                }
                int nicked = 0;  // normal eye
                if (! phenome.get("Eye Morphology").equals("normal eye")) {
                    nicked = 1;  // nicked eye
                }
                String scruffy = "";
                if (phenome.get("Sex").equals("Scruffy male") || 
                        phenome.get("Sex").equals("Scruffy female") ) {
                    scruffy = "S";
                }
                final String lg_img_name = "/images/eyes/LE" + drake.getGender() + color + tail + 
                    armor + eye + nicked + scruffy + ".jpg";
                final String sm_img_name = "/images/eyes/SE" + drake.getGender() + color + tail + 
                    armor + eye + nicked + scruffy + ".jpg";
                this.drakeGeneticsService.isValidDrakeImage(sm_img_name,
                        new AsyncCallback<Boolean>() {
                            public void onSuccess(Boolean valid) {
                                if (valid.booleanValue() == true) {
                                    sm_img.setUrl(sm_img_name);
                                }
                                else {
                                    //sm_img.setUrl("/images/eyes/question_mark_small.jpg");
                                    sm_img.setUrl(sm_img_name);
                                }
                            }

                            public void onFailure(Throwable caught) {
                                caught.printStackTrace();
                                GWT.log(caught.getMessage());
                            }
                        });
                this.drakeGeneticsService.isValidDrakeImage(lg_img_name,
                        new AsyncCallback<Boolean>() {
                            public void onSuccess(Boolean valid) {
                                if (valid.booleanValue() == true) {
                                    lg_img.setUrl(lg_img_name);
                                }
                                else {
                                    //lg_img.setUrl("/images/eyes/question_mark_large.jpg");
                                    lg_img.setUrl(lg_img_name);
                                }
                            }

                            public void onFailure(Throwable caught) {
                                caught.printStackTrace();
                                GWT.log(caught.getMessage());
                            }
                        });

            } else {
                
                int color = this.coatColors.get(phenome.get("Scale Color"));
                int eye = this.eyeColors.get(phenome.get("Eye Color"));
                
                final String lg_img_name = "/images/eyes/LE" + drake.getGender() + color + "bb" + eye + "b"+ ".jpg";
                final String sm_img_name = "/images/eyes/SE" + drake.getGender() + color +  "bb"  + eye + "b" + ".jpg";
                
                this.drakeGeneticsService.isValidDrakeImage(sm_img_name,
                        new AsyncCallback<Boolean>() {
                            public void onSuccess(Boolean valid) {
                                if (valid.booleanValue() == true) {
                                    sm_img.setUrl(sm_img_name);
                                }
                                else {
                                    sm_img.setUrl("/images/eyes/question_mark_small.jpg");
                                    //sm_img.setUrl(sm_img_name);
                                }
                            }

                            public void onFailure(Throwable caught) {
                                caught.printStackTrace();
                                GWT.log(caught.getMessage());
                            }
                        });
                this.drakeGeneticsService.isValidDrakeImage(lg_img_name,
                        new AsyncCallback<Boolean>() {
                            public void onSuccess(Boolean valid) {
                                if (valid.booleanValue() == true) {
                                    lg_img.setUrl(lg_img_name);
                                }
                                else {
                                    lg_img.setUrl("/images/eyes/question_mark_large.jpg");
                                    //lg_img.setUrl(lg_img_name);
                                }
                            }

                            public void onFailure(Throwable caught) {
                                caught.printStackTrace();
                                GWT.log(caught.getMessage());
                            }
                        });
            }

            drake.setSmallimage(sm_img);
            drake.setLargeimage(lg_img);
        }
    }
    
    private void breedingFailed(Throwable caught)
    {
        caught.printStackTrace();
        GWT.log(caught.getMessage());
    }
}
