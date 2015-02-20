package com.rafaelsf80.d4w.hotels;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.rafaelsf80.d4w.hotels.SlidingTabsColorsFragment.SamplePagerItem;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListView;


public class DataTask extends AsyncTask<Void, Integer, Boolean> {
	
   private final String TAG = getClass().getSimpleName();
	
    ArrayList<Item> items;
    protected Main activ;
    protected ListView mylist;
    private ProgressDialog dialog;
    private Bundle savedInstanceState;
	
	public DataTask(Main activ, Bundle savedInstanceState)
    {
		this.activ = activ;
		this.savedInstanceState = savedInstanceState;
		this.items = new ArrayList<Item>();
		dialog = new ProgressDialog(activ);
        Log.d(TAG, "DataTask constructor");
    }
	
	@Override
    protected void onPreExecute() {
        dialog.setMessage("Loading, please wait.");
        dialog.show();
    }
	
    @Override
    protected Boolean doInBackground(Void... params)
    {
        String s = null;
        HttpResponse httpresponse;
        int i;
        try {
	        httpresponse = (new DefaultHttpClient()).execute(new HttpGet("https://script.googleusercontent.com/macros/echo?user_content_key=jC1P2Fd5jS_HXzG7DI8i5fnjPxb3iX9LGHhmEkvdBD4_HtKg9euRDqhsT14DadEJDGjmJTZL_1wtV4ctYAz8R_OQV8YW7mCgm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnDkrjYXUsX96kHDOoP8IPGjNyfLdkXIk-OqZs7D_N5wsaikPxxMJZeyi3eD6jFknQCjHK1wJ9k37&lib=MetPFVXDwnph9dqLON6lqDbPoDC-lvsGV"));
	        Log.i(TAG, (new StringBuilder("Status Code ")).append(Integer.toString(httpresponse.getStatusLine().getStatusCode())).toString());
	        i = httpresponse.getStatusLine().getStatusCode();
	        s = null;
	        
	        if(i == 200) {
	        	
		        JSONArray jsonarray;
		        s = EntityUtils.toString(httpresponse.getEntity());
		        jsonarray = new JSONArray(s);
		        
		        for(int j = 0;j < jsonarray.length();j++) {
	            
			        JSONObject jsonobject = jsonarray.getJSONObject(j);
			        Item item = new Item();
			        item.nombre_actividad = jsonobject.getString("actividad");
			        item.descripcion = jsonobject.getString("descripcion");
			        item.disponibles = jsonobject.getString("disponibles");
			        item.total_plazas = jsonobject.getString("plazas");
			        item.reservadas = jsonobject.getString("habitaciones");
			        item.color = jsonobject.getString("color");
			        items.add(item);		        
		        }
		        
	        } else {
	          
		        Log.e(TAG, "Error loading JSON");
		        Log.e(TAG, s);
	        }
        }
        catch(Exception e) {
        	return false;
        }
        
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b)
    {
    	if (dialog.isShowing()) {
            dialog.dismiss();
        }
    	
    	if (savedInstanceState == null) {
            FragmentTransaction transaction = activ.getSupportFragmentManager().beginTransaction();
            activ.fragment = new SlidingTabsColorsFragment();
            transaction.replace(R.id.sample_content_fragment, activ.fragment);
            transaction.commit();          
        }
    	
    	for (int i=0; i<items.size(); i++) {

    		int total = Integer.parseInt(items.get(i).total_plazas);
    		int available = Integer.parseInt(items.get(i).disponibles);
    		
    		int color = Color.BLUE;  //default color
    		String colorString = items.get(i).color;
    		
    		if (colorString.compareTo("BLUE") == 0)
    			color = Color.BLUE;
    		if (colorString.compareTo("MAGENTA") == 0)
    			color = Color.MAGENTA;
    		if (colorString.compareTo("RED") == 0)
    			color = Color.RED;
    		if (colorString.compareTo("DKGRAY") == 0)
    			color = Color.DKGRAY;
    		if (colorString.compareTo("YELLOW") == 0)
    			color = Color.YELLOW;
    		if (colorString.compareTo("GREEN") == 0)
    			color = Color.GREEN;
    		if (colorString.compareTo("CYAN") == 0)
    			color = Color.CYAN;
    		if (colorString.compareTo("BLACK") == 0)
    			color = Color.BLACK;

    		activ.fragment.mTabs.add(new SamplePagerItem(
    				items.get(i).nombre_actividad, // Title
    				items.get(i).descripcion,
    				total,
    				available,  
    				items.get(i).reservadas,
    				color, // Indicator color
    				Color.GRAY // Divider color
    				));
    	}     	
    }
 }