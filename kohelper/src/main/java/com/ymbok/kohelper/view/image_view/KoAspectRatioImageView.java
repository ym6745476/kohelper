package com.ymbok.kohelper.view.image_view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.ymbok.kohelper.R;

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 可设置宽高比的ImageView
 */
public class KoAspectRatioImageView extends AppCompatImageView {

    private int defaultWidthRatio   = 3;
    private int defaultHeightRatio  = 2;

    private int widthRatio;
    private int heightRatio;

    public KoAspectRatioImageView(Context context) {
        this(context, null);
    }

    public KoAspectRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KoAspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KoAspectRatioImageView);
        try {
            widthRatio = a.getInteger(R.styleable.KoAspectRatioImageView_width_ratio, 0);
            heightRatio = a.getInteger(R.styleable.KoAspectRatioImageView_height_ratio, 0);

            if (widthRatio == 0 || heightRatio == 0) {
                widthRatio = defaultWidthRatio;
                heightRatio = defaultHeightRatio;
            }
        } finally {
            a.recycle();
        }
    }

    /**
     * Sets width ratio.
     *
     * @param widthRatio the width ratio
     */
    public void setWidthRatio(int widthRatio) {
        this.widthRatio = widthRatio;
    }

    /**
     * Sets height ratio.
     *
     * @param heightRatio the height ratio
     */
    public void setHeightRatio(int heightRatio) {
        this.heightRatio = heightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(widthMeasureSpec) * heightRatio / widthRatio;
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
