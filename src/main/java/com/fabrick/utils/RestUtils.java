package com.fabrick.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

public class RestUtils {

	private RestUtils() {
	
	}
	
	public static URI createURIWithParameters(String baseUrl, Map<String, String> parameters) throws URISyntaxException {
        
		URI baseUri = new URI(baseUrl);

        String query = parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + urlEncode(entry.getValue()))
                .collect(Collectors.joining("&"));

        return new URI(baseUri.getScheme(), baseUri.getAuthority(), baseUri.getPath(), query, null);
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Errore durante la codifica URL", e);
        }
    }
}
