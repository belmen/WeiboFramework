package belmen.weiboframework.oauth;

import java.util.LinkedHashMap;
import java.util.Map;

import belmen.weiboframework.api.ApiRequest;
import belmen.weiboframework.http.HttpMethod;

public class OAuthRequest extends ApiRequest {

	private Map<String, String> oauthParams = new LinkedHashMap<String, String>();
	private String username = null;
	private String password = null;
	private String callback = null;
	private String verifier = null;
	
	public OAuthRequest(HttpMethod method, String url) {
		super(method, url);
	}
	
	public Map<String, String> getOauthParams() {
		return oauthParams;
	}

	public void addOAuthParameter(String name, String value) {
		oauthParams.put(name, value);
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
	
	public static OAuthRequest newGetRequest(String url, String... params) {
		OAuthRequest request = new OAuthRequest(HttpMethod.GET, url);
		String[] pair;
		for(String param : params) {
			pair = param.split("=");
			if(pair.length >= 2) {
				request.addQueryParameter(pair[0], pair[1]);
			}
		}
		return request;
	}
	
	public static OAuthRequest newPostRequest(String url, String... params){
		OAuthRequest request = new OAuthRequest(HttpMethod.POST, url);
		String[] pair;
		for(String param : params) {
			pair = param.split("=");
			if(pair.length >= 2) {
				request.addPostParameter(pair[0], pair[1]);
			}
		}
		return request;
	}
}
