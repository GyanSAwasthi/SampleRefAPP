package in.bitstreet.com.itdwallet.view.support.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.bitstreet.com.itdwallet.R;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>{

    String[] userName,openStatus,closeStatus,status;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    private OnItemClick mCallback;

    public HistoryRecyclerViewAdapter(Context context1, String[] userName, String[] openStatus, String[] closeStatus,String[] status){

        this.userName = userName;
        this.openStatus = openStatus;
        this.closeStatus = closeStatus;
        this.status = status;
        context = context1;
    }



    public interface OnItemClick {
        void onClick(String clickedUserName, String clickedUserEmail);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView openStatus;
        public TextView closeStatus;
        public TextView status;
        public ImageView starImg;


        public ViewHolder(View v){

            super(v);

            username = (TextView)v.findViewById(R.id.username);
            openStatus = (TextView)v.findViewById(R.id.open_status);
            closeStatus = (TextView)v.findViewById(R.id.close_status);
           status = (TextView)v.findViewById(R.id.status);
            starImg = (ImageView)v.findViewById(R.id.img_star);



            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        String clickedUserName = userName[pos];
                       // String clickedUserEmail = openStatus[pos];

                        //   Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(clickedUserName,clickedUserName);
                    }
                }
            });
        }
    }



    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.history_support_adapter,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        holder.username.setText(userName[position]);
        holder.openStatus.setText(openStatus[position]);
        holder.closeStatus.setText(closeStatus[position]);
        if(closeStatus[position].equals("")) {

            holder.status.setText("Active");
            holder.status.setTextColor(Color.parseColor("#336600"));
            holder.starImg.setImageResource(R.drawable.allwhite);
        }else{
            holder.status.setText("Closed");
            holder.status.setTextColor(Color.RED);
            holder.starImg.setImageResource(R.drawable.fouryellow_onewhite);
        }
    }

    @Override
    public int getItemCount(){

        return userName.length;
    }


}