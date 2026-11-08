package com.tk2pbo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
interface Pengisi {
    void isi(PreparedStatement ps) throws SQLException;
}
