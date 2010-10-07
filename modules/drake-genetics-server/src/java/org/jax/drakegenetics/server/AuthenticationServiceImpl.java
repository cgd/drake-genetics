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
