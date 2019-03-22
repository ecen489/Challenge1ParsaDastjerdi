package com.example.cameraapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_TAKE_PICTURE = 69;
    private ImageView img;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private EditText idValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        idValue = (EditText) findViewById(R.id.idValue);
        img = (ImageView) findViewById(R.id.imageView);

        Button accessCamera = (Button) findViewById(R.id.accessCamera);
        accessCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(picIntent, REQ_CODE_TAKE_PICTURE);
            }
        });


        Button searchDatabase = (Button) findViewById(R.id.searchDatabase);
        searchDatabase.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               int id;
               // Toast toast = Toast.makeText(this, "Invalid ID", Toast.LENGTH_LONG);

               try {
                   id = Integer.parseInt(idValue.getText().toString());

                   if (id < 1){
                       // toast.show();
                       return;
                   }

               } catch (Exception e) {
                    // toast.show();
                    return;
               }


                db = dbHelper.getWritableDatabase();

                String query = "SELECT IMAGE FROM PICTURES WHERE ID = " + id;
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()){
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("IMAGE"));
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    img.setImageBitmap(bitmap);
                }

                cursor.close();
                db.close();
           }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQ_CODE_TAKE_PICTURE && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            img.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] byteArray = stream.toByteArray();

            db = dbHelper.getWritableDatabase();
            ((DBHelper) dbHelper).insertPicture(db, byteArray);
            db.close();
        }
    }
}
