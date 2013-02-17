package belmen.weiboframework.sina;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.api.ApiRequest;
import belmen.weiboframework.api.OAuth20Client;
import belmen.weiboframework.exception.ApiException;
import belmen.weiboframework.http.HttpResponse;
import belmen.weiboframework.util.Logger;

public class SinaWeibo extends OAuth20Client {

	public static final String TAG = SinaWeibo.class.getSimpleName();
	
	public SinaWeibo(String clientId, String clientSecret) {
		super(clientId, clientSecret);
	}

	public List<Status> getHomeTimeline() throws ApiException {
		ApiRequest request = ApiRequest.newGetRequest("https://api.weibo.com/2/statuses/home_timeline.json");
		return BeanFactory.parseStatusList(sendRequest(request).getContent(), "statuses");
	}
	
	@Override
	public String getAuthorizeUrl() {
		return String.format("https://open.weibo.cn/oauth2/authorize?client_id=%s&redirect_uri=%s&display=mobile",
				getClientId(), getRedirectUri());
	}
	
	@Override
	public void retrieveAccessToken(String code) throws ApiException {
		ApiRequest request = ApiRequest.newPostRequest("https://api.weibo.com/oauth2/access_token");
		request.addPostParameter("client_id", getClientId())
		.addPostParameter("client_secret", getClientSecret())
		.addPostParameter("grant_type", "authorization_code")
		.addPostParameter("code", code)
		.addPostParameter("redirect_uri", getRedirectUri());
		extractToken(sendRequest(request).getContent());
	}

	private void extractToken(String response) throws ApiException {
		try {
			JSONObject json = new JSONObject(response);
			String token = json.optString("access_token");
			int expiresIn = json.optInt("expires_in");
			Date expireDate = new Date(System.currentTimeMillis() + (long) expiresIn * 1000);
			setToken(token, expireDate);
		} catch (JSONException e) {
			throw new ApiException("Response cannot be parsed to Json Object.\nContent: " + response, e);
		}
	}

	@Override
	protected ApiException parseErrorResponse(ApiRequest request,
			HttpResponse response) {
		try {
			JSONObject json = new JSONObject(response.getContent());
			String code = json.optString("error_code");
			String error = json.optString("error");
			return new ApiException(code + ": " + error);
		} catch (JSONException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return super.parseErrorResponse(request, response);
	}
}
