package info.androidhive.jootes.activity;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.jootes.R;
import info.androidhive.jootes.helper.SessionManager;


public class View_Buscar_Fragment extends Fragment {

    //declaracion de botones y texto
    private Spinner spinner1, spinner2;

    private Button btnSearch;
    private Button btnBack;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;



    public View_Buscar_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buscar, container, false);


        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_search));




        //Se toman los botones y textos del layout
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);

        btnSearch = (Button) rootView.findViewById(R.id.btnBuscar);
        btnBack = (Button) rootView.findViewById(R.id.btnVolver);


        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        //Se ingresan manualmente los datos del primer select
        List<String> list1 = new ArrayList<String>();
        list1.add("Seleccionar un Rango");
        list1.add("de 18 a 25");
        list1.add("de 26 a 35");
        list1.add("de 45 a mas");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);

        //Se ingresan manualmente los datos del tercer select
        List<String> list2 = new ArrayList<String>();
        list2.add("Seleccionar Sexo");
        list2.add("Masculino");
        list2.add("Femenino");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);





        // Si se presiona el boton guardar datos
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //parseo todos los datos
                String int_rango  = spinner1.getSelectedItem().toString().trim();
                String int_sexo   = spinner2.getSelectedItem().toString().trim();



                //verifico la existencia de los datos obligatorios
                if (!int_rango.equals("Seleccionar un Rango") && !int_sexo.equals("Seleccionar Sexo")) {

                    String Destino = int_rango;
                    if (!int_sexo.isEmpty() ) {Destino += "and"+int_sexo;}

                    //le paso a la variable interna la direccion
                    ((MainActivity) getActivity()).passDataActivity(Destino);

                    Fragment fragment = new View_Resultados_Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();



                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), "Ingrese los datos solicitados", Toast.LENGTH_SHORT).show();
                }

            }
        });







        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment fragment = new View_Home_Fragment();
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
