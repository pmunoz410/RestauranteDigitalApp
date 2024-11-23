package com.project.restaurantedigitalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.project.restaurantedigitalapp.R;
import com.project.restaurantedigitalapp.entity.service.Usuario;
import com.project.restaurantedigitalapp.utils.DateSerializer;
import com.project.restaurantedigitalapp.utils.TimeSerializer;
import com.project.restaurantedigitalapp.viewmodel.UsuarioViewModel;

import java.sql.Date;
import java.sql.Time;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText edtMail, edtPassword;
    private Button btnIniciarSesion;
    private UsuarioViewModel viewModel;
    private TextInputLayout txtInputUsuario, txtInputPassword;
    private TextView txtNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(v -> {
            try {
                if (validar()) {
                    viewModel.login(edtMail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
                        if(response.getRpta() == 1) {
//                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                            toastCorrecto(response.getMessage());
                            Usuario user = response.getBody();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preferences.edit();
                            final Gson gson = new GsonBuilder()
                                    .registerTypeAdapter(Date.class, new DateSerializer())
                                    .registerTypeAdapter(Time.class, new TimeSerializer())
                                    .create();
                            editor.putString("UsuarioJson", gson.toJson(user, new TypeToken<Usuario>() {
                            }.getType()));
                            editor.apply();
                            edtMail.setText("");
                            edtPassword.setText("");
                            startActivity(new Intent(this, InicioActivity.class));
                        } else {
//                    Toast.makeText(this, "Ocurrió un error " + response.getMessage(), Toast.LENGTH_SHORT).show();
                            toastIncorrecto(response.getMessage());
                        }
                    });
                } else {
                    toastIncorrecto("Por favor, complete todos los campos.");
                }
            } catch (Exception ex) {
                toastIncorrecto("Se ha producido un error al iniciar sesión: " + ex.getMessage());
            }
        });
        txtNuevoUsuario.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarUsuarioActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        edtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                txtInputUsuario.setEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputUsuario.setError(null); // Limpia el mensaje de error
                    txtInputUsuario.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                txtInputPassword.setEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputPassword.setError(null); // Limpia el mensaje de error
                    txtInputPassword.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    private boolean validar() {
        boolean retorno = true;
        String usuario, password;
        usuario = edtMail.getText().toString();
        password = edtPassword.getText().toString();

        if (usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su usario y/o correo electrónico");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else {
            txtInputPassword.setErrorEnabled(false);
        }
        return retorno;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson", "");
        if(!pref.equals("")){
            toastCorrecto("Se detecto una sesión activa, el login será omitido!");
            this.startActivity(new Intent(this, InicioActivity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    @Override
    public void onBackPressed() {
        SweetAlertDialog alert = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Has oprimido el botón atrás")
                .setContentText("¿Quieres cerrar la aplicación?")
                .setCancelText("No, Cancelar!")
                .setConfirmText("Sí, Cerrar")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Operación cancelada")
                            .setContentText("No saliste de la app")
                            .show();
                })
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    finish(); // Cierra la actividad correctamente
                });

        // Ajustar el alto de los botones al mostrar la alerta
        alert.setOnShowListener(dialog -> {
            // Obtener botones de Confirmación y Cancelación
            Button confirmButton = alert.getButton(SweetAlertDialog.BUTTON_CONFIRM);
            Button cancelButton = alert.getButton(SweetAlertDialog.BUTTON_CANCEL);

            // Ajustar el alto si los botones existen
            if (confirmButton != null) {
                ViewGroup.LayoutParams params = confirmButton.getLayoutParams();
                params.height = 80;
                confirmButton.setLayoutParams(params);
            }

            if (cancelButton != null) {
                ViewGroup.LayoutParams params = cancelButton.getLayoutParams();
                params.height = 80;
                cancelButton.setLayoutParams(params);
            }
        });
        alert.show();
    }

}