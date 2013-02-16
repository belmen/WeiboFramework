package belmen.weiboframework.fanfou;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.api.ApiRequest;
import belmen.weiboframework.exception.ApiException;
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
		setCallbackUrl("belmen.anwei.oauth");
	}
	
	public List<Status> getHomeTimeline() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/statuses/home_timeline.json");
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseStatusList(response.getContent());
	}
	
	public List<Status> getPublicTimeline() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/statuses/public_timeline.json");
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseStatusList(response.getContent());
	}
	
	public List<String> getFriendIds() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/friends/ids.json");
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseIdList(response.getContent());
	}
	
	public List<String> getFollowerIds() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/followers/ids.json");
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseIdList(response.getContent());
	}
	
	public User showUser(String id) throws ApiException {
		return showUser(id, false, false);
	}
	
	public User showUser(String id, boolean lite, boolean html) throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/users/show.json");
		request.addQueryParameter("id", id);
		if(lite) {
			request.addQueryParameter("mode", "lite");
		}
		if(html) {
			request.addQueryParameter("format", "html");
		}
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseUser(response.getContent());
	}
	
	public Status showStatus(String id) throws ApiException {
		return showStatus(id, false, false);
	}
	
	public Status showStatus(String id, boolean lite, boolean html) throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/statuses/show.json");
		request.addQueryParameter("id", id);
		if(lite) {
			request.addQueryParameter("mode", "lite");
		}
		if(html) {
			request.addQueryParameter("format", "html");
		}
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseStatus(response.getContent());
	}
	
	public boolean existsFriendship(String userA, String userB) throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://api.fanfou.com/friendships/exists.json");
		request.addQueryParameter("user_a", userA);
		request.addQueryParameter("user_b", userB);
		HttpResponse response = sendRequest(request);
		return Boolean.parseBoolean(response.getContent());
	}
	
	public Status updateStatus(String status) throws ApiException {
		OAuthRequest request = OAuthRequest.newPostRequest("http://api.fanfou.com/statuses/update.json");
		request.addPostParameter("status", status);
		HttpResponse response = sendRequest(request);
		return BeanFactory.parseStatus(response.getContent());
	}
	
	@Override
	public void retrieveRequestToken() throws ApiException {
		OAuthRequest request = OAuthRequest.newGetRequest("http://fanfou.com/oauth/request_token");
		HttpResponse response = sendRequest(request);
		extractToken(response.getContent());
	}

	@Override
	public String getAuthorizeUrl() {
		String token = getToken();
		String callback = getCallbackUrl();
		if(token != null) {
			return String.format("http://m.fanfou.com/oauth/authorize?oauth_token=%s&oauth_callback=%s",
					token, callback);
		}
		return null;
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
		OAuthRequest request = OAuthRequest.newGetRequest("http://fanfou.com/oauth/access_token");
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
