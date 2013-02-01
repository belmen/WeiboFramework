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

	private HttpMethod mMethod;
	private String mUrl;
	private List<Header> mHeaders = new ArrayList<Header>();
	private List<NameValuePair> mQueryParams = new ArrayList<NameValuePair>();
	private List<NameValuePair> mPostParams = new ArrayList<NameValuePair>();
	private List<NameValuePair> mOAuthParams = new ArrayList<NameValuePair>();
	private String mFileName = null;
	private File mFile = null;
	
	public OAuthRequest(HttpMethod method, String url) {
		mMethod = method;
		mUrl = url;
	}
	
	public HttpMethod getMethod() {
		return mMethod;
	}
	
	public String getMethodName() {
		return mMethod.name();
	}

	public String getUrl() {
		return mUrl;
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
		mUrl += append;
	}

	public List<NameValuePair> getQueryStringParams() {
		return mQueryParams;
	}

	public List<NameValuePair> getPostParams() {
		return mPostParams;
	}
	
	public List<NameValuePair> getOAuthParams() {
		return mOAuthParams;
	}
	
	public List<Header> getHeaders() {
		return mHeaders;
	}
	
	public void addHeader(String name, String value) {
		mHeaders.add(new BasicHeader(name, value));
	}
	
	public void addQueryParameter(String name, String value) {
		mQueryParams.add(new BasicNameValuePair(name, value));
	}
	
//	public void addQueryParameter(String name, int value) {
//		mQueryParams.add(new BasicNameValuePair(name, String.valueOf(value)));
//	}
	
	public void addPostParameter(String name, String value) {
		mPostParams.add(new BasicNameValuePair(name, value));
	}
	
//	public void addPostParameter(String name, int value) {
//		mPostParams.add(new BasicNameValuePair(name, String.valueOf(value)));
//	}

	public void addOAuthParameter(String name, String value) {
		mOAuthParams.add(new BasicNameValuePair(name, value));
	}
	
//	public void addParameter(String name, String value) {
//		switch(mMethod) {
//		case GET: mQueryParams.add(new BasicNameValuePair(name, value)); break;
//		case POST: mPostParams.add(new BasicNameValuePair(name, value)); break;
//		}
//	}

	public String getFileName() {
		return mFileName;
	}
	
	public File getFile() {
		return mFile;
	}
	
	public void setFile(String fileName, File file) {
		this.mFileName = fileName;
		this.mFile = file;
	}
	
	public boolean hasFile() {
		return mFileName != null && mFile != null;
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
