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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A service for managing the users login and identification
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
@RemoteServiceRelativePath("authenticationservice")
public interface AuthenticationService extends RemoteService
{
    /**
     * A function to log a user into the application
     * @param username  the user's unique id
     * @param password  the user password
     * @return      the session id for the login
     */
    public String login(String username, String password);
    
    /**
     * A function to check if a users session id is still valid
     * @param sessionid  the user's unique session id
     * @return  the username if the id is valid, "INVALID" otherwise
     */
    public String validateSessionId(String sessionid);
    
    
    /**
     * A function to create a user and log the user in. 
     * @param username  the user's unique id
     * @param password  the user password
     * @return  the session id for the login
     */
    public String createNewUser(String username, String password);
    
}
