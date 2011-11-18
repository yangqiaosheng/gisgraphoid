package com.gisgraphy.gisgraphoid.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MapActivity extends Activity {
	protected boolean isRouteDisplayed() {
		return false;
	}

	private org.osmdroid.views.MapView osmMap;
	//private MapView  googleMap;
	private double lat;
	private double lon;
	private String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		Intent intent = getIntent();
		lat = intent.getDoubleExtra(ExtraInfos.LATITUDE,0);
		lon = intent.getDoubleExtra(ExtraInfos.LONGITUDE,0);
		
		name=intent.getStringExtra(ExtraInfos.FEATURE_NAME);
		osmMap =(org.osmdroid.views.MapView) findViewById(R.id.osm_map);
		osmMap.setBuiltInZoomControls(true);
		osmMap.setMultiTouchControls(true);
		navigateToOsmMAP(lat * 1000000,lon* 1000000 ,name,
			osmMap);
		
	}

	/*public void navigateToGoogleMap(double latitude, double longitude,
			MapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); // new
		// GeoPoint
		mv.displayZoomControls(true); // display Zoom (seems that it doesn't
		// work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); // move map to the given point
		int zoomlevel = mv.getMaxZoomLevel(); // detect maximum zoom level
		mc.setZoom(zoomlevel - 1); // zoom
		mv.setSatellite(false); // display only "normal" mapview

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
		org.osmdroid.util.GeoPoint p = new org.osmdroid.util.GeoPoint((int) latitude, (int) longitude); // new
		// GeoPoint
		org.osmdroid.views.MapController mc = mv.getController();
		//mv.getMaxZoomLevel();
		//mc.setZoom(mv.getMaxZoomLevel() -1);
		//mc.animateTo(p); // move map to the given point
		//int zoomlevel = mv.getMaxZoomLevel(); // detect maximum zoom level
		mc.setZoom(14); // zoom
		mc.setCenter(p);
		//mv.setSatellite(false); // display only "normal" mapview
	}
	
	
}