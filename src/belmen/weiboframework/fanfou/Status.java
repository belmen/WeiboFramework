package belmen.weiboframework.fanfou;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.exception.BeanParsingException;
import belmen.weiboframework.util.Logger;

public class Status implements Serializable {

	public static final String TAG = Status.class.getSimpleName();
	private static final SimpleDateFormat dateFormat =
			new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
	
	private static final long serialVersionUID = 1L;
	private String rawData;
	private Date createdAt;
	private String id;
	private int rawid;
	private String text;
	private String source;
	private boolean truncated;
	private String inReplyToStatusId;
	private String inReplyToUserId;
	private String repostStatusId;
	private Status repostStatus;
	private String repostUserId;
	private String location;
	private boolean favorited;
	private String inReplyToScreenName;
	private User user;
	private String imageurl;
	private String thumburl;
	private String largeurl;
	
	public static Status fromJson(JSONObject json) {
		if(json == null) {
			return null;
		}
		Status status = new Status();
		try {
			status.setRawData(json.toString(2));
		} catch (JSONException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		status.setCreatedAt(json.optString("created_at"));
		status.setId(json.optString("id"));
		status.setRawid(json.optInt("rawid"));
		status.setText(json.optString("text"));
		status.setSource(json.optString("source"));
		status.setTruncated(json.optBoolean("truncated"));
		status.setInReplyToStatusId(json.optString("in_reply_to_status_id"));
		status.setInReplyToUserId(json.optString("in_reply_to_user_id"));
		status.setRepostStatusId(json.optString("repost_status_id"));
		status.setRepostStatus(Status.fromJson(json.optJSONObject("repost_status")));
		status.setRepostUserId(json.optString("repost_user_id"));
		status.setLocation(json.optString("location"));
		status.setFavorited(json.optBoolean("favorited"));
		status.setInReplyToScreenName(json.optString("in_reply_to_screen_name"));
		status.setUser(User.fromJson(json.optJSONObject("user")));
		status.setPhoto(json.optJSONObject("photo"));
		return status;
	}
	
	public static Status fromJson(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		try {
			return fromJson(new JSONObject(content));
		} catch (JSONException e) {
			throw new BeanParsingException("Response cannot be converted to Json Object", e);
		}
	}
	
	public static List<Status> fromJsonArray(JSONArray array) {
		if(array == null) {
			return null;
		}
		List<Status> list = new ArrayList<Status>(array.length());
		for(int i = 0; i < array.length(); i++) {
			list.add(fromJson(array.optJSONObject(i)));
		}
		return list;
	}
	
	public static List<Status> fromJsonArray(String content) throws BeanParsingException {
		if(content == null) {
			return null;
		}
		try {
			return fromJsonArray(new JSONArray(content));
		} catch (JSONException e) {
			throw new BeanParsingException("Response cannot be converted to Json Array", e);
		}
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		try {
			this.createdAt = dateFormat.parse(createdAt);
		} catch (ParseException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRawid() {
		return rawid;
	}
	public void setRawid(int rawid) {
		this.rawid = rawid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public boolean isTruncated() {
		return truncated;
	}
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}
	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public String getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(String inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public String getRepostStatusId() {
		return repostStatusId;
	}
	public void setRepostStatusId(String repostStatusId) {
		this.repostStatusId = repostStatusId;
	}
	public Status getRepostStatus() {
		return repostStatus;
	}
	public void setRepostStatus(Status repostStatus) {
		this.repostStatus = repostStatus;
	}
	public String getRepostUserId() {
		return repostUserId;
	}
	public void setRepostUserId(String repostUserId) {
		this.repostUserId = repostUserId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isFavorited() {
		return favorited;
	}
	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setPhoto(JSONObject json) {
		if(json != null) {
			setImageurl(json.optString("imageurl"));
			setThumburl(json.optString("thumburl"));
			setLargeurl(json.optString("largeurl"));
		}
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getThumburl() {
		return thumburl;
	}
	public void setThumburl(String thumburl) {
		this.thumburl = thumburl;
	}
	public String getLargeurl() {
		return largeurl;
	}
	public void setLargeurl(String largeurl) {
		this.largeurl = largeurl;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
