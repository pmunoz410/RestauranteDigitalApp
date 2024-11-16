package com.project.restaurantedigitalapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.restaurantedigitalapp.api.ClienteApi;
import com.project.restaurantedigitalapp.api.ConfigApi;
import com.project.restaurantedigitalapp.entity.GenericResponse;
import com.project.restaurantedigitalapp.entity.service.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {

    private static ClienteRepository repository;
    private final ClienteApi api;

    public ClienteRepository() {
        this.api = ConfigApi.getClienteApi();
    }

    public static ClienteRepository getInstance() {
        if(repository == null) {
            repository = new ClienteRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente client){
        final MutableLiveData<GenericResponse<Cliente>> mld = new MutableLiveData<>();
        this.api.guardarCliente(client).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}