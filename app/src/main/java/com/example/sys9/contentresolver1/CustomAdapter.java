package com.example.sys9.contentresolver1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Pojo> arr1;

    public CustomAdapter(Context context, ArrayList <Pojo> arr) {
        this.context = context;
        this.arr1 = arr;
    }

    @Override
    public int getCount()
    {
        return arr1.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View customview;
        customview = LayoutInflater.from( context ).inflate( R.layout.activity_main, viewGroup, false );
        TextView phoneNumber = customview.findViewById( R.id.textView1 );
        phoneNumber.setText( arr1.get( i ).phoneNumber );
        TextView name = customview.findViewById( R.id.textView );
        name.setText( arr1.get( i ).name );
        phoneNumber.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e( "phn", arr1.get( i ).getPhoneNumber() );
                Intent intent = new Intent( Intent.ACTION_CALL );
                intent.setData( Uri.parse( "tel:" + arr1.get( i ).getPhoneNumber() ) );
                if (ActivityCompat.checkSelfPermission( context, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                context.startActivity( intent );
            }
        } );
        return customview;
    }
}
