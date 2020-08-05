package com.example.multipic2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView img1,img2;
    private final int CODE_IMG_GALLERY = 1;
    private final int CODE_MULTIPLE_IMG_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), "SelectSingle"), CODE_IMG_GALLERY);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SelectMulti"),CODE_MULTIPLE_IMG_GALLERY);
            }
        });
    }

    private void init(){
        this.img1 = findViewById(R.id.img1);
        this.img2 = findViewById(R.id.img2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){

        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            if(imageUri != null){
                img1.setImageURI(imageUri);
            }
        }else if(requestCode == CODE_MULTIPLE_IMG_GALLERY && resultCode == RESULT_OK){
            ClipData clipData = data.getClipData();

            if(clipData != null){
                img1.setImageURI(clipData.getItemAt(0).getUri());
                img2.setImageURI(clipData.getItemAt(1).getUri());

                for(int i = 0; i<clipData.getItemCount(); i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    Log.e("MAS IMAGES", uri.toString());
                }
            }
        }
    }
}