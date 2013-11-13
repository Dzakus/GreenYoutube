package com.google.android.youtube.debug;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class DownloadButton implements IXposedHookLoadPackage,OnClickListener {
	private static final String CLASS_UI_GM = "com.google.android.apps.youtube.app.ui.gm";
	private static final String CLASS_VIDEO_INFO_FRAGMENTS = "com.google.android.apps.youtube.app.fragments.VideoInfoFragment";
	protected Uri yt_url;
	protected Context context;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(Common.PKG_NAME_YOUTUBE))
			return;

		final Class<?> UiGmClass = XposedHelpers.findClass(CLASS_UI_GM, lpparam.classLoader);
		final Class<?> VideoInfoFragments = XposedHelpers.findClass(CLASS_VIDEO_INFO_FRAGMENTS, lpparam.classLoader);
		
		XposedBridge.hookAllConstructors(UiGmClass, new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				View flag_button = (View) XposedHelpers.getObjectField(param.thisObject, "g");
				if (flag_button == null)
					return;
				XposedBridge.log("flag_button id=" + flag_button.getId());
				Context context = flag_button.getContext();
				Button down_button = new Button(context);
				down_button.setText("DOWN");
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.LEFT_OF, flag_button.getId());
				down_button.setLayoutParams(params);
				down_button.setOnClickListener(DownloadButton.this);
				RelativeLayout like_dislike_panel = (RelativeLayout) flag_button.getParent();
				like_dislike_panel.addView(down_button);
				XposedBridge.log("context_class = " + context);
				XposedBridge.log("context_class = " + context.getClass().getName());
				XposedBridge.log(Log.getStackTraceString(new Exception()));
			}

		});
		XposedHelpers.findAndHookMethod(MediaPlayer.class, "setDataSource", Context.class,Uri.class,Map.class, new XC_MethodHook(){
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				context = (Context) param.args[0];
				yt_url = (Uri) param.args[1];
				XposedBridge.log("!!setDataSource ");
			}});
	}

	@Override
	public void onClick(View v) {
		DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(yt_url);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "test.mp4");
		dm.enqueue(request);
		/*Intent viewMediaIntent = new Intent();   
		viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);   
		viewMediaIntent.setDataAndType(yt_url, "video/*");   
		viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(viewMediaIntent);    */     
	}

}
