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
