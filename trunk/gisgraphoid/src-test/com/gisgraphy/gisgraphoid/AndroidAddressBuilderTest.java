package com.gisgraphy.gisgraphoid;

import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.domain.valueobject.CountriesStaticData;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AndroidAddressBuilderTest {
    
    
    @Test
   public void  transformGisgraphyAdressesToAndroidAddresses(){
	Locale locale = new Locale("FR-fr", "FR");
	       AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	       List<android.location.Address> androidAddresses = builder.transformGisgraphyAdressesToAndroidAddresses(null);
	       Assert.assertNotNull(androidAddresses);
	       Assert.assertEquals(0, androidAddresses.size());
    }
    
   @Test
   public void constructorShouldSetTheLocale(){
       Locale locale = new Locale("FR-fr", "FR");
       AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
       Assert.assertEquals(locale,builder.getLocale());
   }
    
    @Test
    public void transformGisgraphyAdressToAndroidAddress_StreetAndCity(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Address gisgraphyAddress = new Address();
	gisgraphyAddress.setCity("city");
	gisgraphyAddress.setLat(1D);
	gisgraphyAddress.setLng(2D);
	gisgraphyAddress.setStreetName("streetName");
	gisgraphyAddress.setState("state");
	gisgraphyAddress.setZipCode("75000");
	android.location.Address androidAddress = builder.transformGisgraphyAdressToAndroidAddress(gisgraphyAddress);
	Assert.assertEquals(gisgraphyAddress.getCity(), androidAddress.getLocality());
	Assert.assertEquals(locale.getCountry(), androidAddress.getCountryCode());
	Assert.assertEquals(CountriesStaticData.getCountryNameFromCountryCode(locale.getCountry()), androidAddress.getCountryName());
	Assert.assertEquals(locale, androidAddress.getLocale());
	Assert.assertEquals(gisgraphyAddress.getLat(), androidAddress.getLatitude());
	Assert.assertEquals(gisgraphyAddress.getLng(), androidAddress.getLongitude());
	Assert.assertEquals(gisgraphyAddress.getState(), androidAddress.getAdminArea());
	Assert.assertEquals(gisgraphyAddress.getZipCode(), androidAddress.getPostalCode());
	Assert.assertEquals(builder.buildAddressUrl(gisgraphyAddress), androidAddress.getUrl());
	Assert.assertEquals("featureName should be the street name when streetname is not null",gisgraphyAddress.getStreetName(), androidAddress.getFeatureName());
	Assert.assertEquals("line 0 should be set to streetname if streetname is not null",gisgraphyAddress.getStreetName(),androidAddress.getAddressLine(0));
	Assert.assertEquals("line 1 should be set to zip/city/state if streetname is not null","75000 city state",androidAddress.getAddressLine(1));
    }
    
    @Test
    public void transformGisgraphyAdressToAndroidAddress_StreetWithoutcity(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Address gisgraphyAddress = new Address();
	gisgraphyAddress.setLat(1D);
	gisgraphyAddress.setLng(2D);
	gisgraphyAddress.setStreetName("streetName");
	gisgraphyAddress.setState("state");
	gisgraphyAddress.setZipCode("75000");
	android.location.Address androidAddress = builder.transformGisgraphyAdressToAndroidAddress(gisgraphyAddress);
	Assert.assertEquals(gisgraphyAddress.getCity(), androidAddress.getLocality());
	Assert.assertEquals(locale.getCountry(), androidAddress.getCountryCode());
	Assert.assertEquals(CountriesStaticData.getCountryNameFromCountryCode(locale.getCountry()), androidAddress.getCountryName());
	Assert.assertEquals(locale, androidAddress.getLocale());
	Assert.assertEquals(gisgraphyAddress.getLat(), androidAddress.getLatitude());
	Assert.assertEquals(gisgraphyAddress.getLng(), androidAddress.getLongitude());
	Assert.assertEquals(gisgraphyAddress.getState(), androidAddress.getAdminArea());
	Assert.assertEquals(gisgraphyAddress.getZipCode(), androidAddress.getPostalCode());
	Assert.assertEquals(builder.buildAddressUrl(gisgraphyAddress), androidAddress.getUrl());
	Assert.assertEquals("featureName should be the street name when streetname is not null",gisgraphyAddress.getStreetName(), androidAddress.getFeatureName());
	Assert.assertEquals("line 0 should be set to streetname if streetname is not null",gisgraphyAddress.getStreetName(),androidAddress.getAddressLine(0));
	Assert.assertEquals("line 1 should be set to zip/city/state if streetname is not null","75000 state",androidAddress.getAddressLine(1));
    }
    
    @Test
    public void transformGisgraphyAdressToAndroidAddress_CityOnly(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Address gisgraphyAddress = new Address();
	gisgraphyAddress.setCity("city");
	gisgraphyAddress.setLat(1D);
	gisgraphyAddress.setLng(2D);
	gisgraphyAddress.setState("state");
	gisgraphyAddress.setZipCode("75000");
	android.location.Address androidAddress = builder.transformGisgraphyAdressToAndroidAddress(gisgraphyAddress);
	Assert.assertEquals(gisgraphyAddress.getCity(), androidAddress.getLocality());
	Assert.assertEquals(locale.getCountry(), androidAddress.getCountryCode());
	Assert.assertEquals(CountriesStaticData.getCountryNameFromCountryCode(locale.getCountry()), androidAddress.getCountryName());
	Assert.assertEquals(locale, androidAddress.getLocale());
	Assert.assertEquals(gisgraphyAddress.getLat(), androidAddress.getLatitude());
	Assert.assertEquals(gisgraphyAddress.getLng(), androidAddress.getLongitude());
	Assert.assertEquals(gisgraphyAddress.getState(), androidAddress.getAdminArea());
	Assert.assertEquals(gisgraphyAddress.getZipCode(), androidAddress.getPostalCode());
	Assert.assertEquals(builder.buildAddressUrl(gisgraphyAddress), androidAddress.getUrl());
	Assert.assertEquals("featureName should be the city name when streetname is null",gisgraphyAddress.getCity(), androidAddress.getFeatureName());
	Assert.assertEquals("line 0 should be set to zip/city/state if streetname is not null","75000 city state",androidAddress.getAddressLine(0));
	Assert.assertNull("line 1 should be null if streetname is not null",androidAddress.getAddressLine(1));
	
    }
    
    @Test
    public void buildAddressUrl(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Address gisgraphyAddress = new Address();
	long id = 3L;
	gisgraphyAddress.setId(id);
	Assert.assertEquals(AndroidAddressBuilder.STREET_BASE_URL+id,builder.buildAddressUrl(gisgraphyAddress));
    }
    
    @Test
    public void buildAddressUrlWithAddresWithNullId(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Address gisgraphyAddress = new Address();
	gisgraphyAddress.setId(null);
	Assert.assertNull(builder.buildAddressUrl(null));
    }
    
    @Test
    public void buildAddressUrlWithNullAdress(){
	Locale locale = new Locale("FR-fr", "FR");
	AndroidAddressBuilder builder = new AndroidAddressBuilder(locale);
	Assert.assertNull(builder.buildAddressUrl(null));
    }

}
