package in.icomputercoding.folkchat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import in.icomputercoding.folkchat.databinding.FragmentPostsBinding;


public class PostsFragment extends Fragment {

    FragmentPostsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPostsBinding.inflate(getLayoutInflater());
        return  binding.getRoot();

    }
}