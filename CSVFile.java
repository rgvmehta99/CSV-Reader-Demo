package com.example.demoreadexcelfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button read;
	ListView listView;
	ItemArrayAdapter itemArrayAdapter;
	ItemAdapter itemadapter;
	DBHelper helper;
	SQLiteDatabase db = null;
	List<ContactsBean> contactList = new ArrayList<ContactsBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper = new DBHelper(MainActivity.this);
		db = helper.getWritableDatabase();
		
		listView = (ListView) findViewById(R.id.list);
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        File file = new File("/sdcard/demo.csv");
        Log.d("File :", file+"");
        FileInputStream myInput;
		try {
			myInput = new FileInputStream(file);
	        CSVFile csvFile = new CSVFile(/*inputStream*/myInput);       
	        List scoreList = csvFile.read();
	 
	        for(Object object : scoreList ) {
	        	String[] scoredata = (String[]) object;
	        	
	            //itemArrayAdapter.add(scoredata);
	            Log.d("Name :", scoredata[0]+"");
	            Log.d("Number :", scoredata[1]+"");
	            
	            ContentValues values = new ContentValues();
				values.put(DBHelper.CONTACT_NAME, scoredata[0]);
				values.put(DBHelper.CONTACT_NUMBER, scoredata[1]);
				
				//db.insert(DBHelper.TABLE_CONTACT_DETAIL, null, values);
				db.insertWithOnConflict(DBHelper.TABLE_CONTACT_DETAIL, null, values, SQLiteDatabase.CONFLICT_REPLACE);

				Toast.makeText(MainActivity.this, "Contacts Added Successfully",1000).show();
	        }
	        /*for(String[] scoreData : scoreList ) {
        		itemArrayAdapter.add(scoreData);
    		}*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		contactList = getAllItems();
		itemadapter = new ItemAdapter(MainActivity.this, contactList);
		listView.setAdapter(itemadapter);
	}
	
	public List<ContactsBean> getAllItems(){
			
			List<ContactsBean> contactList = new ArrayList<ContactsBean>();
			String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CONTACT_DETAIL+ " GROUP BY contact_name";
			//String selectQuery = "SELECT count(*) as cnt,* FROM " + DBHelper.TABLE_CONTACT_DETAIL+ " WHERE " + DBHelper.TABLE_NAME +" = '" + table_name +"' GROUP BY item_name";
			
			//String selectQuery = "SELECT *  FROM " + TABLE_HISTORY + " WHERE " + QUERYWORD +  "= '" + word + "' AND " + DISTANCEQ + "= '"+ meter +"' "  ;
			
			//String selectQuery = "SELECT count(*) as cnt,* FROM " + DBHelper.TABLE_ORDER+ " GROUP BY item_name WHERE " + DBHelper.TABLE_NAME + " =? " + new String[] {table_name};
			
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					ContactsBean contact = new ContactsBean();
					Log.e("Count :", cursor.getInt(0)+"");
					
					//0 for getCount-Total Count
					//1 for Name
					//2 for Number
					
					//contact.setCount(cursor.getInt(0));
					contact.setName(cursor.getString(1));
					contact.setNumber(cursor.getString(2));
	
					Log.v("Contact Count :", cursor.getInt(0)+"");
					Log.v("Name - DB :", cursor.getString(1));
					Log.v("Number - DB :", cursor.getString(2));
					
					contactList.add(contact);
				} while (cursor.moveToNext());
				Log.d("Contact List :", contactList+"");
			}
			return contactList;
		}	
}
