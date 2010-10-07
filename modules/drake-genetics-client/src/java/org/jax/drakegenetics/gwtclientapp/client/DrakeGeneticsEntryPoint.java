package org.jax.drakegenetics.gwtclientapp.client;

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
    	
    	authenticationService.login("Keith", new AsyncCallback<String>()
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
