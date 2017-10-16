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

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Andrya on 8/18/2015.
 */
public class MenuBaseAdapter extends BaseAdapter {
    private static ArrayList<EntitasMakanan> searchArrayList;
    private LayoutInflater mInflater;
    String urlpic = "http://192.168.42.48/Kuliner/";

    public MenuBaseAdapter(Context context, ArrayList<EntitasMakanan> results){
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder{
        TextView nama, harga,mobPrice;
        ImageView pic;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            String urlDisplay = urls[0];

            try {
                InputStream is = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                Bitmap bmp2 = Bitmap.createScaledBitmap(result, 150, 100, true);
                bmImage.setImageBitmap(bmp2);
            }
        }


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
        ViewHolder holder;

        if(v == null){
            v = mInflater.inflate(R.layout.item_custom_listview,null);
            holder = new ViewHolder();

            holder.nama = (TextView) v.findViewById(R.id.nama);
            holder.harga = (TextView) v.findViewById(R.id.harga);
            holder.mobPrice = (TextView) v.findViewById(R.id.mobDeal);
            holder.pic = (ImageView) v.findViewById(R.id.img_menu);
            new DownloadImageTask(holder.pic).execute(urlpic+searchArrayList.get(position).getPicMenu());
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.nama.setText(searchArrayList.get(position).getNamaMenu());
        holder.harga.setText(searchArrayList.get(position).getHargaMenu());
        holder.mobPrice.setText(searchArrayList.get(position).getMobDeal());

        return v;
    }
}
