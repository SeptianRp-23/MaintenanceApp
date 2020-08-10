package com.azis.skripsiproject.Controller;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterfaceLapor {
    @FormUrlEncoded
    @POST("insertPekerjaanSelesai.php")
    Call<DataLapor> insertData(
            @Field("key") String key,
            @Field("id_perbaikan") String id_perbaikan,
            @Field("id_user") String id_user,
            @Field("nama") String nama,
            @Field("jenis") String jenis,
            @Field("tipe") String tipe,
            @Field("kerusakan") String kerusakan,
            @Field("detail") String detail,
            @Field("perbaikan") String perbaikan,
            @Field("biaya") String biaya,
            @Field("tanggal") String tanggal,
            @Field("gambar") String gambar);

}
