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
import java.util.Collections;
import java.util.List;

import org.jax.drakegenetics.shareddata.client.LibraryNode;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class BreedingPanel {

    private Label failMessage = null;
    private Folder root = null;
    private HorizontalPanel breedingPanel = new HorizontalPanel();
    private ContentPanel treePanel = new ContentPanel();
    private TreeStore<ModelData> store = new TreeStore<ModelData>();
    private TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
    private final VerticalPanel workPanel = new VerticalPanel();
    private ContentPanel formPanel = new ContentPanel();
    private ContentPanel detailPanel = new ContentPanel();

    public BreedingPanel(HorizontalPanel lp,
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        this.breedingPanel = lp;
        final DrakeDetailPanel drakeDetail = new DrakeDetailPanel(detailPanel,
                drakeGeneticsService);
        final BreedingForm breedingForm = new BreedingForm(formPanel, 
                drakeDetail, drakeGeneticsService);

        treePanel.setHeaderVisible(true);
        treePanel.setLayout(new FitLayout());
        treePanel.setHeading("Drakes");

        tree.setDisplayProperty("name");
        tree.setWidth(150);
        //tree.setHeight(450);
        tree.setHeight(670);
        tree.addListener(Events.OnClick,
                new Listener<TreePanelEvent<ModelData>>() {

                    public void handleEvent(TreePanelEvent<ModelData> be) {
                        ModelData item = be.getItem();
                        if ("org.jax.drakegenetics.gwtclientapp.client.Drake".equals(item.getClass().getName())) {
                            Drake drake = (Drake)item;
                            GWT.log("Clicked on drake = " + drake.toString());
                            //  Send drake to parent component of form Panel 
                            //  and to detail Panel
                            breedingForm.sendDrake(drake);
                            drakeDetail.sendDrake(drake);
                            
                        }
                    }
                });
         
        treePanel.add(tree);

        breedingPanel.add(treePanel);

        workPanel.add(formPanel);
        
        workPanel.add(detailPanel);
        
        breedingPanel.add(workPanel);
        
        DrakeSetGenerator dg = new DrakeSetGenerator();
        Folder model = dg.getTreeModel(drakeGeneticsService);
        store.add(model.getChildren(), true);

    }

    private void getTreeModelSucceeded(LibraryNode results,
            DrakeGeneticsServiceAsync drakeGeneticsService) {

        this.root = (Folder) parseLibraryNode(results, null,
                drakeGeneticsService);
        
        store.add(this.root.getChildren(), true);
        //tree.show();
    }

    private void getTreeModelFailed(Throwable caught) {
        caught.printStackTrace();
        this.failMessage = new Label(caught.getMessage());
        this.breedingPanel.add(failMessage);
        //this.breedingPanel.show();
    }

    /**
     * Method used to transfer the nodes of a LibrayNode based tree to a
     * BaseTreeModel based tree of Folder and Document objects for use in a GXT
     * display
     * 
     * @param node
     *            A LibraryNode object that was returned from the server
     * @return The equivalent BaseTreeModel node (Folder or Document) for
     *         display in a GXT Widget.
     */
    private BaseTreeModel parseLibraryNode(LibraryNode node, TreeModel parent,
            DrakeGeneticsServiceAsync drakeGeneticsService) {
        BaseTreeModel displayNode = null;
        
        if (node.isDocument()) {

            GWT.log(node.getDisplayName() + "--" + node.getFileName());
            final Document document = new Document(node.getDisplayName(),
                    node.getFileName());
            if (parent != null) {
                document.setParent(parent);
            }

            // Add code here to actually fetch the document and add it to
            // the Document object
            List<String> path = new ArrayList<String>();
            path.add(document.getDocument());
            Folder fParent = (Folder) parent;
            if (! fParent.getName().equals("/Library/")) {
                path.add(fParent.getName());
                Folder curParent = (Folder) parent.getParent();
                while (curParent != null) {
                    if (!curParent.getName().equals("/Library/"))
                        path.add(curParent.getName());
                    curParent = (Folder) curParent.getParent();
                }
            }
            Collections.reverse(path);
            GWT.log(path.toString());
            
            drakeGeneticsService.getPublication(path,
                    new AsyncCallback<String>() {
                        public void onSuccess(String documentUrl) {
                            GWT.log(document.getName());
                            GWT.log(" Node URL = " + documentUrl);
                            document.setUrl(documentUrl);
                            tree.repaint();
                        }

                        public void onFailure(Throwable caught) {
                            getTreeModelFailed(caught);
                        }
                    });
            displayNode = document;

        } else if (node.isLeaf()) {
            displayNode = new Folder(node.getData());
            if (parent != null) {
                displayNode.setParent(parent);
            }

        } else {
            displayNode = new Folder(node.getData());
            if (parent != null) {
                displayNode.setParent(parent);
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                displayNode.add(parseLibraryNode(
                        (LibraryNode) node.getChild(i), displayNode,
                        drakeGeneticsService));
            }
        }

        return displayNode;
    }

    public Label getFailMessage() {
        return this.failMessage;
    }
    
}
