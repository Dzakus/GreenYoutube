package com.google.android.youtube.debug;

public class Common {
	
	public static final boolean DEBUG = BuildConfig.DEBUG;
	public static final String PKG_NAME = Module.class.getPackage().getName();
	public static final String PKG_NAME_GSF = "com.google.android.gsf";
	public static final String PKG_NAME_YOUTUBE = "com.google.android.youtube";
	
	public static final String KEY_IS_BGOL_ENABLED = "is_bgol_enabled";
	public static final String KEY_HIDE_WATERMARK = "hide_watermark";
	public static final String KEY_SHOW_DOWNLOAD = "show_download";
	public static final String KEY_MEDIA_PLAYER_DEBUG = "media_player_debug";
		
	public static final boolean DEF_IS_BGOL_ENABLED = false;
	public static final boolean DEF_HIDE_WATERMARK = false;
	public static final boolean DEF_SHOW_DOWNLOAD = false;
	public static final boolean DEF_MEDIA_PLAYER_DEBUG = false;
	
}
