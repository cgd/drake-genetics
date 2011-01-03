package org.jax.drakegenetics.gwtclientapp.client;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class Document extends BaseTreeModel {  
	 private static final long serialVersionUID = 1L;  
	  
	 public Document() {  
	   }  
	   
	   public Document(String name, String document) {  
	     set("name", name);  
	     set("document", document);
	     set("url", "");  
	   }  
	   
	   public Document(String name, String document, 
			   String documentUrl) {  
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
		   set("url",documentUrl);
	   }
	   
	   public String getUrl() {  
		   return (String) get("url");  
	   }  
		   
	   public String toString() {  
	     return getName();  
	   }  
	 }  
