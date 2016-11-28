package com.the_incognito.darry.my_application2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

public class ConvosArrayAdapter extends ArrayAdapter<String> {
    Context context;
    int textViewResourceId;
    private static final String TAG = "ConvosArrayAdapter" ;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

    public ConvosArrayAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        for (int i = 0; i < objects.size(); ++i) {
            hashMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return hashMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //@Override
    public void add(String object){
        hashMap.put(object,hashMap.size());
        this.notifyDataSetChanged();
    }
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
