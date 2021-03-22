package com.example.erenew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class profileListAdapter extends RecyclerView.Adapter<profileListAdapter.ViewHolder> {
    private Context mContext;
    public List<User> pay;

    public profileListAdapter(List<User> pay, Context mContext) {
        this.pay = pay;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public profileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilelist, parent, false);
        return new profileListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(pay.get(position).getName());
        holder.phone.setText(pay.get(position).getPhone());
        holder.email.setText(pay.get(position).getEmail());
        Picasso.get().load(pay.get(position).getImgUri()).into(holder.dp);

        holder.uptodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = pay.get(position).getName();
                String ph = pay.get(position).getPhone();
                String mail = pay.get(position).getEmail();
                String pic = pay.get(position).getImgUri();

                Intent intent = new Intent(mContext, profileUpdate.class);
                intent.putExtra("name", nama);
                intent.putExtra("phone", ph);
                intent.putExtra("imgUri", pic);
                intent.putExtra("email", mail);

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
        public ImageView dp;
        public TextView name, email, phone;
        public Button uptodate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewlayout);
            name = (TextView)itemView.findViewById(R.id.name);
            email = (TextView)itemView.findViewById(R.id.email);
            phone = (TextView)itemView.findViewById(R.id.phone);
            dp = (ImageView)itemView.findViewById(R.id.imageView);
            uptodate = (Button)itemView.findViewById(R.id.update);


        }
    }
}
