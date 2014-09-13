package com.sb.skin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private RelativeLayout layout_setting_about_checknew;
	private TextView setting_about_check_text;
	private String buildFile;
	private boolean old;
	private static String FileName = "/system/build.prop";
	private static String TmpName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp/build.yypie";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		layout_setting_about_checknew = (RelativeLayout) findViewById(R.id.layout_setting_about_checknew);
		layout_setting_about_checknew.setOnClickListener(this);
		setting_about_check_text = (TextView) findViewById(R.id.setting_about_check_text);

		old = checkMode();
		if (old) {
			setting_about_check_text.setText(R.string.close);
		} else {
			setting_about_check_text.setText(R.string.open);
		}
	}

	protected boolean checkMode() {
		SystemUtil util = new SystemUtil();
		util.addCommand("cat " + FileName);
		util.executeNormal();
		buildFile = util.getOutput();
		if (buildFile.isEmpty()) {
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setMessage(R.string.fatal).setIcon(R.drawable.ic_launcher)
					.create();
			alertDialog.show();
			this.finish();
			old = false;
			return old;
		}
		Pattern pattern = Pattern.compile("^qemu.hw.mainkeys\\s*=\\s*([0-9]+)", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(buildFile);
		String key = "";
		while(matcher.find()) {
			key = matcher.group(1);
		}
		if (key.isEmpty()) {
			old = false;
			// Generate new content
			buildFile = buildFile + "\nqemu.hw.mainkeys=0";
		} else {
			if (Integer.parseInt(key) == 0) {
				old = true;
				buildFile = matcher.replaceAll("qemu.hw.mainkeys=1");
			} else {
				old = false;
				buildFile = matcher.replaceAll("qemu.hw.mainkeys=0");
			}
		}

		return old;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_setting_about_checknew:
			boolean now = checkMode();
			boolean ret = true;
			if (now == old) {
				// Need change
				if (SystemUtil.canRunRootCommands() && !buildFile.isEmpty()) {
					SystemUtil.writeFile(TmpName, buildFile);
					SystemUtil util = new SystemUtil();
					util.addCommand("mount -o remount,rw /system");
					util.addCommand("cat " + TmpName + " > " + FileName);
					util.addCommand("mount -o remount,ro /system");
					util.execute();
					old = !old;
				} else {
					Dialog alertDialog = new AlertDialog.Builder(this)
							.setMessage(R.string.fail)
							.setIcon(R.drawable.ic_launcher).create();
					alertDialog.show();
					ret = false;
				}
			}
			if (ret) {
				Dialog alertDialog = new AlertDialog.Builder(this)
						.setMessage(R.string.notice)
						.setIcon(R.drawable.ic_launcher).create();
				alertDialog.show();
			}

			if (old) {
				setting_about_check_text.setText(R.string.close);
			} else {
				setting_about_check_text.setText(R.string.open);
			}
			break;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	};
}
