/*
 *	Copyright (c) 2012, Yulong Information Technologies
 *	All rights reserved.
 *  
 *  @Project: LibYulong
 *  @author: Robot	
 */
package belmen.weiboframework.util;

import android.util.Log;

/**
 * 日志类
 * @author Robot
 * @weibo http://weibo.com/feng88724
 * @date Dec 14, 2012	
 */
public class Logger {
	
	private static final String DEFAULT_TAG = "LibYulong";
	
	private static boolean SHOW_FLAG = false;
	
	public static void e(String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.e(DEFAULT_TAG, msg);
	}
	
	public static void e(String tag, String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.e(tag, msg);
	}
	
	public static void d(String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.d(DEFAULT_TAG, msg);
	}
	
	public static void d(String tag, String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.d(tag, msg);
	}
	
	public static void i(String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.i(DEFAULT_TAG, msg);
	}
	
	public static void i(String tag, String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.i(tag, msg);
	}
	
	public static void v(String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.v(DEFAULT_TAG, msg);
	}
	
	public static void v(String tag, String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.v(tag, msg);
	}
	
	public static void w(String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.w(DEFAULT_TAG, msg);
	}
	
	public static void w(String tag, String msg) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.w(tag, msg);
	}
	
	public static void e(String tag, String msg, Throwable e) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.e(tag, msg, e);
	}
	
	public static void d(String tag, String msg, Throwable e) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.d(tag, msg, e);
	}
	
	public static void i(String tag, String msg, Throwable e) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.d(tag, msg, e);
	}
	
	public static void v(String tag, String msg, Throwable e) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.v(tag, msg, e);
	}
	
	public static void w(String tag, String msg, Throwable e) {
		if(!SHOW_FLAG) {
			return;
		}
		Log.w(tag, msg, e);
	}
	
	//日志开关
	public static void setDebug(boolean debug) {
		SHOW_FLAG = debug;
	}
}
