package com.example.qlsach.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsach.R;
import com.example.qlsach.model.Author;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> authorList;

    public AuthorAdapter(List<Author> authorList) {
        this.authorList = authorList;
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
    }

    @Override
    public int getItemCount() {
        return authorList.size();
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
