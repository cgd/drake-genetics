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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class LaboratoryPanel {

    private HorizontalPanel laboratoryPanel = new HorizontalPanel();
    private final VerticalPanel workPanel = new VerticalPanel();
    private ContentPanel formPanel = new ContentPanel();
    private ContentPanel detailPanel = new ContentPanel();
    private final LaboratoryForm laboratoryForm; 

    public LaboratoryPanel(HorizontalPanel lp, 
            DrakeGeneticsServiceAsync drakeGeneticsService, 
            TreeStore<ModelData> store) {
        this.laboratoryPanel = lp;
        DrakeTreePanel drakeTreePanel = new DrakeTreePanel(laboratoryPanel, 
                store, drakeGeneticsService);
        final DrakeDetailPanel drakeDetail = new DrakeDetailPanel(detailPanel,
                drakeTreePanel, drakeGeneticsService);
        laboratoryForm = new LaboratoryForm(formPanel, 
                drakeDetail, drakeGeneticsService);

        drakeTreePanel.addReceiver(laboratoryForm);
        drakeTreePanel.addReceiver(drakeDetail);

        workPanel.add(formPanel);
        
        workPanel.add(detailPanel);
        
        laboratoryPanel.add(workPanel);
        
    }

}
