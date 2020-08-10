package com.azis.skripsiproject.Controller;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface2 {
    @FormUrlEncoded
    @POST("insertPerbaikanFamum.php")
    Call<Data> insertData(
            @Field("key") String key,
            @Field("id_user") String idUser,
            @Field("nama_user") String namaUser,
            @Field("id_barang") String idBrg,
            @Field("jenis") String jenis,
            @Field("tipe") String tipe,
            @Field("nama") String nama,
            @Field("pokja") String pokja,
            @Field("kerusakan") String kerusakan,
            @Field("uraian") String uraian,
            @Field("tanggal") String tanggal,
            @Field("keterangan") String keterangan,
            @Field("biaya") String biaya,
            @Field("gambar") String gambar,
            @Field("status") String status);
}
