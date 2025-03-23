package com.example.qlsach;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Ánh xạ các CardView
        CardView cardBooks = view.findViewById(R.id.card_books);
        CardView cardAuthors = view.findViewById(R.id.card_authors);
        CardView cardGenres = view.findViewById(R.id.card_genres);
        CardView cardBorrowings = view.findViewById(R.id.card_borrowings);

        // Sự kiện khi nhấn vào các CardView
        cardBooks.setOnClickListener(v -> replaceFragment(new BooksFragment()));
        cardAuthors.setOnClickListener(v -> replaceFragment(new AuthorsFragment()));
        cardGenres.setOnClickListener(v -> replaceFragment(new GenresFragment()));
        cardBorrowings.setOnClickListener(v -> replaceFragment(new BorrowingsFragment()));

        return view;
    }

    // Hàm chuyển Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
