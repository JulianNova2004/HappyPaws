package models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.happypaws.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Paseador> paseadorList;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Paseador paseador);
    }

    public ChatAdapter(List<Paseador> paseadorList, OnChatClickListener listener) {
        this.paseadorList = paseadorList;
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
        Paseador paseador = paseadorList.get(position);
        holder.txtChatName.setText(paseador.getName());
        holder.itemView.setOnClickListener(v -> listener.onChatClick(paseador));
    }

    @Override
    public int getItemCount() {
        return paseadorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtChatName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatName = itemView.findViewById(R.id.txtChatName);
        }
    }
}
