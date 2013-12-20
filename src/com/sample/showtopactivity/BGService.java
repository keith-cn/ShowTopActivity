package com.sample.showtopactivity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BGService extends Service {

	public TextView mView;
	public String mTopActivity;
	Timer mTimer;

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ActivityManager am = (ActivityManager) BGService.this
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(100);

			if (list.get(0).topActivity.getClassName().equalsIgnoreCase(
					mTopActivity)) {
				return;
			}
			mTopActivity = list.get(0).topActivity.getClassName();
			mView.setText(mTopActivity);
			FloatingFunc.show(BGService.this, mView);
			int i = 0;

			Log.e("Keith", "[Class name:  " + mTopActivity + "]");
			Log.e("Keith",
					"[Package name:  "
							+ list.get(0).topActivity.getPackageName() + "]");

			String rName = list.get(0).topActivity.getPackageName()
					+ ".R$string";
			// String rName = list.get(0).topActivity.getClassName();

			PackageManager pm = BGService.this.getPackageManager();
			Resources res = null;
			try {
				res = pm.getResourcesForApplication(list.get(0).topActivity
						.getPackageName());
			} catch (NameNotFoundException e1) {
				Log.e("Keith", "Resource name not found");
			}

			try {
				Class cz = Class.forName(rName);
				Object rObj = cz.newInstance();

				for (Field f : cz.getFields()) {
					if (res != null) {
						Log.e("Keith", "<string name=\"" + f.getName() + "\">"
								+ res.getString((Integer) f.get(rObj))
								+ "</string>");
					}
					i++;
				}
			} catch (ClassNotFoundException e) {
				Log.e("Keith", "Class not found");
			} catch (InstantiationException e) {
				Log.e("Keith", "Class not found 2");
			} catch (IllegalAccessException e) {
				Log.e("Keith", "Class not found 3");
			} finally {
				Log.e("Keith", "success");
				super.handleMessage(msg);
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		Log.e("Keith", "BGService::onCreate()");
		mView = new TextView(this);
		mView.setTextColor(0xffff0101);
		mView.setText(getClass().toString());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("Keith", "BGService::onStartCommand()");
		TimerTask task = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		mTimer = new Timer(true);
		mTimer.schedule(task, 1000, 200); // 延时1000ms后执行，1000ms执行一次
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.e("Keith", "BGService::onBind()");
		return null;
	}

	@Override
	public void onDestroy() {

		Log.e("Keith", "BGService::onDestroy()");
		mTimer.cancel();
		FloatingFunc.close(this);
		// Tell the user we stopped.
		Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT)
				.show();
	}

}
