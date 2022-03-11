package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.icomputercoding.folkchat.Model.Comment;
import in.icomputercoding.folkchat.databinding.CommentSampleBinding;

public class CommentAdapter {

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{
        Context context = context;
        ArrayList<Comment> list;


        public CommentAdapter(Context context, ArrayList<Comment> list) {
            this.context = context;
            this.list = list;
        }


        @NonNull
        @Override
        public class viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

            View view = LayoutInflater.from(context).inflate(R.layout.comment_sample, parent, attachToRoot:false);
            return new viewHolder(view);
        }
            @Override
            public void onBindViewHolder(@NonNull viewHolder holder,int position) {
                Comment comment = list.get(position);
                holder.binding.comment.setText(comment.getCommentBody());
                holder.binding.comment.time.setText(comment.getCommentAt()+"")

            }
                @Override
                public int getItemCount() {
                    return list.size();
                }

                public class viewHolder extends RecyclerView.ViewHolder{

            }
        }
    }

