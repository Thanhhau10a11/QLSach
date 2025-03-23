package com.example.qlsach.model;

public class Category {
    private String id;
    private String tenTheLoai;

    public void setId(String id) {
        this.id = id;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public Category() {}

    public Category(String id, String tenTheLoai) {
        this.id = id;
        this.tenTheLoai = tenTheLoai;
    }

    public String getId() {
        return id;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }
}
