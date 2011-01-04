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

import com.extjs.gxt.ui.client.data.BaseTreeModel;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class Document extends BaseTreeModel {
    private static final long serialVersionUID = 1L;

    public Document() {
    }

    public Document(String name, String document) {
        set("name", name);
        set("document", document);
        set("url", "");
    }

    public Document(String name, String document, String documentUrl) {
        set("name", name);
        set("document", document);
        set("url", documentUrl);
    }

    public String getName() {
        return (String) get("name");
    }

    public String getDocument() {
        return (String) get("document");
    }

    public void setUrl(String documentUrl) {
        set("url", documentUrl);
    }

    public String getUrl() {
        return (String) get("url");
    }

    public String toString() {
        return getName();
    }
}
