package org.jax.drakegenetics.gwtclientapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the main entry point for the GWT query application. This gets
 * loaded up as JavaScript into the query page.
 */
public class DrakeGeneticsEntryPoint implements EntryPoint
{
    /**
     * {@inheritDoc}
     */
    public void onModuleLoad()
    {
        HelloServiceAsync helloService = GWT.create(HelloService.class);
        helloService.sayHelloTo("Keith", new AsyncCallback<String>()
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
    
    /**
     * Set the hello world message to the given value
     * @param message   the message
     */
    private void setHelloWorldMessage(String message)
    {
        RootPanel.get("helloWorldContainer").add(new Label(message));
    }
}
