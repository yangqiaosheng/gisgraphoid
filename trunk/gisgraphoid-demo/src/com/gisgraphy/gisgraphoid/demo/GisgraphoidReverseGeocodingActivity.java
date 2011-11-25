package com.gisgraphy.gisgraphoid.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.gisgraphy.domain.valueobject.CountriesStaticData;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoder;
import com.gisgraphy.gisgraphoid.GisgraphyGeocoderMock;

/**
 * Sample code to use Gisgraphoid Geocoder
 * @see {@link GisgraphyGeocoder}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class GisgraphoidReverseGeocodingActivity extends Activity {
	private static final String LOG_TAG = "gisgraphoid-demo";

	protected static List<String> SORTED_COUNTRY_LIST = CountriesStaticData.sortedCountriesName;
	protected static String[] SORTED_COUNTRY_ARRAY = CountriesStaticData.sortedCountriesName.toArray(new String[CountriesStaticData.sortedCountriesName.size()]);

	protected static final int DATA_CHANGED = 0;
	protected static final int NO_INPUT = 1;
	protected static final int NO_RESULT = 2;
	protected static final int GEOCODING_IN_PROGRESS = 3;
	protected static final int GEOCODING_DONE = 4;

	// private MapView googleMap;
	protected Button btnSearch;
	protected EditText latitude;
	protected EditText longitude;
	protected ListView mList;
	protected ProgressDialog progressDialog;

	protected Dialog wrongLatDialog;
	protected Dialog wrongLongDialog;
	protected Dialog noResultDialog;

	protected GisgraphyGeocoder gisgraphyGeocoder;

	protected AddressResultAdapter addressAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reversegeocoding);

		// error dialogs
		wrongLatDialog = new AlertDialog.Builder(GisgraphoidReverseGeocodingActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null).setMessage(getResources().getString(R.string.wrong_lat)).create();
		wrongLongDialog = new AlertDialog.Builder(GisgraphoidReverseGeocodingActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null).setMessage(getResources().getString(R.string.wrong_long)).create();
		noResultDialog = new AlertDialog.Builder(GisgraphoidReverseGeocodingActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null).setMessage(getResources().getString(R.string.no_result)).create();

		// input
		latitude = (EditText) findViewById(R.id.reverse_latitude);
		latitude.setMaxLines(1);
		latitude.requestFocus();
		
		longitude = (EditText) findViewById(R.id.reverse_longitude);
		longitude.setMaxLines(1);

		

		// results display
		mList = (ListView) findViewById(R.id.reverse_results);
		addressAdapter = new AddressResultAdapter();
		mList.setAdapter(addressAdapter);
		mList.setOnItemClickListener(addressAdapter);

		// progress dialog
		progressDialog = new ProgressDialog(this);


		// button
		btnSearch = (Button) findViewById(R.id.reverse_btn);
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doReverseGeocoding();
			}
		});
	}

	protected void doReverseGeocoding() {
		progressDialog.setMessage(getResources().getString(R.string.geocoding_in_progress));

		new Thread(new Runnable() {
			public void run() {
				try {
					String latitude_input = latitude.getText().toString();
					String longitude_input = longitude.getText().toString();
					
				
					// check lat/long =>extraire une fonction dans jts helper
					if (addressToGeocode == null || addressToGeocode.trim().equals("")) {
						handler.sendEmptyMessage(NO_INPUT);
						return;
					}
					

					gisgraphyGeocoder = createGeocoder(locale);
					// gisgraphyGeocoder.setBaseUrl("http://192.168.0.12:8080/geocoding/geocode");

					// Reverse Geocode !
					handler.sendEmptyMessage(GEOCODING_IN_PROGRESS);
					List<Address> foundAdresses = gisgraphyGeocoder.getFromLocation(addressToGeocode, 5); // Search
					handler.sendEmptyMessage(GEOCODING_DONE);
					Log.i(LOG_TAG, foundAdresses.size() + " result found for " + addressInput);

					if (foundAdresses.size() == 0) {
						// if no address found, display a dialog box
						Log.i(LOG_TAG, "no result found for " + addressInput);
						handler.sendEmptyMessage(NO_RESULT);

					} else {
						// else display results
						Log.i(LOG_TAG, foundAdresses.size() + " result(s) found for " + addressInput);
						addressAdapter.setaddress(foundAdresses);
						handler.sendEmptyMessage(DATA_CHANGED);
					}

				} catch (Exception e) {
					Log.e(LOG_TAG, "Error during geocoding of " + addressInput + " : " + e.getMessage(), e);
					Dialog locationError = new AlertDialog.Builder(GisgraphoidReverseGeocodingActivity.this).setIcon(0).setTitle(getResources().getString(R.string.dialog_default_title)).setPositiveButton(R.string.ok, null).setMessage(getResources().getString(R.string.no_result)).create();
					locationError.show();
				}
			}
		}).start();
	}

	/**
	 * Handler for geocoding events
	 */
	private final Handler handler = new Handler() {
		public void handleMessage(Message message) {

			switch (message.what) {
			case DATA_CHANGED:
				addressAdapter.notifyDataSetChanged();
				break;
			case NO_INPUT:
				wrongLatLongialog.show();
				break;
			case NO_RESULT:
				noResultDialog.show();
				break;
			case GEOCODING_IN_PROGRESS:
				progressDialog.show();
				break;
			case GEOCODING_DONE:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					break;
				}
			default:
				break;
			}
		}
	};

	/**
	 * @param locale the locale for the geocoder
	 * @return a new geocoder instance
	 */
	protected GisgraphyGeocoder createGeocoder(Locale locale) {
		// return new GisgraphyGeocoder(this,locale);
		return new GisgraphyGeocoderMock(this, locale);
	}

	/**
	 * start a new Activity to display an address on a map
	 * @param address the address to display on a map
	 */
	protected void viewOnMap(Address address) {
		Intent next = new Intent();
		next.setClass(this, MapActivity.class);
		next.putExtra(ExtraInfos.FEATURE_NAME, address.getFeatureName());
		next.putExtra(ExtraInfos.LATITUDE, address.getLatitude());
		next.putExtra(ExtraInfos.LONGITUDE, address.getLongitude());
		startActivity(next);

	}

	/**
	 * 	Adapter to display Address results
	 *  @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
	 *
	 */
	class AddressResultAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

		private List<Address> addresses = new ArrayList<Address>();;
		private LayoutInflater mInflater;

		public AddressResultAdapter(List<Address> addresses) {
			super();
			setaddress(addresses);
			mInflater = (LayoutInflater) GisgraphoidReverseGeocodingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public AddressResultAdapter() {
			super();
			mInflater = (LayoutInflater) GisgraphoidReverseGeocodingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setaddress(List<Address> addresses) {
			if (addresses != null) {
				this.addresses = addresses;
			}
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