/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jax.drakegenetics.shareddata.client;

/**
 *
 * @author gbeane
 */
public class LibraryNode extends TreeNode<String> {
    private boolean isDocument;

    public LibraryNode(String name) {
        super(name);
        isDocument = false;
    }

    public void setIsDocument(boolean isDocument) {
        assert (this.getChildCount() == 0);
        this.isDocument = isDocument;
    }

    public boolean getIsDocument() {
        return isDocument;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(data);

        if (isDocument) {
            sb.append("}");
        }
        else {
            sb.append(",[");

            for (int i = 0; i < children.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(children.get(i).getData());
            }

            sb.append("]}");
        }
        return sb.toString();
    }

}
