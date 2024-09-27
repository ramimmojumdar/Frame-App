package com.digitalapp.fathersdayphotoframeimageeditor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.LandscapeAdapter;
import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.PortraitAdapter;
import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.SquareAdapter;
import com.digitalapp.fathersdayphotoframeimageeditor.R;
import com.digitalapp.fathersdayphotoframeimageeditor.StickerView.ImgStick;
import com.digitalapp.fathersdayphotoframeimageeditor.StickerView.Stick;
import com.digitalapp.fathersdayphotoframeimageeditor.StickerView.StickerSeriesView;
import com.digitalapp.fathersdayphotoframeimageeditor.StickerView.TextStick;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;


public class EditorActivity extends AppCompatActivity {

    ImageView fm_bg_img, frameImage;
    LinearLayout image_gallery, frameBtn, stickerBtn, textBtn;
    HorizontalScrollView sticker_layout;
    ImageView sticker1, sticker2, sticker3, sticker4, sticker5, sticker6, sticker7;

    TextView images_text, frame_text, sticker_text, text_text;


    RelativeLayout parentLayout;
    private StickerSeriesView stickerSeriesView;

    private EditText editTextInput;
    private Button btnCreateSticker;

    LinearLayout forTextLayoutId, layout1, layout2, toolbar, allEditBtn;

    private SeekBar seekBarTextSize;
    private Button btnPickTextColor, btnPickBackgroundColor;
    int defaultColor = Color.BLACK;
    int defaultBackgroundColor = Color.WHITE;



    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;

    private float lastX = 0f;
    private float lastY = 0f;
    private float posX = 0f;
    private float posY = 0f;



    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    PortraitAdapter portraitAdapter;
    SquareAdapter squareAdapter;
    LandscapeAdapter landscapeAdapter;

    ArrayList<String> imageList;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int UCROP_REQUEST = 69;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        showCustomDialog();


        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> onBackPressed());




        frameImage = findViewById(R.id.frameImage);
        fm_bg_img = findViewById(R.id.fm_bg_img);


        // Load the PNG frame image
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(frameImage);
        }


        //image pic from gallery

        image_gallery = findViewById(R.id.image_gallery);
        image_gallery.setOnClickListener(view -> {
            changeTextColor(images_text);
            showCustomDialog();

        });

        gestureDetector = new GestureDetector(EditorActivity.this, new GestureListener());
        scaleGestureDetector = new ScaleGestureDetector(EditorActivity.this, new ScaleGestureListener());

        fm_bg_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        seekBarTextSize = findViewById(R.id.seekBarTextSize);
        btnPickTextColor = findViewById(R.id.btnPickTextColor);
        btnPickBackgroundColor = findViewById(R.id.btnPickBackgroundColor);





        sticker_layout = findViewById(R.id.sticker_layout);
        frameBtn = findViewById(R.id.frameBtn);
        stickerBtn = findViewById(R.id.stickerBtn);
        textBtn = findViewById(R.id.textBtn);

        images_text = findViewById(R.id.images_text);
        frame_text = findViewById(R.id.frame_text);
        sticker_text = findViewById(R.id.sticker_text);
        text_text = findViewById(R.id.text_text);

        frameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor(frame_text);
                sticker_layout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        stickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor(sticker_text);
                recyclerView.setVisibility(View.GONE);
                sticker_layout.setVisibility(View.VISIBLE);

            }
        });

        forTextLayoutId = findViewById(R.id.forTextLayoutId);

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        allEditBtn = findViewById(R.id.allEditBtn);

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                sticker_layout.setVisibility(View.GONE);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                allEditBtn.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                forTextLayoutId.setVisibility(View.VISIBLE);

            }
        });




        toolbar = findViewById(R.id.toolbar);


// StickerSeriesView
        stickerSeriesView = findViewById(R.id.stickerSeriesView);

// Sticker views
        int[] stickerIds = {R.id.sticker1, R.id.sticker2, R.id.sticker3, R.id.sticker4, R.id.sticker5, R.id.sticker6};
        int[] stickerImages = {R.drawable.sticker, R.drawable.sticker2, R.drawable.sticker3, R.drawable.sticker4, R.drawable.sticker5, R.drawable.sticker6};

// Loop through each sticker and set click listener
        for (int i = 0; i < stickerIds.length; i++) {
            int finalI = i; // index needs to be final for use in inner class
            findViewById(stickerIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stickerSeriesView.setVisibility(View.VISIBLE);
                    Bitmap stickerBitmap = BitmapFactory.decodeResource(getResources(), stickerImages[finalI]);
                    Stick imgStick = new ImgStick(stickerBitmap);
                    stickerSeriesView.setStick(imgStick);
                }
            });
        }



//        TextStick textStick = new TextStick("Hello Bangladesh");
//        stickerSeriesView.setStick(textStick);

        editTextInput = findViewById(R.id.editTextInput);
        btnCreateSticker = findViewById(R.id.btnCreateSticker);
        btnCreateSticker.setOnClickListener(v -> {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            allEditBtn.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            stickerSeriesView.setVisibility(View.VISIBLE);

            String userText = editTextInput.getText().toString();

            if (!userText.isEmpty()) {
                // text Bitmap
                float textSize = seekBarTextSize.getProgress();

                Bitmap textBitmap = createTextBitmap(userText, textSize, defaultColor, defaultBackgroundColor);

//                 TextStick এ Bitmap
                TextStick textStick = new TextStick(textBitmap, userText);

                // StickerView
                stickerSeriesView.setStick(textStick);


          }

            forTextLayoutId.setVisibility(View.GONE);
        });


        btnPickTextColor.setOnClickListener(v -> {
            // Color Picker Dialog
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {

                    defaultColor = color;
                    editTextInput.setTextColor(defaultColor);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    //
                }
            });
            colorPicker.show();
        });


        btnPickBackgroundColor.setOnClickListener(v -> {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultBackgroundColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    defaultBackgroundColor = color;
                    editTextInput.setBackgroundColor(defaultBackgroundColor);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    //
                }
            });
            colorPicker.show();
        });



        // Set initial text size for EditText

        editTextInput.setTextSize(seekBarTextSize.getProgress());

        // Change text size dynamically using SeekBar
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editTextInput.setTextSize(progress);  // Update text size in real time

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });








        // Get the list of image URLs
        imageList = getIntent().getStringArrayListExtra("imageList");

        // Ensure imageList is not null
        if (imageList == null) {
            imageList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.recyclerViewEditor);
        recyclerView.setHasFixedSize(true);

        // Set up the LinearLayoutManager for horizontal scrolling
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set up the adapter
        portraitAdapter = new PortraitAdapter(EditorActivity.this, imageList);
        recyclerView.setAdapter(portraitAdapter);
        squareAdapter = new SquareAdapter(EditorActivity.this, imageList);
        recyclerView.setAdapter(squareAdapter);
        landscapeAdapter = new LandscapeAdapter(EditorActivity.this, imageList);
        recyclerView.setAdapter(landscapeAdapter);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`



        ImageView saveBtn = findViewById(R.id.saveBtn);
        parentLayout = findViewById(R.id.parentLayout);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture and save the RelativeLayout as an image
                Bitmap bitmap = getBitmapFromView(parentLayout);
                saveBitmap(bitmap, "parentLayout_image.png");
                Toast.makeText(EditorActivity.this, "Saved Image in Gallery", Toast.LENGTH_SHORT).show();
            }
        });









    }//onCreate bundle end~~~~~~~~~~~~~~~~~~~~~



//update Frame Image method
    public void updateFrameImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(frameImage);
    }




    // GestureListener class for handling gestures
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final float SCROLL_FACTOR = 0.5f;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Reset the scale when double-tapped
            scaleFactor = 1.0f;
            posX = 0f;
            posY = 0f;
            fm_bg_img.setScaleX(scaleFactor);
            fm_bg_img.setScaleY(scaleFactor);
            fm_bg_img.setTranslationX(posX);
            fm_bg_img.setTranslationY(posY);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            posX -= distanceX * SCROLL_FACTOR;
            posY -= distanceY * SCROLL_FACTOR;
            fm_bg_img.setTranslationX(posX);
            fm_bg_img.setTranslationY(posY);
            return true;
        }
    }

    // ScaleGestureListener class for handling scale gestures (pinch to zoom)
    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            fm_bg_img.setScaleX(scaleFactor);
            fm_bg_img.setScaleY(scaleFactor);
            return true;
        }
    }




//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {

                Uri selectedImage = data.getData();
                startCrop(selectedImage);
            } else if (requestCode == REQUEST_CAMERA && data != null) {

                Uri capturedImage = data.getData();
                startCrop(capturedImage);
            } else if (requestCode == UCROP_REQUEST && data != null) {

                Uri croppedImageUri = UCrop.getOutput(data);
                if (croppedImageUri != null) {

                    ImageView imageView = findViewById(R.id.fm_bg_img);
                    imageView.setImageURI(croppedImageUri);
                }
            }
        }
    }

    private void startCrop(Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "croppedImage.jpg"));
        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)  // ক্রপের রেশিও
                .start(this);
    }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    // Method to change the text color of the clicked layout's TextView
    private void changeTextColor(TextView selectedTextView) {
        // Reset all TextViews to default color
        images_text.setTextColor(Color.BLACK);
        frame_text.setTextColor(Color.BLACK);
        sticker_text.setTextColor(Color.BLACK);
        text_text.setTextColor(Color.BLACK);

        // Set the selected TextView's color to red
        selectedTextView.setTextColor(Color.RED);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    // Method to convert a view to a bitmap
    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // Method to save the bitmap as a PNG file
    private void saveBitmap(Bitmap bitmap, String fileName) {
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.setCancelable(true);

        // Handle Camera Click
        LinearLayout cameraLayout = dialog.findViewById(R.id.camera_layout);
        cameraLayout.setOnClickListener(v -> {

            openCamera();

            dialog.dismiss();
        });

        // Handle Gallery Click
        LinearLayout galleryLayout = dialog.findViewById(R.id.gallery_layout);
        galleryLayout.setOnClickListener(v -> {
            openGallery();
            dialog.dismiss();
        });

        // Handle Cancel Button Click
        TextView cancelButton = dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this, "No camera app found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }




    //============================================





    private Bitmap createTextBitmap(String text, float textSize, int textColor, int backgroundColor) {
        // Paint অবজেক্ট তৈরি করুন
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setColor(textColor);  // টেক্সটের কালার

        // টেক্সটের প্রস্থ এবং উচ্চতা বের করা
        float textWidth = paint.measureText(text);
        float textHeight = paint.getTextSize();

        // Bitmap তৈরি করা
        int bitmapWidth = (int) (textWidth * 2);
        int bitmapHeight = (int) (textHeight * 2);
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

        // Canvas এর উপর টেক্সট এবং ব্যাকগ্রাউন্ড ড্র করা
        Canvas canvas = new Canvas(bitmap);

        // ব্যাকগ্রাউন্ডের জন্য রেক্টেঙ্গল তৈরি করা
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor); // ব্যাকগ্রাউন্ডের কালার
        canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, backgroundPaint); // ব্যাকগ্রাউন্ড ড্র করা

        // টেক্সট ড্র করা
        canvas.scale(2.0f, 2.0f);
        canvas.drawText(text, 0, textHeight, paint);

        return bitmap;
    }







}// EditorActivity end
