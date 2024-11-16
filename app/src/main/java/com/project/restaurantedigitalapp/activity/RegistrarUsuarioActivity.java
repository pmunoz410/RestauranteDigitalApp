package com.project.restaurantedigitalapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.project.restaurantedigitalapp.R;
import com.project.restaurantedigitalapp.entity.service.Cliente;
import com.project.restaurantedigitalapp.entity.service.DocumentoAlmacenado;
import com.project.restaurantedigitalapp.entity.service.Usuario;
import com.project.restaurantedigitalapp.viewmodel.ClienteViewModel;
import com.project.restaurantedigitalapp.viewmodel.DocumentoAlmacenadoViewModel;
import com.project.restaurantedigitalapp.viewmodel.UsuarioViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private File f;
    private ClienteViewModel clienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private DocumentoAlmacenadoViewModel documentoAlmacenadoViewModel;
    private Button btnSubirImagen, btnGuardarDatos;
    private CircleImageView imageUser;
    private AutoCompleteTextView dropdownTipoDoc, dropdownDepartamento, dropdownProvincia, dropdownDistrito;
    private EditText edtNameUser, edtApellidoPaternoU, edtApellidoMaternoU, edtNumDocU, edtTelefonoU,
            edtDireccionU, edtPasswordUser, edtEmailUser;
    private TextInputLayout txtInputNameUser, txtInputApellidoPaternoU, txtInputApellidoMaternoU,
            txtInputTipoDoc, txtInputNumeroDocU, txtInputDepartamento, txtInputProvincia,
            txtInputDistrito, txtInputTelefonoU, txtInputDireccionU, txtInputEmailUser, txtInputPasswordUser;
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        this.init();
        this.initViewModel();
        this.spinners();
    }

    private void spinners() {
        // Lista de tipos de documentos
        String[] tipoDoc = getResources().getStringArray(R.array.tipo_doc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);
        // Lista de departamentos
        String[] departamentos = getResources().getStringArray(R.array.departamentos);
        ArrayAdapter arrayDepartamentos = new ArrayAdapter(this, R.layout.dropdown_item, departamentos);
        dropdownDepartamento.setAdapter(arrayDepartamentos);
        // Lista de provincias
        String[] provincias = getResources().getStringArray(R.array.provincias);
        ArrayAdapter arrayProvincias = new ArrayAdapter(this, R.layout.dropdown_item, provincias);
        dropdownProvincia.setAdapter(arrayProvincias);
        // Lista de distritos
        String[] distritos = getResources().getStringArray(R.array.distritos);
        ArrayAdapter arrayDistritos = new ArrayAdapter(this, R.layout.dropdown_item, distritos);
        dropdownDistrito.setAdapter(arrayDistritos);
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.clienteViewModel = vmp.get(ClienteViewModel.class);
        this.usuarioViewModel = vmp.get(UsuarioViewModel.class);
        this.documentoAlmacenadoViewModel = vmp.get(DocumentoAlmacenadoViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si ya tienes el permiso adecuado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13+ usa READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) !=
                    PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        LOCATION_REQUEST_CODE);
            }
        } else {
            // Para Android 6 a 12 usa READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, "Gracias por conceder los permisos para leer el almacenamiento. Ahora puedes seleccionar tu foto de perfil.", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        btnSubirImagen = findViewById(R.id.btnSubirImagen);
        imageUser = findViewById(R.id.imageUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtApellidoPaternoU = findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = findViewById(R.id.edtApellidoMaternoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtTelefonoU = findViewById(R.id.edtTelefonoU);
        edtDireccionU = findViewById(R.id.edtDireccionU);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);
        dropdownDepartamento = findViewById(R.id.dropdownDepartamento);
        dropdownProvincia = findViewById(R.id.dropdownProvincia);
        dropdownDistrito = findViewById(R.id.dropdownDistrito);
        //TextInputLayout
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputApellidoPaternoU = findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = findViewById(R.id.txtInputApellidoMaternoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputDepartamento = findViewById(R.id.txtInputDepartamento);
        txtInputProvincia = findViewById(R.id.txtInputProvincia);
        txtInputDistrito = findViewById(R.id.txtInputDistrito);
        txtInputTelefonoU = findViewById(R.id.txtInputTelefonoU);
        txtInputDireccionU = findViewById(R.id.txtInputDireccionU);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);
        btnSubirImagen.setOnClickListener(v -> {
            this.cargarImagen();
        });
        btnGuardarDatos.setOnClickListener(v -> {
            this.guardarDatos();
        });
        edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputNameUser.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputNameUser.setError(null); // Limpia el mensaje de error
                    txtInputNameUser.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputApellidoPaternoU.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputApellidoPaternoU.setError(null); // Limpia el mensaje de error
                    txtInputApellidoPaternoU.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputApellidoMaternoU.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputApellidoMaternoU.setError(null); // Limpia el mensaje de error
                    txtInputApellidoMaternoU.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputNumeroDocU.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputNumeroDocU.setError(null); // Limpia el mensaje de error
                    txtInputNumeroDocU.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtTelefonoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputTelefonoU.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputTelefonoU.setError(null); // Limpia el mensaje de error
                    txtInputTelefonoU.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtDireccionU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputDireccionU.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputDireccionU.setError(null); // Limpia el mensaje de error
                    txtInputDireccionU.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dropdownTipoDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputTipoDoc.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputTipoDoc.setError(null); // Limpia el mensaje de error
                    txtInputTipoDoc.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dropdownDepartamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputDepartamento.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputDepartamento.setError(null); // Limpia el mensaje de error
                    txtInputDepartamento.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dropdownProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputProvincia.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputProvincia.setError(null); // Limpia el mensaje de error
                    txtInputProvincia.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dropdownDistrito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                txtInputDistrito.setErrorEnabled(false);
                if (!charSequence.toString().isEmpty()) {
                    txtInputDistrito.setError(null); // Limpia el mensaje de error
                    txtInputDistrito.setErrorEnabled(false); // Desactiva el estado de error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(Intent.createChooser(i, "Seleccione la Aplicación"), 10);
    }

    private void guardarDatos() {
        Cliente c;
        if (validar()) {
            c = new Cliente();
            try {
                c.setNombres(edtNameUser.getText().toString());
                c.setApellidoPaterno(edtApellidoPaternoU.getText().toString());
                c.setApellidoMaterno(edtApellidoMaternoU.getText().toString());
                c.setTipoDoc(dropdownTipoDoc.getText().toString());
                c.setNumDoc(edtNumDocU.getText().toString());
                c.setDepartamento(dropdownDepartamento.getText().toString());
                c.setProvincia(dropdownProvincia.getText().toString());
                c.setDistrito(dropdownDistrito.getText().toString());
                c.setTelefono(edtTelefonoU.getText().toString());
                c.setDireccionEnvio(edtDireccionU.getText().toString());
                c.setId(0);

                // Generar archivo de imagen
                if (f != null && f.exists()) {
                    Log.d("Imagen", "Ruta del archivo: " + f.getAbsolutePath());
                    LocalDateTime ldt = LocalDateTime.now(); //Para generar el nombre al archivo en base a la fecha, hora, año
                    RequestBody rb = RequestBody.create(f, MediaType.parse("multipart/form-data")), somedata; //Le estamos enviando un archivo (imagen) desde el formulario

                    String filename = "" + ldt.getDayOfMonth() + (ldt.getMonthValue() + 1) +
                            ldt.getYear() + ldt.getHour()
                            + ldt.getMinute() + ldt.getSecond(); //Asignar un nombre al archivo (imagen)

                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", f.getName(), rb);
                    somedata = RequestBody.create("profilePh" + filename, MediaType.parse("text/plain")); //Le estamos enviando un nombre al archivo.

                    // Llamada al ViewModel para guardar la imagen
                    this.documentoAlmacenadoViewModel.save(part, somedata).observe(this, response -> {
                        Log.d("Imagen", "Respuesta del servidor para guardar imagen: " + response.getMessage());
                        if (response.getRpta() == 1) {
                            c.setFoto(new DocumentoAlmacenado());
                            c.getFoto().setId(response.getBody().getId());//Asignamos la foto al cliente

                            // Guardar cliente
                            this.clienteViewModel.guardarCliente(c).observe(this, cResponse -> {
                                Log.d("Cliente", "Datos enviados: " + c.toString());
                                if (cResponse != null) {
                                    if (cResponse.getRpta() == 1) {
                                        //Si gustan pueden mostrar este mensaje.
                                        //Toast.makeText(this, response.getMessage() + ", ahora procederemos a registrar sus credenciales.", Toast.LENGTH_SHORT).show();
                                        int idc = cResponse.getBody().getId();//Obtener el id del cliente.
                                        Usuario u = new Usuario();
                                        u.setEmail(edtEmailUser.getText().toString());
                                        u.setClave(edtPasswordUser.getText().toString());
                                        u.setVigencia(true);
                                        u.setCliente(new Cliente(idc));

                                        // Guardar usuario
                                        this.usuarioViewModel.save(u).observe(this, uResponse -> {
                                            //Toast.makeText(this, uResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            if (uResponse.getRpta() == 1) {
                                                //Toast.makeText(this, "Sus Datos y credenciales fueron creados correctamente", Toast.LENGTH_SHORT).show();
                                                successMessage("Estupendo! " + "Su información ha sido guardada con éxito en el sistema.");
                                                //this.finish();
                                            } else {
                                                toastIncorrecto("1. No se ha podido guardar los datos, intentelo de nuevo");
                                            }
                                        });
                                    } else {
                                        toastIncorrecto("2. No se ha podido guardar los datos, intentelo de nuevo");
                                    }
                                } else {
                                    Log.e("Cliente", "Respuesta nula del servidor");
                                    toastIncorrecto("El servidor no respondió. Por favor, verifique la conexión.");
                                }
                            });
                        } else {
                            Log.e("Imagen", "Error al guardar la imagen: " + response.getMessage());
                            toastIncorrecto("Error al guardar la imagen, intentelo de nuevo");
                        }
                    });
                }else {
                    warningMessage("El archivo de imagen no es válido o no existe.");
                }
            } catch (Exception e) {
                warningMessage("Se ha producido un error : " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final String realPath = getRealPathFromURI(uri);
            this.f = new File(realPath);
            this.imageUser.setImageURI(uri);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private boolean validar() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, telefono, direccion, correo, clave,
                dropTipoDoc, dropDepartamento, dropProvincia, dropDistrito;
        nombres = edtNameUser.getText().toString();
        apellidoPaterno = edtApellidoPaternoU.getText().toString();
        apellidoMaterno = edtApellidoMaternoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        telefono = edtTelefonoU.getText().toString();
        direccion = edtDireccionU.getText().toString();
        correo = edtEmailUser.getText().toString();
        clave = edtPasswordUser.getText().toString();
        dropTipoDoc = dropdownTipoDoc.getText().toString();
        dropDepartamento = dropdownDepartamento.getText().toString();
        dropProvincia = dropdownProvincia.getText().toString();
        dropDistrito = dropdownDistrito.getText().toString();
        if (this.f == null) {
            errorMessage("Debe selecionar una foto de perfil");
            retorno = false;
        }
        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingresar número telefónico");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Seleccionar Tipo Doc");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }
        if (dropDepartamento.isEmpty()) {
            txtInputDepartamento.setError("Seleccionar Departamento");
            retorno = false;
        } else {
            txtInputDepartamento.setErrorEnabled(false);
        }
        if (dropProvincia.isEmpty()) {
            txtInputProvincia.setError("Seleccionar Provincia");
            retorno = false;
        } else {
            txtInputProvincia.setErrorEnabled(false);
        }
        if (dropDistrito.isEmpty()) {
            txtInputDistrito.setError("Seleccionar Distrito");
            retorno = false;
        } else {
            txtInputDistrito.setErrorEnabled(false);
        }
        return retorno;
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
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
}