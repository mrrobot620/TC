package com.mrrobot.tc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GridCodeAdapter extends RecyclerView.Adapter<GridCodeAdapter.ViewHolder> {

    private List<GridCodeEntry> gridCodeList;

    public GridCodeAdapter(List<GridCodeEntry> gridCodeList) {
        this.gridCodeList = gridCodeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_code, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GridCodeEntry entry = gridCodeList.get(position);
//        holder.keyTextView.setText(" " + entry.getKey());
        holder.valueTextView.setText(" " + entry.getValue());
    }

    @Override
    public int getItemCount() {
        return gridCodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView keyTextView;
        private TextView valueTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            keyTextView = itemView.findViewById(R.id.keyTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
        }
    }
}
