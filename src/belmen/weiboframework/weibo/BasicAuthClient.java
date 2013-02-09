package belmen.weiboframework.weibo;

import android.util.Base64;

public class BasicAuthClient extends WeiboClient {
	
	private String username = null;
	private String password = null;
	
	public BasicAuthClient() {}

	public BasicAuthClient(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected WeiboRequest signRequest(WeiboRequest request) {
		String b64 = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
		request.addHeader("Authorization", "Basic " + b64);
		return request;
	}
}
