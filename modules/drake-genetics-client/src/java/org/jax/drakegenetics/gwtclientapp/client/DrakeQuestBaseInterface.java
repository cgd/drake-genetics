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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class DrakeQuestBaseInterface
{
    private static final String BANNER_IMAGE_PATH = "/images/DQ_Banner.png";
    private static final String SPLASH_IMAGE_PATH = "/images/DQ_Splash.png";
    private final DrakeGeneticsServiceAsync drakeGeneticsService;
    private final Panel panel;
    private final VerticalPanel masterPanel = new VerticalPanel();
    private final Window helpWindow = new Window();
    private boolean showSplash;
    
    /**
     * instantiation of a selection listener for the Help Button in the
     * Toolbar.
     */
    private final SelectionListener<ButtonEvent> HelpButtonListener = 
        new SelectionListener<ButtonEvent>() {  
          
        @Override  
        public void componentSelected(ButtonEvent ce) {  
            /*  Keith suggestion for making it all display when content loaded
             * 
             * for each onSuccess you set "this.content1Loaded = true" 
             * for the 1st pane's content and "this.content2Loaded = true" 
             * for the second pane's content. 
             * Then each onSuccess calls "showWindowIfContentLoaded(...)". 
             * This function starts with if(this.content1Loaded && this.content2Loaded){ ...}
             */
            helpWindow.setHeading("Drake Quest User Help");
            helpWindow.setSize(600, 400);
            helpWindow.setMaximizable(true);
            //helpWindow.setToolTip("The Drake Quest Help Page...");
            HelpData hd = new HelpData(helpWindow);
            Folder helpTree = hd.getTreeModel(drakeGeneticsService);
        }};


    /**
     * Constructor
     * @param drakeGeneticsService  the service interface
     * @param panel                 the panel to update
     */
    public DrakeQuestBaseInterface(DrakeGeneticsServiceAsync drakeGeneticsService, 
            Panel panel)
    {
        this.drakeGeneticsService = drakeGeneticsService;
        this.panel = panel;
    }
    
    /**
     * Initialize
     */
    public void init()
    {
        //  Do basic page layout, check login when that is available, etc...
        //  Banner, menu, login, splash screen
        //  When done add to panel, like below
        //  this.panel.add(outer panel);
        this.showSplash = true;
        final VerticalPanel masterPanel = new VerticalPanel();
        final ContentPanel bannerPanel = new ContentPanel();
        final ContentPanel mainPanel = new ContentPanel();
        
        //  Place the logo banner first.       
        bannerPanel.setHeaderVisible(false);
        bannerPanel.setSize(694, 101);  
        bannerPanel.setPosition(0, 0);
        Image banner = new Image();
        banner.setUrl(BANNER_IMAGE_PATH);
        bannerPanel.add(banner);
        
        masterPanel.add(bannerPanel);
        
        // Create and add the tool bar
        ToolBar toolBar = new ToolBar();  
        SelectionListener<ButtonEvent> StubButtonListener = 
            new SelectionListener<ButtonEvent>() {  
              
            @Override  
            public void componentSelected(ButtonEvent ce) {  
              Info.display("Not Yet Implemented", "The " + 
                      ce.getButton().getText() + 
                      " functionality has not yet been implemented");  
        
            }};
            
        Button item1 = new Button("Account", StubButtonListener);            
        toolBar.add(item1);          
      
        toolBar.add(new SeparatorToolItem());  
      
        Button item2 = new Button("Breed Drakes", StubButtonListener);            
         toolBar.add(item2);  
      
        toolBar.add(new SeparatorToolItem());  
      
        Button item3 = new Button("Laboratory", StubButtonListener);            
         toolBar.add(item3);  
      
        toolBar.add(new SeparatorToolItem());  
      
        Button item4 = new Button("Library", StubButtonListener);            
         toolBar.add(item4);  
            
        toolBar.add(new SeparatorToolItem());  
      
        Button item5 = new Button("Help", this.HelpButtonListener);            
         toolBar.add(item5);  
        masterPanel.add(toolBar);

        mainPanel.setHeaderVisible(false);
        mainPanel.setSize(694, 451);  
        mainPanel.setBodyStyle("backgroundColor: #ede9e3");
        if (this.showSplash) {
            mainPanel.add(new Image(SPLASH_IMAGE_PATH));
        }
        
        masterPanel.add(mainPanel);

       /*this.drakeGeneticsService.someMethod(
                param1,
                param2,
                new AsyncCallback<List<return_object>>()
                {
                    public void onSuccess(List<return_object> results)
                    {
                        // do stuff like call blahSucceeded()
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeQuestBaseInterface.this.blahFailed(caught);
                    }
                });*/
        this.panel.add(masterPanel);
    }
    
    public void showSplashScreen(boolean show) {
        this.showSplash = show;
    }
    
    private void blahSucceeded(/* return result here */)
    {
        
        
        //this.panel.add(/* stuff to add to interface for example - new HTML(offspringText.toString())*/);
    }

    private void blahFailed(Throwable caught)
    {
        caught.printStackTrace();
        this.panel.add(new Label(caught.getMessage()));
    }
    
    
}

