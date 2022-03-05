package in.icomputercoding.folkchat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Adapters.SearchAdapter;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.databinding.FragmentSearchBinding;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        SearchAdapter adapter = new SearchAdapter(getContext(),list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.listView.setLayoutManager(layoutManager);
        binding.listView.setAdapter(adapter);


        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUid(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(user);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }






}