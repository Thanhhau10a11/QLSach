package com.example.qlsach.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsach.R;
import com.example.qlsach.model.Category;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private Context context;
    private OnCategoryChangeListener listener;

    public interface OnCategoryChangeListener {
        void onCategoryUpdated();
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryChangeListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textGenreName.setText(category.getTenTheLoai());

        holder.buttonGenreMenu.setOnClickListener(v -> showPopupMenu(v, category));
    }

    private void showPopupMenu(View view, Category category) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_genre_options, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_edit_genre) {
                showEditDialog(category);
                return true;
            } else if (item.getItemId() == R.id.menu_delete_genre) {
                deleteGenre(category);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showEditDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa thể loại");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_genre, null);
        builder.setView(view);

        TextInputEditText edtGenreId = view.findViewById(R.id.edit_genre_id);
        TextInputEditText edtGenreName = view.findViewById(R.id.edit_genre_name);

        edtGenreId.setText(category.getId());
        edtGenreId.setEnabled(false);
        edtGenreName.setText(category.getTenTheLoai());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = edtGenreName.getText().toString().trim();
            if (newName.isEmpty()) {
                edtGenreName.setError("Tên thể loại không được để trống!");
                return;
            }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai").child(category.getId());
            ref.child("tenTheLoai").setValue(newName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Đã cập nhật thể loại!", Toast.LENGTH_SHORT).show();
                        listener.onCategoryUpdated();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void deleteGenre(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa thể loại")
                .setMessage("Bạn có chắc chắn muốn xóa thể loại này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai").child(category.getId());
                    ref.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Đã xóa thể loại!", Toast.LENGTH_SHORT).show();
                                listener.onCategoryUpdated();
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textGenreName;
        ImageButton buttonGenreMenu;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textGenreName = itemView.findViewById(R.id.text_genre_name);
            buttonGenreMenu = itemView.findViewById(R.id.button_genre_menu);
        }
    }
}
