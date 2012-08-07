package com.tonchidot.tab.sample.oauth.task;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tonchidot.tab.sample.oauth.CommonConst;
import com.tonchidot.tab.sample.oauth.GetAccessTokenActivity;
import com.tonchidot.tab.sample.oauth.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class GetProfileInfoTask extends
		AsyncTask<Void, Void, HashMap<String, String>> {
	
	ProgressDialog mDialog;
	Context mContext;
	TextView mScreenNameView;
	TextView mEmailView;
	ImageView mProfileImageView;

	public GetProfileInfoTask(Context context, TextView nameView,
			TextView emailView, ImageView imageView) {
		this.mContext = context;
		this.mScreenNameView = nameView;
		this.mEmailView = emailView;
		this.mProfileImageView = imageView;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage(mContext.getString(R.string.processing_load_profile));
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	protected void onPostExecute(HashMap<String, String> result) {
		super.onPostExecute(result);
		mDialog.dismiss();
		if (result.get("statusCode").equals("401")) {
			SharedPreferences pref;
			pref = mContext.getSharedPreferences(CommonConst.PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(CommonConst.PREF_ACCESS_TOKEN, null);
			editor.commit();

			Intent intent = new Intent(mContext, GetAccessTokenActivity.class);
			mContext.startActivity(intent);

		} else if (result.get("statusCode").equals("200")) {

			try {
				
				JSONObject root = new JSONObject(result.get("json"));
				JSONObject user = root.getJSONObject("user");
				mScreenNameView.setText(user.getString("screen_name"));
				mEmailView.setText(user.getString("email"));
				JSONObject profile_image_url = user
						.getJSONObject("profile_image_url");
				new GetImageTask(mContext, mProfileImageView)
						.execute(profile_image_url.getString("crop_M"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	protected HashMap<String, String> doInBackground(Void... arg0) {
		SharedPreferences pref;
		pref = mContext.getSharedPreferences(CommonConst.PREF_NAME, Context.MODE_PRIVATE);
		String accessToken = pref.getString(CommonConst.PREF_ACCESS_TOKEN, null);

		HashMap<String, String> result = new HashMap<String, String>();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(CommonConst.ACCESS_URI);
		request.setHeader("Authorization", "Bearer " + accessToken);

		try {
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			result.put("statusCode", String.valueOf(statusCode));
			if (statusCode != HttpStatus.SC_OK) {
				Log.d(CommonConst.TAG, "Status Code /// " + statusCode);
			} else {
				HttpEntity httpEntity = response.getEntity();
				result.put("json", EntityUtils.toString(httpEntity));
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
