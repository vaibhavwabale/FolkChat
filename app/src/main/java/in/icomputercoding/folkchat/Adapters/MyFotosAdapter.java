package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import in.icomputercoding.folkchat.Fragments.PostDetailsFragment;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.FotosItemBinding;

public class MyFotosAdapter extends RecyclerView.Adapter<MyFotosAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<Post> mPosts;

    public MyFotosAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public MyFotosAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fotos_item, parent, false);
        return new MyFotosAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFotosAdapter.ImageViewHolder holder, final int position) {

        final Post post = mPosts.get(position);

        Picasso.get()
                .load(post.getPostImage())
                .placeholder(R.drawable.background)
                .into(holder.binding.postImage);

        holder.binding.postImage.setOnClickListener(view -> {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("postId", post.getPostId());
            editor.apply();

           ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new PostDetailsFragment()).commit();
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        FotosItemBinding binding;


        public ImageViewHolder(View itemView) {
            super(itemView);
            binding = FotosItemBinding.bind(itemView);

        }
    }
}
