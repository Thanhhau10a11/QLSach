package com.example.qlsach;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import com.example.qlsach.adapter.BookAdapter;
import com.example.qlsach.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
public class BooksFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList, filteredList;
    private DatabaseReference databaseReference;
    private SearchView searchView;
    private FloatingActionButton fabAddBook;

    public BooksFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        recyclerView = view.findViewById(R.id.recycler_books);
        searchView = view.findViewById(R.id.search_view);
        fabAddBook = view.findViewById(R.id.fab_add_book);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Gán adapter trước khi fetch dữ liệu
        bookAdapter = new BookAdapter(filteredList);
        recyclerView.setAdapter(bookAdapter);

        fetchBooksFromFirebase(); // Đảm bảo gọi sau khi set adapter
        setupSearchView();
        setupFabButton();

        return view;
    }


    private void fetchBooksFromFirebase() {
        Log.d("BooksFragment", "Fetching books from Firebase...");

        DatabaseReference sachRef = FirebaseDatabase.getInstance().getReference("Sach");
        DatabaseReference theLoaiRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        DatabaseReference tacGiaRef = FirebaseDatabase.getInstance().getReference("TacGia");

        sachRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sachSnapshot) {
                Log.d("BooksFragment", "Data changed, snapshot size: " + sachSnapshot.getChildrenCount());
                bookList.clear();

                for (DataSnapshot sachData : sachSnapshot.getChildren()) {
                    Book book = sachData.getValue(Book.class);
                    if (book == null) continue;

                    book.setId(sachData.getKey()); // Lưu ID của sách

                    // Lấy tên thể loại
                    theLoaiRef.child(book.getIdTheLoai()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot theLoaiSnapshot) {
                            if (theLoaiSnapshot.exists()) {
                                String tenTheLoai = theLoaiSnapshot.child("tenTheLoai").getValue(String.class);
                                book.setTenTheLoai(tenTheLoai);
                            }

                            // Lấy tên tác giả
                            tacGiaRef.child(book.getIdTacGia()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot tacGiaSnapshot) {
                                    if (tacGiaSnapshot.exists()) {
                                        String tenTacGia = tacGiaSnapshot.child("tenTacGia").getValue(String.class);
                                        book.setTenTacGia(tenTacGia);
                                    }

                                    bookList.add(book);
                                    filteredList.clear();
                                    filteredList.addAll(bookList);
                                    bookAdapter.notifyDataSetChanged();
                                    Log.d("BooksFragment", "Book added: " + book.getTenSach() + " - " + book.getTenTheLoai() + " - " + book.getTenTacGia());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("BooksFragment", "Error fetching author data: " + error.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("BooksFragment", "Error fetching category data: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BooksFragment", "Error fetching book data: " + error.getMessage());
                Toast.makeText(getContext(), "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });
    }

    private void filterBooks(String query) {
        filteredList.clear();
        for (Book book : bookList) {
            if (book.getTenSach().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }
        bookAdapter.notifyDataSetChanged();
    }

    private void setupFabButton() {
        fabAddBook.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_book, null);
            builder.setView(dialogView);

            // Ánh xạ các thành phần giao diện
            EditText edtTitle = dialogView.findViewById(R.id.edtBookTitle);
            EditText edtGenreId = dialogView.findViewById(R.id.edtGenreId);
            EditText edtAuthorId = dialogView.findViewById(R.id.edtAuthorId);

            builder.setPositiveButton("Thêm", (dialog, which) -> {
                String tenSach = edtTitle.getText().toString().trim();
                String idTheLoai = edtGenreId.getText().toString().trim();
                String idTacGia = edtAuthorId.getText().toString().trim();

                if (!tenSach.isEmpty() && !idTheLoai.isEmpty() && !idTacGia.isEmpty()) {
                    addBookToFirebase(tenSach, idTheLoai, idTacGia);
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
    private void addBookToFirebase(String tenSach, String idTheLoai, String idTacGia) {
        DatabaseReference sachRef = FirebaseDatabase.getInstance().getReference("Sach");

        // Tạo ID ngẫu nhiên cho sách mới
        String bookId = sachRef.push().getKey();
        if (bookId == null) {
            Toast.makeText(getContext(), "Lỗi tạo ID sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng sách mới
        Book newBook = new Book(tenSach, idTheLoai, idTacGia);

        // Lưu vào Firebase
        sachRef.child(bookId).setValue(newBook)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Sách đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi thêm sách: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
