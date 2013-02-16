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

import belmen.weiboframework.util.Logger;

public class User implements Serializable {

	public static final String TAG = User.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat dateFormat =
			new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);

	/*id	string	用户id
	name	string	用户姓名
	screen_name	string	用户昵称
	location	string	用户地址
	gender	string	用户性别
	birthday	string	用户生日信息
	description	string	用户自述
	profile_image_url	string	用户头像地址
	profile_image_url_large	string	用户高清头像地址
	url	string	用户页面地址
	protected	boolean	用户是否设置隐私保护
	followers_count	int	用户关注用户数
	friends_count	int	用户好友数
	favourites_count	int	用户收藏消息数
	statuses_count	int	用户消息数
	following	boolean	该用户是被当前登录用户关注
	notifications	boolean	当前登录用户是否已对该用户发出关注请求
	created_at	string	用户注册时间
	utc_offset	int	ref: UTC offset*/
	
	private String rawData = "";
	private String id;
	private String name;
	private String screenName;
	private String location;
	private String gender;
	private String birthday;
	private String description;
	private String profileImageUrl;
	private String profileImageUrlLarge;
	private String url;
	private boolean isProtected;
	private int followersCount;
	private int friendsCount;
	private int favouritesCount;
	private int statusesCount;
	private boolean following;
	private boolean notifications;
	private Date createdAt;
	private int utcOffset;
	private String profileBackgroundColor;
	private String profileTextColor;
	private String profileLinkColor;
	private String profileSidebarFillColor;
	private String profileSidebarBorderColor;
	private String profileBackgroundImageUrl;
	private boolean profileBackgroundTile;
	
	public static User fromJson(JSONObject json) {
		if(json == null) {
			return null;
		}
		User user = new User();
		try {
			user.setRawData(json.toString(2));
		} catch (JSONException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		user.setId(json.optString("id"));
		user.setName(json.optString("name"));
		user.setScreenName(json.optString("screen_name"));
		user.setLocation(json.optString("location"));
		user.setGender(json.optString("gender"));
		user.setBirthday(json.optString("birthday"));
		user.setDescription(json.optString("description"));
		user.setProfileImageUrl(json.optString("profile_image_url"));
		user.setProfileImageUrlLarge(json.optString("profile_image_url_large"));
		user.setUrl(json.optString("url"));
		user.setProtected(json.optBoolean("protected"));
		user.setFollowersCount(json.optInt("followers_count"));
		user.setFriendsCount(json.optInt("friends_count"));
		user.setFavouritesCount(json.optInt("favourites_count"));
		user.setStatusesCount(json.optInt("statuses_count"));
		user.setFollowing(json.optBoolean("following"));
		user.setNotifications(json.optBoolean("notifications"));
		user.setCreatedAt(json.optString("created_at"));
		user.setUtcOffset(json.optInt("utc_offset"));
		user.setProfileBackgroundColor(json.optString("profile_background_color"));
		user.setProfileTextColor(json.optString("profile_text_color"));
		user.setProfileLinkColor(json.optString("profile_link_color"));
		user.setProfileSidebarFillColor(json.optString("profile_sidebar_fill_color"));
		user.setProfileSidebarBorderColor(json.optString("profile_sidebar_border_color"));
		user.setProfileBackgroundImageUrl(json.optString("profile_background_image_url"));
		user.setProfileBackgroundTile(json.optBoolean("profile_background_tile"));
		return user;
	}
	
	public static List<User> fromJsonArray(JSONArray array) {
		if(array == null) {
			return null;
		}
		List<User> list = new ArrayList<User>(array.length());
		for(int i = 0; i < array.length(); i++) {
			list.add(fromJson(array.optJSONObject(i)));
		}
		return list;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileImageUrlLarge() {
		return profileImageUrlLarge;
	}

	public void setProfileImageUrlLarge(String profileImageUrlLarge) {
		this.profileImageUrlLarge = profileImageUrlLarge;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
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

	public int getUtcOffset() {
		return utcOffset;
	}

	public void setUtcOffset(int utcOffset) {
		this.utcOffset = utcOffset;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	public void setProfileBackgroundColor(String profileBackgroundColor) {
		this.profileBackgroundColor = profileBackgroundColor;
	}

	public String getProfileTextColor() {
		return profileTextColor;
	}

	public void setProfileTextColor(String profileTextColor) {
		this.profileTextColor = profileTextColor;
	}

	public String getProfileLinkColor() {
		return profileLinkColor;
	}

	public void setProfileLinkColor(String profileLinkColor) {
		this.profileLinkColor = profileLinkColor;
	}

	public String getProfileSidebarFillColor() {
		return profileSidebarFillColor;
	}

	public void setProfileSidebarFillColor(String profileSidebarFillColor) {
		this.profileSidebarFillColor = profileSidebarFillColor;
	}

	public String getProfileSidebarBorderColor() {
		return profileSidebarBorderColor;
	}

	public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
		this.profileSidebarBorderColor = profileSidebarBorderColor;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}

	public boolean isProfileBackgroundTile() {
		return profileBackgroundTile;
	}

	public void setProfileBackgroundTile(boolean profileBackgroundTile) {
		this.profileBackgroundTile = profileBackgroundTile;
	}
}
