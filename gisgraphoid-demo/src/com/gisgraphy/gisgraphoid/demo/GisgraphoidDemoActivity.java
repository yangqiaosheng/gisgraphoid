package com.gisgraphy.gisgraphoid.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TwoLineListItem;

import com.gisgraphy.gisgraphoid.CountriesData;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoder;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoderMock;

public class GisgraphoidDemoActivity extends Activity {
    private static final String LOG_TAG = "gisgraphoid-demo";

    protected boolean isRouteDisplayed() {
	return false;
    }

    // private MapView googleMap;
    protected Button btnSearch;
    protected EditText address;
    protected ListView mList;
    protected Spinner spinner;
    protected GisgraphyGeocoder gisgraphyGeocoder;
    protected static List<String> SORTED_COUNTRY_LIST = CountriesData.sortedCountriesName;
    protected static String[] SORTED_COUNTRY_ARRAY = CountriesData.sortedCountriesName.toArray(new String[CountriesData.sortedCountriesName.size()]);

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	address = (EditText) findViewById(R.id.simpleGM_adress);
	mList = (ListView) findViewById(R.id.results);

	spinner = (Spinner) findViewById(R.id.spinnerlocale);
	Collections.sort(SORTED_COUNTRY_LIST);

	ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SORTED_COUNTRY_ARRAY);
	// countryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner.setAdapter(countryCodeAdapter);
	String defaultcountryCode = Locale.getDefault().getCountry();
	Log.i(LOG_TAG, "default country code=" + defaultcountryCode);
	int position = CountriesData.getPositionFromCountryCode(defaultcountryCode);
	Log.i(LOG_TAG, "set spinner position to " + position + ". Country=" + CountriesData.sortedCountriesName.get(position));
	spinner.setSelection(position);

	btnSearch = (Button) findViewById(R.id.simpleGM_btn_search);
	address.setMaxLines(1);
	address.requestFocus();
	btnSearch.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		doGeocoding();
	    }
	});
	address.setOnKeyListener(new OnKeyListener() {
	    public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		    doGeocoding();
		    return true;
		}
		return false;

	    }
	});
    }

    protected void doGeocoding() {
	String addressInput = address.getText().toString();
	if (addressInput == null || addressInput.trim().equals("")) {
	    Dialog emptyAddressInputDialog = new AlertDialog.Builder(GisgraphoidDemoActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null)
		    .setMessage(getResources().getString(R.string.empty_input)).create();
	    emptyAddressInputDialog.show();
	    return;
	}

	int spinnerItemPosition = spinner.getSelectedItemPosition();
	Log.i(LOG_TAG, "spinner item position = " + spinnerItemPosition);
	String countryCode = CountriesData.getCountryCodeFromPosition(spinnerItemPosition);
	Log.i(LOG_TAG, "countrycode selected=" + countryCode);
	Locale locale = new Locale("EN", countryCode);

	gisgraphyGeocoder = createGeocoder(locale); // create new geocoder
						    // instance
	// gisgraphyGeocoder.setBaseUrl("http://192.168.0.12:8080/geocoding/geocode");
	try {
	    gisgraphyGeocoder.getFromLocationName(addressInput, 1);
	    List<Address> foundAdresses = gisgraphyGeocoder.getFromLocationName(addressInput, 5); // Search
	    // addresses

	    if (foundAdresses.size() == 0) {
		// if no address found, display an error
		Log.i(LOG_TAG, "no result found for " + addressInput);
		Dialog locationError = new AlertDialog.Builder(GisgraphoidDemoActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null)
			.setMessage(getResources().getString(R.string.no_result)).create();
		locationError.show();
	    } else {
		// else display result
		Log.i(LOG_TAG, foundAdresses.size() + " result(s) found for " + addressInput);
		AddressAdapter addressAdapter = new AddressAdapter(foundAdresses);
		mList.setAdapter(addressAdapter);
		mList.setOnItemClickListener(addressAdapter);
	    }
	} catch (Exception e) {
	    Log.e(LOG_TAG, "Error during geocoding of " + addressInput + " : " + e.getMessage(), e);
	}
    }

    protected GisgraphyGeocoder createGeocoder(Locale locale) {
	// return new GisgraphyGeocoder(this,locale);
	return new GisgraphyGeocoderMock(this, locale);
    }

    protected void viewOnMap(Address address) {
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