package belmen.weiboframework.weibo;

import java.util.LinkedHashMap;
import java.util.Map;

import belmen.weiboframework.http.HttpMethod;
import belmen.weiboframework.http.HttpRequest;

public class OAuthRequest extends HttpRequest {

//	private final HttpMethod method;
//	private String url;
//	private Map<String, String> headers = new LinkedHashMap<String, String>();
//	private Map<String, String> queryParams = new LinkedHashMap<String, String>();
//	private Map<String, String> postParams = new LinkedHashMap<String, String>();
//	private String fileName = null;
//	private File file = null;
	private Map<String, String> oauthParams = new LinkedHashMap<String, String>();
	private String username = null;
	private String password = null;
	private String callback = null;
	private String verifier = null;
	
	public OAuthRequest(HttpMethod method, String url) {
		super(method, url);
	}
	
//	public HttpMethod getMethod() {
//		return method;
//	}
//	
//	public String getMethodName() {
//		return method.name();
//	}
//
//	public String getUrl() {
//		return url;
//	}
	
//	public String getCompleteUrl() {
//		if(mQueryParams.isEmpty())
//			return mUrl;
//		
//		String queryString = toEncodedQueryString(mQueryParams);
//		String url = mUrl;
//		url += url.contains("?") ? "&" : "?";
//		return url + queryString;
//	}
	
//	public void appendUrl(String append) {
//		this.url += append;
//	}
//
//	public Map<String, String> getHeaders() {
//		return headers;
//	}
//
//	public Map<String, String> getQueryParams() {
//		return queryParams;
//	}
//
//	public Map<String, String> getPostParams() {
//		return postParams;
//	}

	public Map<String, String> getOauthParams() {
		return oauthParams;
	}

//	public void addHeader(String name, String value) {
//		headers.put(name, value);
//	}
//	
//	public void addQueryParameter(String name, String value) {
//		queryParams.put(name, value);
//	}
//	
//	public void addPostParameter(String name, String value) {
//		postParams.put(name, value);
//	}
	
	public void addOAuthParameter(String name, String value) {
		oauthParams.put(name, value);
	}
	
//	public String getFileName() {
//		return fileName;
//	}
//	
//	public File getFile() {
//		return file;
//	}
//	
//	public void setFile(String fileName, File file) {
//		this.fileName = fileName;
//		this.file = file;
//	}
//	
//	public boolean hasFile() {
//		return fileName != null && file != null;
//	}
	
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
