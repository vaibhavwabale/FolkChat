package in.icomputercoding.folkchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.icomputercoding.folkchat.Adapters.NotificationAdapter;
import in.icomputercoding.folkchat.Model.Notification;
import in.icomputercoding.folkchat.databinding.FragmentNotificationBinding;


public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    private NotificationAdapter notificationAdapter;
    private List<Notification> list;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(getLayoutInflater(),container,false);

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(),list);
        binding.recyclerView.setAdapter(notificationAdapter);

        readNotification();


        return binding.getRoot();
    }

    private void readNotification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    list.add(notification);
                }
                Collections.reverse(list);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}