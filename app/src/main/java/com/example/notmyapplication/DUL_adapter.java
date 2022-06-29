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

public class DUL_adapter extends RecyclerView.Adapter<DUL_adapter.MyViewHolder>
{
    private final interface_for_adapter interface_for_adapter;
    Context context;
    ArrayList<user> ItemsList;
    StorageReference storageReference;

    public DUL_adapter(Context context, ArrayList<user> ItemsList, interface_for_adapter interface_for_adapter){
        this.context=context;
        this.ItemsList=ItemsList;
        this.interface_for_adapter = interface_for_adapter;
    }

    @NonNull
    @Override
    public DUL_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.usercell, parent, false);
        return new DUL_adapter.MyViewHolder(view, interface_for_adapter);
    }


    public void onBindViewHolder(@NonNull DUL_adapter.MyViewHolder holder, int position) {
        user u= this.ItemsList.get(position);

        holder.name.setText(u.getUserName());
        holder.phone.setText(u.getPhone()+" ");
        holder.email.setText(u.getEmail()+" ");

        storageReference = FirebaseStorage.getInstance().getReference("Image/Users/" + u.getEmail());
        storageReference = storageReference.child(u.getPic());
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

        TextView name,phone, email;
        ImageView pic;

        public MyViewHolder(@NonNull View itemView, interface_for_adapter interface_for_adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            pic=itemView.findViewById(R.id.imageView2);
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