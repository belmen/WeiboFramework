package belmen.weiboframework.sina;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.exception.BeanParsingException;


public class BeanFactory {

	public static final String TAG = BeanFactory.class.getSimpleName();
	
	public static List<Status> parseStatusList(String content, String name) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		JSONObject json = getJsonObject(content);
		JSONArray array = json.optJSONArray(name);
		if(array == null) {
			return null;
		}
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
