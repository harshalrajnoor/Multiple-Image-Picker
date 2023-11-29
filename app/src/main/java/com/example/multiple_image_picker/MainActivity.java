package com.example.multiple_image_picker;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    TextView textView;
    Button imagePickerButton;
    ArrayList<Uri> list;
    RecyclerAdapter adaptor;
    String colum[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_gallery_images);
        textView = findViewById(R.id.totalPhotos);
        imagePickerButton = findViewById(R.id.pick);
        adaptor = new RecyclerAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        recyclerView.setAdapter(adaptor);
        imagePickerButton.setOnClickListener(this);

        if ((ActivityCompat.checkSelfPermission(
                this, colum[0]) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(
                        this, colum[1]) != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(colum, 123);
            }
        }

    }

    @Override
    public void onClick(View view) {

//        switch (view.getId()) {
//            case R.id.pick:
//                openGalley();
//                break;
//        }
        int id = view.getId();
        if(id == R.id.pick){
            openGalley();
        }

    }

    private void openGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    list.add(data.getClipData().getItemAt(i).getUri());
                }
                adaptor.notifyDataSetChanged();
                textView.setText("Image(" + list.size() + ")");
            } else if (data.getData() != null) {
                String imgurl = data.getData().getPath();
                list.add(Uri.parse(imgurl));
            }
        }
    }
}

