package in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;

public class ReceiverRecyclerViewAdapter extends RecyclerView.Adapter<ReceiverRecyclerViewAdapter.ViewHolder>{

    ArrayList<String> Label,walletAddress,AddressValues2;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    private OnItemClick mCallback;

    public ReceiverRecyclerViewAdapter(Context context1, ArrayList<String> Label, ArrayList<String> walletAddress, OnItemClick listener){

      this.Label = Label;
        this.walletAddress = walletAddress;

        this.context = context1;
        this.mCallback = listener;
    }



    public interface OnItemClick {
        void onClick(String Label,String walletAddress);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView labelTv;
        public TextView walletAddressTv;
        public ImageView editAddress;

        public ViewHolder(View v){

            super(v);

            labelTv = (TextView)v.findViewById(R.id.person_name);
            walletAddressTv = (TextView)v.findViewById(R.id.person_age);
            editAddress = (ImageView)v.findViewById(R.id.editaddressimg);



            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        String clickedLabelForAddress = Label.get(pos);
                        String clickedWalletAddress = walletAddress.get(pos);                       //Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(clickedLabelForAddress,clickedWalletAddress);
                    }
                }
            });

            editAddress.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        String clickedLabelForAddress = Label.get(pos);
                        String clickedWalletAddress = walletAddress.get(pos);
                        //Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(clickedLabelForAddress,clickedWalletAddress);
                    }
                }
            });
        }
    }



    @Override
    public ReceiverRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.receiver_list,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        holder.labelTv.setText(Label.get(position));
        holder.walletAddressTv.setText(walletAddress.get(position));
    }

    @Override
    public int getItemCount(){

        return Label.size();
    }


}