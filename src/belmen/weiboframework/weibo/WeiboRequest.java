package belmen.weiboframework.weibo;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import belmen.weiboframework.http.HttpMethod;
import belmen.weiboframework.util.Codec;

public class WeiboRequest {

	public static final String TAG = WeiboRequest.class.getSimpleName();
	
	private HttpMethod method;
	private String url;
	private Map<String, String> headers = new LinkedHashMap<String, String>();
	private Map<String, String> queryParams = new LinkedHashMap<String, String>();
	private Map<String, String> postParams = new LinkedHashMap<String, String>();
	private Map<String, File> files = new LinkedHashMap<String, File>();
	
	public WeiboRequest(HttpMethod method, String url) {
		this.method = method;
		this.url = url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}
	
	public void appendUrl(String append) {
		this.url += append;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}
	
	public Map<String, String> getQueryParams() {
		return queryParams;
	}
	
	public void addQueryParameter(String name, String value) {
		queryParams.put(name, value);
	}

	public Map<String, String> getPostParams() {
		return postParams;
	}
	
	public void addPostParameter(String name, String value) {
		postParams.put(name, value);
	}

	public Map<String, File> getFiles() {
		return files;
	}
	
	public void addFile(String name, File file) {
		files.put(name, file);
	}
	
	public boolean hasFile() {
		return files.size() != 0;
	}
	
	public String getCompleteUrl() {
		StringBuilder sb = new StringBuilder(url);
		sb.append(url.contains("?") ? "&" : "?");
		Iterator<Entry<String, String>> iter = queryParams.entrySet().iterator();
		Entry<String, String> entry;
		String name, value;
		while(iter.hasNext()) {
			entry = iter.next();
			name = entry.getKey();
			value = entry.getValue();
			sb.append(Codec.urlEncode(name)).append("=").append(Codec.urlEncode(value));
		}
		return sb.toString();
	}
	
	public static WeiboRequest newGetRequest(String url) {
		return new WeiboRequest(HttpMethod.GET, url);
	}
	
	public static WeiboRequest newPostRequest(String url){
		return new WeiboRequest(HttpMethod.POST, url);
	}
}
