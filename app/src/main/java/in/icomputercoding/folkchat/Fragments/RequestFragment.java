package in.icomputercoding.folkchat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Adapters.NotificationAdapter;
import in.icomputercoding.folkchat.Model.Notification;
import in.icomputercoding.folkchat.R;


public class RequestFragment extends Fragment {

    RecyclerView requestRv;
    ArrayList<Notification> list;

    public RequestFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        requestRv = view.findViewById(R.id.requestRecyclerView);

        list = new ArrayList<>();
      /*  list.add(new Notification(R.drawable.profile_user,"<b>Vaibhav Wable</b> Send you a friend request","just now"));
        list.add(new Notification(R.drawable.cover,"<b>Sanket Nachankar</b> Send you a friend request","40 minutes ago"));
        list.add(new Notification(R.drawable.cover,"<b>Shubham Landge</b> Suggested for you","2 hours"));
        list.add(new Notification(R.drawable.profile_user,"<b>Shravan Lagad</b> Send you a friend request","3 hours"));
        list.add(new Notification(R.drawable.profile_user,"<b>Siddesh Bhosale</b> Suggested for you","3 hours"));
        list.add(new Notification(R.drawable.cover,"<b>Eshali Wable</b> Send you a friend request","3 hours"));
        list.add(new Notification(R.drawable.profile_user,"<b>Siddesh Bhosale</b> Send you a friend request","3 hours"));
        list.add(new Notification(R.drawable.cover,"<b>Vaibhav Wable</b> Send you a friend request","3 hours")); */


        NotificationAdapter adapter = new NotificationAdapter(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        requestRv.setLayoutManager(layoutManager);
        requestRv.setNestedScrollingEnabled(false);
        requestRv.setAdapter(adapter);

        return view;
    }
}