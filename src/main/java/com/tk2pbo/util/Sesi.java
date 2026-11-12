package com.tk2pbo.util;

import com.tk2pbo.model.Pengguna;

public final class Sesi {

    private static Pengguna pengguna;

    private Sesi() { }

    public static void mulai(Pengguna p) {
        pengguna = p;
    }

    public static Pengguna getPengguna() {
        return pengguna;
    }

    public static void akhiri() {
        pengguna = null;
    }
}
