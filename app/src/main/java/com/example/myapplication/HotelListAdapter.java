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

    private ItemClickListener clickListener;

    public HotelListAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list_layout, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.tvHotelName.setText(hotel.getName());
        holder.tvPrice.setText("$" + hotel.getPrice() + "/night");
        holder.tvAvailability.setText(hotel.getAvailable());
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvHotelName, tvPrice, tvAvailability;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);

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