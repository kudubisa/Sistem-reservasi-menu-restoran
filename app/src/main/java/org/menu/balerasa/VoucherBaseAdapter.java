package org.menu.balerasa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beaconreference.R;

import java.util.ArrayList;

/**
 * Created by Andrya on 1/13/2016.
 */
public class VoucherBaseAdapter extends BaseAdapter {

    private ArrayList<EntitasCusVoucher> searchArrayList;
    private LayoutInflater layoutInflater;


    public VoucherBaseAdapter(Context context, ArrayList<EntitasCusVoucher> result) {
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
            v = layoutInflater.inflate(R.layout.voucher_custom_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) v.findViewById(R.id.codeVoucher);
            viewHolder.deskripsi = (TextView) v.findViewById(R.id.desVoucher);
            viewHolder.expired = (TextView) v.findViewById(R.id.expiredTime);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.code.setText(searchArrayList.get(position).getCode());
        viewHolder.deskripsi.setText(searchArrayList.get(position).getDes());
        viewHolder.expired.setText(searchArrayList.get(position).getExpired());


        return v;
    }

    static class ViewHolder{
        TextView code,deskripsi,expired;
    }
}
