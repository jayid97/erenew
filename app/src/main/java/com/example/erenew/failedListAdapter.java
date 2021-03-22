package com.example.erenew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class failedListAdapter extends RecyclerView.Adapter<failedListAdapter.ViewHolder> {

    private Context mContext;
    public List<Vehicle> status;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public failedListAdapter(List<Vehicle> status, Context mContext) {
        this.status = status;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fail, parent, false);
        return new failedListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(status.get(position).getName());
        holder.ic.setText(status.get(position).getIc());
        holder.phone.setText(status.get(position).getPhone());
        holder.plate.setText(status.get(position).getPlateNo());
        holder.date.setText(status.get(position).getExpDate());
        holder.choice.setText(status.get(position).getInsurance());
        holder.type.setText(status.get(position).getType());
        holder.stat.setText(status.get(position).getStatus());
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plat = status.get(position).getPlateNo();
                DocumentReference vehicleRef = db.collection("Vehicle").document(plat);
                vehicleRef.update("Status", "Permintaan untuk memperbaharui",
                        "DateRequest", FieldValue.serverTimestamp(),"Price","0.00"
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext,"Permintaan Berjaya",Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(mContext, Home.class);
                        mContext.startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Permintaan Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView name, ic, phone, plate, date, choice, stat,type;
        public Button request;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewlayout);
            name = (TextView) itemView.findViewById(R.id.name);
            ic = (TextView) itemView.findViewById(R.id.ic);
            phone = (TextView) itemView.findViewById(R.id.phonenum);
            plate = (TextView) itemView.findViewById(R.id.plate);
            date = (TextView) itemView.findViewById(R.id.date);
            type = (TextView)itemView.findViewById(R.id.type);
            choice = (TextView) itemView.findViewById(R.id.company);
            stat = (TextView) itemView.findViewById(R.id.status);
            request = (Button)itemView.findViewById(R.id.request);
        }
    }
}
