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
