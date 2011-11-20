package com.gisgraphy.gisgraphoid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import android.location.Address;

import com.gisgraphy.addressparser.AddressResultsDto;

public class GisgraphyGeocoderTest {

    @Test(expected=IllegalArgumentException.class)
    public void setBaseUrlWithNullUrlShouldThrowsIllegalArgumentException() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	geocoder.setBaseUrl(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setBaseUrlWithMalformedUrlUrlShouldThrowsIllegalArgumentException() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	geocoder.setBaseUrl("htp://foo");
    }
    
    
    @Test
    public void setBaseUrlShouldSetTheUrl() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	String changedUrl="http://foo.com";
	geocoder.setBaseUrl(changedUrl);
	Assert.assertEquals(changedUrl,geocoder.getBaseUrl());
    }
    
    @Test
    public void defaultBaseUrl() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	Assert.assertEquals(GisgraphyGeocoder.DEFAULT_BASE_URL,geocoder.getBaseUrl());
    }
    
    @Test
    public void ConstructorShouldSetTheLocale(){
	Locale locale =Locale.CANADA;
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null,locale);
	Assert.assertEquals(locale, geocoder.getLocale());
    }
    
    @Test
    public void ConstructorShouldSetTheLocaleAndUrl(){
	Locale locale =Locale.CANADA;
	String url = "http://www.gisgraphy.com/";
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null,locale,url);
	Assert.assertEquals(locale, geocoder.getLocale());
	Assert.assertEquals(url, geocoder.getBaseUrl());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptMalformedUrl(){
	Locale locale =Locale.CANADA;
	String url = "htp://foo";
	new GisgraphyGeocoder(null,locale,url);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptNullLocale(){
	String url = "http://foo";
	new GisgraphyGeocoder(null,null,url);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptNullLocale_bis(){
	Locale locale =null;
	new GisgraphyGeocoder(null,locale);
    }
    
    @Test
    public void ConstructorShouldSetTheDefaultLocale(){
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	Assert.assertEquals(Locale.getDefault(),geocoder.getLocale());
    }
    
    @Test
    public void getFromLocationName() throws IOException{
    	String baseUrl = "http://test.gisgraphy.com";
    	String addressToGeocode = "address to geocode";
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	List<com.gisgraphy.addressparser.Address> results = createGisgraphyAddresses(10);
    	AddressResultsDto addressResultsDto = new AddressResultsDto(results,10L);
    	
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	EasyMock.expect(restClientMock.getWebServiceUrl()).andReturn(baseUrl);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.ADDRESS_PARAMETER_NAME	, addressToGeocode);
    	params.put(GisgraphyGeocoder.COUNTRY_PARAMETER_NAME	, Locale.getDefault().getCountry());
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.GEOCODING_URI, AddressResultsDto.class, params)).andReturn(addressResultsDto);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	List<Address> AndroidAddress = geocoder.getFromLocationName(addressToGeocode, 6);
    	Assert.assertEquals("the max parameter should be taken into account",numberOfResults, AndroidAddress.size());
    }
    
    @Test
    public void isPresent(){
    	Assert.assertTrue(new GisgraphyGeocoder(null).isPresent());
    }
    
    private List<com.gisgraphy.addressparser.Address> createGisgraphyAddresses(int numberOfResults){
    	List<com.gisgraphy.addressparser.Address> results = new ArrayList<com.gisgraphy.addressparser.Address>();
    	for (int i = 0;i<numberOfResults;i++){
    		com.gisgraphy.addressparser.Address address = new com.gisgraphy.addressparser.Address();
    		address.setLat(new Double(i));
    		address.setLng(new Double(i*-1));
    		address.setCity("city"+i);
    		address.setStreetName("street"+i);
    		results.add(address);
    	}
    	return results;
    }
    

}
