package models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.happypaws.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Mensaje> mensajeList;
    private int userId;

    public MessageAdapter(List<Mensaje> mensajeList, int userId) {
        this.mensajeList = mensajeList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == 1 ? R.layout.item_message_sent : R.layout.item_message_recieved, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje mensaje = mensajeList.get(position);
        holder.textMessage.setText(mensaje.getContenido());
    }

    @Override
    public int getItemCount() {
        return mensajeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mensajeList.get(position).isEsDeUsuario() ? 1 : 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }

}
