package belmen.weiboframework.oauth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import belmen.weiboframework.http.HttpMethod;

public class OAuthRequest {

	private final HttpMethod method;
	private String url;
	private List<Header> headers = new ArrayList<Header>();
	private List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
	private List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	private String fileName = null;
	private File file = null;
	private String username = null;
	private String password = null;
	private String callback = null;
	private String verifier = null;
	
	public OAuthRequest(HttpMethod method, String url) {
		this.method = method;
		this.url = url;
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getMethodName() {
		return method.name();
	}

	public String getUrl() {
		return url;
	}
	
//	public String getCompleteUrl() {
//		if(mQueryParams.isEmpty())
//			return mUrl;
//		
//		String queryString = toEncodedQueryString(mQueryParams);
//		String url = mUrl;
//		url += url.contains("?") ? "&" : "?";
//		return url + queryString;
//	}
	
	public void appendUrl(String append) {
		this.url += append;
	}

	public List<NameValuePair> getQueryStringParams() {
		return queryParams;
	}

	public List<NameValuePair> getPostParams() {
		return postParams;
	}
	
	public List<Header> getHeaders() {
		return headers;
	}
	
	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}
	
	public void addQueryParameter(String name, String value) {
		queryParams.add(new BasicNameValuePair(name, value));
	}
	
//	public void addQueryParameter(String name, int value) {
//		mQueryParams.add(new BasicNameValuePair(name, String.valueOf(value)));
//	}
	
	public void addPostParameter(String name, String value) {
		postParams.add(new BasicNameValuePair(name, value));
	}
	
//	public void addPostParameter(String name, int value) {
//		mPostParams.add(new BasicNameValuePair(name, String.valueOf(value)));
//	}

//	public void addOAuthParameter(String name, String value) {
//		mOAuthParams.add(new BasicNameValuePair(name, value));
//	}
	
//	public void addParameter(String name, String value) {
//		switch(mMethod) {
//		case GET: mQueryParams.add(new BasicNameValuePair(name, value)); break;
//		case POST: mPostParams.add(new BasicNameValuePair(name, value)); break;
//		}
//	}

	public String getFileName() {
		return fileName;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(String fileName, File file) {
		this.fileName = fileName;
		this.file = file;
	}
	
	public boolean hasFile() {
		return fileName != null && file != null;
	}
	
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	
//	public String toEncodedQueryString(List<NameValuePair> params) {
//		StringBuilder builder = new StringBuilder();
//		for(NameValuePair param : params) {
//			builder.append('&')
//			.append(Codec.urlEncode(param.getName()) + "=" + Codec.urlEncode(param.getValue()));
//		}
//		return builder.toString().substring(1);
//	}
}
