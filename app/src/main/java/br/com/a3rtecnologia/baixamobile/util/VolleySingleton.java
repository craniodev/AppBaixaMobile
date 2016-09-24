package br.com.a3rtecnologia.baixamobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 
 * Classe que mantem a fila de execucao de requisicoes do Volley. 
 * 
 * O Google recomenda que a class seja um singleton, para gerenciar filas de execucao. 
 * 
 * 
 * @author vagner
 *
 */
public class VolleySingleton {

	private static VolleySingleton instance = null;

	//Fila de execucao
	private RequestQueue requestQueue;
	
	//Image Loader
	private ImageLoader imageLoader;
	
	private VolleySingleton(Context context){
	    
		
		requestQueue  = Volley.newRequestQueue(context);
	  
	    imageLoader = new ImageLoader(this.requestQueue,
	      new ImageLoader.ImageCache() {
	       
	    	// Usando LRU (Last Recent Used) Cache  
	        private final LruCache<String, Bitmap>
	          mCache = new LruCache<String, Bitmap>(10);
	  
	        public void putBitmap(
					String url, Bitmap bitmap) {
	          mCache.put(url, bitmap);  
	        }  
	        
	        public Bitmap getBitmap(String url) {
	          return mCache.get(url);  
	        }
	        
	      });  
	  
	}  
	
	public static VolleySingleton getInstance(Context context){
		   
		if(instance == null){  
		     
			instance = new VolleySingleton(context);  
		    
		}  
		
		return instance;   
	}  
	
	public RequestQueue getRequestQueue(){
	    return this.requestQueue;
	}  
	   
	  
	public ImageLoader getImageLoader(){
	    return this.imageLoader;  
	} 
}
