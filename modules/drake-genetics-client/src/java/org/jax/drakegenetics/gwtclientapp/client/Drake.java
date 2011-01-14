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
import org.jax.drakegenetics.shareddata.client.PhenoConstants;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author <A HREF="mailto:dave.walton@jax.org">Dave Walton</A>
 */
public class Drake extends BaseTreeModel {
    private static final long serialVersionUID = 1L;
    private boolean drake = true;
    private boolean breeder = true;

    public Drake() {
    }

    public Drake(String name, Image smallImg, Image largeImg) {
        set("name", name);
        set("smallimage", smallImg);
        set("largeimage", largeImg);
        this.drake = false;
    }

    public Drake(String name, DiploidGenome dg) {
        set("name", name);
        set("diploidgenome", dg);
    }

    public Drake(String name, DiploidGenome dg, 
            Image smallImg, Image largeImg) {
        set("name", name);
        set("diploidgenome", dg);
        set("smallimage", smallImg);
        set("largeimage", largeImg);
    }

    public Drake(String name, DiploidGenome dg, 
            Map<String,String> phenome, Image smallImg, Image largeImg) {
        set("name", name);
        set("diploidgenome", dg);
        set("phenome",phenome);
        set("smallimage", smallImg);
        set("largeimage", largeImg);
    }

    public void setName(String name) {
        set("name", name);
    }
    
    public String getName() {
        return (String) get("name");
    }

    public String getGender() {
        // tries to return phenotypic sex and failing that tries to
        // return genotypic sex
        Map<String, String> phenome = this.getPhenome();
        String sexPheno = null;
        if(phenome != null) {
            sexPheno = phenome.get(PhenoConstants.CAT_SEX);
        }
        
        if(sexPheno == null) {
            DiploidGenome genome = this.getDiploidgenome();
            if(genome == null) {
                return null;
            } else {
                return genome.isMale() ? "M" : "F";
            }
        } else {
            if(PhenoConstants.SEX_F.equals(sexPheno) || PhenoConstants.SEX_F_SCRUFFY.equals(sexPheno)) {
                return "F";
            } else if(PhenoConstants.SEX_M.equals(sexPheno) || PhenoConstants.SEX_M_SCRUFFY.equals(sexPheno)) {
                return "M";
            }
            else {
                return null;
            }
        }
    }

    public DiploidGenome getDiploidgenome() {
        return (DiploidGenome) get("diploidgenome");
    }
    
    public void setSmallimage(Image smallimage) {
        set("smallimage", smallimage);
    }

    public Image getSmallimage() {
        return (Image) get("smallimage");
    }

    public void setLargeimage(Image largeimage) {
        set("largeimage", largeimage);
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
    
    public boolean isDrake() {
        return drake;
    }
    
    public void setDrake(boolean isdrake) {
        this.drake = isdrake;
    }
    
    public boolean isBreeder () {
        return this.breeder;
    }
    
    public void setBreeder (boolean isbreeder) {
        this.breeder = isbreeder;
    }
}
