package io.oauth.testapp;

import io.oauth.server.api.*;
import io.oauth.server.model.*;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.util.*;

import java.net.*;
import java.io.UnsupportedEncodingException;

/**
 * Hello world!
 *
 */
public class App 
{
	private static AuthorizationApi oauthioAuth = new AuthorizationApi();
    private static ClientApi oauthioClient = new ClientApi();

	private static Client cli = null;
	private static List<Client> cliList = null;
	private static AuthCallback ac = null;
	private static TokenSet ts = null;
	private static TokenInfos ti = null;

    public static void main( String[] args )
    {
    	// Set the platform keys here
	    AuthorizationApi.initialize("69d089d8e5bba09c8b11e1b71bca5da2", "8eeaeffd86f1fd3a76f56121c3fcc478");

	    testClients();
	    testAuthorization();

    	displayResults();
    }

    // Authorization methods test
    private static void testAuthorization() {
    	try {
    		ac = oauthioAuth.authorize("a6e1bb570abc5887d96fe873c218fcd1", "1", "777", "email about_me", "http://localhost/", "gfhgh", "code");
    		URL url = new URL(ac.getCallbackUri());
    		Map<String, String> query = splitQuery(url);
    		if (query.containsKey("code")) {
    			System.out.println("code: " + query.get("code"));
    			ts = oauthioAuth.token("a6e1bb570abc5887d96fe873c218fcd1", "466e7ece0197e7c6fcbc585c5dae15a8", "authorization_code", null, query.get("code"), null);
    		}
    		if (ts != null) {
    			System.out.println("first token set: " + ts.toJson());
    			//ts = oauthioAuth.token("a6e1bb570abc5887d96fe873c218fcd1", "466e7ece0197e7c6fcbc585c5dae15a8", "refresh_token", "", "", ts.getRefreshToken());
    			//System.out.println("refreshed token set: " + ts.toJson());
    			//ts = oauthioAuth.token("a6e1bb570abc5887d96fe873c218fcd1", "466e7ece0197e7c6fcbc585c5dae15a8", "client_credentials", "lol", null, null);
    			//System.out.println("client credentials token set: " + ts.toJson());
    			ti = oauthioAuth.check(ts.getAccessToken());
    		}
    	} catch (Exception e) {
	    	System.err.println( e.toString() );
	    }
    }

    // Clients methods test
    private static void testClients() {
    	try {
	    	cli = oauthioClient.createClient("myAppName", "my new app's description", "http://localhost/", "43");
	    	cli = oauthioClient.updateClient(cli.getClientId(), "myUpdatedAppName", "my app desc", "http://localhost/", "42");
	    	oauthioClient.deleteClient(cli.getClientId());
	    	cliList = oauthioClient.getAllClients();
	    } catch (Exception e) {
	    	System.err.println( e.toString() );
	    }
    }

	public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    String query = url.getQuery();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}

    private static void displayResults() {
		if (cliList != null) {
    		System.out.println("Client List -----");
	    	for (Client c : cliList) {
	    		System.out.println(c.toString());
	    	}
	    }

	    if (cli != null) {
	    	System.out.println("Client -----");
    		System.out.println(cli.toString());
    	}

    	if (ac != null) {
    		System.out.println("AuthCallback ------");
    		System.out.println(ac.toString());
    	}

    	if (ts != null) {
    		System.out.println("TokenSet ------");
    		System.out.println(ts.toString());
    	}

    	if (ti != null) {
    		System.out.println("TokenInfos ------");
    		System.out.println(ti.toString());
    	}
    }
}
