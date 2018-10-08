package com.example.sys9.contentresolver1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    public ListView listView;

    ArrayList <Pojo> arrayList = new ArrayList <Pojo>( );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleitem);

        fetchContacts();
        listView=findViewById( R.id.listView );
        CustomAdapter adapter=new CustomAdapter( this,arrayList );
        listView.setAdapter( adapter );

    }
    public void fetchContacts() {
        String phoneNumber = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();
        if (checkPermission(  )) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query( CONTENT_URI, null ,null, null,
                    "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC" );
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contact_id = cursor.getString( cursor.getColumnIndex( _ID ) );
                    String name = cursor.getString( cursor.getColumnIndex( DISPLAY_NAME ) );
                    int hasPhoneNumber = Integer.parseInt( cursor.getString( cursor.getColumnIndex( HAS_PHONE_NUMBER ) ) );
                    if (hasPhoneNumber > 0) {
                        output.append( "\n First Name:" + name );
                        final Cursor phoneCursor = contentResolver.query( PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
                                new String[]{contact_id}, null );
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString( phoneCursor.getColumnIndex( NUMBER ) );
                            output.append( "\n Phone number:" + phoneNumber );
                            break;
                        }
                        phoneCursor.close();
                        Pojo contacts=new Pojo();
                        contacts.setName(name);
                        contacts.setPhoneNumber(phoneNumber);
                        arrayList.add(contacts);
                    }
                    output.append( "\n" );
                }
            }
        }
        else {
            requestPermissions(  );
        }
    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission( MainActivity.this,READ_CONTACTS);
        int second = ContextCompat.checkSelfPermission( MainActivity.this,CALL_PHONE);

        if (FirstPermissionResult== PackageManager.PERMISSION_GRANTED&&second==PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions( MainActivity.this,new String[]
                {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE},100 );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        switch (requestCode){
            case 100:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)

                    Log.e( "value","Permission Granted,Now you can use local Drive" );
                else
                    Log.e( "value","Permissions Denied,You cannot use local Drive" );
                break;

        }
    }
}