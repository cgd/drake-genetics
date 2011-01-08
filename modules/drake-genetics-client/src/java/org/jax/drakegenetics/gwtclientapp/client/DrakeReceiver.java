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

/**
 * Interace defines a method to hand a Drake object from one class to
 * another.  What the receiving class does with the drake once it's received
 * is up to it.
 * 
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public interface DrakeReceiver {

    /** Send a drake to the implenting class.
     * What the class does with the drake once it's received is specific
     * to the receiver, this is just a generic transport between interface
     * components.
     * @param d  The Drake being sent to the class.
     */
    public void sendDrake(Drake d);
}
