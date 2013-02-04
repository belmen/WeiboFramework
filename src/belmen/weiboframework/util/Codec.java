package belmen.weiboframework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.protocol.HTTP;

import android.util.Base64;

public class Codec {
	
	public static final String TAG = Codec.class.getSimpleName();
	
	public static String urlEncode(String s) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(s, HTTP.UTF_8);
			encoded = encoded.replace("*", "%2A")
					.replace("+", "%20")
					.replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return encoded;
	}
	
	public static String urlDecode(String s) {
		try {
			return URLDecoder.decode(s, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public static String md5(String content) {
		return md5(content.getBytes());
	}
	
	public static String md5(byte[] input) {
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(input);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16) sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public static String base64(String content) {
		return Base64.encodeToString(content.getBytes(), Base64.NO_WRAP);
	}
}
