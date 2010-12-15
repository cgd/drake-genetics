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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software. If not, see <http://www.gnu.org/licenses/>.
*/

package org.jax.drakegenetics.shareddata.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * a node of the Tree<T> class
 * @author gbeane
 */
public class TreeNode<T> implements Serializable {

    private T data;
    private List<TreeNode<T>> children;


    public TreeNode() {
        children = new ArrayList<TreeNode<T>>();
    }


    public TreeNode(T data) {
        this();
        this.data = data;
    }


    public T getData() {
        return data;
    }


    public TreeNode<T> getChild(int i) {
        return children.get(i);
    }


    public List<TreeNode<T>> getChildren() {
        return children;
    }


    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }


    public void addChild(TreeNode<T> child) {
        children.add(child);
    }

    public int getNumberOfChildren() {    
        return children.size();
    }


    public TreeNode<T> findChild(T data) {
        for (TreeNode<T> n : children ) {
            if (data.equals(n.data)) {
                return n;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(data.toString()).append(",[");

        for (int i = 0; i < children.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(children.get(i).getData().toString());
        }

        sb.append("]}");

        return sb.toString();
    }


}
