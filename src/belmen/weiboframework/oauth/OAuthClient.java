package belmen.weiboframework.oauth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import belmen.weiboframework.exception.WeiboException;
import belmen.weiboframework.util.Codec;
import belmen.weiboframework.weibo.WeiboClient;
import belmen.weiboframework.weibo.WeiboRequest;

public abstract class OAuthClient extends WeiboClient {

	public static final String TAG = OAuthClient.class.getSimpleName();
	public static final int SIGN_IN_HEADER = 0;
	public static final int SIGN_IN_QUERY_STRING = 1;
	private static final String DEFAULT_CALLBACK_URL = "oob";
	
	private final String mConsumerKey;
	private final String mConsumerSecret;
	private String mToken = null;
	private String mTokenSecret = null;
	private int mSignStrategy = SIGN_IN_HEADER;
	
	public OAuthClient(String consumerKey, String consumerSecret) {
		this(consumerKey, consumerSecret, DEFAULT_CALLBACK_URL);
	}

	public OAuthClient(String consumerKey, String consumerSecret,
			String callbackUrl) {
		this.mConsumerKey = consumerKey;
		this.mConsumerSecret = consumerSecret;
	}
	
	public void setSignStrategy(int signStrategy) {
		if(signStrategy == SIGN_IN_HEADER || signStrategy == SIGN_IN_QUERY_STRING) {
			this.mSignStrategy = signStrategy;
		}
	}
	
	public String getToken() {
		return mToken;
	}
	
	public String getTokenSecret() {
		return mTokenSecret;
	}
	
	public void setToken(String token, String tokenSecret) {
		this.mToken = token;
		this.mTokenSecret = tokenSecret;
	}
	
	public abstract void retrieveRequestToken() throws WeiboException;
	
	public abstract void retrieveAccessToken(String verifier) throws WeiboException;
	
	public abstract void retrieveAccessToken(String username, String password) throws WeiboException;

	/**
	 * 为请求签名
	 * @param request
	 * @return
	 */
	@Override
	protected OAuthRequest signRequest(WeiboRequest request) {
		if(request == null) {
			return null;
		}
		OAuthRequest oauthRequest = (OAuthRequest) request;
		addOAuthParams(oauthRequest);
		appendOAuthParams(oauthRequest);
		return oauthRequest;
	}

	/**
	 * 为请求添加OAuth参数
	 * @param request
	 */
	private void addOAuthParams(OAuthRequest request) {
		Map<String, String> oauthParams = request.getOauthParams();
		// 添加OAuth 1.0标准参数
		if(mToken != null) {
			addNewParam(oauthParams, "oauth_token", mToken);
		}
		addNewParam(oauthParams, "oauth_consumer_key", mConsumerKey);
		addNewParam(oauthParams, "oauth_signature_method", "HMAC-SHA1");
		addNewParam(oauthParams, "oauth_timestamp", getTimeStamp());
		addNewParam(oauthParams, "oauth_nonce", getNonce());
		addNewParam(oauthParams, "oauth_version", "1.0");
		String verifier = request.getVerifier();
		if(verifier != null) {
			addNewParam(oauthParams, "oauth_verifier", verifier);
		}
		
		// 添加XAuth参数
		String username = request.getUsername();
		String password = request.getPassword();
		if(username != null && password != null) {
			addNewParam(oauthParams, "x_auth_username", username);
			addNewParam(oauthParams, "x_auth_password", password);
			addNewParam(oauthParams, "x_auth_mode", "client_auth");
		}
		addNewParam(oauthParams, "oauth_signature", getSignature(request));
	}
	
	private static void addNewParam(Map<String, String> map, String name, String value) {
		if(map == null) {
			return;
		}
		if(!map.containsKey(name)) {
			map.put(name, value);
		}
	}
	
	private static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	private static String getNonce() {
		return String.valueOf(System.currentTimeMillis() / 1000 + new Random().nextInt());
	}
	
	private String getSignature(OAuthRequest request) {
		String baseString = getBaseString(request);
		return generateSignature(baseString);
	}
	
	private static String getBaseString(OAuthRequest request) {
		String method = request.getMethod().name();
		String url = Codec.urlEncode(request.getUrl());
		Map<String, String> allParams = new LinkedHashMap<String, String>();
		allParams.putAll(request.getOauthParams());
		allParams.putAll(request.getQueryParams());
		if(request.hasFile()) {
			allParams.putAll(request.getPostParams());
		}
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(allParams.entrySet());
		Collections.sort(list, parameterComparator);
		String queryString = Codec.urlEncode(getEncodedString(list));
		return String.format("%s&%s&%s", method, url, queryString);
	}
	
	private static String getEncodedString(List<Map.Entry<String, String>> params) {
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String, String> param : params) {
			builder.append("&").append(Codec.urlEncode(param.getKey()))
			.append("=").append(Codec.urlEncode(param.getValue()));
		}
		return builder.toString().substring(1);
	}
	
	private String generateSignature(String baseString) {
		String keyString = String.format("%s&%s", Codec.urlEncode(mConsumerSecret),
				mTokenSecret != null ? Codec.urlEncode(mTokenSecret) : "");
		byte[] bytes = Codec.HmacSHA1(baseString, keyString);
		return Codec.base64(bytes);
	}
	
	private void appendOAuthParams(OAuthRequest request) {
		switch (mSignStrategy) {
		case SIGN_IN_HEADER:
			request.addHeader("Authorization", getOAuthHeader(request.getOauthParams()));
			break;
		case SIGN_IN_QUERY_STRING:
			request.getQueryParams().putAll(request.getOauthParams());
			break;
		}
	}
	
	private static String getOAuthHeader(Map<String, String> oauthParams) {
		if(oauthParams == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder("OAuth ");
		Iterator<Map.Entry<String, String>> iter = oauthParams.entrySet().iterator();
		Map.Entry<String, String> entry;
		String name, value;
		while(iter.hasNext()) {
			entry = iter.next();
			name = entry.getKey();
			value = entry.getValue();
			sb.append(name).append("=\"")
			.append(Codec.urlEncode(value)).append("\",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
	private static Comparator<Map.Entry<String, String>> parameterComparator =
			new Comparator<Map.Entry<String,String>>() {
		@Override
		public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
			String lname = lhs.getKey();
			String rname = rhs.getKey();
			int nameDiff = compareString(lname, rname);
			if(nameDiff == 0) {
				String lvalue = lhs.getValue();
				String rvalue = rhs.getValue();
				return compareString(lvalue, rvalue);
			}
			return nameDiff;
		}
		
		private int compareString(String l, String r) {
			if(l == null) {
				if(r == null) {
					return 0;
				}
				return -1;
			} else if(r == null) {
				return 1;
			}
			return l.compareTo(r);
		}
	};
}
