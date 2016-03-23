package com.sanmboiboi.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mcexpense.R;
import com.sanmboiboi.activity.AddExpenseActivity;
import com.sanmboiboi.storage.ExpenseInfo;
import com.sanmboiboi.storage.PersonInfo;

public class ListExpenseAdapter extends ArrayAdapter<ExpenseInfo> {
	private final Context mContext;
	private final int layout;
	private ArrayList<ExpenseInfo> data;
	private Holder holder;
	private String now;

	public ListExpenseAdapter(Context context, int resource,
			ArrayList<ExpenseInfo> objects, String n) {
		super(context, resource, objects);
		mContext = context;
		layout = resource;
		data = objects;
		now = n;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = convertView;
		Log.d("LINH", position + "");
		if (row == null) {
			holder = new Holder();
			row = inflater.inflate(layout, null);
			holder.txtLstPeople = (TextView) row
					.findViewById(R.id.main_lstPeople);
			holder.txtBalance = (TextView) row.findViewById(R.id.main_balance);
			holder.txtNote = (TextView) row.findViewById(R.id.main_note);
			holder.imgView = (ImageView) row.findViewById(R.id.main_imgExpense);
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}
		Log.d("LINH", "|date = " + data.get(position).getDate());
		if (data.get(position).getDate().equals(now)) {
			Log.d("LINH", "|equal++");
			holder.txtLstPeople.setText(data.get(position)
					.getStrPeopleParticipate());
			holder.txtNote.setText(data.get(position).getNote());
			holder.txtBalance.setText(data.get(position).getBalance() + "");
			if (!data.get(position).getPath().contains("$")) {
				Log.d("LINH", data.get(position).getPath());
				try {
					Bitmap bm = getRoundedShape(Bitmap.createScaledBitmap(
							decodeUri(Uri.parse(data.get(position).getPath())),
							250, 250, true));
					// holder.imgView.setImageBitmap(decodeUri(Uri.parse(data
					// .get(position).getPath())));
					holder.imgView.setImageBitmap(bm);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d("LINH", data.get(position).getPath());
				int iconPosition = Integer.parseInt(data.get(position)
						.getPath().substring(0, 1));
				switch (iconPosition) {
				case 0:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_food));
					break;
				case 1:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_debt));
					break;
				case 2:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_transport));
					break;
				case 3:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_entertain));
					break;
				case 4:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_shopping));
					break;
				case 5:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_health));
					break;
				case 6:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_love));
					break;
				case 7:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_unidentify));
					break;
				case 8:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.icon_debt));
					break;
				case 9:
					holder.imgView.setImageBitmap(makeBitmapFromRes(mContext, R.drawable.main_add_button));
					break;
				default:
					break;
				}
			}
			return row;
		} else {
			// Log.d("LINH", "|unequal++");
			return row = null;
		}

	}

	// public static Bitmap decodeSampledBitmapFromResource(Resources res,
	// int resId, int reqWidth, int reqHeight) {
	//
	// // First decode with inJustDecodeBounds=true to check dimensions
	// final BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// BitmapFactory.decodeResource(res, resId, options);
	//
	// // Calculate inSampleSize
	// options.inSampleSize = calculateInSampleSize(options, reqWidth,
	// reqHeight);
	//
	// // Decode bitmap with inSampleSize set
	// options.inJustDecodeBounds = false;
	// return BitmapFactory.decodeResource(res, resId, options);
	// }
	//
	// public static int calculateInSampleSize(BitmapFactory.Options options,
	// int reqWidth, int reqHeight) {
	// // Raw height and width of image
	// final int height = options.outHeight;
	// final int width = options.outWidth;
	// int inSampleSize = 1;
	//
	// if (height > reqHeight || width > reqWidth) {
	//
	// final int halfHeight = height / 2;
	// final int halfWidth = width / 2;
	//
	// // Calculate the largest inSampleSize value that is a power of 2 and
	// // keeps both
	// // height and width larger than the requested height and width.
	// while ((halfHeight / inSampleSize) > reqHeight
	// && (halfWidth / inSampleSize) > reqWidth) {
	// inSampleSize *= 2;
	// }
	// }
	//
	// return inSampleSize;
	// }
	
	private static Bitmap makeBitmapFromRes(Context mContext, int res) {
		Bitmap bm;
		bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), res), 150, 150, true);
		return bm;
	}
	private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
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
		BitmapFactory.decodeStream(mContext.getContentResolver()
				.openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 200;

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
		return BitmapFactory.decodeStream(mContext.getContentResolver()
				.openInputStream(selectedImage), null, o2);
	}

	private static class Holder {
		ImageView imgView;
		TextView txtLstPeople;
		TextView txtNote;
		TextView txtBalance;
	}

}
