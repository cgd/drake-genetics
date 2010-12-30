package org.jax.drakegenetics.gwtclientapp.client;

import org.jax.drakegenetics.shareddata.client.LibraryNode;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class HelpData {

    private Label failMessage = null;
    private Folder root = null;
    private Window displayWindow;
    
    public HelpData (Window w) {
        this.displayWindow = w;
    }
    
    public Folder getTreeModel(DrakeGeneticsServiceAsync drakeGeneticsService)   
      {  
          drakeGeneticsService.getHelp(new AsyncCallback<LibraryNode> ()
          {
              public void onSuccess(LibraryNode results)
              {
                  getTreeModelSucceeded(results);
              }
              
              public void onFailure(Throwable caught)
              {
                  getTreeModelFailed(caught);
              }
          });
                    
          return this.root;  
      }

    private void getTreeModelSucceeded(LibraryNode results) {

        this.root = (Folder)parseLibraryNode(results);
        TreeStore<ModelData> store = new TreeStore<ModelData>();    
        store.add(this.root.getChildren(), true);   
        final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);    
        tree.setDisplayProperty("name");    
        tree.setWidth(200); 
        tree.setAutoHeight(true);
        HorizontalPanel displayPanel = new HorizontalPanel();
        displayPanel.setLayout(new FitLayout());
        //displayPanel.setTableWidth("100%");
        //displayPanel.setTableHeight("100%");
        //displayPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
        //displayPanel.setSpacing(1);
        
        ContentPanel treePanel = new ContentPanel();
        treePanel.setHeaderVisible(false);
        treePanel.add(tree);
        
        displayPanel.add(treePanel);
        
        ContentPanel cp = new ContentPanel();
        cp.setHeaderVisible(false);
        cp.setAutoHeight(true);
        cp.setUrl("Help/index.html");
        
        displayPanel.add(cp);
        displayWindow.add(displayPanel);
        displayWindow.show();
          
    }

    private void getTreeModelFailed(Throwable caught) {
        caught.printStackTrace();
        this.failMessage = new Label(caught.getMessage());
        this.displayWindow.add(failMessage);
        this.displayWindow.show();
    }
    
    /**
     * Method used to transfer the nodes of a LibrayNode based tree to
     * a BaseTreeModel based tree of Folder and Document objects for use
     * in a GXT display
     * @param node 
     *         A LibraryNode object that was returned from the server
     * @return 
     *         The equivalent BaseTreeModel node (Folder or Document) for display
     *         in a GXT Widget.
     */
    private BaseTreeModel parseLibraryNode(LibraryNode node) {
        BaseTreeModel displayNode = null;
        if (node.getIsDocument()) {
            displayNode = new Document(node.getData());
            //  Add code here to actually fetch the document and add it to
            //  the Document object
        }
        else if (node.isLeaf()) {
            displayNode = new Folder(node.getData());
        }
        else {
            displayNode = new Folder(node.getData());
            for (int i = 0; i < node.getChildCount(); i++) {
                displayNode.add(parseLibraryNode((LibraryNode)node.getChild(i)));
            }
        }
        
        return displayNode;
    }
    
    public Label getFailMessage() {
        return this.failMessage;
    }
}
