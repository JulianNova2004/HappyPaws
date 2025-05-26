package models;

import android.util.Log;
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
    private boolean isUser;
    //private int userId;
    private int    loggedId;

//    public MessageAdapter(List<Mensaje> mensajeList, int userId) {
//        this.mensajeList = mensajeList;
//        this.userId = userId;
//    }

    public MessageAdapter(List<Mensaje> lista, boolean isUser, int loggedId) {
        this.mensajeList    = lista;
        this.isUser   = isUser;
        this.loggedId = loggedId;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("ViewType", String.valueOf(viewType));
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == 1 ? R.layout.item_message_sent : R.layout.item_message_recieved, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje mensaje = mensajeList.get(position);
        holder.textMessage.setText(mensaje.getContent());
    }

    @Override
    public int getItemCount() {
        return mensajeList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        Log.i("POSICION", String.valueOf(position));
//        Log.i("CONTENT", String.valueOf(mensajeList.get(position).getContent()));
//        Log.i("BOOL", String.valueOf(mensajeList.get(position).isEsDeUsuario()));
//        return mensajeList.get(position).isEsDeUsuario() ? 1 : 0;
        Mensaje msg = mensajeList.get(position);

        if (isUser) {
            return msg.isEsDeUsuario() ? 1 : 0;
        } else {
            return msg.isEsDeUsuario() ? 0 : 1;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }

}
