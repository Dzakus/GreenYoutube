package com.google.android.youtube.debug;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import android.media.MediaPlayer;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class MediaPlayerDebug implements IXposedHookZygoteInit {

	protected String TAG = MediaPlayerDebug.class.getSimpleName();

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {

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
