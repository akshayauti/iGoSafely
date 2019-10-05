package com.prha.igosafely;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Register extends Activity {
	
	EditText name,number;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//Toast.makeText(getApplicationContext(), "Activity created",Toast.LENGTH_LONG).show();

	}
	
	
	
	public void display(View v) {
		Intent i_view=new Intent(Register.this,Display.class);
		startActivity(i_view);
			
		}
	
	public void instructions(View v) {
		Intent i_help=new Intent(Register.this,Instructions.class);
		startActivity(i_help);
		
		}
	
	public void storeInDB(View v) {
//		Toast.makeText(getApplicationContext(), "save started", Toast.LENGTH_LONG).show();
		final SQLiteDatabase db;
		db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
		name = (EditText) this.findViewById(R.id.editText1);
		number = (EditText) this.findViewById(R.id.editText2);
		final String str_name=name.getText().toString();
		final String str_number=number.getText().toString();
		if(isEmpty(name)){
			Toast.makeText(getApplicationContext(),"Name Should not be Empty!!",Toast.LENGTH_SHORT).show();
			name.requestFocus();
		}
		else if (isEmpty(number) && number.getText().toString().trim().length() != 10){
			Toast.makeText(getApplicationContext(),"Number Should not be Empty and it should be 10 digit long!!",Toast.LENGTH_LONG).show();
			number.requestFocus();
		}
		else {

			//Toast.makeText(getApplicationContext(), "db created",Toast.LENGTH_LONG).show();

			db.execSQL("CREATE TABLE IF NOT EXISTS details( name VARCHAR ,number VARCHAR );");
			//Toast.makeText(getApplicationContext(), "table created",Toast.LENGTH_LONG).show();

			Cursor c = db.rawQuery("SELECT * FROM details", null);
			c.moveToFirst();
			if (c.getCount() < 2) {
				db.execSQL("INSERT INTO details VALUES('"+ str_name + "','" + str_number + "');");
				name.setText("");
				number.setText("");
				Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
				db.close();
			} else {
//				db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
//				db.execSQL("DELETE FROM details where name=c.name");
//				final SQLiteDatabase finalDb = db;
				AlertDialog.Builder b=  new  AlertDialog.Builder(this)
						.setTitle("Limit Exceed Delete Previous Numbers")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										db.execSQL("delete from details");
										db.execSQL("INSERT INTO details VALUES('" + str_name + "','" + str_number + "');");
										name.setText("");
										number.setText("");
										Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
										db.close();
									}
								}
						)
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}
						);
						b.show();




//				Toast.makeText(getApplicationContext(), "Maximum Numbers limited reached. Previous numbers are replaced.", Toast.LENGTH_SHORT).show();
			}




		}
			
		}

	private boolean isEmpty(EditText myeditText) {
		return myeditText.getText().toString().trim().length() == 0;
	}
	

	
	
	
	
	
	
	


}
