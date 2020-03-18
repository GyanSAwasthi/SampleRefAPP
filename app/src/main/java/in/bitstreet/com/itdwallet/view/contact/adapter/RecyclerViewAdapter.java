package in.bitstreet.com.itdwallet.view.contact.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.pojo.Country;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<String> SubjectValues, EmailValues, AddressValues2;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    private OnItemClick mCallback;
    String searchedtextStr;

    public RecyclerViewAdapter(Context context1, ArrayList<String> name, ArrayList<String> email, ArrayList<String> country, OnItemClick listener, String searchedtextStr) {

        SubjectValues = name;
        EmailValues = email;
        AddressValues2 = country;
        context = context1;
        this.mCallback = listener;
        this.searchedtextStr = searchedtextStr;
    }


    public interface OnItemClick {
        void onClick(String clickedUserName, String clickedUserEmail);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView useremail;
        public TextView address;

        public ViewHolder(View v) {

            super(v);

            username = (TextView) v.findViewById(R.id.username);
            useremail = (TextView) v.findViewById(R.id.useremail);
            address = (TextView) v.findViewById(R.id.address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        String clickedUserName = SubjectValues.get(pos);
                        String clickedUserEmail = EmailValues.get(pos);

                        //   Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(clickedUserName, clickedUserEmail);
                    }
                }
            });
        }
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view1 = LayoutInflater.from(context).inflate(R.layout.contact_list_adapter, parent, false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.username.setText(SubjectValues.get(position));
        holder.useremail.setText(EmailValues.get(position));
        holder.address.setText(AddressValues2.get(position));

        //Highlighting the searched text
        String usernameStr = SubjectValues.get(position).toLowerCase(Locale.getDefault());
        String emailStr = EmailValues.get(position).toLowerCase(Locale.getDefault());
        String addressStr = AddressValues2.get(position).toLowerCase(Locale.getDefault());


        if (usernameStr.contains(searchedtextStr)) {
            Log.e("test", usernameStr + " contains: " + searchedtextStr);
            int startPos = usernameStr.indexOf(searchedtextStr);
            int endPos = startPos + searchedtextStr.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.username.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.username.setText(spanText, TextView.BufferType.SPANNABLE);
        }
        if (emailStr.contains(searchedtextStr)) {
            Log.e("test", emailStr + " contains: " + searchedtextStr);
            int startPos = emailStr.indexOf(searchedtextStr);
            int endPos = startPos + searchedtextStr.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.useremail.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.useremail.setText(spanText, TextView.BufferType.SPANNABLE);
        }

        if (addressStr.contains(searchedtextStr)) {
            Log.e("test", addressStr + " contains: " + searchedtextStr);
            int startPos = addressStr.indexOf(searchedtextStr);
            int endPos = startPos + searchedtextStr.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.address.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.address.setText(spanText, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public int getItemCount() {

        return SubjectValues.size();
    }


}