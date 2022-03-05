package in.icomputercoding.folkchat.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.FragmentAddPostBinding;

public class AddPostFragment extends Fragment {

    FragmentAddPostBinding binding;


    public AddPostFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddPostBinding.inflate(inflater,container,false);

        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = binding.postDescription.getText().toString();
                if (!description.isEmpty())
                {
                    binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fol));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return binding.getRoot();
    }
}