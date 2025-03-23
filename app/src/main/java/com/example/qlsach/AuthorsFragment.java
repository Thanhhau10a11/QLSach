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
import com.example.qlsach.adapter.AuthorAdapter;
import com.example.qlsach.model.Author;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AuthorsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AuthorAdapter authorAdapter;
    private List<Author> authorList;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabAddAuthor;

    public AuthorsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authors, container, false);

        recyclerView = view.findViewById(R.id.recycler_authors);
        fabAddAuthor = view.findViewById(R.id.fab_add_author);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        authorList = new ArrayList<>();
        authorAdapter = new AuthorAdapter(authorList);
        recyclerView.setAdapter(authorAdapter);

        fetchAuthorsFromFirebase();

        fabAddAuthor.setOnClickListener(v -> {
            // Tạo Dialog thêm tác giả
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_author, null);
            builder.setView(dialogView);

            TextInputEditText edtAuthorId = dialogView.findViewById(R.id.edit_author_id);
            TextInputEditText edtAuthorName = dialogView.findViewById(R.id.edit_author_name);
            Button btnCancel = dialogView.findViewById(R.id.button_cancel);
            Button btnSave = dialogView.findViewById(R.id.button_save);

            AlertDialog dialog = builder.create();
            dialog.show();

            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            btnSave.setOnClickListener(v1 -> {
                String authorId = edtAuthorId.getText().toString().trim();
                String authorName = edtAuthorName.getText().toString().trim();

                if (authorName.isEmpty()) {
                    edtAuthorName.setError("Vui lòng nhập tên tác giả!");
                    return;
                }

                DatabaseReference authorRef = FirebaseDatabase.getInstance().getReference("TacGia");
                if (authorId.isEmpty()) {
                    authorId = authorRef.push().getKey(); // Tạo ID tự động
                }

                Author newAuthor = new Author(authorId, authorName);
                authorRef.child(authorId).setValue(newAuthor)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "Đã thêm tác giả: " + authorName, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(requireContext(), "Lỗi khi thêm tác giả!", Toast.LENGTH_SHORT).show());
            });
        });

        return view;
    }

    private void fetchAuthorsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("TacGia");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authorList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Author author = data.getValue(Author.class);
                    authorList.add(author);
                }
                authorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
