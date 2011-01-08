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

import java.util.Map;

import org.jax.drakegenetics.shareddata.client.DiploidGenome;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class Drake extends BaseTreeModel {
    private static final long serialVersionUID = 1L;

    public Drake() {
    }

    public Drake(String name, String gender, DiploidGenome dg, 
            Image smallImg, Image largeImg) {
        set("name", name);
        set("gender", gender);
        set("diploidgenome", dg);
        set("smallimage", smallImg);
        set("largeimage", largeImg);
    }

    public Drake(String name, String gender, DiploidGenome dg, 
            Map<String,String> phenome, Image smallImg, Image largeImg) {
        set("name", name);
        set("gender", gender);
        set("diploidgenome", dg);
        set("phenome",phenome);
        set("smallimage", smallImg);
        set("largeimage", largeImg);
    }

    public String getName() {
        return (String) get("name");
    }

    public String getGender() {
        return (String) get("gender");
    }

    public DiploidGenome getDiploidgenome() {
        return (DiploidGenome) get("diploidgenome");
    }

    public Image getSmallimage() {
        return (Image) get("smallimage");
    }

    public Image getLargeimage() {
        return (Image) get("largeimage");
    }
    
    public void setPhenome(Map<String, String> phenome) {
        set("phenome",phenome);
    }
    
    public Map<String, String> getPhenome() {
        return (Map<String,String>)get("phenome");
    }

    public String toString() {
        return getName() + "_" + getGender();
    }
}
