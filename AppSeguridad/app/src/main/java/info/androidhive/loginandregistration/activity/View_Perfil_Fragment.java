package info.androidhive.loginandregistration.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.DB_usuarios;
import info.androidhive.loginandregistration.helper.SessionManager;


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



        //txt_factualizacion   = (TextView) rootView.findViewById(R.id.name);


        btnEditUser = (Button) rootView.findViewById(R.id.btnModificar);

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
