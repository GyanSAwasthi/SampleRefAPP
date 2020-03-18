package in.bitstreet.com.itdwallet.view.alternativecurrency.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;

public class AlternativeCurrencyAdapter extends RecyclerView.Adapter<AlternativeCurrencyAdapter.ViewHolder>{

 ArrayList<String> currencyListArray;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    String clickedDataItem;
    TextView textView;
    private int selectedPosition = -1;// no selection by default
    public AlternativeCurrencyAdapter(Context context1, ArrayList<String> currencyListArray){

        this.currencyListArray = currencyListArray;

        context = context1;

    }




    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CheckBox chkSelected;


        public ViewHolder(View v){

            super(v);

            username = (TextView)v.findViewById(R.id.username);
            chkSelected = (CheckBox) v.findViewById(R.id.chkSelected);





            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                         clickedDataItem = currencyListArray.get(pos);
                       // Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();

                    }
                }
            });



        }
    }



    @Override
    public AlternativeCurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.alternative_currency_adapter_list,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){

        holder.username.setText(currencyListArray.get(position));
        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //do your toast programming here.

                // check if item still exists

                    clickedDataItem = currencyListArray.get(selectedPosition);
                Toast.makeText(context, "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
            }
        });


        holder.chkSelected.setChecked(position== selectedPosition);

        holder. chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedPosition =  position;
                }
                else{
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount(){

        return currencyListArray.size();
    }


}