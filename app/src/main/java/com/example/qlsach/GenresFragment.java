package com.example.qlsach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsach.adapter.CategoryAdapter;
import com.example.qlsach.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class GenresFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabAddGenre;

    public GenresFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        recyclerView = view.findViewById(R.id.recycler_genres);
        fabAddGenre = view.findViewById(R.id.fab_add_genre);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        fetchGenresFromFirebase();

        fabAddGenre.setOnClickListener(v -> {
            // Tạo Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()); // Sử dụng requireContext()
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_genre, null);
            builder.setView(dialogView);

            // Ánh xạ các view trong dialog
            TextInputEditText edtGenreName = dialogView.findViewById(R.id.edit_genre_name);
            Button btnCancel = dialogView.findViewById(R.id.button_cancel);
            Button btnSave = dialogView.findViewById(R.id.button_save);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Xử lý khi nhấn "Hủy"
            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            // Xử lý khi nhấn "Lưu"
            btnSave.setOnClickListener(v1 -> {
                String tenTheLoai = edtGenreName.getText().toString().trim();

                if (tenTheLoai.isEmpty()) {
                    edtGenreName.setError("Vui lòng nhập tên thể loại!");
                } else {
                    // Thực hiện lưu vào database hoặc danh sách (tùy theo yêu cầu)
                    Toast.makeText(requireContext(), "Đã thêm thể loại: " + tenTheLoai, Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Đóng dialog sau khi lưu
                }
            });
        });


        return view;
    }

    private void fetchGenresFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("TheLoai");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
