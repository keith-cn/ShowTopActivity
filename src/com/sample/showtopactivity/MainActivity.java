package com.sample.showtopactivity;

import com.sample.showtopactivity.R;

import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

	public TextView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mView = new TextView(this);
		mView.setTextColor(0xffff0101);
		mView.setText(getClass().toString());

		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			startService(new Intent(this, BGService.class));
			// this.bindService(new Intent(this, BGService.class),
			// mConnection, Context.BIND_AUTO_CREATE);

			break;
		case R.id.button2:
			boolean ret = stopService(new Intent(this, BGService.class));
			Log.e("Keith", "stopService ret: " + ret);
			break;
		case R.id.button3:
			finish();
			// System.exit(0);
			break;
		}

	}

}
