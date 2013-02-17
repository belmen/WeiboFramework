package belmen.weiboframework.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.os.Build;
import belmen.weiboframework.exception.HttpException;
import belmen.weiboframework.util.Codec;
import belmen.weiboframework.util.Logger;

public class HttpKit {
	
	public static final String TAG = HttpKit.class.getSimpleName();
	
	private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
	
	private static int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private static int mReadTimeout = DEFAULT_READ_TIMEOUT;
	
	static {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
		     System.setProperty("http.keepAlive", "false");
		}
		setupHttpsConnection();
	}
	
	private static void setupHttpsConnection() {
		TrustManager trustAllCerts = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { trustAllCerts },
					new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		}
	}
	
	public static final HostnameVerifier IGNORE_HOSTNAME_VERIFIER = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

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
		return execute(url, null, null, null);
	}
	
	public static HttpResponse get(String url, Map<String, String> headers) throws HttpException {
		return execute(url, headers, null, null);
	}
	
	public static HttpResponse post(String url, Map<String, String> formEntity) throws HttpException {
		return execute(url, null, formEntity, null);
	}
	
	public static HttpResponse post(String url, Map<String, String> headers,
			Map<String, String> formEntity) throws HttpException {
		return execute(url, headers, formEntity, null);
	}
	
	public static HttpResponse post(String url, Map<String, String> headers,
			Map<String, String> formEntity, Map<String, File> files) throws HttpException {
		return execute(url, headers, formEntity, files);
	}
	
	private static HttpResponse execute(String url, Map<String, String> headers,
			Map<String, String> formEntity, Map<String, File> files) throws HttpException {
		HttpURLConnection connection = null;
		try {
			HttpResponse response = null;
			connection = createConnection(url, headers);
			if(connection != null) {
				if(formEntity != null) {
					writeFormEntity(connection, formEntity);
				}
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
		connection.setHostnameVerifier(IGNORE_HOSTNAME_VERIFIER);
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
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while((line = reader.readLine()) != null) {
		    	sb.append(line);
		    }
		    return sb.toString();
		} finally {
			if(reader != null) {
				reader.close();
			}
		}
	}
	
	private static void writeFormEntity(HttpURLConnection connection,
			Map<String, String> formEntity) throws IOException {
		if(connection == null || formEntity == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = formEntity.entrySet().iterator();
		Entry<String, String> entry;
		while(iter.hasNext()) {
			entry = iter.next();
			sb.append("&").append(Codec.urlEncode(entry.getKey()))
			.append("=").append(Codec.urlEncode(entry.getValue()));
		}
		String body = sb.toString().substring(1);
		
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		OutputStream os = connection.getOutputStream();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(body);
			writer.flush();
		} finally {
			if(writer != null) {
				writer.close();
			}
		}
	}
}
