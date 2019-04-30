package com.example.banksampah.Model;

public class Sampah {
    private String jenis_sampah;
    private String foto_sampah;
    private String harga_sampah;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Sampah(String jenis_sampah, String harga_sampah, String foto_sampah) {
        this.jenis_sampah = jenis_sampah;
        this.harga_sampah = harga_sampah;
        this.foto_sampah = foto_sampah;
    }

    public Sampah() {
    }

    public String getJenis_sampah() {
        return jenis_sampah;
    }

    public void setJenis_sampah(String jenis_sampah) {
        this.jenis_sampah = jenis_sampah;
    }

    public String getFoto_sampah() {
        return foto_sampah;
    }

    public void setFoto_sampah(String foto_sampah) {
        this.foto_sampah = foto_sampah;
    }

    public String getHarga_sampah() {
        return harga_sampah;
    }

    public void setHarga_sampah(String harga_sampah) {
        this.harga_sampah = harga_sampah;
    }
}
