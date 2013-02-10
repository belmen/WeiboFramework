package belmen.weiboframework.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import belmen.weiboframework.exception.HttpException;

public class HttpKit {
	
	public static final String TAG = HttpKit.class.getSimpleName();
	
	private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
	
	private static int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private static int mReadTimeout = DEFAULT_READ_TIMEOUT;

	public static int getConnectTimeout() {
		return mConnectTimeout;
	}

	public static void setConnectTimeout(int connectTimeout) {
		HttpKit.mConnectTimeout = connectTimeout;
	}

	public static int getReadTimeout() {
		return mReadTimeout;
	}

	public static void setReadTimeout(int readTimeout) {
		HttpKit.mReadTimeout = readTimeout;
	}

	public static HttpResponse get(String url) throws HttpException {
		return get(url, null);
	}
	
	public static HttpResponse get(String url, Map<String, String> headers) throws HttpException {
		HttpURLConnection connection = null;
		try {
			HttpResponse response = null;
			connection = createConnection(url, headers);
			if(connection != null) {
				connection.connect();
				int statusCode = connection.getResponseCode();
				Map<String, List<String>> headerFields = connection.getHeaderFields();
				String content = readResponseContent(connection);
				response = new HttpResponse(statusCode, content, headerFields);
			}
			return response;
		} catch(SocketTimeoutException ste) {
			throw new HttpException("Timeout when connecting: " + url, ste);
		} catch (IOException e) {
			throw new HttpException("Exception when connecting: " + url, e);
		} finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}
	
	private static HttpURLConnection createConnection(String url, Map<String, String> headers)
			throws HttpException {
		if(url == null) {
			return null;
		}
		try {
			HttpURLConnection connection = null;
			if(url.startsWith("http://")) {	// 建立Http连接
				connection = createHttpConnection(url);
			} else if(url.startsWith("https://")) {	// 建立Https连接
				connection = createHttpsConnection(url);
			}
			if(headers != null) {	// 添加头部
				Iterator<Entry<String, String>> iter = headers.entrySet().iterator();
				Entry<String, String> entry;
				String name, value;
				while(iter.hasNext()) {
					entry = iter.next();
					name = entry.getKey();
					value = entry.getValue();
					connection.setRequestProperty(name, value);
				}
			}
			return connection;
		} catch (IOException e) {
			throw new HttpException("Exception when creating connection: " + url, e);
		}
	}
	
	private static HttpURLConnection createHttpConnection(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(mConnectTimeout);
		connection.setReadTimeout(mReadTimeout);
		return connection;
	}
	
	private static HttpsURLConnection createHttpsConnection(String url) throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection) createHttpConnection(url);
		
		return connection;
	}
	
	private static String readResponseContent(HttpURLConnection connection) throws HttpException {
		if(connection == null) {
			return null;
		}
		InputStream is = null;
		try {
			is = connection.getInputStream();
		} catch (IOException e) {
			is = connection.getErrorStream();
		}
		try {
			return readStream(is);
		} catch (SocketTimeoutException ste) {
			throw new HttpException("Timeout when reading response: " + connection.getURL().toString(), ste);
		} catch (IOException e) {
			throw new HttpException("Exception when reading response", e);
		}
	}
	
	private static String readStream(InputStream is) throws IOException {
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while((line = reader.readLine()) != null) {
		    	sb.append(line);
		    }
		    return sb.toString();
		} finally {
			if(is != null) {
				is.close();
			}
		}
	}
}
