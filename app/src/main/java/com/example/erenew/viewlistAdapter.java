package com.example.erenew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class viewlistAdapter extends RecyclerView.Adapter<viewlistAdapter.ViewHolder> {
    private Context mContext;
    public List<Vehicle> pay;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public viewlistAdapter(List<Vehicle> pay, Context mContext) {
        this.pay = pay;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public viewlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_list, parent, false);
        return new viewlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewlistAdapter.ViewHolder holder, final int position) {
        holder.name.setText(pay.get(position).getName());
        holder.ic.setText(pay.get(position).getIc());
        holder.phone.setText(pay.get(position).getPhone());
        holder.plate.setText(pay.get(position).getPlateNo());
        holder.date.setText(pay.get(position).getExpDate());
        holder.choice.setText(pay.get(position).getInsurance());
        holder.stat.setText(pay.get(position).getStatus());
        holder.qprice.setText(pay.get(position).getPrice());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quotation = pay.get(position).getQuotation();
                Uri uri = Uri.parse(quotation);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });

        holder.guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plate = pay.get(position).getPlateNo();
                Intent intent  = new Intent(mContext, guidePayment.class);
                intent.putExtra("PlateNo",  plate);
                mContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
            return pay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView name, ic, phone, plate, date, choice, stat, qprice;
        public Button download,guide,req;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewlayout);
            name = (TextView) itemView.findViewById(R.id.name);
            ic = (TextView) itemView.findViewById(R.id.ic);
            phone = (TextView) itemView.findViewById(R.id.phonenum);
            plate = (TextView) itemView.findViewById(R.id.plate);
            date = (TextView) itemView.findViewById(R.id.date);
            choice = (TextView) itemView.findViewById(R.id.company);
            stat = (TextView) itemView.findViewById(R.id.status);
            qprice = (TextView)itemView.findViewById(R.id.price);
            download = (Button)itemView.findViewById(R.id.download);
            guide = (Button)itemView.findViewById(R.id.guide);
        }
    }
}
