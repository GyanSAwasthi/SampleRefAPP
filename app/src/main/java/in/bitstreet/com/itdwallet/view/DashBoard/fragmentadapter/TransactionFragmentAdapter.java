package in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.model.model.Model;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class TransactionFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;

    public static class TextTypeViewLableHolder extends RecyclerView.ViewHolder {


        TextView txtType;

        public TextTypeViewLableHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.tvheader);

        }

    }

    public static class ChlidTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        TextView tvbtc;
        TextView tvdate;
        ImageView imgviewstatus;

        public ChlidTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.tvchild);
            this.imgviewstatus = (ImageView) itemView.findViewById(R.id.imgviewstatus);
            this.tvbtc = (TextView) itemView.findViewById(R.id.tvbtc);
            this.tvdate = (TextView) itemView.findViewById(R.id.tvdate);


        }

    }



    public TransactionFragmentAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.HEADER_TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_fragment_header, parent, false);
                return new TextTypeViewLableHolder(view);
            case Model.TEXT_CHILD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_fragment_child, parent, false);
                return new ChlidTypeViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.HEADER_TEXT_TYPE;
            case 1:
                return Model.TEXT_CHILD_TYPE;

            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.HEADER_TEXT_TYPE:
                    ((TextTypeViewLableHolder) holder).txtType.setText(object.text);
                    break;
                case Model.TEXT_CHILD_TYPE:
                    ((ChlidTypeViewHolder) holder).txtType.setText(object.text1);
                    if(object.text1.equals("Received")){
                        ((ChlidTypeViewHolder) holder).imgviewstatus.setBackgroundResource(R.drawable.received);
                    }
                    else{
                        ((ChlidTypeViewHolder) holder).imgviewstatus.setBackgroundResource(R.drawable.send);

                    }
                   // ((ChlidTypeViewHolder) holder).imgviewstatus.setBackground(R.drawable.editprofile);
                    ((ChlidTypeViewHolder) holder).tvbtc.setText(object.text2);
                    ((ChlidTypeViewHolder) holder).tvdate.setText(object.text3);
                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
