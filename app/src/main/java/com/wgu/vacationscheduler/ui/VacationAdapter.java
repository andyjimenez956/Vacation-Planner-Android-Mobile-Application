package com.wgu.vacationscheduler.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> vacations = new ArrayList<>();
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);
        holder.tvTitle.setText(vacation.getTitle() == null ? "(No title)" : vacation.getTitle());

        String dates = formatter.format(new Date(vacation.getStartDate()))
                + " - " + formatter.format(new Date(vacation.getEndDate()));

        holder.tvDates.setText(dates);

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) listener.onItemClick(vacation);
        });
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations == null ? new ArrayList<>() : vacations;
        notifyDataSetChanged();
    }

    static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDates;

        VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDates = itemView.findViewById(R.id.tvDates);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Vacation vacation);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
