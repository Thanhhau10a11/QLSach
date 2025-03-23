package com.example.qlsach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsach.adapter.AuthorAdapter;
import com.example.qlsach.model.Author;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
            Toast.makeText(getContext(), "Mở màn hình thêm tác giả!", Toast.LENGTH_SHORT).show();
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
