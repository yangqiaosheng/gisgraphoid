package com.gisgraphy.gisgraphoid.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends Activity {
	protected boolean isRouteDisplayed() {
		return false;
	}

	private org.osmdroid.views.MapView osmMap;
	//private MapView  googleMap;
	private double lat;
	private double lon;
	private String name;
	private static String LOG_TAG="Gisgraphoid-demo-map";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		Intent intent = getIntent();
		lat = intent.getDoubleExtra(ExtraInfos.LATITUDE,0);
		lon = intent.getDoubleExtra(ExtraInfos.LONGITUDE,0);
		name=intent.getStringExtra(ExtraInfos.FEATURE_NAME);
		
		Log.i(LOG_TAG, "lat="+lat);
		Log.i(LOG_TAG, "lon="+lon);
		Log.i(LOG_TAG, "name="+name);
		
		
		
		osmMap =(org.osmdroid.views.MapView) findViewById(R.id.osm_map);
		osmMap.setBuiltInZoomControls(true);
		osmMap.setMultiTouchControls(true);
		navigateToOsmMAP(lat * 1000000,lon* 1000000 ,name,
			osmMap);
		
	}

	/*public void navigateToGoogleMap(double latitude, double longitude,
			MapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); 
		mv.displayZoomControls(true); 
		MapController mc = mv.getController();
		mc.setCenter(p); 
		int zoomlevel = mv.getMaxZoomLevel(); 
		mc.setZoom(zoomlevel - 1); 
		mv.setSatellite(false); 

	}*/
	
	
	/**
	 * Navigates a given google Map to the specified Longitude and Latitude
	 * 
	 * @param latitude
	 * @param longitude
	 * @param mv
	 */
	public void navigateToOsmMAP(double latitude, double longitude,String name,
			org.osmdroid.views.MapView mv) {
		org.osmdroid.util.GeoPoint p = new org.osmdroid.util.GeoPoint((int) latitude, (int) longitude); 
		org.osmdroid.views.MapController mc = mv.getController();
		mc.setZoom(14); 
		mc.setCenter(p);
	}
	
	
}