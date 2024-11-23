package com.project.restaurantedigitalapp.activity.ui.inicio;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.restaurantedigitalapp.R;
import com.project.restaurantedigitalapp.adapter.CategoriaAdapter;
import com.project.restaurantedigitalapp.adapter.SliderAdapter;
import com.project.restaurantedigitalapp.entity.SliderItem;
import com.project.restaurantedigitalapp.viewmodel.CategoriaViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private CategoriaViewModel categoriaViewModel;
    private GridView gvCategorias;
    private CategoriaAdapter categoriaAdapter;
    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }

    private void init(View v){
        svCarrusel = v.findViewById(R.id.svCarrusel);
        ViewModelProvider vmp = new ViewModelProvider(this);
        //Categorías
        categoriaViewModel = vmp.get(CategoriaViewModel.class);
        gvCategorias = v.findViewById(R.id.gvCategorias);
        //Platillos
//        rcvPlatillosRecomendados = v.findViewById(R.id.rcvPlatillosRecomendados);
//        rcvPlatillosRecomendados.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        platilloViewModel = vmp.get(PlatilloViewModel.class);

    }

    private void initAdapter() {
        //Carrusel de Imágenes
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();
        //Categorías
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.items_categorias, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);
//        //Platillos
//        adapter = new PlatillosRecomendadosAdapter(platillos, this, this);
//        rcvPlatillosRecomendados.setAdapter(adapter);
    }

    private void loadData() {
        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.platillos_tipicos, "Los Mejores Platillos"));
        lista.add(new SliderItem(R.drawable.postres_ricos, "Los Mejores Postres"));
        lista.add(new SliderItem(R.drawable.postres_muysabrosos, "Los Mejores Postres"));
        lista.add(new SliderItem(R.drawable.peru_postres, "Los Mejores Postres"));
        sliderAdapter.updateItem(lista);
        categoriaViewModel.listarCategoriasActivas().observe(getViewLifecycleOwner(), response -> {
            if(response.getRpta() == 1){
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            }else{
                System.out.println("Error al obtener las categorías activas");
            }
        });
//        platilloViewModel.listarPlatillosRecomendados().observe(getViewLifecycleOwner(), response -> {
//            adapter.updateItems(response.getBody());
//        });

    }
}