package com.tonchidot.tab.sample.oauth;

import com.tonchidot.tab.sample.oauth.task.GetProfileInfoTask;
import com.tonchidot.tab.sample.oauth.CommonConst;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (CommonConst.isHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		TextView screen_name = (TextView) findViewById(R.id.screen_name);
		TextView email = (TextView) findViewById(R.id.email);
		ImageView profile_image = (ImageView) findViewById(R.id.profile_image);

		SharedPreferences pref = getSharedPreferences(CommonConst.PREF_NAME,
				Context.MODE_PRIVATE);
		String accessToken = pref
				.getString(CommonConst.PREF_ACCESS_TOKEN, null);

		if (accessToken == null) {
			Intent intent = new Intent(getApplicationContext(),
					GetAccessTokenActivity.class);
			startActivity(intent);
		} else {
			GetProfileInfoTask task = new GetProfileInfoTask(this, screen_name,
					email, profile_image);
			task.execute();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
