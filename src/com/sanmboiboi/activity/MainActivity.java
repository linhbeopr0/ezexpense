package com.sanmboiboi.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mcexpense.R;
import com.sanmboiboi.adapter.ListExpenseAdapter;
import com.sanmboiboi.adapter.ShowReportAdapter;
import com.sanmboiboi.adapter.SlidingMenuAdapter;
import com.sanmboiboi.slidingmenu.SlidingMenu;
import com.sanmboiboi.slidingmenu.SlidingMenuView;
import com.sanmboiboi.storage.ExpenseDB;
import com.sanmboiboi.storage.ExpenseInfo;
import com.sanmboiboi.storage.PersonDB;
import com.sanmboiboi.storage.PersonInfo;

import com.fxgear.engine.physics.FXPaper;
import com.fxgear.engine.physics.PhysGLSurface;
import com.fxgear.engine.physics.PhysThread;
import com.samsung.graphics.GLAnimation;
import com.samsung.graphics.GLAnimation.AnimationListenerWithAniInfo;
import com.samsung.graphics.GLAnimation.AnimationListenerWithPageInfo;
import com.samsung.graphics.GLAnimationInfo;
import com.samsung.graphics.GLAnimationInfo.AnimationType;
import com.samsung.uieffect.pte.PTERenderer;
import com.samsung.uieffect.pte.PTEView;
import com.samsung.uieffect.pte.PTEView.TouchListener;
import com.samsung.uieffect.pte.PhysPTEView;
import com.samsung.uieffect.pte.PhysPTEView.PageMaterial;
import com.samsung.uieffect.pte.PhysPTEView.UpdateListener;
import com.samsung.uieffect.pte.PhysPageLoader;
import com.samsung.uieffect.pte.PTERenderer.PageType;
import com.samsung.uieffect.pte.PhysPTEView.StatusChangeListener;
import com.samsung.uieffect.pte.physpte.PhysPTEViewForStoryAlbum;

public class MainActivity extends Activity implements UpdateListener,
		TouchListener, com.samsung.graphics.GLAnimation.AnimationListener,
		AnimationListenerWithAniInfo, AnimationListenerWithPageInfo,
		PhysPageLoader {

	private SlidingMenuView mMenu;
	private Context mContext;
	private final String[] menuData = { "Thêm người", "Tổng kết", "Tìm hiểu",
			"Khởi tạo lại" };
	// , "Export", "Import", "Trend", "About" };
	private SlidingMenuAdapter slideMenuAdapter;
	private ImageView btnMenu;
	private ImageView btnViewBy;
	private ImageView btnAdd;
	private ImageView btnPrevDate;
	private ImageView btnNxtDate;

	private TextView txtBalance;
	private TextView txtDate;
	private ActionBar actionBar;
	private ListView lstExpense;
	private ListExpenseAdapter lAdapter;

	private int year, month, day;
	private static int currentMonth;
	private StringBuilder now;
	private static String date;
	private float lastX;

	private final String TAG = "LINH";
	public static PersonDB db;
	public static ExpenseDB eDb;
	public static int dbVersion = 1;
	public static int eDbVersion = 1;
	public static ArrayList<PersonInfo> data = new ArrayList<PersonInfo>();
	public static ArrayList<ExpenseInfo> eData = new ArrayList<ExpenseInfo>();
	private ArrayList<ExpenseInfo> tmpEData = new ArrayList<ExpenseInfo>();

	private final int REQUEST_CODE_ADD_EXPENSE_ACTIVITY = 0;
	public static String BALANCE_PREF = "balance_pref";
	public static String BALANCE_SUM = "balance_sum";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.activity_main);
		mContext = this;
		db = new PersonDB(mContext, dbVersion);
		eDb = new ExpenseDB(mContext, eDbVersion);
		data = db.getall();
		eData = eDb.getall();
		Log.d(TAG, "data.size = " + data.size());
		Log.d(TAG, "eData.size = " + eData.size());

		/* set date */
		btnPrevDate = (ImageView) findViewById(R.id.btnPrevDate);
		btnNxtDate = (ImageView) findViewById(R.id.btnNxtDate);
		txtDate = (TextView) findViewById(R.id.txtDate);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		now = new StringBuilder().append(day).append("/").append(month + 1)
				.append("/").append(year);
		currentMonth = month;
		date = now.toString();
		txtDate.setText(date);
		txtDate.setText("Hôm nay");

		/* set list item */
		lstExpense = (ListView) findViewById(R.id.lstItem);
		tmpEData = filterData(eData, date);
		Log.d("LINH", "FUCKING tmp = " + tmpEData.size());
		lAdapter = new ListExpenseAdapter(mContext,
				R.layout.activity_main_list_expense, tmpEData, date);
		lstExpense.setAdapter(lAdapter);
		listViewAnimation();

		lstExpense.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("LINH", "Click");

			}
		});

		btnPrevDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int checkDay = Calendar.getInstance().get(Calendar.DATE);

				if (month == 0 || month == 2 || month == 4 || month == 6
						|| month == 7 || month == 9 || month == 11) {
					if (day == 1) {
						month -= 1;
						day = 30;
					} else
						day -= 1;
				} else if (month == 1 || month == 3 || month == 5 || month == 7
						|| month == 8 || month == 10) {
					if (day == 1) {
						month -= 1;
						day = 31;
					} else
						day -= 1;
				}

				now = new StringBuilder().append(day).append("/")
						.append(month + 1).append("/").append(year);
				date = now.toString();
				if (day == (checkDay - 1) && currentMonth == month) {
					txtDate.setText("Hôm qua");
				} else if (day == (checkDay + 1) && currentMonth == month) {
					txtDate.setText("Ngày mai");
				} else if (day == checkDay && currentMonth == month) {
					txtDate.setText("Hôm nay");
				} else {
					txtDate.setText(date);
				}
				tmpEData.clear();
				tmpEData = filterData(eData, date);
				Log.d("LINH", "FUCKING tmp in prev = " + tmpEData.size());
				lAdapter = new ListExpenseAdapter(mContext,
						R.layout.activity_main_list_expense, tmpEData, date);
				lstExpense.setAdapter(lAdapter);
				lAdapter.notifyDataSetChanged();
				listViewAnimation();
			}
		});
		btnNxtDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int checkDay = Calendar.getInstance().get(Calendar.DATE);

				if (month == 0 || month == 2 || month == 4 || month == 6
						|| month == 7 || month == 9 || month == 11) {
					if (day == 31) {
						month += 1;
						day = 1;
					} else
						day += 1;
				} else if (month == 1 || month == 3 || month == 5 || month == 7
						|| month == 8 || month == 10) {
					if (day == 30) {
						month += 1;
						day = 1;
					} else
						day += 1;
				}
				now = new StringBuilder().append(day).append("/")
						.append(month + 1).append("/").append(year);
				date = now.toString();
				if (day == (checkDay - 1) && currentMonth == month) {
					txtDate.setText("Hôm qua");
				} else if (day == (checkDay + 1) && currentMonth == month) {
					txtDate.setText("Ngày mai");
				} else if (day == checkDay && currentMonth == month) {
					txtDate.setText("Hôm nay");
				} else {
					txtDate.setText(date);
				}
				tmpEData.clear();
				tmpEData = filterData(eData, date);
				Log.d("LINH", "FUCKING tmp in next = " + tmpEData.size());
				lAdapter = new ListExpenseAdapter(mContext,
						R.layout.activity_main_list_expense, tmpEData, date);
				lstExpense.setAdapter(lAdapter);
				lAdapter.notifyDataSetChanged();
				listViewAnimation();
			}
		});

		/* listExpense listener */
		lstExpense.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				final int p = pos;
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Delete?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								/* Update data for people */
								ExpenseInfo e = MainActivity.eData.get(p);
								String arrPName[] = e.getStrPeopleParticipate()
										.split("/");
								int count = arrPName.length;
								Log.d("LINH", "[DEL]Number of people " + count);
								double pBalance = e.getBalance() / count;
								Log.d("LINH",
										"[DEL]Balance sum " + e.getBalance());
								Log.d("LINH", "[DEL]Balance for each person "
										+ pBalance);

								for (String tmpPName : arrPName) {
									for (PersonInfo p : MainActivity.data) {
										if (p.getName().equals(tmpPName)) {
											double tmpBlc = p.getBalance();
											p.setBalance(tmpBlc - pBalance);
											Log.d("MCE",
													"[DEL]Update substract "
															+ p.getName()
															+ " with blc = "
															+ (tmpBlc - pBalance));
											MainActivity.db.update(p);
											break;
										}
									}
								}

								SharedPreferences sp = getSharedPreferences(
										MainActivity.BALANCE_PREF, 0);
								float tmpBalance = sp.getFloat(BALANCE_SUM, 0);
								sp.edit()
										.putFloat(
												MainActivity.BALANCE_SUM,
												(float) (tmpBalance - e
														.getBalance()))
										.commit();
								txtBalance.setText((tmpBalance - e.getBalance())
										+ "");
								/* Delete */
								MainActivity.eDb.delete(MainActivity.eData
										.get(p));
								MainActivity.eData = MainActivity.eDb.getall();
								lAdapter = new ListExpenseAdapter(mContext,
										R.layout.activity_main_list_expense,
										MainActivity.eData, date);
								lstExpense.setAdapter(lAdapter);

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

		/* init Sliding Menu */
		mMenu = new SlidingMenuView(mContext);
		slideMenuAdapter = new SlidingMenuAdapter(mContext,
				R.layout.activity_main_sliding_menu_item, menuData);
		mMenu.lstSlidingMenu.setAdapter(slideMenuAdapter);
		mMenu.menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);

		/* init layout element */
		btnAdd = (ImageView) findViewById(R.id.btnAdd);

		/* init action bar */
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_main_actionbar, null);
		btnMenu = (ImageView) v.findViewById(R.id.btnSlidingMenu);
		btnViewBy = (ImageView) v.findViewById(R.id.btnViewBy);
		txtBalance = (TextView) v.findViewById(R.id.txtBalance);

		// get summary balance
		SharedPreferences sp = getSharedPreferences(BALANCE_PREF, 0);
		float balance = sp.getFloat(BALANCE_SUM, 0);
		Log.d("LINH", "Total balance = " + balance);
		txtBalance.setText(balance + "");

		@SuppressWarnings("deprecation")
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		actionBar.setCustomView(v, lp);

		/* listener */
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						AddExpenseActivity.class);
				startActivityForResult(i, REQUEST_CODE_ADD_EXPENSE_ACTIVITY);
			}
		});

		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMenu.menu.toggle();
			}
		});

		mMenu.lstSlidingMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i;
				switch (pos) {
				case 0:
					i = new Intent(getApplicationContext(),
							AddPersonActivity.class);
					i.putExtra("mode", "insert");
					startActivity(i);
					break;

				case 1:
					i = new Intent(getApplicationContext(),
							ShowReportActivity.class);
					startActivity(i);
					break;

				case 2:
					break;

				case 3:
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setTitle("Bạn muốn khởi tạo lại tất cả?");
					builder.setPositiveButton("Có",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									for (PersonInfo p : MainActivity.data) {
										p.setBalance(0);
										MainActivity.db.update(p);
									}
									for (ExpenseInfo e : MainActivity.eData) {
										MainActivity.eDb.delete(e);
									}
									SharedPreferences sp = getSharedPreferences(
											BALANCE_PREF, 0);
									sp.edit()
											.putFloat(MainActivity.BALANCE_SUM,
													0).commit();
									txtBalance.setText("0");

									mMenu.menu.toggle();
									lAdapter = new ListExpenseAdapter(
											mContext,
											R.layout.activity_main_list_expense,
											tmpEData, date);
									lstExpense.setAdapter(lAdapter);
								}
							});

					builder.setNegativeButton("Không",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});

					Dialog dialog = builder.create();
					dialog.show();
					break;

				default:
					break;
				}

			}
		});

	}

	public void listViewAnimation() {
		Animation listNoteSet = AnimationUtils.loadAnimation(mContext,
				R.anim.show_main_view);
		LayoutAnimationController listControl = new LayoutAnimationController(
				listNoteSet);
		listControl.setDelay(0.2f);
		lstExpense.setLayoutAnimation(listControl);
		lstExpense.startLayoutAnimation();
		lstExpense.startAnimation(listNoteSet);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_ADD_EXPENSE_ACTIVITY:
			if (data != null) {
				date = data.getStringExtra("date");
				eData = eDb.getall();
				tmpEData.clear();
				tmpEData = filterData(eData, date);
				Log.d("LINH",
						"FUCKING tmp in onactivityresult = " + tmpEData.size());
				lAdapter = new ListExpenseAdapter(mContext,
						R.layout.activity_main_list_expense, tmpEData, date);
				lstExpense.setAdapter(lAdapter);
				SharedPreferences sp = getSharedPreferences(BALANCE_PREF, 0);
				txtBalance.setText(sp.getFloat(BALANCE_SUM, 0) + "");
			}
			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// Log.d("LINH", "ACTION_DOWN");
	// lastX = event.getX();
	// break;
	//
	// case MotionEvent.ACTION_UP:
	// Log.d("LINH", "ACTION_UP");
	// float currentX = event.getX();
	//
	// // left to right swipe
	// if (lastX < currentX) {
	// Log.d("LINH", "swipe from left to right");
	// day -= 1;
	// now = new StringBuilder().append(day).append("/")
	// .append(month + 1).append("/").append(year);
	// date = now.toString();
	// txtDate.setText(date);
	//
	// /* set list item */
	// lstExpense = (ListView) findViewById(R.id.lstItem);
	// lAdapter = new ListExpenseAdapter(mContext,
	// R.layout.activity_main_list_expense,
	// MainActivity.eData, date);
	// lstExpense.setAdapter(lAdapter);
	// // if (viewFlipper.getDisplayedChild() == 0)
	// // break;
	// //
	// // // set the required Animation type to viewFlipper
	// // // The next screen will come in from Left and the current
	// // screen
	// // // will go out to right
	// // viewFlipper.setInAnimation(this, R.anim.in_from_left);
	// // viewFlipper.setOutAnimation(this, R.anim.out_to_right);
	// // viewFlipper.showNext();
	// }
	//
	// if (lastX > currentX) {
	// Log.d("LINH", "swipe from left to right");
	// day += 1;
	// now = new StringBuilder().append(day).append("/")
	// .append(month + 1).append("/").append(year);
	// date = now.toString();
	// txtDate.setText(date);
	//
	// /* set list item */
	// lstExpense = (ListView) findViewById(R.id.lstItem);
	// lAdapter = new ListExpenseAdapter(mContext,
	// R.layout.activity_main_list_expense,
	// MainActivity.eData, date);
	// lstExpense.setAdapter(lAdapter);
	// // if (viewFlipper.getDisplayedChild() == 1)
	// // break;
	// // // set the required Animation type to ViewFlipper
	// // // The Next screen will come in form Right and current Screen
	// // // will go OUT to Left
	// // viewFlipper.setInAnimation(this, R.anim.in_from_right);
	// // viewFlipper.setOutAnimation(this, R.anim.out_to_left);
	// // // Show The Previous Screen
	// // viewFlipper.showPrevious();
	// }
	// break;
	//
	// default:
	// break;
	// }
	//
	// return super.onTouchEvent(event);
	// }

	private ArrayList<ExpenseInfo> filterData(ArrayList<ExpenseInfo> data,
			String now) {
		ArrayList<ExpenseInfo> tmpData = new ArrayList<ExpenseInfo>();
		Log.d("LINH", "filterData|now " + now + " - data size " + data.size());
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getDate().equals(now)) {
				Log.d("LINH", "!!! " + data.get(i).getCategory());
				tmpData.add(data.get(i));
			}
		}
		Log.d("LINH", "After refine = " + tmpData.size());
		return tmpData;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// finish();
		super.onBackPressed();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		// mMenu.menu.toggle();
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * HANDLE PAGE FLIPPING
	 */

	final private boolean TEST_REGION_PAGE = false;
	public PhysPTEViewForStoryAlbum mPTEView;
	private boolean mOrthogonal = true;
	private Rect[] mUVMappedRects = null;

	private FrameLayout mTestFrameLayout = null;
	private MyView mTestView = null;
	private OnTouchListener mTouchListener = null;
	private boolean mIsPTEViewReady = false;
	private Bitmap[] mBitmapList = null;
	private Bitmap mChangedDummyBitmap = null;
	private PhysPTEView.PageMaterial[] mMaterials = null;
	private int mLastPage = 0;
	private boolean mAutoHide = true;
	private int mWidth, mHeight, mStatusBarHeight;

	private int mCurrPage;

	public static Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.WHITE);
		view.draw(canvas);
		return returnedBitmap;
	}

	class MyView extends View {

		public MyView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawColor(0xffffff00);

			int x = (int) (canvas.getWidth() / 2.0f);
			int y = (int) (canvas.getHeight() / 2.0f);
			Paint p = new Paint();

			p.setTextSize(100);
			p.setTextAlign(Align.CENTER);
			p.setColor(0xff000000);

			canvas.drawText("StoryAlbum PTE Demo", x, y, p);
		}

	}

	protected void initView() {
		mPTEView = new PhysPTEViewForStoryAlbum(this);
		mPTEView.initialize(true, 8, 8); // translucent, desire depth bit,
											// desire stencil bit
		mPTEView.setUpdateListener(this);
		mPTEView.setTouchListener(this);
		mPTEView.setUseTouchInteraction(true);
		mPTEView.setAnimationListener(this);
		mPTEView.setAnimationListenerWithInfo(this);
		mPTEView.setAnimationListenerWithPageInfo(this);
		mPTEView.setPageLoader(this);
		mPTEView.setPageLoop(false);
		mPTEView.setLogMessage(true);

		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		mStatusBarHeight = (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH ? 50
				: (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH ? 38
						: (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM ? 25
								: (metrics.densityDpi == DisplayMetrics.DENSITY_LOW ? 19
										: 50))));
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mWidth = metrics.widthPixels;
			mHeight = metrics.heightPixels;

			Point offset = new Point(mWidth / 2, 0), pageSize = new Point(
					mWidth / 2, mHeight - mStatusBarHeight);
			mPTEView.setPageRegion(offset, pageSize);
			mPTEView.setPageType(PageType.ONE_PAGE);
		} else {
			mWidth = metrics.heightPixels;
			mHeight = metrics.widthPixels;

			Point offset = new Point(mHeight / 2, mWidth / 10
					- mStatusBarHeight / 2), pageSize = new Point(
					mHeight * 4 / 10, mWidth * 8 / 10);
			mPTEView.setPageRegion(offset, pageSize);
			mPTEView.setPageType(PageType.TWO_PAGE);
		}

		mPTEView.setStatusChangeListener(new StatusChangeListener() {
			@Override
			public void onStatusChanged(int status, int page, boolean right) {
				// if (status == -1) mPTEView.changePageBitmap(page,
				// mBitmapList[page % mBitmapList.length], mBitmapList[(page+1)
				// % mBitmapList.length]);
				mLastPage = page;
				String result = status == 1 ? " page starts flipping"
						: " page ends flipping";
				if (right)
					result += " to right";
				else
					result += " to left";
				Log.v(TAG, Integer.toString(page) + result);
				if ((status == -1) && !right)
					mCurrPage = page + 2;
				if ((status == -1) && right)
					mCurrPage = page;
			}
		});

		mPTEView.setZOrderOnTop(true); // necessary
		mPTEView.setVisible(!mAutoHide);

		mTestFrameLayout = new FrameLayout(this);
		mTestView = new MyView(this);

		mTestFrameLayout.addView(mPTEView);
		mTestFrameLayout.addView(mTestView);

		mTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mPTEView.getIsReadyToTouchEvent()) {
					// If touch, mPTEView is showed
					if (event.getAction() == MotionEvent.ACTION_DOWN
							&& !mPTEView.getVisible()) {
						mPTEView.setVisible(true);
					}
					mPTEView.onTouchEvent(event);
				}
				return true;
			}

		};
		mTestView.setOnTouchListener(mTouchListener);
		setContentView(mTestFrameLayout);
	}

	protected void initPageBitmaps() {
		for (int i = day; i <= 30; i++) {
			StringBuilder tmpNow = new StringBuilder().append(i).append("/")
					.append(month + 1).append("/").append(year);
			String tmpDate = tmpNow.toString();
			lAdapter = new ListExpenseAdapter(mContext,
					R.layout.activity_main_list_expense, tmpEData, tmpDate);
			lstExpense.setAdapter(lAdapter);
			txtDate.setText(tmpDate);
		}

	}

	@Override
	public Bitmap getPageBitmap(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageMaterial getPageMaterial(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rect getPageRegion(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int totalNumOfPages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void animationEnded(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void animationEnded(AnimationType arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void animationStarted(AnimationType arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void animationEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void animationStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void firstRenderingFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateFinished() {
		// TODO Auto-generated method stub

	}

}
