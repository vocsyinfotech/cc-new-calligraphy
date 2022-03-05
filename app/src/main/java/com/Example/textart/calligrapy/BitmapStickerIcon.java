package com.Example.textart.calligrapy;

import android.graphics.drawable.Drawable;

public class BitmapStickerIcon extends DrawableSticker {
    private float x;
    private float y;

    public BitmapStickerIcon(Drawable drawable)
    {
        super(drawable);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
