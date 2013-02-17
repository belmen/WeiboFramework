package belmen.weiboframework.api;

import android.util.Base64;

public class BasicAuthClient extends ApiClient {
	
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
	protected void signRequest(ApiRequest request) {
		String b64 = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
		request.addHeader("Authorization", "Basic " + b64);
	}
}
