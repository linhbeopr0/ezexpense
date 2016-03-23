package com.sanmboiboi.slidingmenu;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.example.mcexpense.R;
import com.sanmboiboi.slidingmenu.SlidingMenu.CanvasTransformer;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SlidingMenuView {
    public ListView lstSlidingMenu;
    public SlidingMenu menu;
    private final Paint paint = new Paint();
    private final ColorMatrix matrix = new ColorMatrix();
    private static boolean sHackReady;
    private static boolean sHackAvailable;
    private static Field sRecreateDisplayList;
    private static Method sGetDisplayList;

    public SlidingMenuView(Context context) {
        menu = new SlidingMenu(context);
        initMenu(menu);

    }

    public void initMenu(SlidingMenu menu) {
        /* init menu */
        menu.setMode(SlidingMenu.LEFT);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setFadeEnabled(true);
        menu.setMenu(R.layout.activity_main_sliding_menu);

        menu.setBehindCanvasTransformer(new CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                boolean API_17 = Build.VERSION.SDK_INT >= 17;
                boolean API_16 = Build.VERSION.SDK_INT == 16;

                if (API_16) {
                    prepareLayerHack();
                }

                manageLayers(percentOpen);
                updateColorFilter(percentOpen);
                updatePaint(API_17, API_16);
            }
        });

        lstSlidingMenu = (ListView) menu.findViewById(R.id.lstSlidingMenu);
    }

    @TargetApi(17)
    private void updatePaint(boolean API_17, boolean API_16) {
        View backView = menu.getMenu();
        if (API_17) {
            backView.setLayerPaint(paint);
        } else {
            if (API_16) {
                if (sHackAvailable) {
                    try {
                        sRecreateDisplayList.setBoolean(backView, true);
                        sGetDisplayList.invoke(backView, (Object[]) null);
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                } else {
                    // This solution is slow
                    menu.getMenu().invalidate();
                }
            }

            // API level < 16 doesn't need the hack above, but the invalidate is
            // required
            ((View) backView.getParent()).postInvalidate(backView.getLeft(), backView.getTop(), backView.getRight(),
                    backView.getBottom());
        }
    }

    private void updateColorFilter(float percentOpen) {
        matrix.setSaturation(percentOpen);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
    }

    private void manageLayers(float percentOpen) {
        boolean layer = percentOpen > 0.0f && percentOpen < 1.0f;
        int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;

        if (layerType != menu.getContent().getLayerType()) {
            menu.getContent().setLayerType(layerType, null);
            menu.getMenu().setLayerType(layerType, Build.VERSION.SDK_INT <= 16 ? paint : null);
        }
    }

    private static void prepareLayerHack() {
        if (!sHackReady) {
            try {
                sRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                sRecreateDisplayList.setAccessible(true);

                sGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class<?>) null);
                sGetDisplayList.setAccessible(true);

                sHackAvailable = true;
            } catch (NoSuchFieldException e) {
            } catch (NoSuchMethodException e) {
            }
            sHackReady = true;
        }
    }
}
