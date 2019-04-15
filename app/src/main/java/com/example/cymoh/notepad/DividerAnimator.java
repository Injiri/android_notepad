package com.example.cymoh.notepad;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

public class DividerAnimator extends RecyclerView.ItemDecoration {
    public static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable divider;
    private int thisorientation;
    private Context context;
    private int margin;

    public DividerAnimator(int orientation, Context context, int margin) {
        this.thisorientation = orientation;
        this.context = context;
        this.margin = margin;
        final TypedArray arr = context.obtainStyledAttributes(ATTRS);
        divider = arr.getDrawable(0);
        arr.recycle();
        setOrientation(orientation);
    }


    public void setOrientation(int orientation) {

        if (orientation != HORZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("Inverlid orientation");
        }
        thisorientation = orientation;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
if (thisorientation == VERTICAL_LIST){
    drawVertical(c, parent);
}else{
    drawHorizontal(c, parent);
}
    }

    public  void drawVertical(Canvas c,RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final  int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();

    for (int i= 0; i<childCount; i++){
        final View child = parent.getChildAt(i);
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();
final int top = child.getBottom() + layoutParams.bottomMargin;
final  int bottom = top + divider.getIntrinsicHeight();
divider.setBounds(left + dpToPx(margin) ,top, right - dpToPx(margin),bottom);
divider.draw(c);
    }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for(int i= 0; i< childCount; i++){
            final View  child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getRight()+ params.rightMargin;
            final int right = left + divider.getIntrinsicHeight();
            divider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
            divider.draw(c);

        }

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (thisorientation == VERTICAL_LIST)

        {
            outRect.set(0,0,0,divider.getIntrinsicHeight());

        }else{
            outRect.set(0,0,divider.getIntrinsicWidth(),0);

        }
    }
    private  int dpToPx(int dp){
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }
}


