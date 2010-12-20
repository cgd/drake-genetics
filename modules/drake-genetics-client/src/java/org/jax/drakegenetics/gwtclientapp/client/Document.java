package org.jax.drakegenetics.gwtclientapp.client;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class Document extends BaseTreeModel {  
	 private static final long serialVersionUID = 1L;  
	  
	 public Document() {  
	   }  
	   
	   public Document(String name) {  
	     set("name", name);  
	     set("document", "");  
	   }  
	   
	   public String getName() {  
	     return (String) get("name");  
	   }  
	   
	   public void setDoucment(String document) {
		   set("document",document);
	   }
	   
	   public String getDocument() {  
		   return (String) get("document");  
	   }  
		   
	   public String toString() {  
	     return getName();  
	   }  
	 }  
