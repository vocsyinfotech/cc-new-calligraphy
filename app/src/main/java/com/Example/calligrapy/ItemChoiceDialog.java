package com.Example.calligrapy;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;



public class ItemChoiceDialog extends Dialog {

    public ItemChoiceDialog(@NonNull Activity activity, ActionListener actionListener) {
        super(activity);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.item_selection);
        setCancelable(false);

        IconTextView gallery = findViewById(R.id.gallery);
        IconTextView camera = findViewById(R.id.camera);
        IconTextView cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(v -> dismiss());

        gallery.setOnClickListener((view) -> {
            dismiss();
            if (actionListener != null) {
                actionListener.action("gallery");
            }
        });
        camera.setOnClickListener((view) -> {
            dismiss();
            if (actionListener != null) {
                actionListener.action("camera");
            }
        });


        show();
        //item_selection
    }
}
