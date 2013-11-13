package com.google.android.youtube.debug;

import static com.google.android.youtube.debug.Common.PKG_NAME;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import android.media.MediaPlayer;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class MediaPlayerDebug implements IXposedHookZygoteInit {

	protected String TAG = MediaPlayerDebug.class.getSimpleName();
	private static final XSharedPreferences sPref = new XSharedPreferences(PKG_NAME);
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {

		if(!sPref.getBoolean(Common.KEY_MEDIA_PLAYER_DEBUG, Common.DEF_MEDIA_PLAYER_DEBUG))
			return;
		
		Class<MediaPlayer> clazz = MediaPlayer.class;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods)
			XposedBridge.hookMethod(method, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					XposedBridge.log(TAG + "|method|" + ((Method) param.method).toString());
					XposedBridge.log(TAG + "|" + Arrays.toString(param.args));
				}
			});
	
		XposedBridge.hookAllConstructors(MediaPlayer.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				XposedBridge.log(TAG + "|ctor|" + ((Constructor<?>) param.method).toString());
				XposedBridge.log(TAG + "|" + Arrays.toString(param.args));
			}
		});
	}

}
