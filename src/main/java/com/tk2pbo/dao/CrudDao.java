package com.tk2pbo.dao;

import java.sql.SQLException;
import java.util.List;

import com.tk2pbo.exception.ValidasiException;

public interface CrudDao<T> {

    void tambah(T objek) throws SQLException, ValidasiException;

    List<T> semua() throws SQLException;

    T cariById(int id) throws SQLException;

    void ubah(T objek) throws SQLException, ValidasiException;

    void hapus(int id) throws SQLException, ValidasiException;
}
