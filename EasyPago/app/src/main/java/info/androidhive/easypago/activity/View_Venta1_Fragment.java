package info.androidhive.easypago.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.SessionManager;


public class View_Venta1_Fragment extends Fragment {

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;

    //declaracion de botones y texto
    private TextView input_Fono;
    private Spinner spinner1;
    private Button btnConfirmar;
    private Button btnVolver;




    public View_Venta1_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venta_1, container, false);


        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_search1));

        //Se toman los botones y textos del layout
        input_Fono    = (EditText) rootView.findViewById(R.id.Fono1);
        btnConfirmar       = (Button) rootView.findViewById(R.id.btnConfirmar);
        btnVolver          = (Button) rootView.findViewById(R.id.btnVolver);


        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }





        // Si se presiona el boton guardar datos
        btnConfirmar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //parseo todos los datos
                String int_fono    = input_Fono.getText().toString().trim();
                String int_PIN     = spinner1.getSelectedItem().toString().trim();

                //se realiza limpieza de los pines
                int_PIN = int_PIN.replace(" soles", "");
                int_PIN = int_PIN.replace(" ", "");







                //verifico la existencia de los datos obligatorios
                if (!int_fono.isEmpty() && !int_PIN.isEmpty() ) {

                    switch (int_PIN.toLowerCase()) {
                        case "5":      int_PIN = "1"; break;
                        case "10":     int_PIN = "2"; break;
                        case "20":     int_PIN = "3"; break;
                        case "5plus":  int_PIN = "4"; break;
                        case "10plus": int_PIN = "5"; break;
                        case "20plus": int_PIN = "6"; break;

                    }




                    //concateno los posibles datos
                    String Destino = int_fono;
                    Destino += "zzandzz"+int_PIN;


                    //oculto el teclado
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    //le paso a la variable interna la direccion
                    ((MainActivity) getActivity()).passDataActivity(Destino);

                    Fragment fragment = new View_Venta2_Fragment();
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

        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

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



        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Seleccionar un monto");
        /*list.add("5 soles");
        list.add("10 soles");
        list.add("20 soles");*/
        list.add("5 plus soles");
        list.add("10 plus soles");
        list.add("20 plus soles");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);







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




//fin
}