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

	protected OAuthRequest sign(OAuthRequest request) {
		List<NameValuePair> oauthParams = getOAuthParams(request);
		return request;
	}
	
	private List<NameValuePair> getOAuthParams(OAuthRequest request) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(mToken != null) {
			list.add(new BasicNameValuePair("oauth_token", mToken));
		}
		list.add(new BasicNameValuePair("oauth_consumer_key", mConsumerKey));
		list.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
		list.add(new BasicNameValuePair("oauth_timestamp", getTimeStamp()));
		list.add(new BasicNameValuePair("oauth_nonce", getNonce()));
		list.add(new BasicNameValuePair("oauth_version", "1.0"));
		list.add(new BasicNameValuePair("oauth_signature", getSignature(request, list)));
		String username = request.getUsername();
		String password = request.getPassword();
		if(username != null && password != null) {
			list.add(new BasicNameValuePair("x_auth_username", username));
			list.add(new BasicNameValuePair("x_auth_password", password));
			list.add(new BasicNameValuePair("x_auth_mode", "client_auth"));
		}
		return list;
	}
	
	private static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	private static String getNonce() {
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
		String string;
		for(NameValuePair param : params) {
			builder.append('&');
			string = String.format("%s=%s", Codec.urlEncode(param.getName()),
					Codec.urlEncode(param.getValue()));
			builder.append(string);
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
	
	private static Comparator<NameValuePair> mParamComparator = new Comparator<NameValuePair>() {
		@Override
		public int compare(NameValuePair lhs, NameValuePair rhs) {
			int nameDiff = lhs.getName().compareTo(rhs.getName());
			return nameDiff != 0 ? nameDiff : lhs.getValue().compareTo(rhs.getValue());
		}
	};
}
