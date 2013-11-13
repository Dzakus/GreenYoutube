package com.google.android.youtube.debug;

import static com.google.android.youtube.debug.Common.KEY_IS_BGOL_ENABLED;
import static com.google.android.youtube.debug.Common.PKG_NAME;
import static com.google.android.youtube.debug.Common.PKG_NAME_GSF;
import static com.google.android.youtube.debug.Common.PKG_NAME_YOUTUBE;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;



public class Module implements IXposedHookLoadPackage {

	
	public static final String CLASS_PROVIDER = "com.google.android.gsf.gservices.GservicesProvider";
	public static final String CLASS_ANNOTATION_OVERLAY = "com.google.android.apps.youtube.core.player.overlay.j";
	
	public static final Uri URI_GSERIVCES = Uri.parse("content://com.google.android.gsf.gservices");
	public static final Uri URI_GSERIVCES_PREFIX = Uri.parse("content://com.google.android.gsf.gservices/prefix");

	private static final XSharedPreferences mPref = new XSharedPreferences(PKG_NAME);

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if(lpparam.packageName.equals(PKG_NAME_YOUTUBE)){
			mPref.reload();
			if(mPref.getBoolean(Common.KEY_HIDE_WATERMARK, Common.DEF_HIDE_WATERMARK)){
				final Class<?> AnnotationOverlayClass = XposedHelpers.findClass(CLASS_ANNOTATION_OVERLAY, lpparam.classLoader);
				XposedHelpers.findAndHookMethod(AnnotationOverlayClass, "d", XC_MethodReplacement.DO_NOTHING);
			}

		}else if (lpparam.packageName.equals(PKG_NAME_GSF)){
			
			mPref.reload();
			final Class<?> ProviderClass = XposedHelpers.findClass(CLASS_PROVIDER, lpparam.classLoader);
		
				// query (Uri uri, String[] projection, String selection, String[]
				// selectionArgs, String sortOrder)
				XposedHelpers.findAndHookMethod(ProviderClass, "query", Uri.class, String[].class, String.class,
					String[].class, String.class, new XC_MethodHook() {

					/*@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						Object[] args = param.args;
						XposedBridge.log(" query( "
								+ "uri="+((Uri)args[0])
								+ ", projection ="+Arrays.toString((String[])args[1])
								+ ", selection ="+args[2]
								+ ", selectionArgs = "+Arrays.toString((String[])args[3])
								+ ", sortOrder="+args[4]
								+ ");"
						);
					}*/
						@Override
						protected void afterHookedMethod(MethodHookParam param) throws Throwable {
							boolean isEnabledHook = mPref.getBoolean(KEY_IS_BGOL_ENABLED, true);
							if (!isEnabledHook) {
								return;
							}
							Cursor cursor = (Cursor) param.getResult();
							if (cursor == null)
								return;

							// XposedBridge.log(CursorHelper.getCursorColumn(cursor));
							// XposedBridge.log(CursorHelper.getCursorValue(cursor));

							Uri uri = (Uri) param.args[0];
							String[] selectionArgs = (String[]) param.args[3];
							String key = null;
							if (selectionArgs != null && selectionArgs.length > 0)
								key = ((String[]) param.args[3])[0];

							if (key != null & key.equals(KEY_IS_BGOL_ENABLED) & uri != null
									& uri.compareTo(URI_GSERIVCES) == 0) {
								MatrixCursor fake_cursor = new MatrixCursor(new String[] { "key", "value" });
								fake_cursor.addRow(new Object[] { KEY_IS_BGOL_ENABLED, "true" });
								param.setResult(fake_cursor);
							}
						}
					});
		}
	}
}
