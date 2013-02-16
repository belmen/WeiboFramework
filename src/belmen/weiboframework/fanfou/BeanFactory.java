package belmen.weiboframework.fanfou;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.exception.BeanParsingException;

public class BeanFactory {

	public static final String TAG = BeanFactory.class.getSimpleName();
	
	public static User parseUser(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		return User.fromJson(getJsonObject(content));
	}
	
	public static List<User> parseUserList(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		JSONArray array = getJsonArray(content);
		List<User> list = new ArrayList<User>(array.length());
		User user;
		for(int i = 0; i < array.length(); i++) {
			user = User.fromJson(array.optJSONObject(i));
			if(user != null) {
				list.add(user);
			}
		}
		return list;
	}
	
	public static Status parseStatus(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		return Status.fromJson(getJsonObject(content));
	}
	
	public static List<Status> parseStatusList(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		JSONArray array = getJsonArray(content);
		List<Status> list = new ArrayList<Status>(array.length());
		Status status;
		for(int i = 0; i < array.length(); i++) {
			status = Status.fromJson(array.optJSONObject(i));
			if(status != null) {
				list.add(status);
			}
		}
		return list;
	}
	
	public static List<String> parseIdList(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		JSONArray array = getJsonArray(content);
		List<String> list = new ArrayList<String>(array.length());
		for(int i = 0; i < array.length(); i++) {
			list.add(array.optString(i));
		}
		return list;
	}
	
	private static JSONObject getJsonObject(String content) throws BeanParsingException {
		try {
			return new JSONObject(content);
		} catch (JSONException e) {
			throw new BeanParsingException("Content cannot be converted to Json Object", e);
		}
	}
	
	private static JSONArray getJsonArray(String content) throws BeanParsingException {
		try {
			return new JSONArray(content);
		} catch (JSONException e) {
			throw new BeanParsingException("Response cannot be converted to Json Array", e);
		}
	}
}
