package com.Example.calligrapy;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconTextView extends RelativeLayout {

    private Context mContext;
    private View root_view;
    private Resources resources;

    public IconTextView(Context context) {
        super(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        root_view = LayoutInflater.from(mContext).inflate(R.layout.icon_text, this, true);
        TypedArray main_value = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextView, 0, 0);

        ImageView image_view = root_view.findViewById(R.id.image_view);
        TextView text_view = root_view.findViewById(R.id.text_view);
        View top_view = root_view.findViewById(R.id.top_view);
        View bottom_view = root_view.findViewById(R.id.bottom_view);

        try {

            String text = main_value.getString(R.styleable.IconTextView_Summary);
            int icon_id = main_value.getResourceId(R.styleable.IconTextView_IconId, 0);
            int line_type = main_value.getInt(R.styleable.IconTextView_LinePosition, -1);

            if (text != null) {
                text_view.setText(text);
            }

            if (icon_id != 0) {
                image_view.setImageResource(icon_id);
            }

            switch (line_type) {
                case 0:
                    top_view.setVisibility(VISIBLE);
                    bottom_view.setVisibility(GONE);
                    break;
                case 1:
                    top_view.setVisibility(GONE);
                    bottom_view.setVisibility(VISIBLE);
                    break;
                default:
                    top_view.setVisibility(GONE);
                    bottom_view.setVisibility(GONE);
                    break;
            }

        } finally {
            main_value.recycle();
        }

    }
}
