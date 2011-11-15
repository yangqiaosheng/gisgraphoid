package com.gisgraphy.gisgraphoid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.gisgraphy.addressparser.AddressResultsDto;

/**
 * A class for handling geocoding and reverse geocoding with Gisgraphy.
 * Geocoding is the process of transforming a street address or other
 * description of a location into a (latitude, longitude) coordinate. Reverse
 * geocoding is the process of transforming a (latitude, longitude) coordinate
 * into a (partial) address. The amount of detail in a reverse geocoded location
 * description may vary, for example one might contain the full street address
 * of the closest building, while another might contain only a city name and
 * postal code. The Geocoder class requires a backend service that is not
 * included in the core android framework. The Geocoder query methods will
 * return an empty list if there no backend service in the platform. Use the
 * isPresent() method to determine whether a Geocoder implementation exists.
 * 
 * usage :
 * TODO
 */
public class GisgraphyGeocoder {

	protected static final String FORMAT = "json";
	protected static final String GEOCODING_URI = "geocoding/geocode";
	private String className=GisgraphyGeocoder.class.getSimpleName();
	private Locale locale;
	private Long apiKey; 
	private String url="http://services.gisgraphy.com/";

	static boolean isPresent() {
		return true;
	}

	
	/**
	 * @param url the base Url. it must follow the SCHEME://HOST[:PORT]/
	 * Don't add the URI.
	 * Good base URL : http://services.gisgraphy.com/
	 * Bad base URL : http://services.gisgraphy.com/geocoding/geocode
	 */
	public void setBaseUrl(String url){
		if (url==null){
			throw new IllegalArgumentException(className+" does not accept null URL");
		}
		//TODO check format
		this.url = url;
	}
	/**
	 * Constructs a Geocoder whose responses will be localized for the given
	 * Locale. You should prefer the {@link #GisgraphyGeocoder(Context, Locale, String)} constructor.
	 * This method is only to suite the android geocoder interface
	 * 
	 * @param context
	 *            the Context of the calling Activity
	 * @param locale
	 *            the desired Locale for the query results
	 * @throws NullPointerException
	 *             if Locale is null
	 */
	public GisgraphyGeocoder(Context context, Locale locale) {
		if (locale == null) {
			throw new NullPointerException(className+ " does not accept null locale");
		}
		this.locale = locale;
	}
	
	/**
	 * Constructs a Geocoder whose responses will be localized for the given
	 * Locale and URL
	 *  @param context
	 *            the Context of the calling Activity
	 * @param locale
	 *            the desired Locale for the query results
	 * @param url the base url (scheme,host,port). see {@link #setBaseUrl(String)}
	 */
	public GisgraphyGeocoder(Context context, Locale locale,String url) {
		if (locale == null) {
			throw new NullPointerException(className+ " does not accept null locale");
		}
		setBaseUrl(url);
		this.locale = locale;
	}

	/**
	 * Constructs a Gisgraphy Geocoder whose responses will be localized for the default
	 * system Locale. You should prefer the {@link #GisgraphyGeocoder(Context, String)} constructor.
	 * This method is only here to suite the android geocoder interface
	 * 
	 * @param context
	 *            the desired Locale for the query results
	 */
	public GisgraphyGeocoder(Context context) {
		this.locale = Locale.getDefault();
	}
	
	/**
	 * Constructs a Geocoder whose responses will be localized for the default
	 * system Locale.
	 * 
	 * @param context
	 *            the desired Locale for the query results
	 * @param url the base url (scheme,host,port). see {@link #setBaseUrl(String)}
	 */
	public GisgraphyGeocoder(Context context, String url) {
		this.locale = Locale.getDefault();
		setBaseUrl(url);
	}

	/**
	 * Returns an array of Addresses that are known to describe the area
	 * immediately surrounding the given latitude and longitude. The returned
	 * addresses will be localized for the locale provided to this class's
	 * constructor.
	 * 
	 * The returned values may be obtained by means of a network lookup. The
	 * results are a best guess and are not guaranteed to be meaningful or
	 * correct. It may be useful to call this method from a thread separate from
	 * your primary UI thread.
	 * 
	 * @param latitude
	 *            the latitude a point for the search
	 * @param longitude
	 *            the longitude a point for the search
	 * @param maxResults
	 *            max number of addresses to return. Smaller numbers (1 to 5)
	 *            are recommended
	 * @return a list of Address objects. Returns empty list if no
	 *         matches were found or there is no backend service available.
	 * 
	 * @throws IllegalArgumentException
	 *             if latitude is less than -90 or greater than 90
	 * @throws IllegalArgumentException
	 *             if longitude is less than -180 or greater than 180
	 * @throws IOException
	 *             if the network is unavailable or any other I/O problem occurs
	 */
	public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException {
		return null;
	}


	


	/**
	 * Returns an array of Addresses that are known to describe the named
	 * location, which may be a place name such as "Dalvik, Iceland", an address
	 * such as "1600 Amphitheatre Parkway, Mountain View, CA", an airport code
	 * such as "SFO", etc.. The returned addresses will be localized for the
	 * locale provided to this class's constructor.
	 * 
	 * You may specify a bounding box for the search results by including the
	 * Latitude and Longitude of the Lower Left point and Upper Right point of
	 * the box.
	 * 
	 * The query will block and returned values will be obtained by means of a
	 * network lookup. The results are a best guess and are not guaranteed to be
	 * meaningful or correct. It may be useful to call this method from a thread
	 * separate from your primary UI thread.
	 * 
	 * @param locationName
	 *            a user-supplied description of a location
	 * @param maxResults
	 *            max number of addresses to return. Smaller numbers (1 to 5)
	 *            are recommended
	 * @param lowerLeftLatitude
	 *            the latitude of the lower left corner of the bounding box
	 * @param lowerLeftLongitude
	 *            the longitude of the lower left corner of the bounding box
	 * @param upperRightLatitude
	 *            the latitude of the upper right corner of the bounding box
	 * @param upperRightLongitude
	 *            the longitude of the upper right corner of the bounding box
	 * @return a list of Address objects. Returns null or empty list if no
	 *         matches were found or there is no backend service available.
	 * @throws IllegalArgumentException
	 *             if locationName is null
	 * @throws IllegalArgumentException
	 *             if any latitude is less than -90 or greater than 90
	 * @throws IllegalArgumentException
	 *             if any longitude is less than -180 or greater than 180
	 * @throws IOException
	 *             if the network is unavailable or any other I/O problem occurs
	 */
	public List<Address> getFromLocationName(String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) throws IOException{
		return getFromLocationName(locationName, maxResults);
		//TODO extract radius
	}

	/**
	 * Returns an array of Addresses that are known to describe the named
	 * location, which may be a place name such as "Dalvik, Iceland", an address
	 * such as "1600 Amphitheatre Parkway, Mountain View, CA", an airport code
	 * such as "SFO", etc.. The returned addresses will be localized for the
	 * locale provided to this class's constructor.
	 * 
	 * The query will block and returned values will be obtained by means of a
	 * network lookup. The results are a best guess and are not guaranteed to be
	 * meaningful or correct. It may be useful to call this method from a thread
	 * separate from your primary UI thread.
	 * 
	 * @param locationName
	 *            a user-supplied description of a location
	 * @param maxResults
	 *            max number of results to return. Smaller numbers (1 to 5) are
	 *            recommended
	 * @return a list of Address objects. Returns null or empty list if no
	 *         matches were found or there is no backend service available.
	 * 
	 * @throws IllegalArgumentException
	 *             if locationName is null I* @throws OException if the network
	 *             is unavailable or any other I/O problem occurs
	 * @throws IOException
	 *             if the network is unavailable or any other I/O problem occurs
	 */
	public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
		checkUrl();
		List<Address> androidAddress = new ArrayList<Address>();
       RestClient webService = new RestClient(url);
 
       //Pass the parameters if needed , if not then pass dummy one as follows
       Map<String, String> params = new HashMap<String, String>();
       locationName = "rue jean jaures bailleul";
       String iso3countryCode =  locale.getCountry();
       String iso2countryCode = getiso2countryCodeFromiso3countryCode(iso3countryCode);
       Log.d(className,"country='"+locationName+"'");
       Log.d(className,"country='"+iso2countryCode+"'");
       params.put("country", iso2countryCode);
       params.put("address", locationName);
       params.put("format", FORMAT);
       if (apiKey!=null){
    	   params.put("apikey",apiKey+"");
    	   //TODO test
       }
 
       //Get JSON response from server the "" are where the method name would normally go if needed example
       // webService.webGet("getMoreAllerts", params);
       AddressResultsDto response = webService.get(GEOCODING_URI,AddressResultsDto.class, params);
       if (response!=null && response.getResult()!=null && response.getResult().size()>0 ){
    	   androidAddress = transformGisgraphyAdressToAndroidAddress(response.getResult());
       }
		return androidAddress;
		
	}


	private String getiso2countryCodeFromiso3countryCode(String iso3countryCode) {
		return iso3countryCode.substring(0, 2);
	}
	
	private void checkUrl() throws IOException {
		if (url==null){
			throw new IOException(this.getClass().getSimpleName()+" is not initialize, please call setUrl before calling geocoding methods");
		}
	}

	private List<Address> transformGisgraphyAdressToAndroidAddress(List<com.gisgraphy.addressparser.Address> gisgraphyaddresses) {
		List<Address> addresses = new ArrayList<Address>();
		Address address = new Address(locale);
		addresses.add(address);
		return addresses;
	}


	/**
	 * @return the apikey. apikey is only used for Gisgraphy premium services.
	 * It is not required for free services (when those lines are written)
	 * @see http://www.gisgraphy.com/premium
	 */
	public Long getApiKey() {
		return apiKey;
	}


	/**
	 * @param apiKey the apikey provided by gisgraphy
	 * apikey is used for Gisgraphy premium services. 
	 * It is not required for free services (when those lines are written)
	 * @see http://www.gisgraphy.com/premium
	 */
	public void setApiKey(Long apiKey) {
		this.apiKey = apiKey;
	}

}
