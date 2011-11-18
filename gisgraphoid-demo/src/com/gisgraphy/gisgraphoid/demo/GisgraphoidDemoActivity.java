package com.gisgraphy.gisgraphoid.demo;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TwoLineListItem;

import com.gisgraphy.gisgraphoid.GisgraphyGeocoder;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoderMock;

public class GisgraphoidDemoActivity extends Activity {
    protected boolean isRouteDisplayed() {
	return false;
    }

    // private MapView googleMap;
    private Button btnSearch;
    private EditText adress;
    private ListView mList;
    private GisgraphyGeocoder gisgraphyGeocoder;
    private double lat;
    private double lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	// osmMap =(org.osmdroid.views.MapView) findViewById(R.id.osm_map);
	// googleMap = (MapView) findViewById(R.id.simpleGM_map); // Get map
	// from XML
	btnSearch = (Button) findViewById(R.id.simpleGM_btn_search); // Get
								     // button
								     // from xml
	adress = (EditText) findViewById(R.id.simpleGM_adress); // Get address
								// from XML
	mList = (ListView) findViewById(R.id.results);

	gisgraphyGeocoder = createGeocoder(); // create new geocoder instance

	btnSearch.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		String addressInput = adress.getText().toString(); // Get input
								   // text
		gisgraphyGeocoder.setBaseUrl("http://192.168.0.12:8080/geocoding/geocode");
		try {
		    gisgraphyGeocoder.getFromLocationName(addressInput, 1);

		    List<Address> foundAdresses = gisgraphyGeocoder.getFromLocationName(addressInput, 5); // Search
													  // addresses

		    if (foundAdresses.size() == 0) { // if no address found,
			// display an error
			Dialog locationError = new AlertDialog.Builder(GisgraphoidDemoActivity.this).setIcon(0).setTitle("Error").setPositiveButton(R.string.ok, null).setMessage(
			// TODO l10n
				"Sorry, your address doesn't exist.").create();
			locationError.show();
		    } else { // else display address on map
			for (int i = 0; i < foundAdresses.size(); ++i) {
			    // Save results as Longitude and Latitude
			    // @todo: if more than one result, then show a
			    // select-list
			    Address x = foundAdresses.get(i);
			    lat = x.getLatitude();
			    lon = x.getLongitude();
			}
			// navigateToGoogleMap((lat * 1000000), (lon * 1000000),
			// googleMap);
			AddressAdapter addressAdapter = new AddressAdapter(foundAdresses);
			mList.setAdapter(addressAdapter);
			mList.setOnItemClickListener(addressAdapter);
		    }
		} catch (Exception e) {
		    // @todo: Show error message
		}

	    }
	});
    }

    /*
     * public void navigateToGoogleMap(double latitude, double longitude,
     * MapView mv) { GeoPoint p = new GeoPoint((int) latitude, (int) longitude);
     * // new // GeoPoint mv.displayZoomControls(true); // display Zoom (seems
     * that it doesn't // work yet) MapController mc = mv.getController();
     * mc.animateTo(p); // move map to the given point int zoomlevel =
     * mv.getMaxZoomLevel(); // detect maximum zoom level mc.setZoom(zoomlevel -
     * 1); // zoom mv.setSatellite(false); // display only "normal" mapview
     * 
     * }
     */

    private GisgraphyGeocoder createGeocoder() {
	// return new GisgraphyGeocoder(this);
	return new GisgraphyGeocoderMock(this);
    }

   

    private void viewOnMap(Address address) {
	Intent next = new Intent();
	next.setClass(this, MapActivity.class);
	next.putExtra(ExtraInfos.FEATURE_NAME, address.getFeatureName());
	next.putExtra(ExtraInfos.LATITUDE, address.getLatitude());
	next.putExtra(ExtraInfos.LONGITUDE, address.getLongitude());
	startActivity(next);

    }

    class AddressAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

	private final List<Address> addresses;
	private final LayoutInflater mInflater;

	public AddressAdapter(List<Address> addresses) {
	    super();
	    if (addresses == null) {
		this.addresses = new ArrayList<Address>();
	    } else {
		this.addresses = addresses;
	    }
	    mInflater = (LayoutInflater) GisgraphoidDemoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
	    return addresses.size();
	}

	public Object getItem(int position) {
	    return position;
	}

	public long getItemId(int position) {
	    return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	    TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView : createView(parent);
	    bindView(view, addresses.get(position));
	    return view;
	}

	private TwoLineListItem createView(ViewGroup parent) {
	    TwoLineListItem item = (TwoLineListItem) mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
	    item.getText2().setSingleLine();
	    item.getText2().setEllipsize(TextUtils.TruncateAt.END);
	    item.getText1().setSingleLine();
	    item.getText1().setEllipsize(TextUtils.TruncateAt.END);
	    return item;
	}

	private void bindView(TwoLineListItem view, Address address) {
	    view.getText1().setText(address.getFeatureName());
	    view.getText2().setText(getResources().getString(R.string.latitude) + "=" + address.getLatitude() + ";" + getResources().getString(R.string.longitude) + "=" + address.getLongitude());
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    viewOnMap(addresses.get(position));
	    finish();
	}

    }

}