package belmen.weiboframework.api;

import java.util.Date;

import belmen.weiboframework.exception.ApiException;


public abstract class OAuth20Client extends ApiClient {

	public static final String TAG = OAuth20Client.class.getSimpleName();
	
	public static final int SIGN_IN_HEADER = 0;
	public static final int SIGN_IN_QUERY_STRING = 1;
	
	private final String mClientId;
	private final String mClientSecret;
	private int mSignStrategy = SIGN_IN_QUERY_STRING;
	private String mRedirectUri = null;
	private String mToken = null;
	private String mRefreshToken = null;
	private Date mExpireDate = null;
	
	public OAuth20Client(String clientId, String clientSecret) {
		this.mClientId = clientId;
		this.mClientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return mRedirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.mRedirectUri = redirectUri;
	}

	public String getClientId() {
		return mClientId;
	}

	public String getClientSecret() {
		return mClientSecret;
	}
	
	public String getRefreshToken() {
		return mRefreshToken;
	}
	
	public void setToken(String token, int expiresIn) {
		setToken(token, expiresIn, null);
	}
	
	public void setToken(String token, int expiresIn, String refreshToken) {
		this.mToken = token;
		this.mRefreshToken = refreshToken;
		this.mExpireDate = new Date(System.currentTimeMillis() + (long) expiresIn * 1000);
	}

	public Date getExpireDate() {
		return mExpireDate;
	}

	public int getSignStrategy() {
		return mSignStrategy;
	}
	
	public void setSignStrategy(int signStrategy) {
		if(signStrategy == SIGN_IN_HEADER || signStrategy == SIGN_IN_QUERY_STRING) {
			this.mSignStrategy = signStrategy;
		}
	}

	public abstract String getAuthorizeUrl();
	
	public abstract void retrieveAccessToken(String code) throws ApiException;
	
	public boolean refreshToken() throws ApiException {
		return false;
	}
	
	@Override
	protected void signRequest(ApiRequest request) throws ApiException {
		if(mToken == null) {
			return;
		}
		if(refreshToken()) {
			
		} else {
			
		}
		switch (mSignStrategy) {
		case SIGN_IN_HEADER:
			request.addHeader("Authorization", "OAuth2 " + mToken);
			break;
		case SIGN_IN_QUERY_STRING:
			request.addQueryParameter("access_token", mToken);
			break;
		}
	}
}
