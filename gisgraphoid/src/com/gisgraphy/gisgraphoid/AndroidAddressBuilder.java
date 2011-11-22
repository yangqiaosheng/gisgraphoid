package com.gisgraphy.gisgraphoid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;

import android.location.Address;

/**
 *
 *@author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class AndroidAddressBuilder {
	
	protected static final String STREET_BASE_URL = "http://services.gisgraphy.com/public/displaystreet.html?gid=";
	private Locale locale ;
	
	public AndroidAddressBuilder(Locale locale) {
		this.locale = locale;
	}
	
	public AndroidAddressBuilder() {
		this.locale = Locale.getDefault();
	}

	/**
	 * @param gisgraphyAddresses the gisgraphy address list
	 * @return a list of android address or an empty list.
	 */
	public List<Address> transformGisgraphyAdressesToAndroidAddresses(List<com.gisgraphy.addressparser.Address> gisgraphyAddresses) {
		List<Address> androidAddresses = new ArrayList<Address>();
		if (gisgraphyAddresses != null) {
			for (com.gisgraphy.addressparser.Address address : gisgraphyAddresses) {
				Address androidAddress = transformGisgraphyAdressToAndroidAddress(address);
				if (androidAddress != null) {
					androidAddresses.add(androidAddress);
				}
			}
		}
		return androidAddresses;
	}

	/**
	 * @param gisgraphyAddress a gisgraphy address
	 * @return an Android Address
	 */
	public Address transformGisgraphyAdressToAndroidAddress(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		Address androidAddress = new Address(locale);
		String countryCode = locale.getCountry();
		androidAddress.setCountryCode(countryCode);
		androidAddress.setCountryName(CountriesData.getCountryNameFromCountryCode(countryCode));
		androidAddress.setLatitude(gisgraphyAddress.getLat());
		androidAddress.setLongitude(gisgraphyAddress.getLng());
		if (gisgraphyAddress.getStreetName() != null) {
			androidAddress.setFeatureName(gisgraphyAddress.getStreetName());
			androidAddress.setAddressLine(0, ComputeStreetLine(gisgraphyAddress));
			androidAddress.setAddressLine(1, computeCityLine(gisgraphyAddress));
		} else if (gisgraphyAddress.getCity() != null) {
			androidAddress.setFeatureName(gisgraphyAddress.getCity());
			androidAddress.setAddressLine(0, computeCityLine(gisgraphyAddress));
		}
		androidAddress.setLocality(gisgraphyAddress.getCity());
		androidAddress.setAdminArea(gisgraphyAddress.getState());
		androidAddress.setPostalCode(gisgraphyAddress.getZipCode());
		androidAddress.setUrl(buildAddressUrl(gisgraphyAddress));
		return androidAddress;
	}

	private String ComputeStreetLine(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		if (gisgraphyAddress.getStreetName() != null) {
			return gisgraphyAddress.getStreetName();
		} else {
			return null;
		}
	}

	private String computeCityLine(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		StringBuffer sb = new StringBuffer();
		if (gisgraphyAddress.getZipCode() != null) {
			sb.append(gisgraphyAddress.getZipCode()).append(" ");
		}
		if (gisgraphyAddress.getCity() != null) {
			sb.append(gisgraphyAddress.getCity()).append(" ");
		}
		if (gisgraphyAddress.getState() != null) {
			sb.append(gisgraphyAddress.getState());
		}
		String line1 = sb.toString();
		if (line1.trim().length() != 0) {
			return line1;
		} else {
			return null;
		}
	}

	protected String buildAddressUrl(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		if (gisgraphyAddress != null && gisgraphyAddress.getId() != null) {
			return STREET_BASE_URL + gisgraphyAddress.getId();
		} else {
			return null;
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}


}
