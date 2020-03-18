package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.bitstreet.com.itdwallet.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    String[] SubjectValues,EmailValues,AddressValues2;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    private OnItemClick mCallback;

    public RecyclerViewAdapter(Context context1, String[] SubjectValues1, String[] SubjectValues2, String[] SubjectValues3, OnItemClick listener){

        SubjectValues = SubjectValues1;
        EmailValues = SubjectValues2;
        AddressValues2 = SubjectValues3;
        context = context1;
        this.mCallback = listener;
    }



    public interface OnItemClick {
        void onClick(String clickedUserName, String clickedUserEmail);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView useremail;
        public TextView address;

        public ViewHolder(View v){

            super(v);

            username = (TextView)v.findViewById(R.id.username);
            useremail = (TextView)v.findViewById(R.id.useremail);
            address = (TextView)v.findViewById(R.id.address);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        String clickedUserName = SubjectValues[pos];
                        String clickedUserEmail = EmailValues[pos];

                     //   Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(clickedUserName,clickedUserEmail);
                    }
                }
            });
        }
    }



    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.contact_list_adapter,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        holder.username.setText(SubjectValues[position]);
        holder.useremail.setText(EmailValues[position]);
        holder.address.setText(AddressValues2[position]);
    }

    @Override
    public int getItemCount(){

        return SubjectValues.length;
    }


}