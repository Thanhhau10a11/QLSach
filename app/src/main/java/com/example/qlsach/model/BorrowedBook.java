package com.example.qlsach.model;

public class BorrowedBook {
    private String id;
    private String idSach;
    private String ngayMuon;
    private String ngayTra;

    public void setId(String id) {
        this.id = id;
    }

    public void setIdSach(String idSach) {
        this.idSach = idSach;
    }

    public void setNgayMuon(String ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public BorrowedBook() {}
    public BorrowedBook(String id,String idSach, String ngayMuon, String ngayTra) {
        this.id = id;
        this.idSach = idSach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public BorrowedBook(String idSach, String ngayMuon, String ngayTra) {
        this.id = id;
        this.idSach = idSach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }
    public BorrowedBook( String ngayMuon, String ngayTra) {
        this.idSach = idSach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public String getId() {
        return id;
    }

    public String getIdSach() {
        return idSach;
    }

    public String getNgayMuon() {
        return ngayMuon;
    }

    public String getNgayTra() {
        return ngayTra;
    }
}
