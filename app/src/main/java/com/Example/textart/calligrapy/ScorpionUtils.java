package com.Example.textart.calligrapy;

import android.graphics.Bitmap;
import android.net.Uri;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ScorpionUtils {
    public static String SelectedTattooName = null;
    public static String[] cnt = new String[]{"2", "8", "10", "11", "14", "15", "16", "17", "20", "21", "22", "27", "38"};
    public static final String TEMP_FILE_NAME = "temp.png";
    public static ArrayList<String> arr = new ArrayList(Arrays.asList(cnt));
    public static Bitmap bits;
    public static int clr = -16777216;
    public static String[] color = new String[]{"#ffffffff", "#ffc0c0c0", "#ff808080", "#ff000000", "#ffff0000", "#ff800000", "#ffffff00", "#ff808000", "#ff00ff00", "#ff008000", "#ff00ffff", "#ff008080", "#ff0000ff", "#ff000080", "#ffff00ff", "#ff800080", "#ff8e44ad", "#ff7f8c8d", "#ff707b7c", "#ff797d7f", "#ff17202a", "#ff7e5109", "#ff641e16", "#ffbb8fce", "#ffcd5c5c", "#ffff5733", "#ff15a8a5"};
    public static File mFileTemp;
    public static Uri selectedImageUri = null;
    public static ArrayList<StickerView> stickers = new ArrayList();
    public static String text = "Add Text";
    public static String typeface = "fontfile/0.ttf";
}
