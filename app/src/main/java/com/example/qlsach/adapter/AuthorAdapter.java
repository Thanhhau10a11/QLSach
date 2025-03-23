package com.example.qlsach.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsach.R;
import com.example.qlsach.model.Author;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> authorList;
    private DatabaseReference authorRef;

    public AuthorAdapter(List<Author> authorList) {
        this.authorList = authorList;
        this.authorRef = FirebaseDatabase.getInstance().getReference("TacGia");
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authorList.get(position);
        holder.textAuthorName.setText(author.getTenTacGia());

        // Lắng nghe sự kiện nhấn vào menu
        holder.buttonAuthorMenu.setOnClickListener(v -> {
            showEditDeleteDialog(author, v);
        });
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    private void showEditDeleteDialog(Author author, View view) {
        // Hiển thị dialog với các tùy chọn sửa hoặc xóa
        CharSequence[] options = {"Sửa", "Xóa"};
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Sửa
                    editAuthor(author, view);
                    break;
                case 1: // Xóa
                    deleteAuthor(author, view);
                    break;
            }
        });
        builder.show();
    }

    private void editAuthor(Author author, View view) {
        // Hiển thị dialog sửa tác giả
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_add_author, null);
        builder.setView(dialogView);

        TextInputEditText edtAuthorId = dialogView.findViewById(R.id.edit_author_id);
        TextInputEditText edtAuthorName = dialogView.findViewById(R.id.edit_author_name);
        Button btnCancel = dialogView.findViewById(R.id.button_cancel);
        Button btnSave = dialogView.findViewById(R.id.button_save);

        // Điền sẵn tên tác giả vào EditText
        edtAuthorName.setText(author.getTenTacGia());
        edtAuthorId.setText(author.getId());
        edtAuthorId.setEnabled(false); // Không cho sửa ID

        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý khi nhấn "Hủy"
        btnCancel.setOnClickListener(v1 -> dialog.dismiss());

        // Xử lý khi nhấn "Lưu"
        btnSave.setOnClickListener(v1 -> {
            String authorName = edtAuthorName.getText().toString().trim();

            if (authorName.isEmpty()) {
                edtAuthorName.setError("Vui lòng nhập tên tác giả!");
                return;
            }

            // Cập nhật Firebase
            author.setTenTacGia(authorName);
            authorRef.child(author.getId()).setValue(author)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(view.getContext(), "Cập nhật thành công: " + authorName, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(view.getContext(), "Lỗi khi cập nhật tác giả!", Toast.LENGTH_SHORT).show();
                    });
        });
    }


    private void deleteAuthor(Author author, View view) {
        // Xóa tác giả từ Firebase
        authorRef.child(author.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công
                    authorList.remove(author);
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "Tác giả đã được xóa", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xóa thất bại
                    Toast.makeText(view.getContext(), "Lỗi khi xóa tác giả: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView textAuthorName;
        ImageButton buttonAuthorMenu;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            textAuthorName = itemView.findViewById(R.id.text_author_name);
            buttonAuthorMenu = itemView.findViewById(R.id.button_author_menu);
        }
    }
}
