package com.prha.igosafely;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class Display extends Activity {
	

	Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		
		SQLiteDatabase db;
		db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
			try{
				c=db.rawQuery("SELECT * FROM details", null);
				if(c.getCount()==0)
				{
					showMessage("Error", "No records found.");

					AlertDialog.Builder detect = new AlertDialog.Builder(Display.this);
					detect.setMessage("Add atleast one emergency contact to continue.").setPositiveButton("OK", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.cancel();
							Intent in = new Intent(Display.this,Register.class);
							startActivity(in);
						}
					});
					detect.setCancelable(false);
					AlertDialog alert=detect.create();
					alert.show();
					return;

				}
				StringBuffer buffer=new StringBuffer();
				if (c.moveToFirst()) {
					do {
						buffer.append("Name: " + c.getString(0) + "\n");
						buffer.append("Number: " + c.getString(1) + "\n");
					} while (c.moveToNext());
					showMessage("Details", buffer.toString());
				}

			}catch (Exception e){


			}
//		Intent i_startservice=new Intent(Display.this,BgService.class);
//		startService(i_startservice);



          
		
	}
	
	public void showMessage(String title, String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

	public void back(View v) {
		Intent i_back=new Intent(Display.this,MainActivity.class);
		startActivity(i_back);
		
		}

}








