package com.project.restaurantedigitalapp.api;

import com.project.restaurantedigitalapp.entity.GenericResponse;
import com.project.restaurantedigitalapp.entity.service.Cliente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClienteApi {
    // Ruta del controlador cliente
    String base = "api/cliente";

    @POST(base)
    Call<GenericResponse<Cliente>> guardarCliente(@Body Cliente client);
}