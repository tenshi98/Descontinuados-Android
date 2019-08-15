package info.androidhive.easypago.activity;


import android.app.Activity;
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

import java.util.HashMap;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.DB_contactos;
import info.androidhive.easypago.helper.DB_usuarios;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Contact;


public class View_PideMapa_View_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_Nombre;
    private TextView txt_Fono;
    private TextView txt_Estado;

    //Icono del usuario
    private ImageView user_icon;

    //declaracion de botones
    private Button btnMapaUbicacion1;
    private Button btnMapaUbicacion2;
    private Button btnBack;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_contactos dataContactos;
    private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_PideMapa_View_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pide_mapa_view, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_pide_contacto));

        //Se toman los botones y textos del layout
        user_icon         = (ImageView) rootView.findViewById(R.id.icon_user);
        txt_Nombre        = (TextView) rootView.findViewById(R.id.Nombre);
        txt_Fono          = (TextView) rootView.findViewById(R.id.Fono);
        txt_Estado        = (TextView) rootView.findViewById(R.id.Estado);

        btnMapaUbicacion1  = (Button) rootView.findViewById(R.id.btn_ultima);
        btnMapaUbicacion2  = (Button) rootView.findViewById(R.id.btn_actual);
        btnBack           = (Button) rootView.findViewById(R.id.btn_volver);

        // se conecta a la base de datos interna
        dataContactos = new DB_contactos(getActivity());
        dataUsuarios = new DB_usuarios(getActivity());

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // obtengo el id del cliente
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String app_user        = user.get("idCliente");
        final String UsrName         = user.get("Nombre");

        //llamo la variable con el id almacenada en la actividad principal
        final String itemID = ((MainActivity) getActivity()).getDataActivity();
        //la transformo a integer
        int foo = Integer.parseInt(itemID);
        //busco los datos del contacto a traves de su id
        Contact contacts = dataContactos.getContact(foo);

        final String Nombre      = contacts.getName();
        final String Fono        = contacts.getPhoneNumber();
        final String Estado      = contacts.getEstado();
        final String GSM         = contacts.getGSM();
        final String idCliente   = contacts.getidCliente();

        // se escriben los datos en los respectivos elementos
        txt_Nombre.setText(Nombre);
        txt_Fono.setText(Fono);
        txt_Estado.setText(Estado);

        //Se setea el icono
        if(Estado.equals("Registrado")){
            user_icon.setImageResource(R.drawable.ic_user_registrado);
        }else{
            user_icon.setImageResource(R.drawable.ic_user_no_registrado);
        }


        //boton ultima ubicacion
        btnMapaUbicacion1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_PideMapaUbicacion_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        //boton pedir ubicacion actual
        btnMapaUbicacion2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //llamar metodo del main
                ((MainActivity)getActivity()).notificacion_unitaria(app_user, "3", "7", UsrName + "   te ha solicitado tu ubicacion", GSM, idCliente );

                //Toast.makeText(getActivity(), "asd"+ GSM, Toast.LENGTH_SHORT).show();
            }
        });



        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_PideMapa_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });



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






}
