package in.bitstreet.com.itdwallet.view.DashBoard.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.model.model.Model;
import in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter.TransactionFragmentAdapter;
import in.bitstreet.com.itdwallet.view.contact.adapter.DividerItemDecoration;

public class TransactionFragment extends Fragment {

    public TransactionFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        ArrayList<Model> list= new ArrayList<>();
        list.add(new Model(Model.HEADER_TEXT_TYPE,"Recent",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","12 BTC","Nov 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Nov 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received.","0.2546 BTC","Nov 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Nov 5,20173",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","12 BTC","Nov 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","0.2546 BTC","Nov 5,2017",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","12 BTC","Nov 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","0.2546 BTC","Nov 5,2017",0));

        list.add(new Model(Model.HEADER_TEXT_TYPE,"July",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","12 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","12 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received ","0.2546 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent ","0.2546 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received ","0.2546 BTC","Jul 5,2017",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","12 BTC","Jul 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received ","0.2546 BTC","Jul 5,2017",0));

        list.add(new Model(Model.HEADER_TEXT_TYPE,"August",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","12 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","0.2546 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","12 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received ","0.2546 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Aug 5,2017",0));

        list.add(new Model(Model.TEXT_CHILD_TYPE,"Sent","0.2546 BTC","Aug 5,2017",0));
        list.add(new Model(Model.TEXT_CHILD_TYPE,"Received","0.2546 BTC","Aug 5,2017",0));







        TransactionFragmentAdapter adapter = new TransactionFragmentAdapter(list,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);

        RecyclerView mRecyclerView = (RecyclerView)view. findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
// Inflate the layout for this fragment
        return view;
    }

}