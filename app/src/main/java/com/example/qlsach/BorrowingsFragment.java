package com.example.qlsach;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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

import com.example.qlsach.adapter.BorrowedBookAdapter;
import com.example.qlsach.model.BorrowedBook;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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

        FloatingActionButton fabAddBorrowing = view.findViewById(R.id.fab_add_borrowing);

        fabAddBorrowing.setOnClickListener(v -> {
            // Sử dụng inflater đã có sẵn
            View dialogView = inflater.inflate(R.layout.dialog_add_borrowing, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(dialogView);

            // Ánh xạ view
            TextInputEditText edtIdSach = dialogView.findViewById(R.id.edit_id_sach);
            TextInputEditText edtNgayMuon = dialogView.findViewById(R.id.edit_ngay_muon);
            TextInputEditText edtNgayTra = dialogView.findViewById(R.id.edit_ngay_tra);
            Button btnCancel = dialogView.findViewById(R.id.button_cancel);
            Button btnSave = dialogView.findViewById(R.id.button_save);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Xử lý sự kiện nút Hủy
            btnCancel.setOnClickListener(v1 -> dialog.dismiss());

            // Xử lý sự kiện nút Lưu
            btnSave.setOnClickListener(v1 -> {
                String idSach = edtIdSach.getText().toString().trim();
                String ngayMuon = edtNgayMuon.getText().toString().trim();
                String ngayTra = edtNgayTra.getText().toString().trim();

                if (idSach.isEmpty() || ngayMuon.isEmpty() || ngayTra.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu dữ liệu vào database hoặc danh sách
                    Toast.makeText(requireContext(), "Đã thêm phiếu mượn!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });


        return view;
    }
    private void showDatePickerDialog(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

}
