package com.tonchidot.tab.sample.oauth.task;

import com.tonchidot.tab.sample.oauth.CommonConst;
import com.tonchidot.tab.sample.oauth.MainActivity;
import com.tonchidot.tab.sample.oauth.R;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class GetAccessTokenTask extends AsyncTask<Void, Void, Void> {

	Context mContext;
	String mCode;
	ProgressDialog mDialog;

	public GetAccessTokenTask(Context context, String code) {
		mContext = context;
		mCode = code;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		OAuthClientRequest request = null;
		OAuthJSONAccessTokenResponse response = null;

		try {
			request = OAuthClientRequest
					.tokenLocation(CommonConst.TOKEN_LOCATION)
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(CommonConst.CLIENT_ID)
					.setClientSecret(CommonConst.CLIENT_SECRET)
					.setRedirectURI(CommonConst.REDIRECT_URI)
					.setCode(mCode).buildBodyMessage();

			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			response = oAuthClient.accessToken(request);
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}

		if (response != null) {
			SharedPreferences pref = mContext.getSharedPreferences(CommonConst.PREF_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(CommonConst.PREF_ACCESS_TOKEN, response.getAccessToken());
			editor.putString(CommonConst.PREF_REFRESH_TOKEN, response.getRefreshToken());
			editor.putString(CommonConst.PREF_EXPIRES_IN, response.getExpiresIn());
			editor.putString(CommonConst.PREF_TOKEN_TYPE, response.getParam("token_type"));
			editor.apply();
		}

		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage(mContext.getString(R.string.processing_get_token));
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mDialog.dismiss();
		Intent intent = new Intent(mContext, MainActivity.class);
		mContext.startActivity(intent);
	}

}
