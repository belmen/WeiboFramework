package belmen.weiboframework.weibo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.exception.ApiException;
import belmen.weiboframework.http.HttpMethod;
import belmen.weiboframework.http.HttpResponse;
import belmen.weiboframework.oauth.OAuthClient;
import belmen.weiboframework.oauth.OAuthRequest;
import belmen.weiboframework.util.Logger;

public class Fanfou extends OAuthClient {

	private static final String COMSUMER_KEY = "6f45ffcaea4ea8b0d1aedad2d2e6518f";
	private static final String COMSUMER_SECRET = "0ae1b550f6f6a0b6a3522d4317425d47";
	private static final String TOKEN_REGEX = "oauth_token=([^&]+)&oauth_token_secret=([^&]+)"; 
	
	public Fanfou() {
		super(COMSUMER_KEY, COMSUMER_SECRET);
	}
	
	public String getPublicTimeline() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/statuses/public_timeline.json");
		HttpResponse response = sendRequest(request);
		try {
			JSONArray json = new JSONArray(response.getContent());
			return json.toString(2);
		} catch (JSONException e) {
			throw new ApiException("Exception when parsing json", e);
		}
	}

	@Override
	public void retrieveRequestToken() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://fanfou.com/oauth/request_token");
		HttpResponse response = sendRequest(request);
		extractToken(response.getContent());
	}

	@Override
	public void retrieveAccessToken(String verifier) throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://fanfou.com/oauth/access_token");
		if(verifier != null) {
			request.setVerifier(verifier);
		}
		HttpResponse response = sendRequest(request);
		extractToken(response.getContent());
	}

	@Override
	public void retrieveAccessToken(String username, String password) throws ApiException {
		OAuthRequest request = new OAuthRequest(HttpMethod.GET, "http://fanfou.com/oauth/access_token");
		request.setCredentials(username, password);
		HttpResponse response = sendRequest(request);
		extractToken(response.getContent());
	}

	private void extractToken(String response) {
		Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
		String token = null, secret = null;
		if(matcher.find() && matcher.groupCount() >= 2) {
			token = matcher.group(1);
			secret = matcher.group(2);
		}
		setToken(token, secret);
	}

	@Override
	protected ApiException parseErrorResponse(ApiRequest request,
			HttpResponse response) {
		try {
			JSONObject json = new JSONObject(response.getContent());
			return new ApiException(json.optString("error"));
		} catch (JSONException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return super.parseErrorResponse(request, response);
	}
}
