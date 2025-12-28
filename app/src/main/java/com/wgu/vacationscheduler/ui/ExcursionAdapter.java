package com.wgu.vacationscheduler.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.entities.Excursion;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> excursions = new ArrayList<>();
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Excursion excursion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setExcursions(List<Excursion> list) {
        excursions = (list == null) ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        Excursion e = excursions.get(position);
        holder.tvTitle.setText(e.getTitle() == null ? "(No title)" : e.getTitle());
        holder.tvDate.setText(formatter.format(new Date(e.getDate())));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(e);
        });
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }


    static class ExcursionViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate;

        ExcursionViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvExcTitle);
            tvDate = itemView.findViewById(R.id.tvExcDate);
        }
    }
}

