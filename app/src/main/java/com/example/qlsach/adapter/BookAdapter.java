package com.example.qlsach.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        // Tạo dialog chỉnh sửa
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_book, null);
        builder.setView(dialogView);

        // Ánh xạ các EditText từ dialog
        EditText edtTitle = dialogView.findViewById(R.id.edtBookTitle);
        EditText edtGenreId = dialogView.findViewById(R.id.edtGenreId);
        EditText edtAuthorId = dialogView.findViewById(R.id.edtAuthorId);

        // Điền các giá trị hiện tại của sách vào dialog
        edtTitle.setText(book.getTenSach());
        edtGenreId.setText(book.getIdTheLoai());
        edtAuthorId.setText(book.getIdTacGia());

        // Khi người dùng nhấn nút Lưu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String title = edtTitle.getText().toString().trim();
            String genreId = edtGenreId.getText().toString().trim();
            String authorId = edtAuthorId.getText().toString().trim();

            if (!title.isEmpty() && !genreId.isEmpty() && !authorId.isEmpty()) {
                // Cập nhật thông tin sách trong Firebase
                updateBookInFirebase(book.getId(), title, genreId, authorId, v);
            } else {
                Toast.makeText(v.getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Khi người dùng nhấn nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateBookInFirebase(String bookId, String title, String genreId, String authorId, View v) {
        // Lấy reference của sách cần cập nhật
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Sach").child(bookId);

        // Cập nhật các trường thông tin của sách
        bookRef.child("tenSach").setValue(title);
        bookRef.child("idTheLoai").setValue(genreId);
        bookRef.child("idTacGia").setValue(authorId)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật thành công
                    Toast.makeText(v.getContext(), "Cập nhật thông tin sách thành công!", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại danh sách sách sau khi chỉnh sửa
                    fetchBooksFromFirebase();
                })
                .addOnFailureListener(e -> {
                    // Cập nhật thất bại
                    Toast.makeText(v.getContext(), "Lỗi khi cập nhật sách: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchBooksFromFirebase() {
        // Lấy lại danh sách sách từ Firebase
        // Phương thức này có thể được sử dụng để refresh lại dữ liệu trong adapter
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
