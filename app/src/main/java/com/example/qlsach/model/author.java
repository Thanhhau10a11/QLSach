package com.example.qlsach.model;

public class Author {
    private String id;
    private String tenTacGia;

    public void setId(String id) {
        this.id = id;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public Author() {}

    public Author(String id, String tenTacGia) {
        this.id = id;
        this.tenTacGia = tenTacGia;
    }

    public String getId() {
        return id;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }
}
