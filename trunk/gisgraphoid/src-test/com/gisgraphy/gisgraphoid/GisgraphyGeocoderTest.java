package com.gisgraphy.gisgraphoid;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

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
	Assert.assertEquals(changedUrl,geocoder.getUrl());
    }
    
    @Test
    public void defaultBaseUrl() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	Assert.assertEquals(GisgraphyGeocoder.DEFAULT_BASE_URL,geocoder.getUrl());
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
	Assert.assertEquals(url, geocoder.getUrl());
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

}
