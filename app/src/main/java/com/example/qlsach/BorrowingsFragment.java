package com.example.qlsach;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsach.adapter.BorrowedBookAdapter;
import com.example.qlsach.model.BorrowedBook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BorrowingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private BorrowedBookAdapter adapter;
    private List<BorrowedBook> borrowedBookList;
    private DatabaseReference databaseReference;

    public BorrowingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrowing_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_borrowings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách rỗng & Adapter ngay từ đầu
        borrowedBookList = new ArrayList<>();
        adapter = new BorrowedBookAdapter(borrowedBookList);
        recyclerView.setAdapter(adapter); // ⚠️ Đặt adapter ngay lập tức

        // Lấy dữ liệu từ Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("MuonSach");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                borrowedBookList.clear();
                Log.d("FirebaseData", "Số lượng sách mượn: " + snapshot.getChildrenCount());

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BorrowedBook book = dataSnapshot.getValue(BorrowedBook.class);
                    if (book != null) {
                        borrowedBookList.add(book);
                        Log.d("FirebaseData", "Sách mượn: " + book.getId() + " - " + book.getIdSach());
                    } else {
                        Log.e("FirebaseData", "Lỗi parse dữ liệu!");
                    }
                }

                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }

}
