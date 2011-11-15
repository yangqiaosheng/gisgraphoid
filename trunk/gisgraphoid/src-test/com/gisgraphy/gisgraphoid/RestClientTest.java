package com.gisgraphy.gisgraphoid;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

	@Test
	public void ConstructorShouldSetUrlAndAddEndingSlash() {
		String webServiceUrl = "http://www.perdu.com";
		RestClient resClient = new RestClient(webServiceUrl);
		Assert.assertEquals(webServiceUrl+"/", resClient.getWebServiceUrl());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ConstructorWithNullUrl() {
		 new RestClient(null);
	}
	
	@Test
	public void getWithNullURI(){
		String webServiceUrl = "http://www.perdu.com";
		RestClient client = new RestClient(webServiceUrl);
		Object o = client.get(null, Object.class, new HashMap<String, String>());
		System.out.println(o);
	}
	
	@Test
	public void getWithNullUrl(){
		String webServiceUrl = "http://www.perdu.com";
		RestClient client = new RestClient(webServiceUrl);
		client.get(null, Object.class, new HashMap<String, String>());
	}

}
