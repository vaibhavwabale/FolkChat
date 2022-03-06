package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.PostsRvDesignBinding;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.viewHolder> {

    ArrayList<Post> list;
    Context context;
    boolean clicked = false;
    int likeCount;

    public PostsAdapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.binding.postImg.setOnClickListener(v -> {
            if (!clicked){
                holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                likeCount = Integer.parseInt(holder.binding.like.getText()+"");
                //holder.like.setText(likeCount++);
                clicked = true;
            }
            else {
                holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);
                likeCount = Integer.parseInt(holder.binding.like.getText()+"");
                // holder.like.setText(Integer.parseInt(model.getLike())-1);
                clicked = false;
            }
        });

        holder.binding.saveImg.setOnClickListener(v -> {

            if (!clicked){
                holder.binding.saveImg.setImageResource(R.drawable.saved);
                clicked = true;
            }
            else {
                holder.binding.saveImg.setImageResource(R.drawable.ic_bookmark);
                clicked = false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        PostsRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostsRvDesignBinding.bind(itemView);

        }
    }
}

