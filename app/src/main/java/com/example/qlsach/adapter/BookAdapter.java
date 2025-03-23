package com.example.qlsach.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsach.R;
import com.example.qlsach.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private OnBookClickListener listener;

    // Constructor không có listener (cho trường hợp không cần click menu)
    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
        this.listener = null;
    }

    // Constructor có listener (cho trường hợp cần click menu)
    public BookAdapter(List<Book> bookList, OnBookClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textBookTitle.setText(book.getTenSach());
        holder.textBookAuthor.setText(book.getTenTacGia()); // Hiển thị tên tác giả
        holder.textBookGenre.setText(book.getTenTheLoai()); // Hiển thị tên tác giả

        // Nếu có listener thì gán sự kiện click, nếu không thì bỏ qua
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onMenuClick(book, v));
        } else {
            holder.buttonBookMenu.setVisibility(View.GONE); // Ẩn nút nếu không có listener
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textBookTitle, textBookAuthor, textBookGenre;
        ImageButton buttonBookMenu;
        ImageView imageBookCover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textBookTitle = itemView.findViewById(R.id.text_book_title);
            textBookAuthor = itemView.findViewById(R.id.text_book_author);
            textBookGenre = itemView.findViewById(R.id.text_book_genre);
            buttonBookMenu = itemView.findViewById(R.id.button_book_menu);
            imageBookCover = itemView.findViewById(R.id.image_book_cover);
        }
    }

    public interface OnBookClickListener {
        void onMenuClick(Book book, View view);
    }
}
