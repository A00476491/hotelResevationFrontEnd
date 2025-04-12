package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.HotelViewHolder> {
    private List<Hotel> hotelList;

    private HotelClickListener clickListener;

    public HotelListAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list_viewholder, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.HotelName.setText(hotel.getName());
        holder.Price.setText("$" + hotel.getPrice());
        if ("true".equalsIgnoreCase(hotel.getAvailable())) {
            holder.Availability.setText("Available");
        } else {
            holder.Availability.setText("Not Available");
        }

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void setClickListener(HotelClickListener hotelClickListener){
        this.clickListener = hotelClickListener;
    }

    class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView HotelName, Price, Availability;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            HotelName = itemView.findViewById(R.id.HotelName);
            Price = itemView.findViewById(R.id.Price);
            Availability = itemView.findViewById(R.id.Availability);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }


}