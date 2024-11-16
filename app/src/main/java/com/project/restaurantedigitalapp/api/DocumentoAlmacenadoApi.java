package com.project.restaurantedigitalapp.api;

import com.project.restaurantedigitalapp.entity.GenericResponse;
import com.project.restaurantedigitalapp.entity.service.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentoAlmacenadoApi {
    // Ruta del controlador documento almacenado
    String base = "api/documento-almacenado";

    @Multipart
    @POST(base)
    Call<GenericResponse<DocumentoAlmacenado>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}