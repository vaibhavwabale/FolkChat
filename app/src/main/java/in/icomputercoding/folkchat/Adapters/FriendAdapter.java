package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.Friend;
import in.icomputercoding.folkchat.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.viewHolder>{

    ArrayList<Friend> list;
    Context context;

    public FriendAdapter(ArrayList<Friend> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Friend friend = list.get(position);
        holder.profile.setImageResource(friend.getProfile());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profile;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            profile = itemView.findViewById(R.id.profile_image);

        }
    }


}
