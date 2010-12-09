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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple tree of Objects of type T
 * @author gbeane
 */
public class Tree<T> {

    private TreeNode<T> root;

    public Tree() {

    }

    public Tree(TreeNode<T> root) {
        this.root = root;
    }

    /**
     *
     * @return root node of the tree
     */
    public TreeNode<T> getRoot() {
        return this.root;
    }


    public void setRoot(TreeNode<T> node) {
        this.root = node;
    }

    /**
     * turn the Tree<T> into a List by doing a preorder traversal
     * @return a list of all nodes in the tree
     */
    public List<TreeNode<T>> toList() {
        List<TreeNode<T>> list = new ArrayList<TreeNode<T>>();
        traversePreOrder(root, list);
        return list;

    }

    /**
     * check to see if a list of child nodes is a valid path starting from the
     * root of the tree
     * @param nodes
     * @return
     */
    public boolean validateTreePath(List<T> nodes) {

        TreeNode<T> node = root;

        for (T t : nodes) {
            // look to see if this String is one of the child nodes of our current node
            node = node.findChild(t);
            if (node == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    /**
     * perform a preorder traversal from @param node and add nodes to @param list
     * @param node
     * @param list
     */
    private void traversePreOrder(TreeNode<T> node, List<TreeNode<T>> list) {
        list.add(node);

        for (int i = 0; i < node.getNumberOfChildren(); i++) {
            traversePreOrder(node.getChild(i), list);
        }

    }


}
