package com.sanmboiboi.activity;

import java.io.FileNotFoundException;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcexpense.R;
import com.sanmboiboi.storage.PersonInfo;

public class AddPersonActivity extends Activity {

	private EditText add_edtName;
	private EditText add_edtBalance;
	private Button add_btnSave;
	private Button add_btnCancel;
	private ImageView add_img;
	private PersonInfo p;
	private static String path = "";
	private Context mContext = this;
	private String mode;
	public static int REQUEST_CODE_FROM_GALLERY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add_person);
		add_edtBalance = (EditText) findViewById(R.id.add_edtBalance);
		add_edtName = (EditText) findViewById(R.id.add_edtName);
		add_btnSave = (Button) findViewById(R.id.add_btnSave);
		add_btnCancel = (Button) findViewById(R.id.add_btnCancel);
		add_img = (ImageView) findViewById(R.id.add_img);
		p = new PersonInfo();
		mContext = this;
		mode = getIntent().getStringExtra("mode");
		Log.d("MCExpense", "AddPersonActivity|mode = " + mode);
		if (mode.equals("update")) {
			/* load data with id */
			PersonInfo p = MainActivity.db
					.get(getIntent().getIntExtra("id", 0));
			Log.d("MCExpense", "AddPersonActivity|path = " + p.getPath());
			String tmpPath = p.getPath();
			if (tmpPath.equals("")) {
				Bitmap tmpBitmap = getRoundedShape(BitmapFactory
						.decodeResource(mContext.getResources(),
								R.drawable.icon_avatar_default_2));
				add_img.setImageBitmap(tmpBitmap);
			} else {
				try {
					add_img.setImageBitmap(getRoundedShape(decodeUri(Uri
							.parse(tmpPath))));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			add_edtName.setText(p.getName());
			add_edtBalance.setText(p.getBalance() + "");
		}

		// Action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_back_actionbar, null);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText("Thêm mem");
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

		if (mode.equals("insert")) {
			add_img.setImageBitmap(getRoundedShape(BitmapFactory
					.decodeResource(getResources(),
							R.drawable.icon_avatar_default_2)));
		}
		// Listener
		add_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType("image/*");
				startActivityForResult(i, REQUEST_CODE_FROM_GALLERY);
			}
		});

		add_btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		add_btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String pName = add_edtName.getText().toString();
				String pBalance = add_edtBalance.getText().toString();

				if (!pName.equals("")) {
					p.setName(pName);
				}

				if (!pBalance.equals("")) {
					p.setBalance(Double.parseDouble(pBalance));
				}

				p.setPath(path);
				Log.d("MCE", "path " + p.getPath());

				if (mode.equals("insert")) {
					if (!p.getName().equals("")) {
						MainActivity.db.insert(p);
					}
					Intent i = new Intent(getApplicationContext(),
							ShowReportActivity.class);
					startActivity(i);
					finish();
				}
				if (mode.equals("update")) {
					int id = getIntent().getIntExtra("id", -1);

					for (PersonInfo _p : MainActivity.data) {
						if (_p.getId() == id) {
							p.setId(id);
							Log.d("LINH", "AddPersonActivity|id = " + p.getId()
									+ " name = " + p.getName());
							MainActivity.db.update(p);
						}
					}
					setResult(RESULT_OK);
					finish();
				}
			}
		});

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (data != null) {
				Uri selectedImage = data.getData();
				Log.d("LINH", "AddExpenseActivity|imgUriPath = "
						+ selectedImage);
				try {
					add_img.setImageBitmap(getRoundedShape(decodeUri(selectedImage)));
					String tmpPath = selectedImage.toString();
					path = tmpPath;
					Log.d("MCE", "Uri get " + tmpPath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 150;
		int targetHeight = 150;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 250;

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
