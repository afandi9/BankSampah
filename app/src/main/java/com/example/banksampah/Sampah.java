package com.example.banksampah;

import java.io.Serializable;

public class Sampah implements Serializable {
    private String jenis_sampah;
    private String foto_sampah;
    private String harga_sampah;
    private String kuantitas_sampah;
    private String date_Sampah;
    private String parent_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Sampah(String parent_name, String jenis_sampah, String harga_sampah, String foto_sampah, String date_sampah, String kuantitas_sampah) {
        this.parent_name = parent_name;
        this.kuantitas_sampah = kuantitas_sampah;
        this.date_Sampah = date_sampah;
        this.jenis_sampah = jenis_sampah;
        this.harga_sampah = harga_sampah;
        this.foto_sampah = foto_sampah;
    }

    public Sampah() {
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getKuantitas_sampah() {
        return kuantitas_sampah;
    }

    public void setKuantitas_sampah(String kuantitas_sampah) {
        this.kuantitas_sampah = kuantitas_sampah;
    }

    public void setDate_Sampah(String date_Sampah) {
        this.date_Sampah = date_Sampah;
    }

    public String getDate_Sampah() {
        return date_Sampah;
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
