package com.example.pdfview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class selectPic extends AppCompatActivity {

    private GridView mGridView = null;
    private Button mButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        mGridView = findViewById(R.id.gridView1);
        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(ButtonSelect_OmClickListener);

        //SDカードより画像データのIDを取得
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        ArrayList<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++){
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                    lstItem.add(new CheckedImage(false,id));
                    cursor.moveToNext();
                }

                //グリッド用のアダプターを作成
                CheckedImageArrayAdapter adapter = new CheckedImageArrayAdapter(getApplicationContext(),lstItem);
                //グリッドにアダプターをセット
                mGridView.setAdapter(adapter);

    }

    private View.OnClickListener ButtonSelect_OmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckedImageArrayAdapter adapter = (CheckedImageArrayAdapter)mGridView.getAdapter();
                        List<CheckedImage> lstCheckedItem = adapter.getCheckedItem();
                        Toast.makeText(selectPic.this, String.valueOf(lstCheckedItem.size()), Toast.LENGTH_LONG).show();

        }
    };

    public class CheckedImage {
    private boolean mChecked = false;
    private long mBitmapId;
    private Bitmap mBitmap = null;

    public boolean getChecked() {
        return mChecked;
    }
    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }
    public long getBitmapId() {
        return mBitmapId;
    }
    public void setBitmapId(long bitmapId) {
        this.mBitmapId = bitmapId;
    }
    public Bitmap getBitmap(Context context) {
        if (mBitmap == null){
            mBitmap = MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(), mBitmapId, MediaStore.Images.Thumbnails.MICRO_KIND, null);
        }
        return mBitmap;
    }

    public CheckedImage(boolean checked, long bitmapId){
        mChecked = checked;
        mBitmapId = bitmapId;
    }

}
    public static class CheckedImageArrayAdapter extends ArrayAdapter<CheckedImage> {
    private static class ViewHolder {
        int position;
        ImageView imageview = null;
        CheckBox checkbox = null;
    }

    private final static int LAYOUT_ID = R.layout.grid_item;


    private Context mContext;
    private LayoutInflater mInflater;


    public CheckedImageArrayAdapter(Context context, List<CheckedImage> objects) {
        super(context, LAYOUT_ID, objects);
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT_ID, null);
            holder = new ViewHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.checkbox.setOnCheckedChangeListener(CheckBox1_OnCheckedChangeListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        CheckedImage item = getItem(position);
        holder.position = position;
        holder.imageview.setImageBitmap(item.getBitmap(mContext));
        /*holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(selectPic.this)
                        .setView(holder.imageview)
                        .show();

            }
        });*/
        holder.checkbox.setChecked(item.getChecked());

        return convertView;
    }

    public List<CheckedImage> getCheckedItem(){
        List<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for ( int i = 0; i < getCount(); i++) {
            if (getItem(i).getChecked()){
                lstItem.add(getItem(i));
            }
        }
        return lstItem;
    }
    private CompoundButton.OnCheckedChangeListener CheckBox1_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          View view = (View)buttonView.getParent();
            ViewHolder holder = (ViewHolder)view.getTag();
            CheckedImage item = CheckedImageArrayAdapter.this.getItem(holder.position);
            item.setChecked(isChecked);
        }};
}


}

