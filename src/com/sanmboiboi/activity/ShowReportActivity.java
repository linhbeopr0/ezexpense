package com.sanmboiboi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.mcexpense.R;
import com.sanmboiboi.adapter.ShowReportAdapter;

public class ShowReportActivity extends Activity {

	private ListView rp_lstView;
	private ShowReportAdapter rp_adapter;

	private Context mContext;
	private final String TAG = "LINH";
	private final int REQUEST_CODE_UPDATE_DB = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_show_report);
		mContext = this;
		MainActivity.data = MainActivity.db.getall();
		rp_lstView = (ListView) findViewById(R.id.rp_lstView);
		rp_adapter = new ShowReportAdapter(mContext,
				R.layout.activity_show_report_item, MainActivity.data);
		rp_lstView.setAdapter(rp_adapter);
		rp_lstView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				final int p = pos;
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Delete?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.db.delete(MainActivity.data.get(p));
								MainActivity.data = MainActivity.db.getall();
								Log.d("LINH", "ShowReportActivity|data.size = "
										+ MainActivity.data.size());
								rp_adapter = new ShowReportAdapter(mContext,
										R.layout.activity_show_report_item,
										MainActivity.data);
								rp_lstView.setAdapter(rp_adapter);
							}
						});

				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

				AlertDialog aDialog = builder.create();
				aDialog.show();

				return true;
			}

		});
		rp_lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent i = new Intent(mContext, AddPersonActivity.class);
				i.putExtra("mode", "update");
				i.putExtra("id", MainActivity.data.get(pos).getId());
				startActivityForResult(i, REQUEST_CODE_UPDATE_DB);
			}
		});

		// Action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_back_actionbar, null);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText("Tổng kết");

		ImageView btnBack = (ImageView) v.findViewById(R.id.btnBackAcb);
		@SuppressWarnings("deprecation")
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		actionBar.setCustomView(v, lp);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume()");
		rp_adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_UPDATE_DB) {
			MainActivity.data = MainActivity.db.getall();
			rp_adapter = new ShowReportAdapter(mContext,
					R.layout.activity_show_report_item, MainActivity.data);
			rp_lstView.setAdapter(rp_adapter);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
		super.onBackPressed();
	}
}
