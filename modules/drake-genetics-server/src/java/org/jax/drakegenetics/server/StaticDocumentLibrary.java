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

    private static final String DEFAULT_ROOT_NAME = "LibraryRoot";

    public StaticDocumentLibrary(File libraryRootDir, String rootName)
            throws IllegalArgumentException {
        

        if (!libraryRootDir.exists() || !libraryRootDir.isDirectory()) {
            IllegalArgumentException e =
                    new IllegalArgumentException("Invalid library root directory: "
                    + libraryRootDir.toString());
            
            throw e;
        }

        this.libraryRootDir = libraryRootDir;

        // create a root node for the index tree and create the tree
        root = new LibraryNode(rootName);


        // scan the library from the root directory and build the tree
        scanLibrary();
    }


    public StaticDocumentLibrary(File libraryRootDir) {
        this(libraryRootDir, DEFAULT_ROOT_NAME);
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
     * get the contents of a document
     * @param nodes an List containing the path through the child nodes to the document
     * @return document contents as a String
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String retrieveDocument(List<String> nodes)
            throws FileNotFoundException, IOException {
        File file;

        if (!root.validatePath(nodes)) {
            FileNotFoundException e = new FileNotFoundException("Invalid Document Path");
            throw e;
        }



        //build the relative path, the first node is the library root, we can skip that
        StringBuilder relativePath = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            relativePath.append(nodes.get(i));
            if (i < nodes.size() - 1) {
                relativePath.append(File.separator);
            }
        }
        file = new File(libraryRootDir, relativePath.toString());

        if (file.isDirectory()) {
            //XXX FileNotFound is probably not the best exception
            FileNotFoundException e = new FileNotFoundException("Node is not a document");
            throw e;
        }

        return getFileContents(file);
    }

    /**
     * read an entire file and return the contents as a String
     * @param file
     * @return the contents of the File as a String
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String getFileContents(File file) throws FileNotFoundException,
            IOException {
        StringBuilder fileContents = new StringBuilder();


        BufferedReader input = new BufferedReader(new FileReader(file));
        try {
            String line = null;

            while ((line = input.readLine()) != null) {
                fileContents.append(line);
                fileContents.append(System.getProperty("line.separator"));
            }
        } finally {
            input.close();
        }


        return fileContents.toString();
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

            // create a node to represent this child
            LibraryNode node = new LibraryNode(children[i]);
            File f = new File(dir, children[i]);

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

}
