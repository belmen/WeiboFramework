package belmen.weiboframework.tencent;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import belmen.weiboframework.api.ApiRequest;
import belmen.weiboframework.api.OAuth20Client;
import belmen.weiboframework.exception.ApiException;

public class TencentWeibo extends OAuth20Client {
	
	private static final String TOKEN_REGEX = "access_token=([^&]+)&expires_in=([^&]+)&refresh_token=([^&]+)"; 

	public TencentWeibo(String clientId, String clientSecret) {
		super(clientId, clientSecret);
	}

	@Override
	public String getAuthorizeUrl() {
		return String.format("https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s", getClientId(), getRedirectUri());
	}

	@Override
	public void retrieveAccessToken(String code) throws ApiException {
		ApiRequest request = ApiRequest.newPostRequest("https://open.t.qq.com/cgi-bin/oauth2/access_token");
		request.addPostParameter("client_id", getClientId())
		.addPostParameter("client_secret", getClientSecret())
		.addPostParameter("grant_type", "authorization_code")
		.addPostParameter("code", code)
		.addPostParameter("redirect_uri", getRedirectUri());
		extractToken(sendRequest(request).getContent());
	}

	private void extractToken(String response) throws ApiException {
		Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
		String token = null, expiresIn = null, refreshToken = null;
		if(matcher.find() && matcher.groupCount() >= 2) {
			token = matcher.group(1);
			expiresIn = matcher.group(2);
			if(matcher.groupCount() >= 3) {
				refreshToken = matcher.group(3);
			}
			Date expireDate = new Date(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000);
			setToken(token, expireDate, refreshToken);
		}
	}
}
