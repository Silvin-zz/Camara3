package com.example.silviobravocado.camara3;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.support.v4.app.NotificationCompat.*;

public class MainActivity extends Activity implements View.OnClickListener {

    Button      btnPicture;
    Button      btnLoad;
    ImageView   imgPicture;
    Bitmap      bitMap;
    int         typeAction;
    static int  TAKE_PICTURE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos nuestros objetos visuales.

        this.btnPicture = (Button)      this.findViewById(R.id.btnTakePic);
        this.btnLoad    = (Button)      this.findViewById(R.id.btnLoad);
        this.imgPicture = (ImageView)   this.findViewById(R.id.picture);


        this.btnPicture.setOnClickListener(this);
        this.btnLoad.setOnClickListener(this);




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){

            case R.id.btnTakePic:

                this.typeAction =1;
                intent          = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
                break;

            case R.id.btnLoad:
                intent          = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                this.typeAction = 2;
                startActivityForResult(intent, TAKE_PICTURE);
                break;

        }

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == TAKE_PICTURE) {
            if (this.typeAction == 1 && resultCode == RESULT_OK && intent!=null) { // Tomo una foto
                this.showPicture(intent);
                this.sendNotification();
            } else{                                                                 // Cargo una imagen
                if(resultCode == Activity.RESULT_OK){
                    this.loadPhoto(intent);
                    this.sendNotification();
                }
            }
        }

    }

    private void showPicture(Intent intent){
        Bundle extras       = intent.getExtras();
        bitMap              = (Bitmap) extras.get("data");
        imgPicture.setImageBitmap(bitMap);
    }


    private void loadPhoto(Intent intent){

        InputStream stream  = null;
        Bundle extras       = intent.getExtras();
        Bitmap bitmap       = (Bitmap) extras.get("data");

        try{
            if(bitmap !=null){
                bitmap.recycle();
            }

            stream = getContentResolver().openInputStream(intent.getData());
            bitmap = BitmapFactory.decodeStream(stream);
            imgPicture.setImageBitmap(bitmap);

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }


    private void sendNotification(){


        Uri notificationSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Envio de notificaion")
                .setContentText("Descripcion")
                .setSound(notificationSound);


        Intent notificationIntent   =  new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(500,builder.build());


    }


}
