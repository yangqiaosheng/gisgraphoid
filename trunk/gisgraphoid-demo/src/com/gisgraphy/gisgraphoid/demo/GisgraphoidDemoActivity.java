package com.gisgraphy.gisgraphoid.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.gisgraphy.gisgraphoid.CountriesData;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoder;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoderMock;

public class GisgraphoidDemoActivity extends Activity {
    protected boolean isRouteDisplayed() {
	return false;
    }

    // private MapView googleMap;
    private Button btnSearch;
    private EditText address;
    private TextView localetextview;
    private ListView mList;
    private GisgraphyGeocoder gisgraphyGeocoder;
    private double lat;
    private double lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	btnSearch = (Button) findViewById(R.id.simpleGM_btn_search); 
	address = (EditText) findViewById(R.id.simpleGM_adress); 
	mList = (ListView) findViewById(R.id.results);
	Spinner spinner  = (Spinner) findViewById(R.id.spinnerlocale);
	String defaultcountry = Locale.getDefault().getCountry();
	Set<String> countryname = CountriesData.countries.keySet();
	 ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countryname.toArray(new String[countryname.size()]));
	 spinner.setAdapter(adapter_time);

	 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.planets_array, android.R.layout.simple_spinner_item);

	 //adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter_time);

	gisgraphyGeocoder = createGeocoder(); // create new geocoder instance

	btnSearch.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		String addressInput = address.getText().toString(); // Get input
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