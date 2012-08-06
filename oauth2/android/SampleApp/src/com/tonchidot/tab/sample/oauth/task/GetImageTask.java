package com.tonchidot.tab.sample.oauth.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.tonchidot.tab.sample.oauth.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


public class GetImageTask extends AsyncTask<String, Void, Bitmap> {

	ProgressDialog mDialog;
	Context mContext;
	ImageView mImageView;

	public GetImageTask(Context context, ImageView imageView) {
		this.mContext = context;
		this.mImageView =imageView;
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
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		mDialog.dismiss();
		if (result != null){
			mImageView.setImageBitmap(result);
		}
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bmp = null;
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(params[0]);
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				bmp = BitmapFactory.decodeStream(httpResponse.getEntity()
						.getContent());
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bmp;
	}
}
