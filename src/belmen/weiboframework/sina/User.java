package belmen.weiboframework.sina;

import java.io.Serializable;

import org.json.JSONObject;

public class User implements Serializable {

	public static final String TAG = User.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;

	public static User fromJson(JSONObject json) {
		if(json == null) {
			return null;
		}
		return null;
	}
}
