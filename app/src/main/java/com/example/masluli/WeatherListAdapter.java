package com.example.masluli;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.masluli.Model.Model;
        import com.example.masluli.Model.Weather.Forecastday;
        import com.squareup.picasso.Picasso;

        import java.util.List;

class WeatherListAdapter extends RecyclerView.Adapter<WeatherListViewHolder>{

    LayoutInflater inflater;
    List<Forecastday> data;


    public WeatherListAdapter(LayoutInflater inflater, List<Forecastday> data) {
        this.inflater = inflater;
        this.data = data;
    }

    public void setData(List<Forecastday> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.weather_list_row, parent,false);
        WeatherListViewHolder holder = new WeatherListViewHolder(view, data);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherListViewHolder holder, int position) {
        Forecastday forecastday = data.get(position);
        holder.bind(forecastday, position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}

class WeatherListViewHolder extends RecyclerView.ViewHolder{
    TextView tempTv;
    TextView dateTv;
    ImageView iconIv;
    List<Forecastday> data;

    public WeatherListViewHolder(@NonNull View itemView, List<Forecastday> data) {
        super(itemView);
        this.data = data;
        tempTv = itemView.findViewById(R.id.weather_temp);
        dateTv = itemView.findViewById(R.id.weather_date);
        iconIv = itemView.findViewById(R.id.weather_icon);
    }

    void bind(Forecastday forecastday, int pos){
        dateTv.setText(forecastday.getDate());
        tempTv.setText(forecastday.getDay().getMintemp_c() + "°C - " + forecastday.getDay().getMaxtemp_c() + "°C");

//        iconIv.setImageResource(R.drawable.no_image_maslul);
        Picasso.get()
                    .load("https:"+forecastday.getDay().getCondition().getIcon())
                    .into(iconIv);
    }
}
