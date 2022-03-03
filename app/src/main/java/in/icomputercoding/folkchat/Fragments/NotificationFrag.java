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
import in.icomputercoding.folkchat.Model.NotificationModel;
import in.icomputercoding.folkchat.R;


public class NotificationFrag extends Fragment {

    RecyclerView notificationRV;
    ArrayList<NotificationModel> list;

    public NotificationFrag() {
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
        View view = inflater.inflate(R.layout.fragment_notification2, container, false);
        notificationRV = view.findViewById(R.id.notificationRV);
        list = new ArrayList<>();
        list.add(new NotificationModel(R.drawable.cover,"<b>Vaibhav Wable</b> mention you in a comment: Nice Try","just now"));
        list.add(new NotificationModel(R.drawable.profile_user,"<b>Siddesh Bhosale</b> Liked your picture.","40 minutes ago"));
        list.add(new NotificationModel(R.drawable.cover,"<b>Shubham Landge</b> Commented on your post.","2 hours"));
        list.add(new NotificationModel(R.drawable.profile_user,"<b>Vaibhav Wable</b> mention you in a comment: Nice Try","3 hours"));
        list.add(new NotificationModel(R.drawable.cover,"<b>Siddesh Bhosale</b> Liked your picture.","3 hours"));
        list.add(new NotificationModel(R.drawable.profile_user,"<b>Shubham Landge</b> Commented on your post.","4 hours"));
        list.add(new NotificationModel(R.drawable.cover,"<b>Vaibhav Wable</b> mention you in a comment: Nice Try","4 hours"));
        list.add(new NotificationModel(R.drawable.profile_user,"<b>Siddesh Bhosale</b> Liked your picture.","4 hours"));
        list.add(new NotificationModel(R.drawable.cover,"<b>Shubham Landge</b> Commented on your post.","4 hours"));



        NotificationAdapter adapter = new NotificationAdapter(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notificationRV.setLayoutManager(layoutManager);
        notificationRV.setNestedScrollingEnabled(false);
        notificationRV.setAdapter(adapter);

        return view;
    }
}