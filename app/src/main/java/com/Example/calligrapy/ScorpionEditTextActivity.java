package com.Example.calligrapy;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView.WHEEL_TYPE;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import me.grantland.widget.BuildConfig;
import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public class ScorpionEditTextActivity extends Activity implements OnClickListener {

    ScorpionCustomeFontAdapter adpt;
    ImageButton btn_back;
    ImageButton btn_clr;
    ImageButton btn_next;
    ImageButton btn_ok;
    int cnt_pos = 0;
    EditText edittext;
    NonScrollListView fontgrid;
    String[] fontname;
    TextView output_autofit;
    Typeface typeface;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_edit_text);
        findById();

        this.cnt_pos = 0;
        try {
            this.fontname = getImagefont("fontfile");
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            this.output_autofit.setTypeface(Typeface.createFromAsset(getAssets(), ScorpionUtils.typeface));
            this.output_autofit.setTextColor(ScorpionUtils.clr);
        } catch (Exception e4) {
        }
        this.adpt = new ScorpionCustomeFontAdapter(this, new ArrayList(Arrays.asList(this.fontname)));
        this.fontgrid.setAdapter(this.adpt);


        this.fontgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {


                        if (!ScorpionUtils.arr.contains(arg2 + BuildConfig.FLAVOR)) {
                            ScorpionUtils.typeface = "fontfile/" + ScorpionEditTextActivity.this.fontname[arg2];
                            ScorpionEditTextActivity.this.typeface = Typeface.createFromAsset(ScorpionEditTextActivity.this.getAssets(), "fontfile/" + ScorpionEditTextActivity.this.fontname[arg2]);
                            ScorpionEditTextActivity.this.output_autofit.setTypeface(ScorpionEditTextActivity.this.typeface);
                        } else if (Pattern.compile("([0-9])").matcher(ScorpionEditTextActivity.this.edittext.getText().toString()).find()) {
                            Toast.makeText(ScorpionEditTextActivity.this.getApplicationContext(), "Please Select another font style!!!", Toast.LENGTH_LONG).show();
                        } else {
                            ScorpionUtils.typeface = "fontfile/" + ScorpionEditTextActivity.this.fontname[arg2];
                            ScorpionEditTextActivity.this.typeface = Typeface.createFromAsset(ScorpionEditTextActivity.this.getAssets(), "fontfile/" + ScorpionEditTextActivity.this.fontname[arg2]);
                            ScorpionEditTextActivity.this.output_autofit.setTypeface(ScorpionEditTextActivity.this.typeface);
                        }
                        ScorpionEditTextActivity.this.cnt_pos = arg2;


            }
        });
        this.edittext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!ScorpionUtils.arr.contains(ScorpionEditTextActivity.this.cnt_pos + BuildConfig.FLAVOR)) {
                    ScorpionEditTextActivity.this.output_autofit.setText(charSequence);
                } else if (Pattern.compile("([0-9])").matcher(ScorpionEditTextActivity.this.edittext.getText().toString()).find()) {
                    Toast.makeText(ScorpionEditTextActivity.this.getApplicationContext(), "Please Select another font style!!!", 1).show();
                } else {
                    ScorpionEditTextActivity.this.output_autofit.setText(charSequence);
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        this.btn_back.setOnClickListener(this);
        this.btn_next.setOnClickListener(this);
        this.btn_ok.setOnClickListener(this);
        this.btn_clr.setOnClickListener(this);
    }

    private String[] getImagefont(String folderName) throws IOException {
        return getAssets().list(folderName);
    }

    private void findById() {
        this.btn_back = (ImageButton) findViewById(R.id.btn_back);
        this.btn_next = (ImageButton) findViewById(R.id.btn_next);
        this.btn_ok = (ImageButton) findViewById(R.id.btn_ok);
        this.btn_clr = (ImageButton) findViewById(R.id.btn_clr);
        this.fontgrid = (NonScrollListView) findViewById(R.id.fontgrid);
        this.edittext = (EditText) findViewById(R.id.edittext);
        this.output_autofit = (TextView) findViewById(R.id.output_android);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_next /*2131427437*/:

                        if (edittext.getText().toString().length() > 0) {
                            ScorpionUtils.text = output_autofit.getText().toString();
                            setResult(-1);
                            onBackPressed();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "Please Enter Text!", Toast.LENGTH_LONG).show();



                return;
            case R.id.btn_back /*2131427456*/:

                        onBackPressed();




                return;
            case R.id.btn_ok /*2131427480*/:

                        output_autofit.setText(edittext.getText().toString());
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext.getWindowToken(), 0);



                return;
            case R.id.btn_clr /*2131427481*/:

                        openColorDialog();



                return;
            default:
                return;
        }
    }


    private void openColorDialog() {
        ColorPickerDialogBuilder.with(this).setTitle("Choose color").initialColor(-1).wheelType(WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(int selectedColor) {
            }
        }).setPositiveButton((CharSequence) "ok", new ColorPickerClickListener() {
            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                ScorpionEditTextActivity.this.output_autofit.setTextColor(selectedColor);
                ScorpionUtils.clr = selectedColor;
                if (allColors != null) {
                    StringBuilder sb = null;
                    for (Integer color : allColors) {
                        if (color != null) {
                            if (sb == null) {
                                sb = new StringBuilder("Color List:");
                            }
                            sb.append("\r\n#" + Integer.toHexString(color.intValue()).toUpperCase());
                        }
                    }
                }
            }
        }).setNegativeButton((CharSequence) "cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        }).showColorEdit(true).setColorEditTextColor(getResources().getColor(17170459)).build().show();
    }
    public void onBackPressed() {

                finish();

    }

}
