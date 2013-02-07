package belmen.weiboframework.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;

import belmen.weiboframework.util.Logger;

public class HttpKit {
	
	public static final String TAG = HttpKit.class.getSimpleName();
	
	private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 30 * 1000;

	public static HttpResponse get(String url) {
		return get(url, null);
	}
	
	public static HttpResponse get(String url, List<Header> headers) {
		HttpResponse response = null;
		HttpURLConnection connection = null;
		try {
			connection = createConnection(url, headers);
			if(connection != null) {
				int statusCode = connection.getResponseCode();
				Map<String, List<String>> headerFields = connection.getHeaderFields();
				String content = readResponseContent(connection);
				response = new HttpResponse(statusCode, content, headerFields);
			}
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		} finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return response;
	}
	
	private static HttpURLConnection createConnection(String url, List<Header> headers) {
		if(url == null) {
			return null;
		}
		HttpURLConnection connection = null;
		try {
			if(url.startsWith("http://")) {	// 建立Http连接
				connection = createHttpConnection(url);
			} else if(url.startsWith("https://")) {	// 建立Https连接
				connection = createHttpsConnection(url);
			}
			if(headers != null) {	// 添加头部
				for(Header header : headers) {
					connection.setRequestProperty(header.getName(), header.getValue());
				}
			}
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return connection;
	}
	
	private static HttpURLConnection createHttpConnection(String url)
			throws MalformedURLException, IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		return connection;
	}
	
	private static HttpsURLConnection createHttpsConnection(String url)
			throws MalformedURLException, IOException, ClassCastException {
		HttpsURLConnection connection = (HttpsURLConnection) createHttpConnection(url);
		return connection;
	}
	
	private static String readResponseContent(HttpURLConnection connection) {
		if(connection == null) {
			return null;
		}
		InputStream is = null;
		try {
			is = connection.getInputStream();
		} catch (IOException e) {
			is = connection.getErrorStream();
		}
		return readStream(is);
	}
	
	private static String readStream(InputStream is) {
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while((line = reader.readLine()) != null) {
		    	sb.append(line);
		    }
		    return sb.toString();
		} catch (IOException e) {
			Logger.e(TAG, e.getMessage(), e);
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				Logger.i(TAG, e.getMessage(), e);
			}
		}
	    return null;
	}
}
