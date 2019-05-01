package com.maruf.tourmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maruf.tourmate.Models.Event;
import com.maruf.tourmate.Models.Expense;
import com.maruf.tourmate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {


    private List<Expense> expenseList;
    private Context context;

    ExpenseAdapterInterface expenseAdapterInterface;
    Event event;





    public ExpenseAdapter(Context context, List<Expense> expenseList, Event event) {
        this.expenseList = expenseList;
        this.context = context;
        this.event = event;

        expenseAdapterInterface = (ExpenseAdapterInterface) context;

    }

    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View expenseView = LayoutInflater.from(context).inflate(
                R.layout.single_expense_item, viewGroup, false);


        return new ExpenseAdapter.ExpenseViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, final int i) {



        holder.titleTV.setText(expenseList.get(i).getTitle());
        holder.amountTV.setText("$"+expenseList.get(i).getAmount());
        holder.dateTV.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(new Date(Long.valueOf(expenseList.get(i).getDate()))));


        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseAdapterInterface.onExpenseDeleteImageClicked(expenseList.get(i),event);
            }
        });

        holder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseAdapterInterface.onExpenseEditImageClicked(expenseList.get(i),event);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder{


        TextView titleTV, amountTV, dateTV;
        ImageView editImage, deleteImage;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.expTitleTV);
            amountTV = itemView.findViewById(R.id.expAmountTV);
            dateTV = itemView.findViewById(R.id.expDateTV);
            editImage = itemView.findViewById(R.id.expEditIV);
            deleteImage = itemView.findViewById(R.id.expDeleteIV);



        }


    }

    public interface ExpenseAdapterInterface{

        void onExpenseEditImageClicked(Expense expense, Event event);
        void onExpenseDeleteImageClicked(Expense expense, Event event);
    }


}
