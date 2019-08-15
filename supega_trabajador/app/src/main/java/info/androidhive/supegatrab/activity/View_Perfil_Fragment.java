package info.androidhive.supegatrab.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import info.androidhive.supegatrab.R;
import info.androidhive.supegatrab.app.AppConfig;
import info.androidhive.supegatrab.helper.DB_usuarios;
import info.androidhive.supegatrab.helper.SessionManager;



public class View_Perfil_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_email;
    private TextView txt_Nombre;
    private TextView txt_Rut;
    private TextView txt_fNacimiento;
    private TextView txt_Direccion;
    private TextView txt_Fono1;
    private TextView txt_Fono2;

    //declaracion de botones
    private ImageButton btnEditUser;
    private ImageButton btnEditImg;

 
    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
    private SessionManager session;

    private ImageLoader imageLoader;
    private NetworkImageView imageView;

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
        txt_Direccion        = (TextView) rootView.findViewById(R.id.Direccion);
        txt_Fono1            = (TextView) rootView.findViewById(R.id.Fono1);
        txt_Fono2            = (TextView) rootView.findViewById(R.id.Fono2);
        imageView            = (NetworkImageView) rootView.findViewById(R.id.imageView);



        btnEditUser = (ImageButton) rootView.findViewById(R.id.btnModificar);
        btnEditImg = (ImageButton) rootView.findViewById(R.id.btnImagen);

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
        if(!email.isEmpty()){         txt_email.setText(email);}
        if(!Nombre.isEmpty()){        txt_Nombre.setText(Nombre);}
        if(!Rut.isEmpty()){           txt_Rut.setText(Rut);}
        if(!fNacimiento.isEmpty()){   txt_fNacimiento.setText(fNacimiento);}
        if(!Distrito.isEmpty()){      txt_Direccion.setText(Departamento + ", " + Provincia + ", " + Distrito + ", " + Direccion);}
        if(!Fono1.isEmpty()){         txt_Fono1.setText(Fono1);}
        if(!Fono2.isEmpty()){         txt_Fono2.setText(Fono2);}

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


        String url = AppConfig.FOLDER_UPLOAD + idCliente  + ".jpeg" ;
        loadImage(url);



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


    private void loadImage(String url){

        if(url.equals("")){
            Toast.makeText(getActivity(), "Por favor ingrese una URL valida", Toast.LENGTH_LONG).show();
            return;
        }


        CustomVolleyRequest.getInstance(getActivity()).getRequestQueue().getCache().invalidate(url, true);
        CustomVolleyRequest.getInstance(getActivity()).getRequestQueue().getCache().remove(url);

        imageLoader = CustomVolleyRequest.getInstance(getActivity()).getImageLoader();

        imageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.drawable.com_facebook_profile_picture_blank_portrait,
                android.R.drawable.ic_dialog_alert
        ));







        imageView.setImageUrl(url, imageLoader);


    }






}
