package com.example.masluli;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


interface OnItemClickListener{
    void onItemClick(View v,int position);
}

class RoutesListAdapter extends RecyclerView.Adapter<RoutesListViewHolder>{

    LayoutInflater inflater;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public RoutesListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public RoutesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.routes_list_row,parent,false);
        RoutesListViewHolder holder = new RoutesListViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesListViewHolder holder, int position) {
//        Donation donation = viewModel.getData().getValue().get(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
//        if(viewModel.getData().getValue() == null){
//            return 0;
//        }
//        return viewModel.getData().getValue().size();
        return 2;
    }
}

class RoutesListViewHolder extends RecyclerView.ViewHolder{
    TextView routeNameTv;
    TextView locationTv;
    TextView descTv;
    TextView userNameTv;

    public RoutesListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        routeNameTv = itemView.findViewById(R.id.routeslistrow_route_name_tv);
        locationTv = itemView.findViewById(R.id.routeslistrow_location_tv);
        descTv = itemView.findViewById(R.id.routeslistrow_desc_tv);
        userNameTv = itemView.findViewById(R.id.routeslistrow_name_tv);

        itemView.setOnClickListener(v -> {
            int pos = getAdapterPosition();
            listener.onItemClick(v, pos);
        });
    }

    void bind(){
        routeNameTv.setText("Nahal David");
        locationTv.setText("Yehuda desert");
        descTv.setText("hard, 6km");
        userNameTv.setText("shir konrad");

    }
}
