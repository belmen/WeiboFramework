package belmen.weiboframework.oauth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;
import belmen.weiboframework.util.Codec;
import belmen.weiboframework.util.Logger;

public class OAuthClient {

	public static final String TAG = OAuthClient.class.getSimpleName();
	public static final int SIGN_IN_HEADER = 0;
	public static final int SIGN_IN_QUERY_STRING = 1;
	private static final String DEFAULT_CALLBACK_URL = "oob";
	private static final String SIGNATURE_ALGORITHM = "HmacSHA1";
	private static final String SIGNATURE_CODE = "UTF-8";
	
	private final String mConsumerKey;
	private final String mConsumerSecret;
	@SuppressWarnings("unused")
	private String mCallbackUrl;
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
		this.mCallbackUrl = callbackUrl;
	}

	public final void setSignStrategy(int signStrategy) {
		if(signStrategy == SIGN_IN_HEADER
				|| signStrategy == SIGN_IN_QUERY_STRING) {
			this.mSignStrategy = signStrategy;
		}
	}
	
	public final void setToken(String token, String tokenSecret) {
		this.mToken = token;
		this.mTokenSecret = tokenSecret;
	}

	/**
	 * 为请求签名
	 * @param request
	 * @return
	 */
	protected OAuthRequest sign(OAuthRequest request) {
		appendOAuthParams(getOAuthParams(request), request);
		return request;
	}
	
	/**
	 * 添加额外的OAuth参数
	 * @return
	 */
	protected List<NameValuePair> getAdditionalOAuthParameters() {
		return null;
	}
	
	/**
	 * 获取请求的OAuth参数
	 * @param request
	 * @return
	 */
	private List<NameValuePair> getOAuthParams(OAuthRequest request) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		// 添加OAuth 1.0标准参数
		if(mToken != null) {
			list.add(new BasicNameValuePair("oauth_token", mToken));
		}
		list.add(new BasicNameValuePair("oauth_consumer_key", mConsumerKey));
		list.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
		list.add(new BasicNameValuePair("oauth_timestamp", getTimeStamp()));
		list.add(new BasicNameValuePair("oauth_nonce", getNonce()));
		list.add(new BasicNameValuePair("oauth_version", "1.0"));
		
		// 添加XAuth参数
		String username = request.getUsername();
		String password = request.getPassword();
		if(username != null && password != null) {
			list.add(new BasicNameValuePair("x_auth_username", username));
			list.add(new BasicNameValuePair("x_auth_password", password));
			list.add(new BasicNameValuePair("x_auth_mode", "client_auth"));
		}
		
		// 添加客户端额外的参数
		List<NameValuePair> additional = getAdditionalOAuthParameters();
		if(additional != null) {
			list.addAll(additional);
		}
		// 生成OAuth1.0签名
		list.add(new BasicNameValuePair("oauth_signature", getSignature(request, list)));
		return list;
	}
	
	private static final String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	private static final String getNonce() {
		return String.valueOf(System.currentTimeMillis() / 1000 + new Random().nextInt());
	}
	
	private String getSignature(OAuthRequest request, List<NameValuePair> oauthParams) {
		String baseString = getBaseString(request, oauthParams);
		return generateSignature(baseString);
	}
	
	private static String getBaseString(OAuthRequest request, List<NameValuePair> oauthParams) {
		String method = request.getMethodName();
		String url = Codec.urlEncode(request.getUrl());
		
		List<NameValuePair> allParams = new ArrayList<NameValuePair>();
		allParams.addAll(oauthParams);
		allParams.addAll(request.getQueryStringParams());
		if(!request.hasFile()) {
			allParams.addAll(request.getPostParams());
		}
		Collections.sort(allParams, mParamComparator);
		String queryString = Codec.urlEncode(getEncodedString(allParams));
		return String.format("%s&%s&%s", method, url, queryString);
	}
	
	private static String getEncodedString(List<NameValuePair> params) {
		StringBuilder builder = new StringBuilder();
		for(NameValuePair param : params) {
			builder.append("&").append(Codec.urlEncode(param.getName()))
			.append("=").append(Codec.urlEncode(param.getValue()));
		}
		return builder.toString().substring(1);
	}
	
	private String generateSignature(String baseString) {
		try {
			String keyString = String.format("%s&%s", Codec.urlEncode(mConsumerSecret),
					mTokenSecret != null ? Codec.urlEncode(mTokenSecret) : "");
			SecretKeySpec key = new SecretKeySpec(keyString.getBytes(SIGNATURE_CODE), SIGNATURE_ALGORITHM);
			Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
			mac.init(key);
			byte[] bytes = mac.doFinal(baseString.getBytes(SIGNATURE_CODE));
			return Base64.encodeToString(bytes, Base64.NO_WRAP);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	private void appendOAuthParams(List<NameValuePair> oauthParams, OAuthRequest request) {
		if(request == null || oauthParams == null) {
			return;
		}
		switch (mSignStrategy) {
		case SIGN_IN_HEADER:
			request.addHeader("Authorization", getOAuthHeader(oauthParams));
			break;
		case SIGN_IN_QUERY_STRING:
			for(NameValuePair param : oauthParams) {
				request.addQueryParameter(param.getName(), param.getValue());
			}
			break;
		}
	}
	
	private static String getOAuthHeader(List<NameValuePair> oauthParams) {
		if(oauthParams == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder("OAuth ");
		for(NameValuePair param : oauthParams) {
			sb.append(String.format("%s=\"%s\",", param.getName(), Codec.urlEncode(param.getValue())));
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
	}
	
	private static Comparator<NameValuePair> mParamComparator = new Comparator<NameValuePair>() {
		@Override
		public int compare(NameValuePair lhs, NameValuePair rhs) {
			int nameDiff = lhs.getName().compareTo(rhs.getName());
			return nameDiff != 0 ? nameDiff : lhs.getValue().compareTo(rhs.getValue());
		}
	};
}
