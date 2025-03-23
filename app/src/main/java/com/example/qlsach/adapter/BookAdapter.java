package com.example.qlsach.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsach.R;
import com.example.qlsach.model.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> bookList;
    private DatabaseReference bookRef;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
        this.bookRef = FirebaseDatabase.getInstance().getReference("Sach");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        // Hiển thị thông tin sách
        holder.textBookTitle.setText(book.getTenSach());
        holder.textBookAuthor.setText(book.getTenTacGia());
        holder.textBookGenre.setText(book.getTenTheLoai());

        // Lắng nghe sự kiện nhấn nút sửa
        holder.buttonEditBook.setOnClickListener(v -> {
            // Gọi API sửa sách tại đây
            editBook(book, v);
        });

        // Lắng nghe sự kiện nhấn nút xóa
        holder.buttonDeleteBook.setOnClickListener(v -> {
            // Gọi API xóa sách tại đây
            deleteBook(book, v);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    private void editBook(Book book, View v) {
        // Chỉnh sửa sách (ví dụ: hiển thị dialog để thay đổi thông tin)
        // Đây là ví dụ cơ bản, bạn có thể mở một dialog cho phép người dùng sửa thông tin sách
        Toast.makeText(v.getContext(), "Chỉnh sửa sách: " + book.getTenSach(), Toast.LENGTH_SHORT).show();
        // Cập nhật dữ liệu sách vào Firebase nếu cần
    }

    private void deleteBook(Book book, View v) {
        // Xóa sách từ Firebase
        bookRef.child(book.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công
                    bookList.remove(book);
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Sách đã được xóa", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xóa thất bại
                    Toast.makeText(v.getContext(), "Lỗi khi xóa sách: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textBookTitle, textBookAuthor, textBookGenre;
        ImageButton buttonEditBook, buttonDeleteBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBookTitle = itemView.findViewById(R.id.text_book_title);
            textBookAuthor = itemView.findViewById(R.id.text_book_author);
            textBookGenre = itemView.findViewById(R.id.text_book_genre);
            buttonEditBook = itemView.findViewById(R.id.button_edit_book);
            buttonDeleteBook = itemView.findViewById(R.id.button_delete_book);
        }
    }
}
