package com.digitalapp.fathersdayphotoframeimageeditor.StickerView;

import android.graphics.Bitmap;


public class TextStick extends Stick {
    public String content;

    public TextStick(Bitmap stickBitmap , String content) {
        super(stickBitmap);
        this.content = content;
    }


}