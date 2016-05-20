package com.example.foodcust;


import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import com.example.foodcust.webservices.HttpRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btnLogin;
	private EditText edtUsername, edtPassword;
	private TextView txtForgotPassword;
	private final int REQUEST_FROM_LOGIN = 1 ;
	private int FROM = 0;
	private String strEmailId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.btn1);
		edtUsername = (EditText) findViewById(R.id.et1);
		edtPassword = (EditText) findViewById(R.id.et2);
		txtForgotPassword = (TextView) findViewById(R.id.textView2);
		
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edtUsername.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please enter a valid email id or mobile no",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (edtUsername.getText().toString().length() < 10) {
					// Toast.makeText(getApplicationContext(),
					// "Please enter valid mobile no !",
					// Toast.LENGTH_LONG).show();
					// return;
				}
				if (edtPassword.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_LONG).show();
					return;
				}

				new AsyncApp().execute(edtUsername.getText().toString(), edtPassword.getText().toString());

			}
		});
		
		txtForgotPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
				startActivity(intent);
			}
		});
		
		
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private class AsyncApp extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;
		private boolean isSuccess = false;
		boolean j1,j2,j3;
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			dialog.dismiss();

			if (isSuccess) {
				// if (result != -2) {

				try {
					
					JSONArray jObj = new JSONArray(result);
					Log.d("Json Array", jObj.toString());
					if (jObj.getJSONObject(0).has("Invaliduser") && jObj.getJSONObject(0).getString("Invaliduser").equals("3")) {
					Toast.makeText(getApplicationContext(), "Invalid credentials, Please try again",
								Toast.LENGTH_LONG).show();
					}else if (jObj.getJSONObject(0).has("Invaliduser") && jObj.getJSONObject(0).getString("Invaliduser").equals("2")) {
					Toast.makeText(getApplicationContext(), "User is blocked.Please contact to administrator",
								Toast.LENGTH_LONG).show();
					}
					 else if (jObj.getJSONObject(0).has("Invaliduser") && jObj.getJSONObject(0).getString("Invaliduser").equals("1")) {
	                  Intent i = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(i);
					}
					 
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Invalid credentials, Please try again", Toast.LENGTH_LONG)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Something went wrong, Please try again", Toast.LENGTH_LONG)
							.show();
				}
			} 
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(LoginActivity.this);
			//dialog.setIndeterminate(true);
			// dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
			dialog.setCancelable(false);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpRequest hr = new HttpRequest();
			try {

				FROM = REQUEST_FROM_LOGIN;
				ContentValues cv = new ContentValues();
				cv.put("UserName", edtUsername.getText().toString());
				cv.put("Password", edtPassword.getText().toString());

				String res = hr.getDataFromServer(cv, "CustomerMasterLogin");
				Log.d("Res" , res);
				isSuccess = true;
				return res;
			} 
			catch (SocketTimeoutException e) {
				isSuccess = false;
				e.printStackTrace();
			} catch (SocketException e) {
				isSuccess = false;
				e.printStackTrace();
			} catch (IOException e) {
				isSuccess = false;
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	
	
}
