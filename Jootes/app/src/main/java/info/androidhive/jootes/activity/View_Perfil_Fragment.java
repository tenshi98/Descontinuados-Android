package info.androidhive.jootes.activity;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import info.androidhive.jootes.R;
import info.androidhive.jootes.app.AppConfig;
import info.androidhive.jootes.helper.DB_usuarios;
import info.androidhive.jootes.helper.SessionManager;



public class View_Perfil_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_email;
    private TextView txt_Nombre;
    private TextView txt_Rut;
    private TextView txt_fNacimiento;
    private TextView txt_Direccion;
    private TextView txt_Fono1;
    private TextView txt_Fono2;
    private TextView txt_Departamento;
    private TextView txt_Provincia;
    private TextView txt_Distrito;

    //declaracion de botones
    private Button btnEditUser;
    private Button btnEditImg;

    private Bitmap loadedImage;
    private ImageView imageView;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Perfil_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_profile));

        //Se toman los botones y textos del layout
        txt_email            = (TextView) rootView.findViewById(R.id.email);
        txt_Nombre           = (TextView) rootView.findViewById(R.id.Nombre);
        txt_Rut              = (TextView) rootView.findViewById(R.id.Rut);
        txt_fNacimiento      = (TextView) rootView.findViewById(R.id.fNacimiento);
        txt_Departamento     = (TextView) rootView.findViewById(R.id.Departamento);
        txt_Provincia        = (TextView) rootView.findViewById(R.id.Provincia);
        txt_Distrito         = (TextView) rootView.findViewById(R.id.Distrito);
        txt_Direccion        = (TextView) rootView.findViewById(R.id.Direccion);
        txt_Fono1            = (TextView) rootView.findViewById(R.id.Fono1);
        txt_Fono2            = (TextView) rootView.findViewById(R.id.Fono2);

        imageView            = (ImageView) rootView.findViewById(R.id.circular_image_view);


        //txt_factualizacion   = (TextView) rootView.findViewById(R.id.name);


        btnEditUser = (Button) rootView.findViewById(R.id.btnModificar);
        btnEditImg = (Button) rootView.findViewById(R.id.btnImagen);

        // se conecta a la base de datos interna
        dataUsuarios = new DB_usuarios(getActivity());

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();

        String idCliente       = user.get("idCliente");
        String email           = user.get("email");
        String Nombre          = user.get("Nombre");
        String Rut             = user.get("Rut");
        String fNacimiento     = user.get("fNacimiento");
        String Direccion       = user.get("Direccion");
        String Fono1           = user.get("Fono1");
        String Fono2           = user.get("Fono2");
        String Departamento    = user.get("Departamento");
        String Provincia       = user.get("Provincia");
        String Distrito        = user.get("Distrito");


        // se escriben los datos en los respectivos elementos
        txt_email.setText(email);
        txt_Nombre.setText(Nombre);
        txt_Rut.setText(Rut);
        txt_fNacimiento.setText(fNacimiento);
        txt_Departamento.setText(Departamento);
        txt_Provincia.setText(Provincia);
        txt_Distrito.setText(Distrito);
        txt_Direccion.setText(Direccion);
        txt_Fono1.setText(Fono1);
        txt_Fono2.setText(Fono2);

        // se redirije a la pantalla de edicion de datos del usuario
        btnEditUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_PerfilMod_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // se redirije a la pantalla de edicion de datos del usuario
        btnEditImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_UploadIMG_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        carga_imagen(idCliente, Nombre);



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void  carga_imagen(final String idCliente, final String Nombre) {


        try{

            String image_url = AppConfig.FOLDER_UPLOAD + idCliente + "_" + Nombre + ".jpeg" ;
            downloadFile(image_url);

        } catch (Exception e) {
            //verifica la conectividad y evita que la aplicacion deje de funcionar
            Toast.makeText(getActivity(), "Error :" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }



    void downloadFile(final String imageHttpAddress) {
        Thread t0 = new Thread(new Runnable() {
            public void run() {
                // you can use b in here

                try {

                    URL imageUrl = null;
                    imageUrl = new URL(imageHttpAddress);

                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();

                    conn.setRequestMethod("HEAD");

                    if( (conn.getResponseCode() == HttpURLConnection.HTTP_OK) ){

                        imageUrl = new URL(imageHttpAddress);
                        HttpURLConnection conn1 = (HttpURLConnection) imageUrl.openConnection();
                        conn1.connect();
                        loadedImage = BitmapFactory.decodeStream(conn1.getInputStream());
                        imageView.setImageBitmap(loadedImage);

                    }else{

                        String imageUrl2 = AppConfig.FOLDER_UPLOAD + "perfil.jpeg" ;
                        imageUrl = new URL(imageUrl2);
                        HttpURLConnection conn2 = (HttpURLConnection) imageUrl.openConnection();
                        conn2.connect();
                        loadedImage = BitmapFactory.decodeStream(conn2.getInputStream());
                        imageView.setImageBitmap(loadedImage);

                    }


                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
        t0.start();
        // wait for the thread
        try {
            t0.join();
        } catch (InterruptedException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }


    }

}
