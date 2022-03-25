package com.ymbok.kohelper.view.image_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ymbok.kohelper.R;
import com.ymbok.kohelper.utils.KoUnitUtil;

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2016/12/13 09:13
 * Email 396196516@qq.com
 * Info 圆角ImageView
 */
public class KoRoundAspectRadioImageView extends KoAspectRatioImageView {

    private Context context;
    private float width,height;
    private float radius = 0;

    public KoRoundAspectRadioImageView(Context context) {
        this(context, null);
    }

    public KoRoundAspectRadioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KoRoundImageView);
        try {
            int radiusDip = typedArray.getInteger(R.styleable.KoRoundImageView_radius, 0);
            radius = KoUnitUtil.INSTANCE.dip2px(context,radiusDip);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > radius && height > radius) {
            Path path = new Path();
            path.moveTo(radius, 0);
            path.lineTo(width - radius, 0);
            path.quadTo(width, 0, width, radius);
            path.lineTo(width, height - radius);
            path.quadTo(width, height, width - radius, height);
            path.lineTo(radius, height);
            path.quadTo(0, height, 0, height - radius);
            path.lineTo(0, radius);
            path.quadTo(0, 0, radius, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}