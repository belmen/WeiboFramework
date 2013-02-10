package belmen.weiboframework.oauth;

import java.util.LinkedHashMap;
import java.util.Map;

import belmen.weiboframework.http.HttpMethod;
import belmen.weiboframework.weibo.ApiRequest;

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
	
	public static OAuthRequest newGetRequest(String url) {
		return new OAuthRequest(HttpMethod.GET, url);
	}
	
	public static OAuthRequest newPostRequest(String url){
		return new OAuthRequest(HttpMethod.POST, url);
	}
}
