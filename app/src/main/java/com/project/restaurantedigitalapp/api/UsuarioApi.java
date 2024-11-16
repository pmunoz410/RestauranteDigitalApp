package com.project.restaurantedigitalapp.api;

import com.project.restaurantedigitalapp.entity.GenericResponse;
import com.project.restaurantedigitalapp.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {
    // Ruta del controlador usuario
    String base = "api/usuario";

    // Rura del controlador usuario + ruta del método
    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("email") String email, @Field("password") String password);

    @POST(base)
    Call<GenericResponse<Usuario>> save(@Body Usuario user);
}