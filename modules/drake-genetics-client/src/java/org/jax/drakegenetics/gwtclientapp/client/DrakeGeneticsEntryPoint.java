package org.jax.drakegenetics.gwtclientapp.client;

import com.google.gwt.core.client.EntryPoint;
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
        RootPanel.get("helloWorldContainer").add(new Label("Hello World"));
    }
}
