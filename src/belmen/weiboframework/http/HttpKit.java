package belmen.weiboframework.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.Header;

import belmen.weiboframework.util.Logger;

public class HttpKit {
	
	public static final String TAG = HttpKit.class.getSimpleName();

	public static String get(String url) {
		return get(url, null);
	}
	
	public static String get(String url, List<Header> headers) {
		HttpURLConnection urlConnection = null;
		String result = null;
		try {
			urlConnection = (HttpURLConnection) new URL(url).openConnection();
			if(headers != null) {
				for(Header header : headers) {
					urlConnection.setRequestProperty(header.getName(), header.getValue());
				}
			}
			InputStream is = urlConnection.getInputStream();
			result = readResponse(is);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		} finally {
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return result;
	}
	
	private static String readResponse(InputStream stream)
			throws IOException, UnsupportedEncodingException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while((line = reader.readLine()) != null) {
	    	sb.append(line);
	    }
	    return sb.toString();
	}
}
