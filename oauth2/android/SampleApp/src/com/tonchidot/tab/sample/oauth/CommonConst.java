package com.tonchidot.tab.sample.oauth;

import android.os.Build;

public final class CommonConst {

	public static final String TAG = "tabsamle";

	public static final String PREF_NAME = "tab";
	public static final String PREF_ACCESS_TOKEN = "access_token";
	public static final String PREF_REFRESH_TOKEN = "refresh_token";
	public static final String PREF_EXPIRES_IN = "expires_in";
	public static final String PREF_TOKEN_TYPE = "token_type";

	public final static String AUTHORIZATION_LOCATION = "https://tab.do/oauth2/authorize";
	public final static String TOKEN_LOCATION = "https://tab.do/api/1/oauth2/token";
	public static final String ACCESS_URI = "https://tab.do/api/1/users/me.json";

	public final static String CLIENT_ID = "<<Your Client ID>>";
	public final static String CLIENT_SECRET = "<<Your Client Secret>>";
	public final static String REDIRECT_URI = "<<Your Redirect Uri>>";

	public static boolean isHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

}
