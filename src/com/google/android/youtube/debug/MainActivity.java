package com.google.android.youtube.debug;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;

@SuppressWarnings("deprecation")
public class MainActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int DLG_NEED_FORCE_STOP = 0;
	private static final String EXTRA_PKG_NAME = "PACKAGE_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_WRITEABLE | MODE_WORLD_READABLE);
		addPreferencesFromResource(R.xml.preference);
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		//Disable YT Downloader for non-debug builds
		if(Common.DEBUG){
			findPreference(Common.KEY_SHOW_DOWNLOAD).setEnabled(Common.DEBUG);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if (key.equals(Common.KEY_IS_BGOL_ENABLED)) {
			Bundle b = new Bundle();
			b.putString(EXTRA_PKG_NAME, Common.PKG_NAME_GSF);
			showDialog(DLG_NEED_FORCE_STOP, b);
		} else if (key.equals(Common.KEY_HIDE_WATERMARK) || key.equals(Common.KEY_SHOW_DOWNLOAD)) {
			Bundle b = new Bundle();
			b.putString(EXTRA_PKG_NAME, Common.PKG_NAME_YOUTUBE);
			showDialog(DLG_NEED_FORCE_STOP, b);
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		Builder builder;
		switch (id) {
		case DLG_NEED_FORCE_STOP:
			final String pkg_name = args.getString(EXTRA_PKG_NAME, Common.PKG_NAME_YOUTUBE);
			builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.msg_need_force_stop);
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Uri uri = Uri.fromParts("package", pkg_name, null);
					Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
					MainActivity.this.startActivity(i);
				}
			});
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

}
