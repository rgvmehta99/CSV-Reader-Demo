package com.example.demoreadexcelfile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayList extends Activity {

	ListView listView;
	Button delete;
	ItemAdapter itemadapter;
	DBHelper helper;
	SQLiteDatabase db = null;
	List<ContactsBean> contactList = new ArrayList<ContactsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);

		helper = new DBHelper(DisplayList.this);
		db = helper.getWritableDatabase();

		listView = (ListView) findViewById(R.id.list);
		delete = (Button) findViewById(R.id.delete);

		contactList = getAllItems();
		itemadapter = new ItemAdapter(DisplayList.this, contactList);
		listView.setAdapter(itemadapter);

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						DisplayList.this);

				alertDialog.setTitle("Delete Confirmation ??");
				alertDialog
						.setMessage("Are you sure want to Delete all Data/Records?");

				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								db.execSQL("delete from "
										+ DBHelper.TABLE_CONTACT_DETAIL);
								Toast.makeText(DisplayList.this,
										"Deleted all Data SucessFuly", 2000).show();
								itemadapter.notifyDataSetChanged();
								startActivity(getIntent());
							}
						});

				alertDialog.setNegativeButton("Cancle",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				AlertDialog al = alertDialog.create();
				al.show();

			}
		});

	}

	public List<ContactsBean> getAllItems() {

		List<ContactsBean> contactList = new ArrayList<ContactsBean>();
		String selectQuery = "SELECT * FROM " + DBHelper.TABLE_CONTACT_DETAIL
				+ " GROUP BY contact_name";

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				ContactsBean contact = new ContactsBean();
				Log.e("Count :", cursor.getInt(0) + "");

				// 0 for getCount-Total Count
				// 1 for Name
				// 2 for Number

				// contact.setCount(cursor.getInt(0));
				contact.setName(cursor.getString(1));
				contact.setNumber(cursor.getString(2));

				Log.v("Contact Count :", cursor.getInt(0) + "");
				Log.v("Name - DB :", cursor.getString(1));
				Log.v("Number - DB :", cursor.getString(2));

				contactList.add(contact);
			} while (cursor.moveToNext());
			Log.d("Contact List :", contactList + "");
		}
		return contactList;
	}
}
