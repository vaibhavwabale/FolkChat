package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.Follow;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.FriendsRvDesignBinding;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.viewHolder> {

    ArrayList<Follow> list;
    Context context;

    public FollowAdapter(ArrayList<Follow> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_rv_design,parent,false);
        return new FollowAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        FriendsRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendsRvDesignBinding.bind(itemView);
        }
    }
}
