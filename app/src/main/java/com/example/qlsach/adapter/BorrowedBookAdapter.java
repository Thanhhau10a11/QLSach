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
    private OnItemClickListener listener; // Interface xử lý sự kiện click

    // Interface sự kiện click
    public interface OnItemClickListener {
        void onItemClick(BorrowedBook book);
    }

    // Constructor
    public BorrowedBookAdapter(List<BorrowedBook> borrowedBookList, OnItemClickListener listener) {
        this.borrowedBookList = borrowedBookList;
        this.listener = listener;
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

        // Hiển thị thông tin sách mượn
        holder.textBookTitle.setText(book.getIdSach());
        holder.textBorrowDate.setText(book.getNgayMuon());
        holder.textDueDate.setText(book.getNgayTra());

        // Gán sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(book);
            }
        });
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
