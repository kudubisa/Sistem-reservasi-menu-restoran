package org.menu.balerasa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beaconreference.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//import org.altbeacon.beaconreference.R;

/**
 * Created by Andrya on 8/26/2015.
 */
public class CartBaseAdapter extends BaseAdapter {

    private ArrayList<EntitasCart> searchArrayList;
    private LayoutInflater layoutInflater;

    String urlpic = "http://192.168.42.48/Kuliner/";

    public CartBaseAdapter(Context context, ArrayList<EntitasCart> result) {
        searchArrayList = result;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if(v==null){
            v = layoutInflater.inflate(R.layout.cart_custom_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.nama = (TextView) v.findViewById(R.id.namaMenu);
            viewHolder.qtyMenu = (TextView) v.findViewById(R.id.qtyMenu);
            viewHolder.jumHrg = (TextView) v.findViewById(R.id.jumHrg);
            viewHolder.picMenu = (ImageView) v.findViewById(R.id.imgMenu);
            new DownloadImageTask(viewHolder.picMenu).execute(urlpic+searchArrayList.get(position).picMenu);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.nama.setText(searchArrayList.get(position).getNamaMenu());
        viewHolder.qtyMenu.setText(searchArrayList.get(position).getQtyMenu());
        viewHolder.jumHrg.setText(searchArrayList.get(position).getJumHrg());


        return v;
    }

    static class ViewHolder{
        TextView nama, qtyMenu, jumHrg;
        ImageView picMenu;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap mIcon = null;
            String url = (String) params[0];

            try {
                InputStream is = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null) {
                Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 150, 100, true);
                bmImage.setImageBitmap(bmp);
            }
        }
    }

}
