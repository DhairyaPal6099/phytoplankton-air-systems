package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class FunFactAdapter  extends RecyclerView.Adapter<FunFactAdapter.ViewHolder> {
    private final List<String> funFacts;

    public FunFactAdapter(List<String> funFacts) {
        this.funFacts = funFacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fun_fact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.funFactText.setText(funFacts.get(position));
    }

    @Override
    public int getItemCount() {
        return funFacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView funFactText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            funFactText = itemView.findViewById(R.id.funFactText);
        }
    }
}