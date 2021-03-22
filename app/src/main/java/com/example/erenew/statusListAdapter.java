package com.example.erenew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class statusListAdapter extends RecyclerView.Adapter<statusListAdapter.ViewHolder> {
    private Context mContext;
    public List<Vehicle> status;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public statusListAdapter(List<Vehicle> status, Context mContext) {
        this.status = status;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public statusListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new statusListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final statusListAdapter.ViewHolder holder, int position) {
        holder.name.setText(status.get(position).getName());
        holder.ic.setText(status.get(position).getIc());
        holder.phone.setText(status.get(position).getPhone());
        holder.plate.setText(status.get(position).getPlateNo());
        holder.date.setText(status.get(position).getExpDate());
        holder.choice.setText(status.get(position).getInsurance());
        holder.type.setText(status.get(position).getType());
        holder.stat.setText(status.get(position).getStatus());


    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView name, ic, phone, plate, date, choice, stat, type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewlayout);
            name = (TextView) itemView.findViewById(R.id.name);
            ic = (TextView) itemView.findViewById(R.id.ic);
            phone = (TextView) itemView.findViewById(R.id.phonenum);
            plate = (TextView) itemView.findViewById(R.id.plate);
            date = (TextView) itemView.findViewById(R.id.date);
            choice = (TextView) itemView.findViewById(R.id.company);
            type = (TextView)itemView.findViewById(R.id.type);
            stat = (TextView) itemView.findViewById(R.id.status);

        }
    }
}