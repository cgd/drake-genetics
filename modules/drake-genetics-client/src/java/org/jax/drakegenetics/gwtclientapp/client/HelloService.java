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
 * A test service to make sure that client and server are correctly wired up
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
@RemoteServiceRelativePath("helloservice")
public interface HelloService extends RemoteService
{
    /**
     * A function to say hello
     * @param name  the name to say hello to
     * @return      the hello message
     */
    public String sayHelloTo(String name);
}
