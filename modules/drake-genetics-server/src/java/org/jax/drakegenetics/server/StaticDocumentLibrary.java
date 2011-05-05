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

package org.jax.drakegenetics.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.jax.drakegenetics.shareddata.client.LibraryNode;

/**
 * A simple library to contain a directory 
 * @author gbeane
 */
public class StaticDocumentLibrary {
    private static final Logger LOG = Logger.getLogger(
            StaticDocumentLibrary.class.getName());

    private LibraryNode root;
    private int numberOfDocuments;

    public StaticDocumentLibrary(String libraryRoot, ServletContext context) {
        
        // create a root node for the index tree and create the tree
        root = new LibraryNode(libraryRoot);

        // scan the library from the root directory and build the tree
        scanLibrary(context);
    }

    /**
     * Get the number of non-directory nodes in the library tree
     * @return number of documents in the library tree
     */
    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    /**
     * get the index of the library - this is a tree representing the directory
     * structure
     * @return the root LibraryNode of the tree representing the file structure
     */
    public LibraryNode getLibraryRoot() {
        return root;
    }

    /**
     * get the URL of a document
     * @param nodes an List containing the path through the child nodes to the document
     * @return URL for the document
     * @throws FileNotFoundException
     */
    public String getDocumentURL(List<String> nodes)
            throws FileNotFoundException {

        if (!root.validatePath(nodes)) {
            FileNotFoundException e = new FileNotFoundException("Invalid Document Path");
            throw e;
        }



        //build the url path
        StringBuilder url = new StringBuilder(root.getData());
        for (int i = 0; i < nodes.size(); i++) {
            url.append(File.separator);
            url.append(nodes.get(i));
            
        }

        return url.toString();
    }



    /**
     * Scan the library starting from the library root directory and build the
     * library index tree
     * @param context   the servlet context that we use to scan web resources
     */
    private void scanLibrary(ServletContext context) {
        numberOfDocuments = 0;
        scanLibraryRecursive(context, root.getData(), root);
    }

    /**
     * recursively scan the library starting from a specified directory and build
     * a tree representing the structure
     * @param context       the servlet context that we use to scan web resources
     * @param resourcePath  web resource path to begin scanning at
     * @param parentNode    LibraryNode representing this directory
     */
    private void scanLibraryRecursive(
            ServletContext context,
            String resourcePath,
            LibraryNode parentNode) {

        @SuppressWarnings("unchecked")
		Set<String> children = context.getResourcePaths(resourcePath);
        
        // apply an ordering to the child nodes
        List<String> orderedChildren = new ArrayList<String>();
        String orderFile = resourcePath + ".order";
        if(children.contains(resourcePath + ".order")) {
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(
                        context.getResourceAsStream(orderFile)));
                String currItem;
                while((currItem = r.readLine()) != null) {
                    currItem = resourcePath + currItem.trim();
                    if(children.contains(currItem)) {
                        orderedChildren.add(currItem);
                    }
                    else if(children.contains(currItem + "/")) {
                        orderedChildren.add(currItem + "/");
                    }
                    else {
                        LOG.warning("failed to find: " + resourcePath + currItem);
                    }
                }
                r.close();
            }
            catch(IOException ex) {
                LOG.warning("failed to read: " + resourcePath + ".order");
            }
        }

        // for each file in this directory
        for(String childResource : orderedChildren) {

            // skip over any files that start with a "."
            if(childResource.startsWith(".")) {
                continue;
            }

            boolean isDir = childResource.endsWith("/");
            if(! isDir) {
                if (!getFileExtension(childResource).equalsIgnoreCase("html")
                        && !getFileExtension(childResource).equalsIgnoreCase("htm")) {
                    continue;
                }
            }

            // create a node to represent this child
            String nodeName = childResource.substring(resourcePath.length());
            if (nodeName.endsWith("/")) {
                nodeName = nodeName.substring(0,nodeName.length() - 1);
            }
            if (nodeName.startsWith("/")) {
                nodeName = nodeName.substring(1);
            }
            //LibraryNode node = new LibraryNode(
            //        childResource.substring(resourcePath.length()));
            LibraryNode node = new LibraryNode(nodeName);

            // if the child is a directory then call scanLibraryDir on it
            if (isDir) {
                scanLibraryRecursive(context, childResource, node);
            } else { // not a directory, must be a document
                node.setDocument(true);
                ++numberOfDocuments;
            }


            // add the child node (and its children) to the parent node
            parentNode.addChild(node);

        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
