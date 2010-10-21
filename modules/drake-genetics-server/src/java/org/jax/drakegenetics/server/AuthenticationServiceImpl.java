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

package org.jax.drakegenetics.server;

import org.jax.drakegenetics.admin.User;
import org.jax.drakegenetics.admin.Users;
import org.jax.drakegenetics.gwtclientapp.client.AuthenticationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server-side implementation of the {@link AuthenticationService} interface
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class AuthenticationServiceImpl
extends RemoteServiceServlet
implements AuthenticationService
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 5304344574572230630L;
    
    /**
     * this is a shared User Lookup
     */
    private static Users userLookup = new Users();
    
    /**
     * {@inheritDoc}
     */
    public String login(String username, String password) {
    	return Users.login(AuthenticationServiceImpl.userLookup, username, 
    				password);
    }
 
    /**
     * {@inheritDoc}
     */
    public String validateSessionId(String sessionid) {
    	
    	return "";
    }
    
    /**
     * {@inheritDoc}
     */
    public String createNewUser(String username, String password) {
    	User user = AuthenticationServiceImpl.userLookup.createNewUser(username, 
    			password);
    	
    	//  Be sure to use the hashed password that's part of the user object
    	return Users.login(AuthenticationServiceImpl.userLookup, username, 
    			user.getPassword());
    }
    
    
}
