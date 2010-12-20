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
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.jax.drakegenetics.shareddata.client.LibraryNode;

/**
 * A simple library to contain a directory 
 * @author gbeane
 */
public class StaticDocumentLibrary {

    private LibraryNode root;
    private File libraryRootDir;
    private int numberOfDocuments;


    public StaticDocumentLibrary(File libraryRootDir)
            throws IllegalArgumentException {
        

        if (!libraryRootDir.exists() || !libraryRootDir.isDirectory()) {
            IllegalArgumentException e =
                    new IllegalArgumentException("Invalid library root directory: "
                    + libraryRootDir.toString());
            
            throw e;
        }

        this.libraryRootDir = libraryRootDir;

        // create a root node for the index tree and create the tree
        root = new LibraryNode(libraryRootDir.getName());


        // scan the library from the root directory and build the tree
        scanLibrary();
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
     * @return relative path to document
     * @throws FileNotFoundException
     */
    public String getDocumentURL(List<String> nodes)
            throws FileNotFoundException {
        File file;

        if (!root.validatePath(nodes)) {
            FileNotFoundException e = new FileNotFoundException("Invalid Document Path");
            throw e;
        }



        //build the relative path

        StringBuilder relativePath = new StringBuilder(root.getData());
        for (int i = 0; i < nodes.size(); i++) {
            relativePath.append(File.separator);
            relativePath.append(nodes.get(i));
            
        }
        

        return relativePath.toString();
    }



    /**
     * Scan the library starting from the library root directory and build the
     * library index tree
     */
    private void scanLibrary() {
        numberOfDocuments = 0;
        scanLibraryDir(libraryRootDir, root);
    }

    /**
     * recursively scan the library starting from a specified directory and build
     * a tree representing the structure
     * @param dir           directory to begin scanning at
     * @param parentNode    LibraryNode representing this directory
     */
    private void scanLibraryDir(File dir, LibraryNode parentNode) {

        String[] children = dir.list();

        // for each file in this directory
        for (int i = 0; i < children.length; i++) {

            // skip over any files that start with a "."
            if(children[i].startsWith(".")) {
                continue;
            }

            File f = new File(dir, children[i]);
            if (!f.isDirectory()) {
                if (!getFileExtension(children[i]).equalsIgnoreCase("html")
                        && !getFileExtension(children[i]).equalsIgnoreCase("htm")) {
                    continue;
                }
            }

            // create a node to represent this child
            LibraryNode node = new LibraryNode(children[i]);

            // if the child is a directory then call scanLibraryDir on it
            if (f.isDirectory()) {
                scanLibraryDir(f, node);
            } else { // not a directory, must be a document
                node.setIsDocument(true);
                ++numberOfDocuments;
            }


            // add the child node (and its children) to the parent node
            parentNode.addChild(node);

        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
    }

}
