package com.example.qlsach.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsach.R;
import com.example.qlsach.model.BorrowedBook;

import java.util.List;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder> {
    private List<BorrowedBook> borrowedBookList;

    public BorrowedBookAdapter(List<BorrowedBook> borrowedBookList) {
        this.borrowedBookList = borrowedBookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrowing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowedBook book = borrowedBookList.get(position);

        // Sử dụng đúng thuộc tính từ model BorrowedBook
        holder.textBookTitle.setText(book.getIdSach());  // ID sách
        holder.textBorrowDate.setText(book.getNgayMuon());  // Ngày mượn
        holder.textDueDate.setText(book.getNgayTra());  // Ngày trả
    }

    @Override
    public int getItemCount() {
        return borrowedBookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textBookTitle, textBorrowDate, textDueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBookTitle = itemView.findViewById(R.id.text_book_title);
            textBorrowDate = itemView.findViewById(R.id.text_borrow_date);
            textDueDate = itemView.findViewById(R.id.text_due_date);
        }
    }
}
