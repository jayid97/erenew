package com.example.erenew;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    private Context mContext;
    public List<Vehicle> vehicles;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference statusref = db.collection("Vehicle");


    public VehicleListAdapter(List<Vehicle> vehicles,Context mContext)
    {
        this.vehicles = vehicles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.name.setText(vehicles.get(position).getName());
        holder.ic.setText(vehicles.get(position).getIc());
        holder.phone.setText(vehicles.get(position).getPhone());
        holder.plate.setText(vehicles.get(position).getPlateNo());
        holder.date.setText(vehicles.get(position).getExpDate());
        holder.choice.setText(vehicles.get(position).getInsurance());
        holder.stat.setText(vehicles.get(position).getStatus());
        holder.type.setText(vehicles.get(position).getType());
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String[] options = {"Kemas Kini Data", "Padam Data" , "Permintaan untuk memperbaharui"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0)
                        {
                            // update when click
                            String plat = vehicles.get(position).getPlateNo();
                            String nama = vehicles.get(position).getName();
                            String nric = vehicles.get(position).getIc();
                            String ph = vehicles.get(position).getPhone();

                            Intent intent = new Intent(mContext, vehicleupdate.class);
                            intent.putExtra("PlateNo", plat);
                            intent.putExtra("Name", nama);
                            intent.putExtra("IC", nric);
                            intent.putExtra("Phone", ph);

                            mContext.startActivity(intent);
                        }
                        if(which == 1)
                        {
                            // delete when click
                            String plat = vehicles.get(position).getPlateNo();
                            db.collection("Vehicle").document(plat).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mContext,"Padam Data Berjaya",Toast.LENGTH_SHORT).show();
                                    Intent intent  = new Intent(mContext, Home.class);
                                    mContext.startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mContext, "Padam Data Gagal", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if(which == 2)
                            {
                                String plat = vehicles.get(position).getPlateNo();
                                DocumentReference vehicleRef = db.collection("Vehicle").document(plat);
                                vehicleRef.update("Status", "Permintaan untuk memperbaharui",
                                        "DateRequest", FieldValue.serverTimestamp()
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
        }
    }).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CardView cardView;
        public TextView name,ic,phone,plate,date,choice,stat,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardviewlayout);
            name = (TextView)itemView.findViewById(R.id.name);
            ic = (TextView)itemView.findViewById(R.id.ic);
            phone = (TextView)itemView.findViewById(R.id.phonenum);
            plate = (TextView)itemView.findViewById(R.id.plate);
            date = (TextView)itemView.findViewById(R.id.date);
            choice = (TextView)itemView.findViewById(R.id.company);
            stat = (TextView)itemView.findViewById(R.id.status);
            type = (TextView)itemView.findViewById(R.id.type);
        }
    }



}
