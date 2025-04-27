package models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.happypaws.R;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {
    private List<User> userList;
    private OnUserChatClickListener listener;

    public interface OnUserChatClickListener {
        void onUserChatClick(User user);
    }

    public UserChatAdapter(List<User> userList, OnUserChatClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtChatName.setText(user.getFirstname() + " " + user.getLastname());
        holder.itemView.setOnClickListener(v -> listener.onUserChatClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtChatName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatName = itemView.findViewById(R.id.txtChatName);
        }
    }
}

