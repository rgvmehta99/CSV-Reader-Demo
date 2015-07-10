package com.example.demoreadexcelfile;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAdapter extends BaseAdapter {

	Context contect;
	List<ContactsBean> contactList;
	DBHelper helper;
	SQLiteDatabase db;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	public ItemAdapter(Context context, List<ContactsBean> contactList) {
		// TODO Auto-generated constructor stub
		this.contect = context;
		this.contactList = contactList;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = preferences.edit();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView txtName, txtNumber;
		Button delete;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) contect
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater
					.inflate(R.layout.adapter_item, parent, false);

			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtNumber = (TextView) convertView
					.findViewById(R.id.txtNumber);
			holder.delete = (Button) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName.setText(contactList.get(position).getName());
		holder.txtNumber.setText(contactList.get(position).getNumber());
		holder.txtNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						contect);
				alertDialogBuilder.setTitle("Message");
				alertDialogBuilder
						.setMessage(
								"Do you want to call Mr."
										+ contactList.get(position).getName()
										+ " on "
										+ contactList.get(position).getNumber()
										+ " number ?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(
												Intent.ACTION_CALL);
										intent.setData(Uri.parse("tel:"
												+ holder.txtNumber.getText()
														.toString()));
										contect.startActivity(intent);
									}
								})

						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			}
		});

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.execSQL("delete from " + DBHelper.TABLE_CONTACT_DETAIL
						+ "  where contact_name = '"
						+ contactList.get(position).getName() + "'");
				Toast.makeText(contect, "Deleted one Row SucessFuly", 2000)
						.show();

				contactList.remove(position);
				notifyDataSetChanged();
				// Refresh Content After click Remove Button
				Intent i = new Intent(contect, DisplayList.class);
				contect.startActivity(i);
			}
		});
		return convertView;
	}

}
