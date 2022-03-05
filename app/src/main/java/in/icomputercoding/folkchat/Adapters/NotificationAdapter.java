package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.Notification;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.NotificationRvDesignBinding;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification model = list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        NotificationRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationRvDesignBinding.bind(itemView);

        }
    }
}

