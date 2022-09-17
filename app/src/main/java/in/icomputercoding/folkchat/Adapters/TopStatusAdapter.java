package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Activities.HomeActivity;
import in.icomputercoding.folkchat.Model.Status;
import in.icomputercoding.folkchat.Model.UserStatus;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ItemStatusBinding;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder> {

    Context context;
    ArrayList<UserStatus> userStatuses;

   public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {

        UserStatus userStatus = userStatuses.get(position);

        Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);

        Picasso.get()
                .load(lastStatus.getImageUrl())
                .into(holder.binding.profileImage);

       holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

        holder.binding.circularStatusView.setOnClickListener(v -> {
            ArrayList<MyStory> myStories = new ArrayList<>();
            for(Status status : userStatus.getStatuses()) {
                myStories.add(new MyStory(status.getImageUrl()));
            }



            new StoryView.Builder(((HomeActivity)context).getSupportFragmentManager())
                    .setStoriesList(myStories) // Required
                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                    .setTitleText(userStatus.getName()) // Default is Hidden
                    .setSubtitleText("")// Default is Hidden
                    .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position1) {
                            //your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position1) {
                            //your action
                        }
                    }) // Optional Listeners
                    .build() // Must be called before calling show method
                    .show();
        });

        holder.binding.name.setText(userStatus.getName());
        Picasso.get()
                .load(userStatus.getProfileImage())
                .placeholder(R.drawable.background)
                .into(holder.binding.addStoryImg);

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public static class TopStatusViewHolder extends RecyclerView.ViewHolder {

        ItemStatusBinding binding;

        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStatusBinding.bind(itemView);
        }
    }
}
