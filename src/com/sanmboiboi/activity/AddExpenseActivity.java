package com.sanmboiboi.activity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcexpense.R;
import com.sanmboiboi.adapter.GridPersonAdapter;
import com.sanmboiboi.storage.ExpenseInfo;
import com.sanmboiboi.storage.PersonInfo;

public class AddExpenseActivity extends Activity {

	// public static int[] lstIconExp = { R.drawable.icon_add_person,
	// R.drawable.icon_debt, R.drawable.icon_entertain,
	// R.drawable.icon_food, R.drawable.icon_health, R.drawable.icon_home,
	// R.drawable.icon_love, R.drawable.icon_note, R.drawable.icon_note_2,
	// R.drawable.icon_other, R.drawable.icon_question,
	// R.drawable.icon_shopping, R.drawable.icon_transport,
	// R.drawable.icon_unidentify };
	public static boolean isSelfPaid = false;
	private static int iconPosition;
	private Button addExp_chooseCategory;
	private Button addExp_btnSave;
	private Button addExp_money;
	private Button addExp_datePicker;
	private ImageView addExp_person;
	private ImageView addExp_imgCategory;
	private ImageView addExp_imgView_alt;
	private ImageView addExp_imgView;
	private ImageView addExp_imgNote;
	private GridView addExp_gridPerson;
	private EditText addExp_note;

	private Context mContext;

	private int year, month, day;
	private StringBuilder now;
	private static String imgPath = "";

	public static HashMap<String, Integer> dataCheck;
	private ArrayList<String> dataRefine;

	private GridPersonAdapter gAdapter;
	private Dialog addMoneyDialog;
	private EditText d_edtMoney;

	private final int REQUEST_CODE_FROM_LIST_CATEGORY = 1;
	private final int REQUEST_CODE_FROM_LIST_PERSON = 2;
	private final int REQUEST_CODE_FROM_GALLERY = 3;
	private final int DATE_DIALOG_ID = 0;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_expense);
		super.onCreate(savedInstanceState);
		mContext = this;
		addExp_chooseCategory = (Button) findViewById(R.id.addExp_chooseCategory);
		addExp_imgCategory = (ImageView) findViewById(R.id.addExp_imgCategory);
		addExp_money = (Button) findViewById(R.id.addExp_money);
		addExp_person = (ImageView) findViewById(R.id.addExp_person);
		addExp_gridPerson = (GridView) findViewById(R.id.addExp_gridPerson);
		addExp_datePicker = (Button) findViewById(R.id.addExp_datePicker);
		addExp_imgView = (ImageView) findViewById(R.id.addExp_imgView);
		addExp_imgView_alt = (ImageView) findViewById(R.id.addExp_imgView_alt);
		addExp_imgNote = (ImageView) findViewById(R.id.addExp_imgNote);
		addExp_note = (EditText) findViewById(R.id.addExp_note);
		addExp_btnSave = (Button) findViewById(R.id.addExp_btnSave);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		now = new StringBuilder().append(day).append("/").append(month + 1)
				.append("/").append(year);
		addExp_datePicker.setText(now);

		dataCheck = new HashMap<String, Integer>();
		dataRefine = new ArrayList<String>();

		addMoneyDialog = new Dialog(mContext);
		addMoneyDialog.setContentView(R.layout.dialog_add_money);
		addMoneyDialog.setTitle("Add Money");
		addMoneyDialog.getWindow().setLayout(1000, 800);

		d_edtMoney = (EditText) addMoneyDialog.findViewById(R.id.d_edtMoney);

		// Action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_back_actionbar, null);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText("Thêm chi tiêu");
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

		/* Listener */
		addExp_btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExpenseInfo e = new ExpenseInfo();
				e.setCategory(addExp_chooseCategory.getText().toString());
				double eBalance = Double.parseDouble(addExp_money.getText()
						.toString());
				e.setBalance(eBalance);
				// people
				String lstPeople = "";
				for (String str : dataRefine) {
					lstPeople += str + "/";
				}
				e.setStrPeopleParticipate(lstPeople);
				e.setDate(now.toString());
				e.setPath(imgPath);
				e.setNote(addExp_note.getText().toString());
				if (e.getBalance() > 0)
					MainActivity.eDb.insert(e);

				/* Update data for people */
				int count = dataRefine.size();
				Log.d("LINH", "Number of people " + count);
				double pBalance = eBalance / count;
				Log.d("LINH", "Balance for each person " + pBalance);
				String arrPName[] = lstPeople.split("/");
				for (String tmpPName : arrPName) {
					for (PersonInfo p : MainActivity.data) {
						if (p.getName().equals(tmpPName)) {
							double tmpBlc = p.getBalance();
							p.setBalance(tmpBlc + pBalance);
							Log.d("MCE", "Update " + p.getName()
									+ " with blc = " + (tmpBlc + pBalance));
							MainActivity.db.update(p);
							break;
						}
					}
				}

				if (isSelfPaid) {
					Log.d("MCExpense", "isSelfPaid " + isSelfPaid);
					String tmpName = addExp_note.getText().toString()
							.toLowerCase();
					for (PersonInfo p : MainActivity.data) {
						if (p.getName().toLowerCase().equals(tmpName)) {
							double tmpBlc = p.getBalance();
							p.setBalance(tmpBlc-eBalance); // subtract self paid 
							MainActivity.db.update(p);
							Log.d("MCExpense", "whoSelfPaid " + p.getName());
						}
					}
				}

				SharedPreferences sp = getSharedPreferences(
						MainActivity.BALANCE_PREF, 0);
				float tmpBalance = sp.getFloat(MainActivity.BALANCE_SUM, 0);
				sp.edit()
						.putFloat(MainActivity.BALANCE_SUM,
								(float) (tmpBalance + eBalance)).commit();
				Intent i = new Intent();
				i.putExtra("date", e.getDate());
				setResult(RESULT_OK, i);
				finish();
			}
		});
		addExp_imgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType("image/*");
				startActivityForResult(i, REQUEST_CODE_FROM_GALLERY);
			}
		});
		addExp_datePicker.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		addExp_chooseCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						ListCategoryActivity.class);
				startActivityForResult(i, REQUEST_CODE_FROM_LIST_CATEGORY);
			}
		});

		addExp_person.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, ListPersonActivity.class);
				startActivityForResult(i, REQUEST_CODE_FROM_LIST_PERSON);
			}
		});

		addExp_gridPerson.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("LINH",
						"AddExpenseActivity|remove item = "
								+ dataRefine.get(pos));
				dataRefine.remove(pos);
				gAdapter.notifyDataSetChanged();
			}
		});

		addExp_money.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addMoneyDialog.show();
				d_edtMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
				final InputMethodManager im = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				d_edtMoney.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						im.showSoftInput(d_edtMoney, 0);
					}
				});

				d_edtMoney.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_ENTER) {
							Log.d("LINH",
									"AddExpenseActivity|button done pressed!");
							addExp_money.setText(d_edtMoney.getText() + "");
							im.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							addMoneyDialog.dismiss();
						}
						return false;
					}
				});
			}
		});
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(mContext, mDatePickerListener, year,
					month, day);

		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	private final DatePickerDialog.OnDateSetListener mDatePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int _year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			year = _year;
			month = monthOfYear;
			day = dayOfMonth;
			now = new StringBuilder().append(day).append("/").append(month + 1)
					.append("/").append(year);
			addExp_datePicker.setText(now);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUEST_CODE_FROM_LIST_CATEGORY:
			if (data != null) {
				String category = data.getStringExtra("category");
				addExp_chooseCategory.setText(category);
				int position = data.getIntExtra("pos", 0);
				iconPosition = position;
				imgPath = iconPosition + "$";
				switch (position) {
				case 0:
					addExp_imgCategory.setImageResource(R.drawable.icon_food);
					break;
				case 1:
					addExp_imgCategory.setImageResource(R.drawable.icon_debt);
					isSelfPaid = true;
					break;
				case 2:
					addExp_imgCategory
							.setImageResource(R.drawable.icon_transport);
					break;
				case 3:
					addExp_imgCategory
							.setImageResource(R.drawable.icon_entertain);
					break;
				case 4:
					addExp_imgCategory
							.setImageResource(R.drawable.icon_shopping);
					break;
				case 5:
					addExp_imgCategory.setImageResource(R.drawable.icon_health);
					break;
				case 6:
					addExp_imgCategory.setImageResource(R.drawable.icon_love);
					break;
				case 7:
					addExp_imgCategory
							.setImageResource(R.drawable.icon_unidentify);
					break;
				case 8:
					addExp_imgCategory.setImageResource(R.drawable.icon_debt);
					break;
				case 9:
					addExp_imgCategory
							.setImageResource(R.drawable.main_add_button);
					break;
				default:
					break;
				}
			}
			break;
		case REQUEST_CODE_FROM_LIST_PERSON:
			dataRefine.clear();
			MainActivity.data = MainActivity.db.getall();
			for (PersonInfo p : MainActivity.data) {
				if (dataCheck.get(p.getName()) == 1) {
					dataRefine.add(p.getName());
					Log.d("LINH",
							"AddExpenseActivity|checked item = " + p.getName());
				}
			}

			gAdapter = new GridPersonAdapter(mContext,
					R.layout.activity_add_expense_grid_person_item, dataRefine);
			addExp_gridPerson.setAdapter(gAdapter);
			break;

		case REQUEST_CODE_FROM_GALLERY:
			if (data != null) {
				Uri selectedImage = data.getData();
				Log.d("LINH", "AddExpenseActivity|imgUriPath = "
						+ selectedImage);
				try {
					addExp_imgView.setImageBitmap(decodeUri(selectedImage));
					addExp_imgView_alt.setVisibility(View.GONE);
					imgPath = selectedImage.toString();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				imgPath = iconPosition + "$";
				Log.d("LINH", "REQUEST_CODE_FROM_GALLERY " + imgPath);
				iconPosition = 0;
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}
}
