package com.tonchidot.tab.sample.oauth;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.exception.OAuthSystemException;

import com.tonchidot.tab.sample.oauth.task.GetAccessTokenTask;
import com.tonchidot.tab.sample.oauth.task.RefreshTokenTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class GetAccessTokenActivity extends Activity {

	private String mAccessToken;
	private String mRefreshToken;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_accesstoken);
		if (CommonConst.isHoneycomb())
			getActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = this;

	}

	@Override
	protected void onStart() {
		super.onStart();

		// 認可コード取得のコールバック処理
		getAccessToken(getIntent());

		Button getAuthorizationCodeButton = (Button) findViewById(R.id.button_get_authorization_code);

		SharedPreferences prefs = getSharedPreferences(CommonConst.PREF_NAME,
				Context.MODE_PRIVATE);
		mAccessToken = prefs.getString(CommonConst.PREF_ACCESS_TOKEN, null);
		mRefreshToken = prefs.getString(CommonConst.PREF_REFRESH_TOKEN, null);

		if (mAccessToken == null) {
			if (mRefreshToken == null) {
				getAuthorizationCodeButton
						.setText(R.string.button_get_authorization_code);
				getAuthorizationCodeButton
						.setOnClickListener(getAuthorizationCode);
			} else {
				getAuthorizationCodeButton
						.setText(R.string.button_refresh_token);
				getAuthorizationCodeButton
						.setOnClickListener(refreshAccessToken);
			}
		} else {
			getAuthorizationCodeButton.setOnClickListener(null);
			getAuthorizationCodeButton.setVisibility(View.GONE);
		}
	}

	private final OnClickListener getAuthorizationCode = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			// 認可コードの取得
			OAuthClientRequest request = null;
			try {
				request = OAuthClientRequest
						.authorizationLocation(
								CommonConst.AUTHORIZATION_LOCATION)
						.setResponseType("code")
						.setClientId(CommonConst.CLIENT_ID)
						.setRedirectURI(CommonConst.REDIRECT_URI)
						.buildQueryMessage();
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}

			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request
					.getLocationUri()));
			startActivity(intent);
		}
	};

	private final OnClickListener refreshAccessToken = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			// アクセストークン再取得
			RefreshTokenTask task = new RefreshTokenTask(mContext,
					mRefreshToken);
			task.execute();
		}

	};

	private void getAccessToken(Intent intent) {
		Uri uri = intent.getData();

		if (uri != null && uri.toString().startsWith(CommonConst.REDIRECT_URI)) {

			// エラーチェック
			String error = uri.getQueryParameter("error");
			if (error != null) {
				StringBuilder errorMsg = new StringBuilder("エラーが発生しました");
				errorMsg.append("\n");
				errorMsg.append("error:");
				errorMsg.append(error);
				errorMsg.append("\n");
				errorMsg.append("description:");
				errorMsg.append(intent.getData().getQueryParameter(
						"error_description"));
				Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
			} else {

				// 認可コードの取得
				String code = uri.getQueryParameter("code");
				GetAccessTokenTask task = new GetAccessTokenTask(this, code);
				task.execute();
			}
		}
	}
}
