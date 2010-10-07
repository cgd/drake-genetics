/*
 * Copyright (c) 2010 The Jackson Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining  a copy
 * of this software and associated documentation files (the  "Software"), to
 * deal in the Software without restriction, including  without limitation the
 * rights to use, copy, modify, merge, publish,  distribute, sublicense, and/or
 * sell copies of the Software, and to  permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be  included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE  SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
/**
 * 
 */
package org.jax.drakegenetics.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Class for accessing user account information from a repository.
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class Users {
    //  Temporary location for usernames and passwords until I set up a db
    private static final String ACCOUNT_FILE = "users.txt";
    //  TODO:  These should all be handled through properties files...
    private static final int NUM_COLS = 2;
    private static final int USER_NAME = 0;
    private static final int PASSWORD = 1;
        
    //  An in memory lookup of users, stored by user name
    private Map<String, User> users;
 
	
	public Users () {
		openDataSource();	
	}
	
	public boolean userExists(String userName) {
		return users.containsKey(userName);
	}
	
	public User getUser(String userName) {
		return users.get(userName);
	}
	
	private void openDataSource() {
		File userFile = new File(Users.ACCOUNT_FILE);
		if (! userFile.exists()) {
			this.users = new HashMap<String,User>();
		} else {
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(userFile);
				bufferedReader = new BufferedReader(fileReader);

				// loop through lines of the file. Separating each line
				// into a row of the users list.
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					String[] cols = line.split("\t");

					if (cols.length != Users.NUM_COLS) {
						// TODO: Add code to deal with an invalid datasource
						System.out
								.println("COULD NOT READ AUTHENTICATION FILE "
										+ Users.ACCOUNT_FILE
										+ ".  Incorrect number of columns.");
					} else {
						String username = cols[Users.USER_NAME];
						String password = cols[Users.PASSWORD];
						User user = new User(username, password);
						this.users.put(username, user);
					}
				}
				// Close the file readers, we're all done with them.
				bufferedReader.close();
				fileReader.close();
			} catch (IOException ioe) {
				//TODO: Like above, better handle errors with the datasource
				ioe.printStackTrace();
			} finally {
				try {
				if (fileReader != null) fileReader.close();
				if (bufferedReader != null) bufferedReader.close();
				} catch (IOException e) {
					// do nothing
				}
			}
	        
		}
	}
	
	public User createNewUser(String username, String password) {
		User user = null;
		if (! this.users.containsKey(username)) {
			String hash = BCrypt.hashpw(password, BCrypt.gensalt());
			user = new User (username, hash);
			this.users.put(username, user);
		}
    	return user;
	}
	
	public static String  login (Users users, String username, String password) {
    	if (users.userExists(username)) {
    		User user = users.getUser(username);
    		
            String hashFromDB = user.getPassword();
            boolean valid = BCrypt.checkpw(password, hashFromDB);
            if ( valid ) return generateSessionID();
            else return "INVALID PASSWORD";
    	} else
    		return "INVALID USER " + username;
    }
	

	public static String generateSessionID() {
		return "";
	}

}
