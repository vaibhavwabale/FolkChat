package in.icomputercoding.folkchat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

     FragmentProfileBinding binding;
     ArrayList<User> users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}