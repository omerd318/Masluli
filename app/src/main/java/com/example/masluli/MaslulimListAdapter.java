package com.example.masluli;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masluli.Model.Maslul;
import com.squareup.picasso.Picasso;

import java.util.List;


interface OnItemClickListener{
    void onItemClick(View v,int position);
}

class MaslulimListAdapter extends RecyclerView.Adapter<MaslulimListViewHolder>{

    LayoutInflater inflater;
    OnItemClickListener listener;
    List<Maslul> data;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public MaslulimListAdapter(LayoutInflater inflater, List<Maslul> data) {
        this.inflater = inflater;
        this.data = data;
    }

    public void setData(List<Maslul> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MaslulimListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.maslulim_list_row,parent,false);
        MaslulimListViewHolder holder = new MaslulimListViewHolder(view,listener, data);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaslulimListViewHolder holder, int position) {
        Maslul maslul = data.get(position);
        holder.bind(maslul, position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}

class MaslulimListViewHolder extends RecyclerView.ViewHolder{
    TextView maslulNameTv;
    TextView locationTv;
    TextView descTv;
    TextView userNameTv;
    ImageView image;
    List<Maslul> data;

    public MaslulimListViewHolder(@NonNull View itemView, OnItemClickListener listener, List<Maslul> data) {
        super(itemView);
        this.data = data;
        maslulNameTv = itemView.findViewById(R.id.maslulimlistrow_maslul_name_tv);
        locationTv = itemView.findViewById(R.id.maslulimlistrow_location_tv);
        descTv = itemView.findViewById(R.id.maslulimlistrow_desc_tv);
        userNameTv = itemView.findViewById(R.id.maslulimlistrow_name_tv);
        image = itemView.findViewById(R.id.maslulimlistrow_img);

        itemView.setOnClickListener(v -> {
            int pos = getAdapterPosition();
            listener.onItemClick(v, pos);
        });
    }

    void bind(Maslul maslul, int pos){
        maslulNameTv.setText(maslul.getTitle());
        locationTv.setText(maslul.getLocation());
        descTv.setText(maslul.getDifficulty().name() + ", " + maslul.getLength() + " Km");
        userNameTv.setText(maslul.getUserId());  // TODO: get user name
        image.setImageResource(R.drawable.no_image);
        if (maslul.getImageUrl() != null && !maslul.getImageUrl().equals("")) {
            Picasso.get()
                    .load(maslul.getImageUrl())
                    .into(image);
        }

    }
}
