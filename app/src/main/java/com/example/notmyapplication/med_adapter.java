package com.example.notmyapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class med_adapter extends RecyclerView.Adapter<med_adapter.MyViewHolder>
{
    private final interface_for_adapter interface_for_adapter;
    Context context;
    ArrayList<Meds> ItemsList;
    StorageReference storageReference;

    public med_adapter(Context context, ArrayList<Meds> ItemsList, interface_for_adapter interface_for_adapter){
        this.context=context;
        this.ItemsList=ItemsList;
        this.interface_for_adapter = interface_for_adapter;
    }

    @NonNull
    @Override
    public med_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.medcell, parent, false);
        return new med_adapter.MyViewHolder(view, interface_for_adapter);
    }

    @Override
    public void onBindViewHolder(@NonNull med_adapter.MyViewHolder holder, int position) {
        Meds r= this.ItemsList.get(position);
        holder.medName.setText(r.getMedName());
        holder.price.setText(r.getPrice()+" ");
        storageReference = FirebaseStorage.getInstance().getReference("Image/meds/" + r.getMedName());
        storageReference = storageReference.child(r.getPicName());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into( holder.pic);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView medName, price;
        ImageView pic;
        public MyViewHolder(@NonNull View itemView, interface_for_adapter interface_for_adapter) {
            super(itemView);
            medName = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            pic = itemView.findViewById(R.id.imageView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(interface_for_adapter !=null){
                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            interface_for_adapter.onItemClick(pos);
                        }

                    }
                }
            });

        }
    }
}