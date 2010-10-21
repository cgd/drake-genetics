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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A service for managing the users login and identification
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public interface AuthenticationServiceAsync
{
    /**
     * A function to log a user into the application
     * @param username  the user's unique id
     * @param password  the user password
     * @param callback  the session id for the login
     */
    public void login(String username, String password, 
    		AsyncCallback<String> callback);

    /**
     * A function to check if a users session id is still valid
     * @param sessionid  the user's unique session id
     * @param callback  the username if the id is valid, "INVALID" otherwise
     */
    public void validateSessionId(String sessionid, 
    		AsyncCallback<String> callback);

    /**
     * A function to create a user and log the user in. 
     * @param username  the user's unique id
     * @param password  the user password
     * @param callback  the session id for the login
     */
    public void createNewUser(String username, String password, 
    		AsyncCallback<String> callback);

}
