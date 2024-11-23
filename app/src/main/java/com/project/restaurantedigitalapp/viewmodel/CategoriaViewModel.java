package com.project.restaurantedigitalapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.restaurantedigitalapp.entity.GenericResponse;
import com.project.restaurantedigitalapp.entity.service.Categoria;
import com.project.restaurantedigitalapp.repository.CategoriaRepository;

import java.util.List;

public class CategoriaViewModel extends AndroidViewModel {
    private final CategoriaRepository categoriaRepository;


    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.categoriaRepository = CategoriaRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Categoria>>> listarCategoriasActivas(){
        return this.categoriaRepository.listarCategoriasActivas();
    }
}