package belmen.weiboframework.sina;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import belmen.weiboframework.util.Logger;

public class Status implements Serializable {
	
	public static final String TAG = Status.class.getSimpleName();
	private static final SimpleDateFormat dateFormat =
			new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
	
	private static final long serialVersionUID = 1L;
	private String rawData;
	private Date createdAt;
	private long id;
	private long mid;
	private String idstr;
	private String text;
	private String source;
	private boolean favorited;
	private boolean truncated;
	private String thumbnailPic;
	private String bmiddlePic;
	private String originalPic;
	private User user;
	private Status retweetedStatus;
	private int repostsCount;
	private int commentsCount;
	private int attitudesCount;
	
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
		status.setId(json.optLong("id"));
		status.setMid(json.optLong("mid"));
		status.setIdstr(json.optString("idstr"));
		status.setText(json.optString("text"));
		status.setSource(json.optString("source"));
		status.setFavorited(json.optBoolean("favorited"));
		status.setTruncated(json.optBoolean("truncated"));
		status.setThumbnailPic(json.optString("thumbnail_pic"));
		status.setBmiddlePic(json.optString("bmiddle_pic"));
		status.setOriginalPic(json.optString("original_pic"));
		status.setUser(User.fromJson(json.optJSONObject("user")));
		status.setRetweetedStatus(fromJson(json.optJSONObject("retweeted_status")));
		status.setRepostsCount(json.optInt("reposts_count"));
		status.setCommentsCount(json.optInt("comments_count"));
		status.setAttitudesCount(json.optInt("attitudes_count"));
		return status;
	}
	
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
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
	public boolean isFavorited() {
		return favorited;
	}
	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}
	public boolean isTruncated() {
		return truncated;
	}
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}
	public String getThumbnailPic() {
		return thumbnailPic;
	}
	public void setThumbnailPic(String thumbnailPic) {
		this.thumbnailPic = thumbnailPic;
	}
	public String getBmiddlePic() {
		return bmiddlePic;
	}
	public void setBmiddlePic(String bmiddlePic) {
		this.bmiddlePic = bmiddlePic;
	}
	public String getOriginalPic() {
		return originalPic;
	}
	public void setOriginalPic(String originalPic) {
		this.originalPic = originalPic;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Status getRetweetedStatus() {
		return retweetedStatus;
	}
	public void setRetweetedStatus(Status retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}
	public int getRepostsCount() {
		return repostsCount;
	}
	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	public int getAttitudesCount() {
		return attitudesCount;
	}
	public void setAttitudesCount(int attitudesCount) {
		this.attitudesCount = attitudesCount;
	}
}
