package in.icomputercoding.folkchat.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.icomputercoding.folkchat.Adapters.PostAdapter;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.databinding.FragmentPostDetailsBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


public class PostDetailsFragment extends Fragment {

    String postId;
    private PostAdapter postAdapter;
    private List<Post> postList;
    FragmentPostDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostDetailsBinding.inflate(getLayoutInflater(),container,false);

        SharedPreferences prefs = requireContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        postId = prefs.getString("postId", "none");

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        binding.recyclerView.setAdapter(postAdapter);

        readPost();

        return binding.getRoot();
    }

    private void readPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Post post = dataSnapshot.getValue(Post.class);
                postList.add(post);

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
