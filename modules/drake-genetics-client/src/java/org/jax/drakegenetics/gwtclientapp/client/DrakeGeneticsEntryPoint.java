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

import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the main entry point for the GWT query application. This gets
 * loaded up as JavaScript into the query page.
 */
public class DrakeGeneticsEntryPoint implements EntryPoint
{
    private final AuthenticationServiceAsync authenticationService = 
    	GWT.create(AuthenticationService.class);
    private final HelloServiceAsync helloService = 
    	GWT.create(HelloService.class);
    private final String username = "No User";

    /**
     * {@inheritDoc}
     */
    public void onModuleLoad()
    {
        // NOTE: this is a complete hack which is needed to avoid
        //       "Invalid memory access of location 0x8 eip=0x4a8aeb" error
        //       that occurs in dev mode otherwise. For more info on this hack
        //       please see: http://www.extjs.com/forum/showthread.php?t=87668
        @SuppressWarnings("unused") Layout junk = new AnchorLayout();
        
    	String sessionID = Cookies.getCookie("sid");
        if ( sessionID != null ) validateSessionId(sessionID);
        else displayLoginBox();
        
    }
    
    /**
     * Set the hello world message to the given value
     * @param message   the message
     */
    private void setHelloWorldMessage(String message)
    {
        RootPanel.get("helloWorldContainer").add(new Label(message));
    }
    
    private void displayLoginBox() {
    	
    	MessageBox login = MessageBox.prompt("Login", "Do you want to log in?");
    	
    	/*authenticationService.login("Keith", new AsyncCallback<String>()
                {
                    
                    public void onSuccess(String result)
                    {
                        DrakeGeneticsEntryPoint.this.setHelloWorldMessage(result);
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeGeneticsEntryPoint.this.setHelloWorldMessage(caught.toString());
                    }
                });*/
    }
    
    private void validateSessionId(String sessionID) {
    	
    	authenticationService.validateSessionId(sessionID, new AsyncCallback<String>()
                {
                    
                    public void onSuccess(String result)
                    {
                    	if (result.equals("INVALID")) {
                    		displayLoginBox();
                    	} else {
                    		
                            DrakeGeneticsEntryPoint.this.
                            	setHelloWorldMessage(result);
                    	}
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeGeneticsEntryPoint.this.setHelloWorldMessage(caught.toString());
                    }
                });
    }
    
    private void initLayout() {
    	
    	helloService.sayHelloTo(username, new AsyncCallback<String>()
                {
                    
                    public void onSuccess(String result)
                    {
                        DrakeGeneticsEntryPoint.this.setHelloWorldMessage(result);
                    }
                    
                    public void onFailure(Throwable caught)
                    {
                        DrakeGeneticsEntryPoint.this.setHelloWorldMessage(caught.toString());
                    }
                });
    }
}
