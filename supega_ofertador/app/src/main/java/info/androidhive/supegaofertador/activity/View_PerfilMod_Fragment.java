package info.androidhive.supegaofertador.activity;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.helper.DB_usuarios;
import info.androidhive.supegaofertador.helper.SessionManager;




public class View_PerfilMod_Fragment extends Fragment {

    //declaracion de botones y texto
    private TextView input_email;
    private TextView input_Nombre;
    private TextView input_Rut;
    private TextView input_fNacimiento;
    private TextView input_Direccion;
    private TextView input_Fono1;
    private TextView input_Fono2;
    private TextView input_Edad;
    private Spinner spinner1, spinner2, spinner3, spinner4;

    private Button btnSaveUser;
    private Button btnBack;



    public String var_idCliente;
    public String var_email;
    public String var_Nombre;
    public String var_Rut;
    public String var_fNacimiento;
    public String var_Direccion;
    public String var_Fono1;
    public String var_Fono2;
    public String var_Departamento;
    public String var_Provincia;
    public String var_Distrito;
    public String var_fcreacion;
    public String var_factualizacion;
    public String var_Sexo;
    public String var_Edad;
    public String Error = "" ;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
    private SessionManager session;



    public View_PerfilMod_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_edit, container, false);


        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_profile_edit));



        //Se toman los botones y textos del layout
        input_email            = (EditText) rootView.findViewById(R.id.email);
        input_Nombre           = (EditText) rootView.findViewById(R.id.Nombre);
        input_Rut              = (EditText) rootView.findViewById(R.id.Rut);
        input_fNacimiento      = (EditText) rootView.findViewById(R.id.fNacimiento);
        input_Direccion        = (EditText) rootView.findViewById(R.id.Direccion);
        input_Fono1            = (EditText) rootView.findViewById(R.id.Fono1);
        input_Fono2            = (EditText) rootView.findViewById(R.id.Fono2);
        input_Edad             = (EditText) rootView.findViewById(R.id.edad);

        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        spinner3 = (Spinner) rootView.findViewById(R.id.spinner3);
        spinner4 = (Spinner) rootView.findViewById(R.id.spinner4);

        btnSaveUser = (Button) rootView.findViewById(R.id.btnGuardar);
        btnBack = (Button) rootView.findViewById(R.id.btnVolver);

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

        var_idCliente       = user.get("idCliente");
        var_email           = user.get("email");
        var_Nombre          = user.get("Nombre");
        var_Rut             = user.get("Rut");
        var_fNacimiento     = user.get("fNacimiento");
        var_Departamento    = user.get("Departamento");
        var_Provincia       = user.get("Provincia");
        var_Distrito        = user.get("Distrito");
        var_Direccion       = user.get("Direccion");
        var_Fono1           = user.get("Fono1");
        var_Fono2           = user.get("Fono2");
        var_Sexo            = user.get("Sexo");
        var_Edad            = user.get("Edad");



        // se escriben los datos en los respectivos elementos
        input_email.setText(var_email);
        input_Nombre.setText(var_Nombre);
        input_Rut.setText(var_Rut);
        input_fNacimiento.setText(var_fNacimiento);
        input_Direccion.setText(var_Direccion);
        input_Fono1.setText(var_Fono1);
        input_Fono2.setText(var_Fono2);
        input_Edad.setText(var_Edad);


        //Se ingresan manualmente los datos del primer select
        List<String> list = new ArrayList<String>();
        if(var_Departamento.isEmpty()){
            list.add("Seleccionar un Departamento");
        }else{
            list.add(var_Departamento);
        }
        list.add("Amazonas");
        list.add("Ancash");
        list.add("Apurimac");
        list.add("Arequipa");
        list.add("Ayacucho");
        list.add("Cajamarca");
        list.add("Callao");
        list.add("Cusco");
        list.add("Huancavelica");
        list.add("Huanuco");
        list.add("Ica");
        list.add("Junin");
        list.add("La Libertad");
        list.add("Lambayeque");
        list.add("Lima");
        list.add("Loreto");
        list.add("Madre de Dios");
        list.add("Moquegua");
        list.add("Pasco");
        list.add("Piura");
        list.add("Puno");
        list.add("San Martín");
        list.add("Tacna");
        list.add("Tumbes");
        list.add("Ucayali");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(new actualizaProvincia());

        //Se ingresan manualmente los datos del segundo select
        List<String> list2 = new ArrayList<String>();
        if(var_Provincia.isEmpty()){
            list2.add("Seleccionar una Provincia");
        }else{
            list2.add(var_Provincia);
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        spinner2.setOnItemSelectedListener(new actualizaDistrito());


        //Se ingresan manualmente los datos del tercer select
        List<String> list3 = new ArrayList<String>();
        if(var_Distrito.isEmpty()){
            list3.add("Seleccionar un Distrito");
        }else{
            list3.add(var_Distrito);
        }
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list3);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter3);


        //Se ingresan manualmente los datos del tercer select
        List<String> list4 = new ArrayList<String>();
        if(var_Sexo.isEmpty()){
            list4.add("Seleccionar Sexo");
        }else{
            list4.add(var_Sexo);
        }
        list4.add("Masculino");
        list4.add("Femenino");
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list4);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(dataAdapter4);


        // Si se presiona el boton guardar datos
        btnSaveUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //se vacia la variable error
                Error = "" ;

                //parseo todos los datos
                String int_email = input_email.getText().toString().trim();
                String int_nombre = input_Nombre.getText().toString().trim();
                String int_rut = input_Rut.getText().toString().trim();
                String int_fnacimiento = input_fNacimiento.getText().toString().trim();
                String int_departamento = spinner1.getSelectedItem().toString().trim();
                String int_provincia = spinner2.getSelectedItem().toString().trim();
                String int_distrito = spinner3.getSelectedItem().toString().trim();
                String int_sexo = spinner4.getSelectedItem().toString().trim();
                String int_edad = input_Edad.getText().toString().trim();
                String int_direccion = input_Direccion.getText().toString().trim();
                String int_fono1 = input_Fono1.getText().toString().trim();
                String int_fono2 = input_Fono2.getText().toString().trim();

                //Se comprueban datos vacios
                if (int_email.isEmpty()  ) {
                    Error = "No ha ingresado un email" ;
                }
                if (int_nombre.isEmpty()  ) {
                    Error = "No ha ingresado un nombre" ;
                }
                if (int_rut.isEmpty()  ) {
                    Error = "No ha ingresado el rut" ;
                }
                if (int_direccion.isEmpty()  ) {
                    Error = "No ha ingresado la direccion" ;
                }
                if (int_sexo.isEmpty()  ) {
                    Error = "No ha seleccionado el sexo" ;
                }
                if (int_edad.isEmpty()  ) {
                    Error = "No ha ingresado la edad" ;
                }
                if (int_fono1.isEmpty()  ) {
                    Error = "No ha ingresado el telefono movil" ;
                }



                //verifico la existencia de los datos obligatorios
                if (Error.isEmpty()) {

                    // modificar usuario
                    ((MainActivity) getActivity()).modUser(var_idCliente, int_email, int_nombre, int_rut, int_fnacimiento, int_direccion, int_fono1, int_fono2,
                            int_departamento, int_provincia, int_distrito, var_fcreacion, var_factualizacion, int_sexo, int_edad);

                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), Error , Toast.LENGTH_SHORT).show();
                }

            }
        });







        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Perfil_Fragment();
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





    public class actualizaProvincia implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            List<String> localidades_Amazonas = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Amazonas.add("Seleccione una Provincia");
            }else{
                localidades_Amazonas.add(var_Provincia);
            }
            localidades_Amazonas.add("Chachapoyas");
            localidades_Amazonas.add("Bagua");
            localidades_Amazonas.add("Bongara");
            localidades_Amazonas.add("Condorcanqui");
            localidades_Amazonas.add("Luya");
            localidades_Amazonas.add("Rodriguez De Mendo");
            localidades_Amazonas.add("Utcubamba");

            List<String> localidades_Ancash = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Ancash.add("Seleccione una Provincia");
            }else{
                localidades_Ancash.add(var_Provincia);
            }
            localidades_Ancash.add("Huaraz");
            localidades_Ancash.add("Aija");
            localidades_Ancash.add("Antonio Raymondi");
            localidades_Ancash.add("Asuncion");
            localidades_Ancash.add("Bolognesi");
            localidades_Ancash.add("Carhuaz");
            localidades_Ancash.add("Carlos F. Fitzcarrald");
            localidades_Ancash.add("Casma");
            localidades_Ancash.add("Corongo");
            localidades_Ancash.add("Huari");
            localidades_Ancash.add("Huarmey");
            localidades_Ancash.add("Huaylas");
            localidades_Ancash.add("Mariscal Luzuriaga");
            localidades_Ancash.add("Ocros");
            localidades_Ancash.add("Pallasca");
            localidades_Ancash.add("Pomabamba");
            localidades_Ancash.add("Recuay");
            localidades_Ancash.add("Santa");
            localidades_Ancash.add("Sihuas");
            localidades_Ancash.add("Yungay");

            List<String> localidades_Apurimac = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Apurimac.add("Seleccione una Provincia");
            }else{
                localidades_Apurimac.add(var_Provincia);
            }
            localidades_Apurimac.add("Abancay");
            localidades_Apurimac.add("Andahuaylas");
            localidades_Apurimac.add("Antabamba");
            localidades_Apurimac.add("Aymaraes");
            localidades_Apurimac.add("Cotabambas");
            localidades_Apurimac.add("Chincheros");
            localidades_Apurimac.add("Grau");

            List<String> localidades_Arequipa = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Arequipa.add("Seleccione una Provincia");
            }else{
                localidades_Arequipa.add(var_Provincia);
            }
            localidades_Arequipa.add("Arequipa");
            localidades_Arequipa.add("Camana");
            localidades_Arequipa.add("Caraveli");
            localidades_Arequipa.add("Castilla");
            localidades_Arequipa.add("Caylloma");
            localidades_Arequipa.add("Condesuyos");
            localidades_Arequipa.add("Islay");
            localidades_Arequipa.add("La Union");

            List<String> localidades_Ayacucho = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Ayacucho.add("Seleccione una Provincia");
            }else{
                localidades_Ayacucho.add(var_Provincia);
            }
            localidades_Ayacucho.add("Huamanga");
            localidades_Ayacucho.add("Cangallo");
            localidades_Ayacucho.add("Huanca Sancos");
            localidades_Ayacucho.add("Huanta");
            localidades_Ayacucho.add("La Mar");
            localidades_Ayacucho.add("Lucanas");
            localidades_Ayacucho.add("Parinacochas");
            localidades_Ayacucho.add("Paucar Del Sara Sara");
            localidades_Ayacucho.add("Sucre");
            localidades_Ayacucho.add("Victor Fajardo");
            localidades_Ayacucho.add("Vilcas Huaman");

            List<String> localidades_Cajamarca = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Cajamarca.add("Seleccione una Provincia");
            }else{
                localidades_Cajamarca.add(var_Provincia);
            }
            localidades_Cajamarca.add("Cajamarca");
            localidades_Cajamarca.add("Cajabamba");
            localidades_Cajamarca.add("Celendin");
            localidades_Cajamarca.add("Chota");
            localidades_Cajamarca.add("Contumaza");
            localidades_Cajamarca.add("Cutervo");
            localidades_Cajamarca.add("Hualgayoc");
            localidades_Cajamarca.add("Jaen");
            localidades_Cajamarca.add("San Ignacio");
            localidades_Cajamarca.add("San Marcos");
            localidades_Cajamarca.add("San Miguel");
            localidades_Cajamarca.add("San Pablo");
            localidades_Cajamarca.add("Santa Cruz");

            List<String> localidades_Callao = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Callao.add("Seleccione una Provincia");
            }else{
                localidades_Callao.add(var_Provincia);
            }
            localidades_Callao.add("Callao");

            List<String> localidades_Cusco = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Cusco.add("Seleccione una Provincia");
            }else{
                localidades_Cusco.add(var_Provincia);
            }
            localidades_Cusco.add("Cusco");
            localidades_Cusco.add("Acomayo");
            localidades_Cusco.add("Anta");
            localidades_Cusco.add("Calca");
            localidades_Cusco.add("Canas");
            localidades_Cusco.add("Canchis");
            localidades_Cusco.add("Chumbivilcas");
            localidades_Cusco.add("Espinar");
            localidades_Cusco.add("La Convencion");
            localidades_Cusco.add("Paruro");
            localidades_Cusco.add("Paucartambo");
            localidades_Cusco.add("Quispicanchi");
            localidades_Cusco.add("Urubamba");

            List<String> localidades_Huancavelica = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Huancavelica.add("Seleccione una Provincia");
            }else{
                localidades_Huancavelica.add(var_Provincia);
            }
            localidades_Huancavelica.add("Huancavelica");
            localidades_Huancavelica.add("Acobamba");
            localidades_Huancavelica.add("Angaraes");
            localidades_Huancavelica.add("Castrovirreyna");
            localidades_Huancavelica.add("Churcampa");
            localidades_Huancavelica.add("Huaytara");
            localidades_Huancavelica.add("Tayacaja");

            List<String> localidades_Huanuco = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Huanuco.add("Seleccione una Provincia");
            }else{
                localidades_Huanuco.add(var_Provincia);
            }
            localidades_Huanuco.add("Huanuco");
            localidades_Huanuco.add("Ambo");
            localidades_Huanuco.add("Dos De Mayo");
            localidades_Huanuco.add("Huacaybamba");
            localidades_Huanuco.add("Huamalies");
            localidades_Huanuco.add("Leoncio Prado");
            localidades_Huanuco.add("Marañon");
            localidades_Huanuco.add("Pachitea");
            localidades_Huanuco.add("Puerto Inca");
            localidades_Huanuco.add("Lauricocha");
            localidades_Huanuco.add("Yarowilca");

            List<String> localidades_Ica = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Ica.add("Seleccione una Provincia");
            }else{
                localidades_Ica.add(var_Provincia);
            }
            localidades_Ica.add("Ica");
            localidades_Ica.add("Chincha");
            localidades_Ica.add("Nazca");
            localidades_Ica.add("Palpa");
            localidades_Ica.add("Pisco");

            List<String> localidades_Junin = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Junin.add("Seleccione una Provincia");
            }else{
                localidades_Junin.add(var_Provincia);
            }
            localidades_Junin.add("Huancayo");
            localidades_Junin.add("Concepcion");
            localidades_Junin.add("Chanchamayo");
            localidades_Junin.add("Jauja");
            localidades_Junin.add("Junin");
            localidades_Junin.add("Satipo");
            localidades_Junin.add("Tarma");
            localidades_Junin.add("Yauli");
            localidades_Junin.add("Chupaca");

            List<String> localidades_LaLibertad = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_LaLibertad.add("Seleccione una Provincia");
            }else{
                localidades_LaLibertad.add(var_Provincia);
            }
            localidades_LaLibertad.add("Trujillo");
            localidades_LaLibertad.add("Ascope");
            localidades_LaLibertad.add("Bolivar");
            localidades_LaLibertad.add("Chepen");
            localidades_LaLibertad.add("Julcan");
            localidades_LaLibertad.add("Otuzco");
            localidades_LaLibertad.add("Pacasmayo");
            localidades_LaLibertad.add("Pataz");
            localidades_LaLibertad.add("Sanchez Carrion");
            localidades_LaLibertad.add("Santiago De Chuco");
            localidades_LaLibertad.add("Gran Chimu");
            localidades_LaLibertad.add("Viru");


            List<String> localidades_LaLambayeque = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_LaLambayeque.add("Seleccione una Provincia");
            }else{
                localidades_LaLambayeque.add(var_Provincia);
            }
            localidades_LaLambayeque.add("Chiclayo");
            localidades_LaLambayeque.add("Ferreñafe");
            localidades_LaLambayeque.add("Lambayeque");

            List<String> localidades_Lima = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Lima.add("Seleccione una Provincia");
            }else{
                localidades_Lima.add(var_Provincia);
            }
            localidades_Lima.add("Lima");
            localidades_Lima.add("Barranca");
            localidades_Lima.add("Cajatambo");
            localidades_Lima.add("Canta");
            localidades_Lima.add("Cañete");
            localidades_Lima.add("Huaral");
            localidades_Lima.add("Huarochiri");
            localidades_Lima.add("Huaura");
            localidades_Lima.add("Oyon");
            localidades_Lima.add("Yauyos");

            List<String> localidades_Loreto = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Loreto.add("Seleccione una Provincia");
            }else{
                localidades_Loreto.add(var_Provincia);
            }
            localidades_Loreto.add("Maynas");
            localidades_Loreto.add("Alto Amazonas");
            localidades_Loreto.add("Loreto");
            localidades_Loreto.add("Mariscal Ramon Castilla");
            localidades_Loreto.add("Requena");
            localidades_Loreto.add("Ucayali");

            List<String> localidades_MadredeDios = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_MadredeDios.add("Seleccione una Provincia");
            }else{
                localidades_MadredeDios.add(var_Provincia);
            }
            localidades_MadredeDios.add("Tambopata");
            localidades_MadredeDios.add("Manu");
            localidades_MadredeDios.add("Tahuamanu");

            List<String> localidades_Moquegua = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Moquegua.add("Seleccione una Provincia");
            }else{
                localidades_Moquegua.add(var_Provincia);
            }
            localidades_Moquegua.add("Mariscal Nieto");
            localidades_Moquegua.add("General Sanchez Cerro");
            localidades_Moquegua.add("Ilo");

            List<String> localidades_Pasco = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Pasco.add("Seleccione una Provincia");
            }else{
                localidades_Pasco.add(var_Provincia);
            }
            localidades_Pasco.add("Pasco");
            localidades_Pasco.add("Daniel Alcides Carrion");
            localidades_Pasco.add("Oxapampa");

            List<String> localidades_Piura = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Piura.add("Seleccione una Provincia");
            }else{
                localidades_Piura.add(var_Provincia);
            }
            localidades_Piura.add("Piura");
            localidades_Piura.add("Ayabaca");
            localidades_Piura.add("Huancabamba");
            localidades_Piura.add("Morropon");
            localidades_Piura.add("Paita");
            localidades_Piura.add("Sullana");
            localidades_Piura.add("Talara");
            localidades_Piura.add("Sechura");

            List<String> localidades_Puno = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Puno.add("Seleccione una Provincia");
            }else{
                localidades_Puno.add(var_Provincia);
            }
            localidades_Puno.add("Puno");
            localidades_Puno.add("Azangaro");
            localidades_Puno.add("Carabaya");
            localidades_Puno.add("Chucuito");
            localidades_Puno.add("El Collao");
            localidades_Puno.add("Huancane");
            localidades_Puno.add("Lampa");
            localidades_Puno.add("Melgar");
            localidades_Puno.add("Moho");
            localidades_Puno.add("San Antonio De Putina");
            localidades_Puno.add("San Roman");
            localidades_Puno.add("Sandia");
            localidades_Puno.add("Yunguyo");

            List<String> localidades_SanMartín = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_SanMartín.add("Seleccione una Provincia");
            }else{
                localidades_SanMartín.add(var_Provincia);
            }
            localidades_SanMartín.add("Moyobamba");
            localidades_SanMartín.add("Bellavista");
            localidades_SanMartín.add("El Dorado");
            localidades_SanMartín.add("Huallaga");
            localidades_SanMartín.add("Lamas");
            localidades_SanMartín.add("Mariscal Caceres");
            localidades_SanMartín.add("Picota");
            localidades_SanMartín.add("Rioja");
            localidades_SanMartín.add("San Martin");
            localidades_SanMartín.add("Tocache");

            List<String> localidades_Tacna = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Tacna.add("Seleccione una Provincia");
            }else{
                localidades_Tacna.add(var_Provincia);
            }
            localidades_Tacna.add("Tacna");
            localidades_Tacna.add("Candarave");
            localidades_Tacna.add("Jorge Basadre");
            localidades_Tacna.add("Tarata");

            List<String> localidades_Tumbes = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Tumbes.add("Seleccione una Provincia");
            }else{
                localidades_Tumbes.add(var_Provincia);
            }
            localidades_Tumbes.add("Tumbes");
            localidades_Tumbes.add("Contralmirante Villar");
            localidades_Tumbes.add("Zarumilla");

            List<String> localidades_Ucayali = new ArrayList<String>();
            if(var_Provincia.isEmpty()){
                localidades_Ucayali.add("Seleccione una Provincia");
            }else{
                localidades_Ucayali.add(var_Provincia);
            }
            localidades_Ucayali.add("Coronel Portillo");
            localidades_Ucayali.add("Atalaya");
            localidades_Ucayali.add("Padre Abad");
            localidades_Ucayali.add("Purus");


            parent.getItemAtPosition(pos);

            if (pos == 1) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Amazonas);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 2) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Ancash);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 3) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Apurimac);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 4) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Arequipa);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 5) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Ayacucho);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 6) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Cajamarca);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 7) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Callao);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 8) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Cusco);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 9) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Huancavelica);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 10) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Huanuco);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 11) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Ica);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 12) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Junin);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 13) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_LaLibertad);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 14) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_LaLambayeque);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 15) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Lima);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 16) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Loreto);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 17) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_MadredeDios);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 18) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Moquegua);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 19) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Pasco);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 20) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Piura);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 21) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Puno);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 22) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_SanMartín);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 23) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Tacna);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 24) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Tumbes);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            } else if (pos == 25) {
                ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, localidades_Ucayali);
                s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(s1);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

    public class actualizaDistrito implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int pos,long id) {



            List<String> provincia_Chachapoyas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chachapoyas.add("Seleccionar un Distrito");
            }else{
                provincia_Chachapoyas.add(var_Distrito);
            }
            provincia_Chachapoyas.add("Chachapoyas");
            provincia_Chachapoyas.add("Asuncion");
            provincia_Chachapoyas.add("Balsas");
            provincia_Chachapoyas.add("Cheto");
            provincia_Chachapoyas.add("Chiliquin");
            provincia_Chachapoyas.add("Chuquibamba");
            provincia_Chachapoyas.add("Granada");
            provincia_Chachapoyas.add("Huancas");
            provincia_Chachapoyas.add("La Jalca");
            provincia_Chachapoyas.add("Leimebamba");
            provincia_Chachapoyas.add("Levanto");
            provincia_Chachapoyas.add("Magdalena");
            provincia_Chachapoyas.add("Mariscal Castilla");
            provincia_Chachapoyas.add("Molinopampa");
            provincia_Chachapoyas.add("Montevideo");
            provincia_Chachapoyas.add("Olleros");
            provincia_Chachapoyas.add("Quinjalca");
            provincia_Chachapoyas.add("San Francisco De Daguas");
            provincia_Chachapoyas.add("San Isidro De Maino");
            provincia_Chachapoyas.add("Soloco");
            provincia_Chachapoyas.add("Sonche");


            List<String> provincia_Bagua = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Bagua.add("Seleccionar un Distrito");
            }else{
                provincia_Bagua.add(var_Distrito);
            }
            provincia_Bagua.add("La Peca");
            provincia_Bagua.add("Aramango");
            provincia_Bagua.add("Copallin");
            provincia_Bagua.add("El Parco");
            provincia_Bagua.add("Imaza");


            List<String> provincia_Bongara = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Bongara.add("Seleccionar un Distrito");
            }else{
                provincia_Bongara.add(var_Distrito);
            }
            provincia_Bongara.add("Jumbilla");
            provincia_Bongara.add("Chisquilla");
            provincia_Bongara.add("Churuja");
            provincia_Bongara.add("Corosha");
            provincia_Bongara.add("Cuispes");
            provincia_Bongara.add("Florida");
            provincia_Bongara.add("Jazan");
            provincia_Bongara.add("Recta");
            provincia_Bongara.add("San Carlos");
            provincia_Bongara.add("Shipasbamba");
            provincia_Bongara.add("Valera");
            provincia_Bongara.add("Yambrasbamba");


            List<String> provincia_Condorcanqui = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Condorcanqui.add("Seleccionar un Distrito");
            }else{
                provincia_Condorcanqui.add(var_Distrito);
            }
            provincia_Condorcanqui.add("Nieva");
            provincia_Condorcanqui.add("El Cenepa");
            provincia_Condorcanqui.add("Rio Santiago");

            List<String> provincia_Luya = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Luya.add("Seleccionar un Distrito");
            }else{
                provincia_Luya.add(var_Distrito);
            }
            provincia_Luya.add("Lamud");
            provincia_Luya.add("Camporredondo");
            provincia_Luya.add("Cocabamba");
            provincia_Luya.add("Colcamar");
            provincia_Luya.add("Conila");
            provincia_Luya.add("Inguilpata");
            provincia_Luya.add("Longuita");
            provincia_Luya.add("Lonya Chico");
            provincia_Luya.add("Luya");
            provincia_Luya.add("Luya Viejo");
            provincia_Luya.add("Maria");
            provincia_Luya.add("Ocalli");
            provincia_Luya.add("Ocumal");
            provincia_Luya.add("Pisuquia");
            provincia_Luya.add("Providencia");
            provincia_Luya.add("San Cristobal");
            provincia_Luya.add("San Francisco Del Yeso");
            provincia_Luya.add("San Jeronimo");
            provincia_Luya.add("San Juan De Lopecancha");
            provincia_Luya.add("Santa Catalina");
            provincia_Luya.add("Santo Tomas");
            provincia_Luya.add("Tingo");
            provincia_Luya.add("Trita");

            List<String> provincia_RodriguezDeMendo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_RodriguezDeMendo.add("Seleccionar un Distrito");
            }else{
                provincia_RodriguezDeMendo.add(var_Distrito);
            }
            provincia_RodriguezDeMendo.add("San Nicolas");
            provincia_RodriguezDeMendo.add("Chirimoto");
            provincia_RodriguezDeMendo.add("Cochamal");
            provincia_RodriguezDeMendo.add("Huambo");
            provincia_RodriguezDeMendo.add("Limabamba");
            provincia_RodriguezDeMendo.add("Longar");
            provincia_RodriguezDeMendo.add("Mariscal Benavides");
            provincia_RodriguezDeMendo.add("Milpuc");
            provincia_RodriguezDeMendo.add("Omia");
            provincia_RodriguezDeMendo.add("Santa Rosa");
            provincia_RodriguezDeMendo.add("Totora");
            provincia_RodriguezDeMendo.add("Vista Alegre");


            List<String> provincia_Utcubamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Utcubamba.add("Seleccionar un Distrito");
            }else{
                provincia_Utcubamba.add(var_Distrito);
            }
            provincia_Utcubamba.add("Bagua Grande");
            provincia_Utcubamba.add("Cajaruro");
            provincia_Utcubamba.add("Cumba");
            provincia_Utcubamba.add("El Milagro");
            provincia_Utcubamba.add("Jamalca");
            provincia_Utcubamba.add("Lonya Grande");
            provincia_Utcubamba.add("Yamon");

            List<String> provincia_Huaraz = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huaraz.add("Seleccionar un Distrito");
            }else{
                provincia_Huaraz.add(var_Distrito);
            }
            provincia_Huaraz.add("Huaraz");
            provincia_Huaraz.add("Cochabamba");
            provincia_Huaraz.add("Colcabamba");
            provincia_Huaraz.add("Huanchay");
            provincia_Huaraz.add("Independencia");
            provincia_Huaraz.add("Jangas");
            provincia_Huaraz.add("La Libertad");
            provincia_Huaraz.add("Olleros");
            provincia_Huaraz.add("Pampas");
            provincia_Huaraz.add("Pariacoto");
            provincia_Huaraz.add("Pira");
            provincia_Huaraz.add("Tarica");

            List<String> provincia_Aija = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Aija.add("Seleccionar un Distrito");
            }else{
                provincia_Aija.add(var_Distrito);
            }
            provincia_Aija.add("Aija");
            provincia_Aija.add("Coris");
            provincia_Aija.add("Huacllan");
            provincia_Aija.add("La Merced");
            provincia_Aija.add("Succha");

            List<String> provincia_AntonioRaymondi = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_AntonioRaymondi.add("Seleccionar un Distrito");
            }else{
                provincia_AntonioRaymondi.add(var_Distrito);
            }
            provincia_AntonioRaymondi.add("Llamellin");
            provincia_AntonioRaymondi.add("Aczo");
            provincia_AntonioRaymondi.add("Chaccho");
            provincia_AntonioRaymondi.add("Chingas");
            provincia_AntonioRaymondi.add("Mirgas");
            provincia_AntonioRaymondi.add("San Juan De Rontoy");

            List<String> provincia_Asuncion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Asuncion.add("Seleccionar un Distrito");
            }else{
                provincia_Asuncion.add(var_Distrito);
            }
            provincia_Asuncion.add("Chacas");
            provincia_Asuncion.add("Acochaca");

            List<String> provincia_Bolognesi = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Bolognesi.add("Seleccionar un Distrito");
            }else{
                provincia_Bolognesi.add(var_Distrito);
            }
            provincia_Bolognesi.add("Chiquian");
            provincia_Bolognesi.add("Abelardo Pardo Lezameta");
            provincia_Bolognesi.add("Antonio Raymondi");
            provincia_Bolognesi.add("Aquia");
            provincia_Bolognesi.add("Cajacay");
            provincia_Bolognesi.add("Canis");
            provincia_Bolognesi.add("Colquioc");
            provincia_Bolognesi.add("Huallanca");
            provincia_Bolognesi.add("Huasta");
            provincia_Bolognesi.add("Huayllacayan");
            provincia_Bolognesi.add("La Primavera");
            provincia_Bolognesi.add("Mangas");
            provincia_Bolognesi.add("Pacllon");
            provincia_Bolognesi.add("San Miguel De Corpanqui");
            provincia_Bolognesi.add("Ticllos");

            List<String> provincia_Carhuaz = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Carhuaz.add("Seleccionar un Distrito");
            }else{
                provincia_Carhuaz.add(var_Distrito);
            }
            provincia_Carhuaz.add("Carhuaz");
            provincia_Carhuaz.add("Acopampa");
            provincia_Carhuaz.add("Amashca");
            provincia_Carhuaz.add("Anta");
            provincia_Carhuaz.add("Ataquero");
            provincia_Carhuaz.add("Marcara");
            provincia_Carhuaz.add("Pariahuanca");
            provincia_Carhuaz.add("San Miguel De Aco");
            provincia_Carhuaz.add("Shilla");
            provincia_Carhuaz.add("Tinco");
            provincia_Carhuaz.add("Yungar");

            List<String> provincia_CarlosFFitzcarrald = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_CarlosFFitzcarrald.add("Seleccionar un Distrito");
            }else{
                provincia_CarlosFFitzcarrald.add(var_Distrito);
            }
            provincia_CarlosFFitzcarrald.add("San Luis");
            provincia_CarlosFFitzcarrald.add("San Nicolas");
            provincia_CarlosFFitzcarrald.add("Yauya");

            List<String> provincia_Casma = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Casma.add("Seleccionar un Distrito");
            }else{
                provincia_Casma.add(var_Distrito);
            }
            provincia_Casma.add("Casma");
            provincia_Casma.add("Buena Vista Alta");
            provincia_Casma.add("Comandante Noel");
            provincia_Casma.add("Yautan");

            List<String> provincia_Corongo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Corongo.add("Seleccionar un Distrito");
            }else{
                provincia_Corongo.add(var_Distrito);
            }
            provincia_Corongo.add("Corongo");
            provincia_Corongo.add("Aco");
            provincia_Corongo.add("Bambas");
            provincia_Corongo.add("Cusca");
            provincia_Corongo.add("La Pampa");
            provincia_Corongo.add("Yanac");
            provincia_Corongo.add("Yupan");

            List<String> provincia_Huari = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huari.add("Seleccionar un Distrito");
            }else{
                provincia_Huari.add(var_Distrito);
            }
            provincia_Huari.add("Huari");
            provincia_Huari.add("Anra");
            provincia_Huari.add("Cajay");
            provincia_Huari.add("Chavin De Huantar");
            provincia_Huari.add("Huacachi");
            provincia_Huari.add("Huacchis");
            provincia_Huari.add("Huachis");
            provincia_Huari.add("Huantar");
            provincia_Huari.add("Masin");
            provincia_Huari.add("Paucas");
            provincia_Huari.add("Ponto");
            provincia_Huari.add("Rahuapampa");
            provincia_Huari.add("Rapayan");
            provincia_Huari.add("San Marcos");
            provincia_Huari.add("San Pedro De Chana");
            provincia_Huari.add("Uco");

            List<String> provincia_Huarmey = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huarmey.add("Seleccionar un Distrito");
            }else{
                provincia_Huarmey.add(var_Distrito);
            }
            provincia_Huarmey.add("Huarmey");
            provincia_Huarmey.add("Cochapeti");
            provincia_Huarmey.add("Culebras");
            provincia_Huarmey.add("Huayan");
            provincia_Huarmey.add("Malvas");

            List<String> provincia_Huaylas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huaylas.add("Seleccionar un Distrito");
            }else{
                provincia_Huaylas.add(var_Distrito);
            }
            provincia_Huaylas.add("Caraz");
            provincia_Huaylas.add("Huallanca");
            provincia_Huaylas.add("Huata");
            provincia_Huaylas.add("Huaylas");
            provincia_Huaylas.add("Mato");
            provincia_Huaylas.add("Pamparomas");
            provincia_Huaylas.add("Pueblo Libre");
            provincia_Huaylas.add("Santa Cruz");
            provincia_Huaylas.add("Santo Toribio");
            provincia_Huaylas.add("Yuracmarca");

            List<String> provincia_MariscalLuzuriaga = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_MariscalLuzuriaga.add("Seleccionar un Distrito");
            }else{
                provincia_MariscalLuzuriaga.add(var_Distrito);
            }
            provincia_MariscalLuzuriaga.add("Piscobamba");
            provincia_MariscalLuzuriaga.add("Casca");
            provincia_MariscalLuzuriaga.add("Eleazar Guzman Barron");
            provincia_MariscalLuzuriaga.add("Fidel Olivas Escudero");
            provincia_MariscalLuzuriaga.add("Llama");
            provincia_MariscalLuzuriaga.add("Llumpa");
            provincia_MariscalLuzuriaga.add("Lucma");
            provincia_MariscalLuzuriaga.add("Musga");

            List<String> provincia_Ocros = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ocros.add("Seleccionar un Distrito");
            }else{
                provincia_Ocros.add(var_Distrito);
            }
            provincia_Ocros.add("Ocros");
            provincia_Ocros.add("Acas");
            provincia_Ocros.add("Cajamarquilla");
            provincia_Ocros.add("Carhuapampa");
            provincia_Ocros.add("Cochas");
            provincia_Ocros.add("Congas");
            provincia_Ocros.add("Llipa");
            provincia_Ocros.add("San Cristobal De Rajan");
            provincia_Ocros.add("San Pedro");
            provincia_Ocros.add("Santiago De Chilcas");


            List<String> provincia_Pallasca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pallasca.add("Seleccionar un Distrito");
            }else{
                provincia_Pallasca.add(var_Distrito);
            }
            provincia_Pallasca.add("Cabana");
            provincia_Pallasca.add("Bolognesi");
            provincia_Pallasca.add("Conchucos");
            provincia_Pallasca.add("Huacaschuque");
            provincia_Pallasca.add("Huandoval");
            provincia_Pallasca.add("Lacabamba");
            provincia_Pallasca.add("Llapo");
            provincia_Pallasca.add("Pallasca");
            provincia_Pallasca.add("Pampas");
            provincia_Pallasca.add("Santa Rosa");
            provincia_Pallasca.add("Tauca");

            List<String> provincia_Pomabamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pomabamba.add("Seleccionar un Distrito");
            }else{
                provincia_Pomabamba.add(var_Distrito);
            }
            provincia_Pomabamba.add("Pomabamba");
            provincia_Pomabamba.add("Huayllan");
            provincia_Pomabamba.add("Parobamba");
            provincia_Pomabamba.add("Quinuabamba");

            List<String> provincia_Recuay = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Recuay.add("Seleccionar un Distrito");
            }else{
                provincia_Recuay.add(var_Distrito);
            }
            provincia_Recuay.add("Recuay");
            provincia_Recuay.add("Catac");
            provincia_Recuay.add("Cotaparaco");
            provincia_Recuay.add("Huayllapampa");
            provincia_Recuay.add("Llacllin");
            provincia_Recuay.add("Marca");
            provincia_Recuay.add("Pampas Chico");
            provincia_Recuay.add("Pararin");
            provincia_Recuay.add("Tapacocha");
            provincia_Recuay.add("Ticapampa");

            List<String> provincia_Santa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Santa.add("Seleccionar un Distrito");
            }else{
                provincia_Santa.add(var_Distrito);
            }
            provincia_Santa.add("Chimbote");
            provincia_Santa.add("Caceres Del Peru");
            provincia_Santa.add("Coishco");
            provincia_Santa.add("Macate");
            provincia_Santa.add("Moro");
            provincia_Santa.add("Nepeña");
            provincia_Santa.add("Samanco");
            provincia_Santa.add("Santa");
            provincia_Santa.add("Nuevo Chimbote");


            List<String> provincia_Sihuas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Sihuas.add("Seleccionar un Distrito");
            }else{
                provincia_Sihuas.add(var_Distrito);
            }
            provincia_Sihuas.add("Sihuas");
            provincia_Sihuas.add("Acobamba");
            provincia_Sihuas.add("Alfonso Ugarte");
            provincia_Sihuas.add("Cashapampa");
            provincia_Sihuas.add("Chingalpo");
            provincia_Sihuas.add("Huayllabamba");
            provincia_Sihuas.add("Quiches");
            provincia_Sihuas.add("Ragash");
            provincia_Sihuas.add("San Juan");
            provincia_Sihuas.add("Sicsibamba");


            List<String> provincia_Yungay = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Yungay.add("Seleccionar un Distrito");
            }else{
                provincia_Yungay.add(var_Distrito);
            }
            provincia_Yungay.add("Yungay");
            provincia_Yungay.add("Cascapara");
            provincia_Yungay.add("Mancos");
            provincia_Yungay.add("Matacoto");
            provincia_Yungay.add("Quillo");
            provincia_Yungay.add("Ranrahirca");
            provincia_Yungay.add("Shupluy");
            provincia_Yungay.add("Yanama");


            List<String> provincia_Abancay = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Abancay.add("Seleccionar un Distrito");
            }else{
                provincia_Abancay.add(var_Distrito);
            }
            provincia_Abancay.add("Abancay");
            provincia_Abancay.add("Chacoche");
            provincia_Abancay.add("Circa");
            provincia_Abancay.add("Curahuasi");
            provincia_Abancay.add("Huanipaca");
            provincia_Abancay.add("Lambrama");
            provincia_Abancay.add("Pichirhua");
            provincia_Abancay.add("San Pedro De Cachora");
            provincia_Abancay.add("Tamburco");


            List<String> provincia_Andahuaylas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Andahuaylas.add("Seleccionar un Distrito");
            }else{
                provincia_Andahuaylas.add(var_Distrito);
            }
            provincia_Andahuaylas.add("Andahuaylas");
            provincia_Andahuaylas.add("Andarapa");
            provincia_Andahuaylas.add("Chiara");
            provincia_Andahuaylas.add("Huancarama");
            provincia_Andahuaylas.add("Huancaray");
            provincia_Andahuaylas.add("Huayana");
            provincia_Andahuaylas.add("Kishuara");
            provincia_Andahuaylas.add("Pacobamba");
            provincia_Andahuaylas.add("Pacucha");
            provincia_Andahuaylas.add("Pampachiri");
            provincia_Andahuaylas.add("Pomacocha");
            provincia_Andahuaylas.add("San Antonio De Cachi");
            provincia_Andahuaylas.add("San Jeronimo");
            provincia_Andahuaylas.add("San Miguel De Chaccrampa");
            provincia_Andahuaylas.add("Santa Maria De Chicmo");
            provincia_Andahuaylas.add("Talavera");
            provincia_Andahuaylas.add("Tumay Huaraca");
            provincia_Andahuaylas.add("Turpo");
            provincia_Andahuaylas.add("Kaquiabamba");


            List<String> provincia_Antabamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Antabamba.add("Seleccionar un Distrito");
            }else{
                provincia_Antabamba.add(var_Distrito);
            }
            provincia_Antabamba.add("Antabamba");
            provincia_Antabamba.add("El Oro");
            provincia_Antabamba.add("Huaquirca");
            provincia_Antabamba.add("Juan Espinoza Medrano");
            provincia_Antabamba.add("Oropesa");
            provincia_Antabamba.add("Pachaconas");
            provincia_Antabamba.add("Sabaino");


            List<String> provincia_Aymaraes = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Aymaraes.add("Seleccionar un Distrito");
            }else{
                provincia_Aymaraes.add(var_Distrito);
            }
            provincia_Aymaraes.add("Chalhuanca");
            provincia_Aymaraes.add("Capaya");
            provincia_Aymaraes.add("Caraybamba");
            provincia_Aymaraes.add("Chapimarca");
            provincia_Aymaraes.add("Colcabamba");
            provincia_Aymaraes.add("Cotaruse");
            provincia_Aymaraes.add("Huayllo");
            provincia_Aymaraes.add("Justo Apu Sahuaraura");
            provincia_Aymaraes.add("Lucre");
            provincia_Aymaraes.add("Pocohuanca");
            provincia_Aymaraes.add("San Juan De Chacña");
            provincia_Aymaraes.add("Sañayca");
            provincia_Aymaraes.add("Soraya");
            provincia_Aymaraes.add("Tapairihua");
            provincia_Aymaraes.add("Tintay");
            provincia_Aymaraes.add("Toraya");
            provincia_Aymaraes.add("Yanaca");


            List<String> provincia_Cotabambas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cotabambas.add("Seleccionar un Distrito");
            }else{
                provincia_Cotabambas.add(var_Distrito);
            }
            provincia_Cotabambas.add("Tambobamba");
            provincia_Cotabambas.add("Cotabambas");
            provincia_Cotabambas.add("Coyllurqui");
            provincia_Cotabambas.add("Haquira");
            provincia_Cotabambas.add("Mara");
            provincia_Cotabambas.add("Challhuahuacho");


            List<String> provincia_Chincheros = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chincheros.add("Seleccionar un Distrito");
            }else{
                provincia_Chincheros.add(var_Distrito);
            }
            provincia_Chincheros.add("Chincheros");
            provincia_Chincheros.add("Anco-Huallo");
            provincia_Chincheros.add("Cocharcas");
            provincia_Chincheros.add("Huaccana");
            provincia_Chincheros.add("Ocobamba");
            provincia_Chincheros.add("Ongoy");
            provincia_Chincheros.add("Uranmarca");
            provincia_Chincheros.add("Ranracancha");


            List<String> provincia_Grau = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Grau.add("Seleccionar un Distrito");
            }else{
                provincia_Grau.add(var_Distrito);
            }
            provincia_Grau.add("Chuquibambilla");
            provincia_Grau.add("Curpahuasi");
            provincia_Grau.add("Gamarra");
            provincia_Grau.add("Huayllati");
            provincia_Grau.add("Mamara");
            provincia_Grau.add("Micaela Bastidas");
            provincia_Grau.add("Pataypampa");
            provincia_Grau.add("Progreso");
            provincia_Grau.add("San Antonio");
            provincia_Grau.add("Santa Ros a");
            provincia_Grau.add("Turpay");
            provincia_Grau.add("Vilcabamba");
            provincia_Grau.add("Virundo");
            provincia_Grau.add("Curasco");

            List<String> provincia_Arequipa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Arequipa.add("Seleccionar un Distrito");
            }else{
                provincia_Arequipa.add(var_Distrito);
            }
            provincia_Arequipa.add("Arequipa");
            provincia_Arequipa.add("Alto Selva Alegre");
            provincia_Arequipa.add("Cayma");
            provincia_Arequipa.add("Cerro Colorado");
            provincia_Arequipa.add("Characato");
            provincia_Arequipa.add("Chiguata");
            provincia_Arequipa.add("Jacobo Hunter");
            provincia_Arequipa.add("La Joya");
            provincia_Arequipa.add("Mariano Melgar");
            provincia_Arequipa.add("Miraflores");
            provincia_Arequipa.add("Mollebaya");
            provincia_Arequipa.add("Paucarpata");
            provincia_Arequipa.add("Pocsi");
            provincia_Arequipa.add("Polobaya");
            provincia_Arequipa.add("Quequeña");
            provincia_Arequipa.add("Sabandia");
            provincia_Arequipa.add("Sachaca");
            provincia_Arequipa.add("San Juan De Siguas");
            provincia_Arequipa.add("San Juan De Tarucani");
            provincia_Arequipa.add("Santa Isabel De Siguas");
            provincia_Arequipa.add("Santa Rita De Siguas");
            provincia_Arequipa.add("Socabaya");
            provincia_Arequipa.add("Tiabaya");
            provincia_Arequipa.add("Uchumayo");
            provincia_Arequipa.add("Vitor");
            provincia_Arequipa.add("Yanahuara");
            provincia_Arequipa.add("Yarabamba");
            provincia_Arequipa.add("Yura");
            provincia_Arequipa.add("Jose Luis Bustamante Y Rive");


            List<String> provincia_Camana = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Camana.add("Seleccionar un Distrito");
            }else{
                provincia_Camana.add(var_Distrito);
            }
            provincia_Camana.add("Camana");
            provincia_Camana.add("Jose Maria Quimper");
            provincia_Camana.add("Mariano Nicolas Valcarcel");
            provincia_Camana.add("Mariscal Caceres");
            provincia_Camana.add("Nicolas De Pierola");
            provincia_Camana.add("Ocoña");
            provincia_Camana.add("Quilca");
            provincia_Camana.add("Samuel Pastor");


            List<String> provincia_Caraveli = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Caraveli.add("Seleccionar un Distrito");
            }else{
                provincia_Caraveli.add(var_Distrito);
            }
            provincia_Caraveli.add("Caraveli");
            provincia_Caraveli.add("Acari");
            provincia_Caraveli.add("Atico");
            provincia_Caraveli.add("Atiquipa");
            provincia_Caraveli.add("Bella Union");
            provincia_Caraveli.add("Cahuacho");
            provincia_Caraveli.add("Chala");
            provincia_Caraveli.add("Chaparra");
            provincia_Caraveli.add("Huanuhuanu");
            provincia_Caraveli.add("Jaqui");
            provincia_Caraveli.add("Lomas");
            provincia_Caraveli.add("Quicacha");
            provincia_Caraveli.add("Yauca");


            List<String> provincia_Castilla = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Castilla.add("Seleccionar un Distrito");
            }else{
                provincia_Castilla.add(var_Distrito);
            }
            provincia_Castilla.add("Aplao");
            provincia_Castilla.add("Andagua");
            provincia_Castilla.add("Ayo");
            provincia_Castilla.add("Chachas");
            provincia_Castilla.add("Chilcaymarca");
            provincia_Castilla.add("Choco");
            provincia_Castilla.add("Huancarqui");
            provincia_Castilla.add("Machaguay");
            provincia_Castilla.add("Orcopampa");
            provincia_Castilla.add("Pampacolca");
            provincia_Castilla.add("Tipan");
            provincia_Castilla.add("Uñon");
            provincia_Castilla.add("Uraca");
            provincia_Castilla.add("Viraco");


            List<String> provincia_Caylloma = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Caylloma.add("Seleccionar un Distrito");
            }else{
                provincia_Caylloma.add(var_Distrito);
            }
            provincia_Caylloma.add("Chivay");
            provincia_Caylloma.add("Achoma");
            provincia_Caylloma.add("Cabanaconde");
            provincia_Caylloma.add("Callalli");
            provincia_Caylloma.add("Caylloma");
            provincia_Caylloma.add("Coporaque");
            provincia_Caylloma.add("Huambo");
            provincia_Caylloma.add("Huanca");
            provincia_Caylloma.add("Ichupampa");
            provincia_Caylloma.add("Lari");
            provincia_Caylloma.add("Lluta");
            provincia_Caylloma.add("Maca");
            provincia_Caylloma.add("Madrigal");
            provincia_Caylloma.add("San Antonio De Chuca");
            provincia_Caylloma.add("Sibayo");
            provincia_Caylloma.add("Tapay");
            provincia_Caylloma.add("Tisco");
            provincia_Caylloma.add("Tuti");
            provincia_Caylloma.add("Yanque");
            provincia_Caylloma.add("Majes");


            List<String> provincia_Condesuyos = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Condesuyos.add("Seleccionar un Distrito");
            }else{
                provincia_Condesuyos.add(var_Distrito);
            }
            provincia_Condesuyos.add("Chuquibamba");
            provincia_Condesuyos.add("Andaray");
            provincia_Condesuyos.add("Cayarani");
            provincia_Condesuyos.add("Chichas");
            provincia_Condesuyos.add("Iray");
            provincia_Condesuyos.add("Rio Grande");
            provincia_Condesuyos.add("Salamanca");
            provincia_Condesuyos.add("Yanaquihua");


            List<String> provincia_Islay = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Islay.add("Seleccionar un Distrito");
            }else{
                provincia_Islay.add(var_Distrito);
            }
            provincia_Islay.add("Mollendo");
            provincia_Islay.add("Cocachacra");
            provincia_Islay.add("Dean Valdivia");
            provincia_Islay.add("Islay");
            provincia_Islay.add("Mejia");
            provincia_Islay.add("Punta De Bombon");


            List<String> provincia_LaUnion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_LaUnion.add("Seleccionar un Distrito");
            }else{
                provincia_LaUnion.add(var_Distrito);
            }
            provincia_LaUnion.add("Cotahuasi");
            provincia_LaUnion.add("Alca");
            provincia_LaUnion.add("Charcana");
            provincia_LaUnion.add("Huaynacotas");
            provincia_LaUnion.add("Pampamarca");
            provincia_LaUnion.add("Puyca");
            provincia_LaUnion.add("Quechualla");
            provincia_LaUnion.add("Sayla");
            provincia_LaUnion.add("Tauria");
            provincia_LaUnion.add("Tomepampa");
            provincia_LaUnion.add("Toro");


            List<String> provincia_Huamanga = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huamanga.add("Seleccionar un Distrito");
            }else{
                provincia_Huamanga.add(var_Distrito);
            }
            provincia_Huamanga.add("Ayacucho");
            provincia_Huamanga.add("Acocro");
            provincia_Huamanga.add("Acos Vinchos");
            provincia_Huamanga.add("Carmen Alto");
            provincia_Huamanga.add("Chiara");
            provincia_Huamanga.add("Ocros");
            provincia_Huamanga.add("Pacaycasa");
            provincia_Huamanga.add("Quinua");
            provincia_Huamanga.add("San Jose De Ticllas");
            provincia_Huamanga.add("San Juan Bautista");
            provincia_Huamanga.add("Santiago De Pischa");
            provincia_Huamanga.add("Socos");
            provincia_Huamanga.add("Tambillo");
            provincia_Huamanga.add("Vinchos");
            provincia_Huamanga.add("Jesus Nazareno");


            List<String> provincia_Cangallo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cangallo.add("Seleccionar un Distrito");
            }else{
                provincia_Cangallo.add(var_Distrito);
            }
            provincia_Cangallo.add("Cangallo");
            provincia_Cangallo.add("Chuschi");
            provincia_Cangallo.add("Los Morochucos");
            provincia_Cangallo.add("Maria Parado De Bellido");
            provincia_Cangallo.add("Paras");
            provincia_Cangallo.add("Totos");


            List<String> provincia_HuancaSancos = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_HuancaSancos.add("Seleccionar un Distrito");
            }else{
                provincia_HuancaSancos.add(var_Distrito);
            }
            provincia_HuancaSancos.add("Sancos");
            provincia_HuancaSancos.add("Carapo");
            provincia_HuancaSancos.add("Sacsamarca");
            provincia_HuancaSancos.add("Santiago De Lucanamarca");


            List<String> provincia_Huanta = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huanta.add("Seleccionar un Distrito");
            }else{
                provincia_Huanta.add(var_Distrito);
            }
            provincia_Huanta.add("Huanta");
            provincia_Huanta.add("Ayahuanco");
            provincia_Huanta.add("Huamanguilla");
            provincia_Huanta.add("Iguain");
            provincia_Huanta.add("Luricocha");
            provincia_Huanta.add("Santillana");
            provincia_Huanta.add("Sivia");
            provincia_Huanta.add("Llochegua");


            List<String> provincia_LaMar = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_LaMar.add("Seleccionar un Distrito");
            }else{
                provincia_LaMar.add(var_Distrito);
            }
            provincia_LaMar.add("San Miguel");
            provincia_LaMar.add("Anco");
            provincia_LaMar.add("Ayna");
            provincia_LaMar.add("Chilcas");
            provincia_LaMar.add("Chungui");
            provincia_LaMar.add("Luis Carranza");
            provincia_LaMar.add("Santa Rosa");
            provincia_LaMar.add("Tambo");


            List<String> provincia_Lucanas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lucanas.add("Seleccionar un Distrito");
            }else{
                provincia_Lucanas.add(var_Distrito);
            }
            provincia_Lucanas.add("Puquio");
            provincia_Lucanas.add("Aucara");
            provincia_Lucanas.add("Cabana");
            provincia_Lucanas.add("Carmen Salcedo");
            provincia_Lucanas.add("Chaviña");
            provincia_Lucanas.add("Chipao");
            provincia_Lucanas.add("Huac-Huas");
            provincia_Lucanas.add("Laramate");
            provincia_Lucanas.add("Leoncio Prado");
            provincia_Lucanas.add("Llauta");
            provincia_Lucanas.add("Lucanas");
            provincia_Lucanas.add("Ocaña");
            provincia_Lucanas.add("Otoca");
            provincia_Lucanas.add("Saisa");
            provincia_Lucanas.add("San Cristobal");
            provincia_Lucanas.add("San Juan");
            provincia_Lucanas.add("San Pedro");
            provincia_Lucanas.add("San Pedro De Palco");
            provincia_Lucanas.add("Sancos");
            provincia_Lucanas.add("Santa Ana De Huaycahuacho");
            provincia_Lucanas.add("Santa Lucia");


            List<String> provincia_Parinacochas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Parinacochas.add("Seleccionar un Distrito");
            }else{
                provincia_Parinacochas.add(var_Distrito);
            }
            provincia_Parinacochas.add("Coracora");
            provincia_Parinacochas.add("Chumpi");
            provincia_Parinacochas.add("Coronel Castaðeda");
            provincia_Parinacochas.add("Pacapausa");
            provincia_Parinacochas.add("Pullo");
            provincia_Parinacochas.add("Puyusca");
            provincia_Parinacochas.add("San Francisco De Ravacayco");
            provincia_Parinacochas.add("Upahuacho");


            List<String> provincia_PaucarDelSaraSara = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_PaucarDelSaraSara.add("Seleccionar un Distrito");
            }else{
                provincia_PaucarDelSaraSara.add(var_Distrito);
            }
            provincia_PaucarDelSaraSara.add("Pausa");
            provincia_PaucarDelSaraSara.add("Colta");
            provincia_PaucarDelSaraSara.add("Corculla");
            provincia_PaucarDelSaraSara.add("Lampa");
            provincia_PaucarDelSaraSara.add("Marcabamba");
            provincia_PaucarDelSaraSara.add("Oyolo");
            provincia_PaucarDelSaraSara.add("Pararca");
            provincia_PaucarDelSaraSara.add("San Javier De Alpabamba");
            provincia_PaucarDelSaraSara.add("San Jose De Ushua");
            provincia_PaucarDelSaraSara.add("Sara Sara");


            List<String> provincia_Sucre = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Sucre.add("Seleccionar un Distrito");
            }else{
                provincia_Sucre.add(var_Distrito);
            }
            provincia_Sucre.add("Querobamba");
            provincia_Sucre.add("Belen");
            provincia_Sucre.add("Chalcos");
            provincia_Sucre.add("Chilcayoc");
            provincia_Sucre.add("Huacaña");
            provincia_Sucre.add("Morcolla");
            provincia_Sucre.add("Paico");
            provincia_Sucre.add("San Pedro De Larcay");
            provincia_Sucre.add("San Salvador De Quije");
            provincia_Sucre.add("Santiago De Paucaray");
            provincia_Sucre.add("Soras");


            List<String> provincia_VictorFajardo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_VictorFajardo.add("Seleccionar un Distrito");
            }else{
                provincia_VictorFajardo.add(var_Distrito);
            }
            provincia_VictorFajardo.add("Huancapi");
            provincia_VictorFajardo.add("Alcamenca");
            provincia_VictorFajardo.add("Apongo");
            provincia_VictorFajardo.add("Asquipata");
            provincia_VictorFajardo.add("Canaria");
            provincia_VictorFajardo.add("Cayara");
            provincia_VictorFajardo.add("Colca");
            provincia_VictorFajardo.add("Huamanquiquia");
            provincia_VictorFajardo.add("Huancaraylla");
            provincia_VictorFajardo.add("Huaya");
            provincia_VictorFajardo.add("Sarhua");
            provincia_VictorFajardo.add("Vilcanchos");

            List<String> provincia_VilcasHuaman = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_VilcasHuaman.add("Seleccionar un Distrito");
            }else{
                provincia_VilcasHuaman.add(var_Distrito);
            }
            provincia_VilcasHuaman.add("Vilcas Huaman");
            provincia_VilcasHuaman.add("Accomarca");
            provincia_VilcasHuaman.add("Carhuanca");
            provincia_VilcasHuaman.add("Concepcion");
            provincia_VilcasHuaman.add("Huambalpa");
            provincia_VilcasHuaman.add("Independencia");
            provincia_VilcasHuaman.add("Saurama");
            provincia_VilcasHuaman.add("Vischongo");


            List<String> provincia_Cajamarca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cajamarca.add("Seleccionar un Distrito");
            }else{
                provincia_Cajamarca.add(var_Distrito);
            }
            provincia_Cajamarca.add("Cajamarca");
            provincia_Cajamarca.add("Asuncion");
            provincia_Cajamarca.add("Chetilla");
            provincia_Cajamarca.add("Cospan");
            provincia_Cajamarca.add("Encañada");
            provincia_Cajamarca.add("Jesus");
            provincia_Cajamarca.add("Llacanora");
            provincia_Cajamarca.add("Los Baños Del Inca");
            provincia_Cajamarca.add("Magdalena");
            provincia_Cajamarca.add("Matara");
            provincia_Cajamarca.add("Namora");
            provincia_Cajamarca.add("San Juan");


            List<String> provincia_Cajabamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cajabamba.add("Seleccionar un Distrito");
            }else{
                provincia_Cajabamba.add(var_Distrito);
            }
            provincia_Cajabamba.add("Cajabamba");
            provincia_Cajabamba.add("Cachachi");
            provincia_Cajabamba.add("Condebamba");
            provincia_Cajabamba.add("Sitacocha");


            List<String> provincia_Celendin = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Celendin.add("Seleccionar un Distrito");
            }else{
                provincia_Celendin.add(var_Distrito);
            }
            provincia_Celendin.add("Celendin");
            provincia_Celendin.add("Chumuch");
            provincia_Celendin.add("Cortegana");
            provincia_Celendin.add("Huasmin");
            provincia_Celendin.add("Jorge Chavez");
            provincia_Celendin.add("Jose Galvez");
            provincia_Celendin.add("Miguel Iglesias");
            provincia_Celendin.add("Oxamarca");
            provincia_Celendin.add("Sorochuco");
            provincia_Celendin.add("Sucre");
            provincia_Celendin.add("Utco");
            provincia_Celendin.add("La Libertad De Pallan");


            List<String> provincia_Chota = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chota.add("Seleccionar un Distrito");
            }else{
                provincia_Chota.add(var_Distrito);
            }
            provincia_Chota.add("Chota");
            provincia_Chota.add("Anguia");
            provincia_Chota.add("Chadin");
            provincia_Chota.add("Chiguirip");
            provincia_Chota.add("Chimban");
            provincia_Chota.add("Choropampa");
            provincia_Chota.add("Cochabamba");
            provincia_Chota.add("Conchan");
            provincia_Chota.add("Huambos");
            provincia_Chota.add("Lajas");
            provincia_Chota.add("Llama");
            provincia_Chota.add("Miracosta");
            provincia_Chota.add("Paccha");
            provincia_Chota.add("Pion");
            provincia_Chota.add("Querocoto");
            provincia_Chota.add("San Juan De Licupis");
            provincia_Chota.add("Tacabamba");
            provincia_Chota.add("Tocmoche");
            provincia_Chota.add("Chalamarca");


            List<String> provincia_Contumaza = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Contumaza.add("Seleccionar un Distrito");
            }else{
                provincia_Contumaza.add(var_Distrito);
            }
            provincia_Contumaza.add("Contumaza");
            provincia_Contumaza.add("Chilete");
            provincia_Contumaza.add("Cupisnique");
            provincia_Contumaza.add("Guzmango");
            provincia_Contumaza.add("San Benito");
            provincia_Contumaza.add("Santa Cruz De Toled");
            provincia_Contumaza.add("Tantarica");
            provincia_Contumaza.add("Yonan");


            List<String> provincia_Cutervo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cutervo.add("Seleccionar un Distrito");
            }else{
                provincia_Cutervo.add(var_Distrito);
            }
            provincia_Cutervo.add("Cutervo");
            provincia_Cutervo.add("Callayuc");
            provincia_Cutervo.add("Choros");
            provincia_Cutervo.add("Cujillo");
            provincia_Cutervo.add("La Ramada");
            provincia_Cutervo.add("Pimpingos");
            provincia_Cutervo.add("Querocotillo");
            provincia_Cutervo.add("San Andres De Cutervo");
            provincia_Cutervo.add("San Juan De Cutervo");
            provincia_Cutervo.add("San Luis De Lucma");
            provincia_Cutervo.add("Santa Cruz");
            provincia_Cutervo.add("Santo Domingo De La Capilla");
            provincia_Cutervo.add("Santo Tomas");
            provincia_Cutervo.add("Socota");
            provincia_Cutervo.add("Toribio Casanova");


            List<String> provincia_Hualgayoc = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Hualgayoc.add("Seleccionar un Distrito");
            }else{
                provincia_Hualgayoc.add(var_Distrito);
            }
            provincia_Hualgayoc.add("Bambamarca");
            provincia_Hualgayoc.add("Chugur");
            provincia_Hualgayoc.add("Hualgayoc");


            List<String> provincia_Jaen = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Jaen.add("Seleccionar un Distrito");
            }else{
                provincia_Jaen.add(var_Distrito);
            }
            provincia_Jaen.add("Jaen");
            provincia_Jaen.add("Bellavista");
            provincia_Jaen.add("Chontali");
            provincia_Jaen.add("Colasay");
            provincia_Jaen.add("Huabal");
            provincia_Jaen.add("Las Pirias");
            provincia_Jaen.add("Pomahuaca");
            provincia_Jaen.add("Pucara");
            provincia_Jaen.add("Sallique");
            provincia_Jaen.add("San Felipe");
            provincia_Jaen.add("San Jose Del Alto");
            provincia_Jaen.add("Santa Rosa");


            List<String> provincia_SanIgnacio = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanIgnacio.add("Seleccionar un Distrito");
            }else{
                provincia_SanIgnacio.add(var_Distrito);
            }
            provincia_SanIgnacio.add("San Ignacio");
            provincia_SanIgnacio.add("Chirinos");
            provincia_SanIgnacio.add("Huarango");
            provincia_SanIgnacio.add("La Coipa");
            provincia_SanIgnacio.add("Namballe");
            provincia_SanIgnacio.add("San Jose De Lourdes");
            provincia_SanIgnacio.add("Tabaconas");


            List<String> provincia_SanMarcos = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanMarcos.add("Seleccionar un Distrito");
            }else{
                provincia_SanMarcos.add(var_Distrito);
            }
            provincia_SanMarcos.add("Pedro Galvez");
            provincia_SanMarcos.add("Chancay");
            provincia_SanMarcos.add("Eduardo Villanueva");
            provincia_SanMarcos.add("Gregorio Pita");
            provincia_SanMarcos.add("Ichocan");
            provincia_SanMarcos.add("Jose Manuel Quiroz");
            provincia_SanMarcos.add("Jose Sabogal");


            List<String> provincia_SanMiguel = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanMiguel.add("Seleccionar un Distrito");
            }else{
                provincia_SanMiguel.add(var_Distrito);
            }
            provincia_SanMiguel.add("San Miguel");
            provincia_SanMiguel.add("Bolivar");
            provincia_SanMiguel.add("Calquis");
            provincia_SanMiguel.add("Catilluc");
            provincia_SanMiguel.add("El Prado");
            provincia_SanMiguel.add("La Florida");
            provincia_SanMiguel.add("Llapa");
            provincia_SanMiguel.add("Nanchoc");
            provincia_SanMiguel.add("Niepos");
            provincia_SanMiguel.add("San Gregorio");
            provincia_SanMiguel.add("San Silvestre De Cochan");
            provincia_SanMiguel.add("Tongod");
            provincia_SanMiguel.add("Union Agua Blanca");


            List<String> provincia_SanPablo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanPablo.add("Seleccionar un Distrito");
            }else{
                provincia_SanPablo.add(var_Distrito);
            }
            provincia_SanPablo.add("San Pablo");
            provincia_SanPablo.add("San Bernardino");
            provincia_SanPablo.add("San Luis");
            provincia_SanPablo.add("Tumbaden");


            List<String> provincia_SantaCruz = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SantaCruz.add("Seleccionar un Distrito");
            }else{
                provincia_SantaCruz.add(var_Distrito);
            }
            provincia_SantaCruz.add("Santa Cruz");
            provincia_SantaCruz.add("Andabamba");
            provincia_SantaCruz.add("Catache");
            provincia_SantaCruz.add("Chancaybaños");
            provincia_SantaCruz.add("La Esperanza");
            provincia_SantaCruz.add("Ninabamba");
            provincia_SantaCruz.add("Pulan");
            provincia_SantaCruz.add("Saucepampa");
            provincia_SantaCruz.add("Sexi");
            provincia_SantaCruz.add("Uticyacu");
            provincia_SantaCruz.add("Yauyucan");


            List<String> provincia_Callao = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Callao.add("Seleccionar un Distrito");
            }else{
                provincia_Callao.add(var_Distrito);
            }
            provincia_Callao.add("Callao");
            provincia_Callao.add("Bellavista");
            provincia_Callao.add("Carmen De La Legua Reynoso");
            provincia_Callao.add("La Perla");
            provincia_Callao.add("La Punta");
            provincia_Callao.add("Ventanilla");


            List<String> provincia_Cusco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cusco.add("Seleccionar un Distrito");
            }else{
                provincia_Cusco.add(var_Distrito);
            }
            provincia_Cusco.add("Cusco");
            provincia_Cusco.add("Ccorca");
            provincia_Cusco.add("Poroy");
            provincia_Cusco.add("San Jeronimo");
            provincia_Cusco.add("San Sebastian");
            provincia_Cusco.add("Santiago");
            provincia_Cusco.add("Saylla");
            provincia_Cusco.add("Wanchaq");


            List<String> provincia_Acomayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Acomayo.add("Seleccionar un Distrito");
            }else{
                provincia_Acomayo.add(var_Distrito);
            }
            provincia_Acomayo.add("Acomayo");
            provincia_Acomayo.add("Acopia");
            provincia_Acomayo.add("Acos");
            provincia_Acomayo.add("Mosoc Llacta");
            provincia_Acomayo.add("Pomacanchi");
            provincia_Acomayo.add("Rondocan");
            provincia_Acomayo.add("Sangarara");


            List<String> provincia_Anta = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Anta.add("Seleccionar un Distrito");
            }else{
                provincia_Anta.add(var_Distrito);
            }
            provincia_Anta.add("Anta");
            provincia_Anta.add("Ancahuasi");
            provincia_Anta.add("Cachimayo");
            provincia_Anta.add("Chinchaypujio");
            provincia_Anta.add("Huarocondo");
            provincia_Anta.add("Limatambo");
            provincia_Anta.add("Mollepata");
            provincia_Anta.add("Pucyura");
            provincia_Anta.add("Zurite");


            List<String> provincia_Calca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Calca.add("Seleccionar un Distrito");
            }else{
                provincia_Calca.add(var_Distrito);
            }
            provincia_Calca.add("Calca");
            provincia_Calca.add("Coya");
            provincia_Calca.add("Lamay");
            provincia_Calca.add("Lares");
            provincia_Calca.add("Pisac");
            provincia_Calca.add("San Salvador");
            provincia_Calca.add("Taray");
            provincia_Calca.add("Yanatile");


            List<String> provincia_Canas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Canas.add("Seleccionar un Distrito");
            }else{
                provincia_Canas.add(var_Distrito);
            }
            provincia_Canas.add("Yanaoca");
            provincia_Canas.add("Checca");
            provincia_Canas.add("Kunturkanki");
            provincia_Canas.add("Langui");
            provincia_Canas.add("Layo");
            provincia_Canas.add("Pampamarca");
            provincia_Canas.add("Quehue");
            provincia_Canas.add("Tupac Amaru");


            List<String> provincia_Canchis = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Canchis.add("Seleccionar un Distrito");
            }else{
                provincia_Canchis.add(var_Distrito);
            }
            provincia_Canchis.add("Sicuani");
            provincia_Canchis.add("Checacupe");
            provincia_Canchis.add("Combapata");
            provincia_Canchis.add("Marangani");
            provincia_Canchis.add("Pitumarca");
            provincia_Canchis.add("San Pablo");
            provincia_Canchis.add("San Pedro");
            provincia_Canchis.add("Tinta");


            List<String> provincia_Chumbivilcas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chumbivilcas.add("Seleccionar un Distrito");
            }else{
                provincia_Chumbivilcas.add(var_Distrito);
            }
            provincia_Chumbivilcas.add("Santo Tomas");
            provincia_Chumbivilcas.add("Capacmarca");
            provincia_Chumbivilcas.add("Chamaca");
            provincia_Chumbivilcas.add("Colquemarca");
            provincia_Chumbivilcas.add("Livitaca");
            provincia_Chumbivilcas.add("Llusco");
            provincia_Chumbivilcas.add("Quiñota");
            provincia_Chumbivilcas.add("Velille");


            List<String> provincia_Espinar = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Espinar.add("Seleccionar un Distrito");
            }else{
                provincia_Espinar.add(var_Distrito);
            }
            provincia_Espinar.add("Espinar");
            provincia_Espinar.add("Condoroma");
            provincia_Espinar.add("Coporaque");
            provincia_Espinar.add("Ocoruro");
            provincia_Espinar.add("Pallpata");
            provincia_Espinar.add("Pichigua");
            provincia_Espinar.add("Suyckutambo");
            provincia_Espinar.add("Alto Pichigua");


            List<String> provincia_LaConvencion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_LaConvencion.add("Seleccionar un Distrito");
            }else{
                provincia_LaConvencion.add(var_Distrito);
            }
            provincia_LaConvencion.add("Santa Ana");
            provincia_LaConvencion.add("Echarate");
            provincia_LaConvencion.add("Huayopata");
            provincia_LaConvencion.add("Maranura");
            provincia_LaConvencion.add("Ocobamba");
            provincia_LaConvencion.add("Quellouno");
            provincia_LaConvencion.add("Kimbiri");
            provincia_LaConvencion.add("Santa Teresa");
            provincia_LaConvencion.add("Vilcabamba");
            provincia_LaConvencion.add("Pichari");


            List<String> provincia_Paruro = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Paruro.add("Seleccionar un Distrito");
            }else{
                provincia_Paruro.add(var_Distrito);
            }
            provincia_Paruro.add("Paruro");
            provincia_Paruro.add("Accha");
            provincia_Paruro.add("Ccapi");
            provincia_Paruro.add("Colcha");
            provincia_Paruro.add("Huanoquite");
            provincia_Paruro.add("Omacha");
            provincia_Paruro.add("Paccaritambo");
            provincia_Paruro.add("Pillpinto");
            provincia_Paruro.add("Yaurisque");


            List<String> provincia_Paucartambo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Paucartambo.add("Seleccionar un Distrito");
            }else{
                provincia_Paucartambo.add(var_Distrito);
            }
            provincia_Paucartambo.add("Paucartambo");
            provincia_Paucartambo.add("Caicay");
            provincia_Paucartambo.add("Challabamba");
            provincia_Paucartambo.add("Colquepata");
            provincia_Paucartambo.add("Huancarani");
            provincia_Paucartambo.add("Kosñipata");


            List<String> provincia_Quispicanchi = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Quispicanchi.add("Seleccionar un Distrito");
            }else{
                provincia_Quispicanchi.add(var_Distrito);
            }
            provincia_Quispicanchi.add("Urcos");
            provincia_Quispicanchi.add("Andahuaylillas");
            provincia_Quispicanchi.add("Camanti");
            provincia_Quispicanchi.add("Ccarhuayo");
            provincia_Quispicanchi.add("Ccatca");
            provincia_Quispicanchi.add("Cusipata");
            provincia_Quispicanchi.add("Huaro");
            provincia_Quispicanchi.add("Lucre");
            provincia_Quispicanchi.add("Marcapata");
            provincia_Quispicanchi.add("Ocongate");
            provincia_Quispicanchi.add("Oropesa");
            provincia_Quispicanchi.add("Quiquijana");


            List<String> provincia_Urubamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Urubamba.add("Seleccionar un Distrito");
            }else{
                provincia_Urubamba.add(var_Distrito);
            }
            provincia_Urubamba.add("Urubamba");
            provincia_Urubamba.add("Chinchero");
            provincia_Urubamba.add("Huayllabamba");
            provincia_Urubamba.add("Machupicchu");
            provincia_Urubamba.add("Maras");
            provincia_Urubamba.add("Ollantaytambo");
            provincia_Urubamba.add("Yucay");


            List<String> provincia_Huancavelica = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huancavelica.add("Seleccionar un Distrito");
            }else{
                provincia_Huancavelica.add(var_Distrito);
            }
            provincia_Huancavelica.add("Huancavelica");
            provincia_Huancavelica.add("Acobambilla");
            provincia_Huancavelica.add("Acoria");
            provincia_Huancavelica.add("Conayca");
            provincia_Huancavelica.add("Cuenca");
            provincia_Huancavelica.add("Huachocolpa");
            provincia_Huancavelica.add("Huayllahuara");
            provincia_Huancavelica.add("Izcuchaca");
            provincia_Huancavelica.add("Laria");
            provincia_Huancavelica.add("Manta");
            provincia_Huancavelica.add("Mariscal Caceres");
            provincia_Huancavelica.add("Moya");
            provincia_Huancavelica.add("Nuevo Occoro");
            provincia_Huancavelica.add("Palca");
            provincia_Huancavelica.add("Pilchaca");
            provincia_Huancavelica.add("Vilca");
            provincia_Huancavelica.add("Yauli");
            provincia_Huancavelica.add("Ascension");


            List<String> provincia_Acobamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Acobamba.add("Seleccionar un Distrito");
            }else{
                provincia_Acobamba.add(var_Distrito);
            }
            provincia_Acobamba.add("Acobamba");
            provincia_Acobamba.add("Andabamba");
            provincia_Acobamba.add("Anta");
            provincia_Acobamba.add("Caja");
            provincia_Acobamba.add("Marcas");
            provincia_Acobamba.add("Paucara");
            provincia_Acobamba.add("Pomacocha");
            provincia_Acobamba.add("Rosario");


            List<String> provincia_Angaraes = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Angaraes.add("Seleccionar un Distrito");
            }else{
                provincia_Angaraes.add(var_Distrito);
            }
            provincia_Angaraes.add("Lircay");
            provincia_Angaraes.add("Anchonga");
            provincia_Angaraes.add("Callanmarca");
            provincia_Angaraes.add("Ccochaccasa");
            provincia_Angaraes.add("Chincho");
            provincia_Angaraes.add("Congalla");
            provincia_Angaraes.add("Huanca-Huanca");
            provincia_Angaraes.add("Huayllay Grande");
            provincia_Angaraes.add("Julcamarca");
            provincia_Angaraes.add("San Antonio De Antaparco");
            provincia_Angaraes.add("Santo Tomas De Pata");
            provincia_Angaraes.add("Secclla");


            List<String> provincia_Castrovirreyna = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Castrovirreyna.add("Seleccionar un Distrito");
            }else{
                provincia_Castrovirreyna.add(var_Distrito);
            }
            provincia_Castrovirreyna.add("Castrovirreyna");
            provincia_Castrovirreyna.add("Arma");
            provincia_Castrovirreyna.add("Aurahua");
            provincia_Castrovirreyna.add("Capillas");
            provincia_Castrovirreyna.add("Chupamarca");
            provincia_Castrovirreyna.add("Cocas");
            provincia_Castrovirreyna.add("Huachos");
            provincia_Castrovirreyna.add("Huamatambo");
            provincia_Castrovirreyna.add("Mollepampa");
            provincia_Castrovirreyna.add("San Juan");
            provincia_Castrovirreyna.add("Santa Ana");
            provincia_Castrovirreyna.add("Tantara");
            provincia_Castrovirreyna.add("Ticrapo");


            List<String> provincia_Churcampa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Churcampa.add("Seleccionar un Distrito");
            }else{
                provincia_Churcampa.add(var_Distrito);
            }
            provincia_Churcampa.add("Churcampa");
            provincia_Churcampa.add("Anco");
            provincia_Churcampa.add("Chinchihuasi");
            provincia_Churcampa.add("El Carmen");
            provincia_Churcampa.add("La Merced");
            provincia_Churcampa.add("Locroja");
            provincia_Churcampa.add("Paucarbamba");
            provincia_Churcampa.add("San Miguel De Mayocc");
            provincia_Churcampa.add("San Pedro De Coris");
            provincia_Churcampa.add("Pachamarca");


            List<String> provincia_Huaytara = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huaytara.add("Seleccionar un Distrito");
            }else{
                provincia_Huaytara.add(var_Distrito);
            }
            provincia_Huaytara.add("Huaytara");
            provincia_Huaytara.add("Ayavi");
            provincia_Huaytara.add("Cordova");
            provincia_Huaytara.add("Huayacundo Arma");
            provincia_Huaytara.add("Laramarca");
            provincia_Huaytara.add("Ocoyo");
            provincia_Huaytara.add("Pilpichaca");
            provincia_Huaytara.add("Querco");
            provincia_Huaytara.add("Quito-Arma");
            provincia_Huaytara.add("San Antonio De Cusicancha");
            provincia_Huaytara.add("San Francisco De Sangayaico");
            provincia_Huaytara.add("San Isidro");
            provincia_Huaytara.add("Santiago De Chocorvos");
            provincia_Huaytara.add("Santiago De Quirahuara");
            provincia_Huaytara.add("Santo Domingo De Capillas");
            provincia_Huaytara.add("Tambo");


            List<String> provincia_Tayacaja = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tayacaja.add("Seleccionar un Distrito");
            }else{
                provincia_Tayacaja.add(var_Distrito);
            }
            provincia_Tayacaja.add("Pampas");
            provincia_Tayacaja.add("Acostambo");
            provincia_Tayacaja.add("Acraquia");
            provincia_Tayacaja.add("Ahuaycha");
            provincia_Tayacaja.add("Colcabamba");
            provincia_Tayacaja.add("Daniel Hernandez");
            provincia_Tayacaja.add("Huachocolpa");
            provincia_Tayacaja.add("Huando");
            provincia_Tayacaja.add("Huaribamba");
            provincia_Tayacaja.add("Ñahuimpuquio");
            provincia_Tayacaja.add("Pazos");
            provincia_Tayacaja.add("Quishuar");
            provincia_Tayacaja.add("Salcabamba");
            provincia_Tayacaja.add("Salcahuasi");
            provincia_Tayacaja.add("San Marcos De Rocchac");
            provincia_Tayacaja.add("Surcubamba");
            provincia_Tayacaja.add("Tintay Puncu");


            List<String> provincia_Huanuco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huanuco.add("Seleccionar un Distrito");
            }else{
                provincia_Huanuco.add(var_Distrito);
            }
            provincia_Huanuco.add("Huanuco");
            provincia_Huanuco.add("Amarilis");
            provincia_Huanuco.add("Chinchao");
            provincia_Huanuco.add("Churubamba");
            provincia_Huanuco.add("Margos");
            provincia_Huanuco.add("Quisqui");
            provincia_Huanuco.add("San Francisco De Cayran");
            provincia_Huanuco.add("San Pedro De Chaulan");
            provincia_Huanuco.add("Santa Maria Del Valle");
            provincia_Huanuco.add("Yarumayo");
            provincia_Huanuco.add("Pillco Marca");


            List<String> provincia_Ambo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ambo.add("Seleccionar un Distrito");
            }else{
                provincia_Ambo.add(var_Distrito);
            }
            provincia_Ambo.add("Ambo");
            provincia_Ambo.add("Cayna");
            provincia_Ambo.add("Colpas");
            provincia_Ambo.add("Conchamarca");
            provincia_Ambo.add("Huacar");
            provincia_Ambo.add("San Francisco");
            provincia_Ambo.add("San Rafael");
            provincia_Ambo.add("Tomay Kichwa");


            List<String> provincia_DosDeMayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_DosDeMayo.add("Seleccionar un Distrito");
            }else{
                provincia_DosDeMayo.add(var_Distrito);
            }
            provincia_DosDeMayo.add("La Union");
            provincia_DosDeMayo.add("Chuquis");
            provincia_DosDeMayo.add("Marias");
            provincia_DosDeMayo.add("Pachas");
            provincia_DosDeMayo.add("Quivilla");
            provincia_DosDeMayo.add("Ripan");
            provincia_DosDeMayo.add("Shunqui");
            provincia_DosDeMayo.add("Sillapata");
            provincia_DosDeMayo.add("Yanas");


            List<String> provincia_Huacaybamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huacaybamba.add("Seleccionar un Distrito");
            }else{
                provincia_Huacaybamba.add(var_Distrito);
            }
            provincia_Huacaybamba.add("Huacaybamba");
            provincia_Huacaybamba.add("Canchabamba");
            provincia_Huacaybamba.add("Cochabamba");
            provincia_Huacaybamba.add("Pinra");


            List<String> provincia_Huamalies = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huamalies.add("Seleccionar un Distrito");
            }else{
                provincia_Huamalies.add(var_Distrito);
            }
            provincia_Huamalies.add("Llata");
            provincia_Huamalies.add("Arancay");
            provincia_Huamalies.add("Chavin De Pariarca");
            provincia_Huamalies.add("Jacas Grande");
            provincia_Huamalies.add("Jircan");
            provincia_Huamalies.add("Miraflores");
            provincia_Huamalies.add("Monzon");
            provincia_Huamalies.add("Punchao");
            provincia_Huamalies.add("Puños");
            provincia_Huamalies.add("Singa");
            provincia_Huamalies.add("Tantamayo");


            List<String> provincia_LeoncioPrado = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_LeoncioPrado.add("Seleccionar un Distrito");
            }else{
                provincia_LeoncioPrado.add(var_Distrito);
            }
            provincia_LeoncioPrado.add("Rupa-Rupa");
            provincia_LeoncioPrado.add("Daniel Alomia Robles");
            provincia_LeoncioPrado.add("Hermilio Valdizan");
            provincia_LeoncioPrado.add("Jose Crespo Y Castillo");
            provincia_LeoncioPrado.add("Luyando");
            provincia_LeoncioPrado.add("Mariano Damaso Beraun");


            List<String> provincia_Marañon = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Marañon.add("Seleccionar un Distrito");
            }else{
                provincia_Marañon.add(var_Distrito);
            }
            provincia_Marañon.add("Huacrachuco");
            provincia_Marañon.add("Cholon");
            provincia_Marañon.add("San Buenaventura");


            List<String> provincia_Pachitea = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pachitea.add("Seleccionar un Distrito");
            }else{
                provincia_Pachitea.add(var_Distrito);
            }
            provincia_Pachitea.add("Panao");
            provincia_Pachitea.add("Chaglla");
            provincia_Pachitea.add("Molino");
            provincia_Pachitea.add("Umari");


            List<String> provincia_PuertoInca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_PuertoInca.add("Seleccionar un Distrito");
            }else{
                provincia_PuertoInca.add(var_Distrito);
            }
            provincia_PuertoInca.add("Puerto Inca");
            provincia_PuertoInca.add("Codo Del Pozuzo");
            provincia_PuertoInca.add("Honoria");
            provincia_PuertoInca.add("Tournavista");
            provincia_PuertoInca.add("Yuyapichis");


            List<String> provincia_Lauricocha = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lauricocha.add("Seleccionar un Distrito");
            }else{
                provincia_Lauricocha.add(var_Distrito);
            }
            provincia_Lauricocha.add("Jesus");
            provincia_Lauricocha.add("Baños");
            provincia_Lauricocha.add("Jivia");
            provincia_Lauricocha.add("Queropalca");
            provincia_Lauricocha.add("Rondos");
            provincia_Lauricocha.add("San Francisco De Asis");
            provincia_Lauricocha.add("San Miguel De Cauri");


            List<String> provincia_Yarowilca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Yarowilca.add("Seleccionar un Distrito");
            }else{
                provincia_Yarowilca.add(var_Distrito);
            }
            provincia_Yarowilca.add("Chavinillo");
            provincia_Yarowilca.add("Cahuac");
            provincia_Yarowilca.add("Chacabamba");
            provincia_Yarowilca.add("Aparicio Pomares");
            provincia_Yarowilca.add("Jacas Chico");
            provincia_Yarowilca.add("Obas");
            provincia_Yarowilca.add("Pampamarca");
            provincia_Yarowilca.add("Choras");


            List<String> provincia_Ica = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ica.add("Seleccionar un Distrito");
            }else{
                provincia_Ica.add(var_Distrito);
            }
            provincia_Ica.add("Ica");
            provincia_Ica.add("La Tinguiða");
            provincia_Ica.add("Los Aquijes");
            provincia_Ica.add("Ocucaje");
            provincia_Ica.add("Pachacutec");
            provincia_Ica.add("Parcona");
            provincia_Ica.add("Pueblo Nuevo");
            provincia_Ica.add("Salas");
            provincia_Ica.add("San Jose De Los Molinos");
            provincia_Ica.add("San Juan Bautista");
            provincia_Ica.add("Santiago");
            provincia_Ica.add("Subtanjalla");
            provincia_Ica.add("Tate");
            provincia_Ica.add("Yauca Del Rosario");


            List<String> provincia_Chincha = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chincha.add("Seleccionar un Distrito");
            }else{
                provincia_Chincha.add(var_Distrito);
            }
            provincia_Chincha.add("Chincha Alta");
            provincia_Chincha.add("Alto Laran");
            provincia_Chincha.add("Chavin");
            provincia_Chincha.add("Chincha Baja");
            provincia_Chincha.add("El Carmen");
            provincia_Chincha.add("Grocio Prado");
            provincia_Chincha.add("Pueblo Nuevo");
            provincia_Chincha.add("San Juan De Yanac");
            provincia_Chincha.add("San Pedro De Huacarpana");
            provincia_Chincha.add("Sunampe");
            provincia_Chincha.add("Tambo De Mora");


            List<String> provincia_Nazca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Nazca.add("Seleccionar un Distrito");
            }else{
                provincia_Nazca.add(var_Distrito);
            }
            provincia_Nazca.add("Nazca");
            provincia_Nazca.add("Changuillo");
            provincia_Nazca.add("El Ingenio");
            provincia_Nazca.add("Marcona");
            provincia_Nazca.add("Vista Alegre");


            List<String> provincia_Palpa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Palpa.add("Seleccionar un Distrito");
            }else{
                provincia_Palpa.add(var_Distrito);
            }
            provincia_Palpa.add("Palpa");
            provincia_Palpa.add("Llipata");
            provincia_Palpa.add("Rio Grande");
            provincia_Palpa.add("Santa Cruz");
            provincia_Palpa.add("Tibillo");


            List<String> provincia_Pisco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pisco.add("Seleccionar un Distrito");
            }else{
                provincia_Pisco.add(var_Distrito);
            }
            provincia_Pisco.add("Pisco");
            provincia_Pisco.add("Huancano");
            provincia_Pisco.add("Humay");
            provincia_Pisco.add("Independencia");
            provincia_Pisco.add("Paracas");
            provincia_Pisco.add("San Andres");
            provincia_Pisco.add("San Clemente");
            provincia_Pisco.add("Tupac Amaru Inca");


            List<String> provincia_Huancayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huancayo.add("Seleccionar un Distrito");
            }else{
                provincia_Huancayo.add(var_Distrito);
            }
            provincia_Huancayo.add("Huancayo");
            provincia_Huancayo.add("Carhuacallanga");
            provincia_Huancayo.add("Chacapampa");
            provincia_Huancayo.add("Chicche");
            provincia_Huancayo.add("Chilca");
            provincia_Huancayo.add("Chongos Alto");
            provincia_Huancayo.add("Chupuro");
            provincia_Huancayo.add("Colca");
            provincia_Huancayo.add("Cullhuas");
            provincia_Huancayo.add("El Tambo");
            provincia_Huancayo.add("Huacrapuquio");
            provincia_Huancayo.add("Hualhuas");
            provincia_Huancayo.add("Huancan");
            provincia_Huancayo.add("Huasicancha");
            provincia_Huancayo.add("Huayucachi");
            provincia_Huancayo.add("Ingenio");
            provincia_Huancayo.add("Pariahuanca");
            provincia_Huancayo.add("Pilcomayo");
            provincia_Huancayo.add("Pucara");
            provincia_Huancayo.add("Quichuay");
            provincia_Huancayo.add("Quilcas");
            provincia_Huancayo.add("San Agustin");
            provincia_Huancayo.add("San Jeronimo De Tunan");
            provincia_Huancayo.add("Saño");
            provincia_Huancayo.add("Sapallanga");
            provincia_Huancayo.add("Sicaya");
            provincia_Huancayo.add("Santo Domingo De Acobamba");
            provincia_Huancayo.add("Viques");


            List<String> provincia_Concepcion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Concepcion.add("Seleccionar un Distrito");
            }else{
                provincia_Concepcion.add(var_Distrito);
            }
            provincia_Concepcion.add("Concepcion");
            provincia_Concepcion.add("Aco");
            provincia_Concepcion.add("Andamarca");
            provincia_Concepcion.add("Chambara");
            provincia_Concepcion.add("Cochas");
            provincia_Concepcion.add("Comas");
            provincia_Concepcion.add("Heroinas Toledo");
            provincia_Concepcion.add("Manzanares");
            provincia_Concepcion.add("Mariscal Castilla");
            provincia_Concepcion.add("Matahuasi");
            provincia_Concepcion.add("Mito");
            provincia_Concepcion.add("Nueve De Julio");
            provincia_Concepcion.add("Orcotuna");
            provincia_Concepcion.add("San Jose De Quero");
            provincia_Concepcion.add("Santa Rosa De Ocopa");


            List<String> provincia_Chanchamayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chanchamayo.add("Seleccionar un Distrito");
            }else{
                provincia_Chanchamayo.add(var_Distrito);
            }
            provincia_Chanchamayo.add("Chanchamayo");
            provincia_Chanchamayo.add("Perene");
            provincia_Chanchamayo.add("Pichanaqui");
            provincia_Chanchamayo.add("San Luis De Shuaro");
            provincia_Chanchamayo.add("San Ramon");
            provincia_Chanchamayo.add("Vitoc");


            List<String> provincia_Jauja = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Jauja.add("Seleccionar un Distrito");
            }else{
                provincia_Jauja.add(var_Distrito);
            }
            provincia_Jauja.add("Jauja");
            provincia_Jauja.add("Acolla");
            provincia_Jauja.add("Apata");
            provincia_Jauja.add("Ataura");
            provincia_Jauja.add("Canchayllo");
            provincia_Jauja.add("Curicaca");
            provincia_Jauja.add("El Mantaro");
            provincia_Jauja.add("Huamali");
            provincia_Jauja.add("Huaripampa");
            provincia_Jauja.add("Huertas");
            provincia_Jauja.add("Janjaillo");
            provincia_Jauja.add("Julcan");
            provincia_Jauja.add("Leonor Ordoñez");
            provincia_Jauja.add("Llocllapampa");
            provincia_Jauja.add("Marco");
            provincia_Jauja.add("Masma");
            provincia_Jauja.add("Masma Chicche");
            provincia_Jauja.add("Molinos");
            provincia_Jauja.add("Monobamba");
            provincia_Jauja.add("Muqui");
            provincia_Jauja.add("Muquiyauyo");
            provincia_Jauja.add("Paca");
            provincia_Jauja.add("Paccha");
            provincia_Jauja.add("Pancan");
            provincia_Jauja.add("Parco");
            provincia_Jauja.add("Pomacancha");
            provincia_Jauja.add("Ricran");
            provincia_Jauja.add("San Lorenzo");
            provincia_Jauja.add("San Pedro De Chunan");
            provincia_Jauja.add("Sausa");
            provincia_Jauja.add("Sincos");
            provincia_Jauja.add("Tunan Marca");
            provincia_Jauja.add("Yauli");
            provincia_Jauja.add("Yauyos");

            List<String> provincia_Junin = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Junin.add("Seleccionar un Distrito");
            }else{
                provincia_Junin.add(var_Distrito);
            }
            provincia_Junin.add("Junin");
            provincia_Junin.add("Carhuamayo");
            provincia_Junin.add("Ondores");
            provincia_Junin.add("Ulcumayo");


            List<String> provincia_Satipo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Satipo.add("Seleccionar un Distrito");
            }else{
                provincia_Satipo.add(var_Distrito);
            }
            provincia_Satipo.add("Satipo");
            provincia_Satipo.add("Coviriali");
            provincia_Satipo.add("Llaylla");
            provincia_Satipo.add("Mazamari");
            provincia_Satipo.add("Pampa Hermosa");
            provincia_Satipo.add("Pangoa");
            provincia_Satipo.add("Rio Negro");
            provincia_Satipo.add("Rio Tambo");


            List<String> provincia_Tarma = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tarma.add("Seleccionar un Distrito");
            }else{
                provincia_Tarma.add(var_Distrito);
            }
            provincia_Tarma.add("Tarma");
            provincia_Tarma.add("Acobamba");
            provincia_Tarma.add("Huaricolca");
            provincia_Tarma.add("Huasahuasi");
            provincia_Tarma.add("La Union");
            provincia_Tarma.add("Palca");
            provincia_Tarma.add("Palcamayo");
            provincia_Tarma.add("San Pedro De Cajas");
            provincia_Tarma.add("Tapo");


            List<String> provincia_Yauli = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Yauli.add("Seleccionar un Distrito");
            }else{
                provincia_Yauli.add(var_Distrito);
            }
            provincia_Yauli.add("La Oroya");
            provincia_Yauli.add("Chacapalpa");
            provincia_Yauli.add("Huay-Huay");
            provincia_Yauli.add("Marcapomacocha");
            provincia_Yauli.add("Morococha");
            provincia_Yauli.add("Paccha");
            provincia_Yauli.add("Santa Barbara De Carhuacaya");
            provincia_Yauli.add("Santa Rosa De Sacco");
            provincia_Yauli.add("Suitucancha");
            provincia_Yauli.add("Yauli");


            List<String> provincia_Chupaca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chupaca.add("Seleccionar un Distrito");
            }else{
                provincia_Chupaca.add(var_Distrito);
            }
            provincia_Chupaca.add("Chupaca");
            provincia_Chupaca.add("Ahuac");
            provincia_Chupaca.add("Chongos Bajo");
            provincia_Chupaca.add("Huachac");
            provincia_Chupaca.add("Huamancaca Chico");
            provincia_Chupaca.add("San Juan De Iscos");
            provincia_Chupaca.add("San Juan De Jarpa");
            provincia_Chupaca.add("Tres De Diciembre");
            provincia_Chupaca.add("Yanacancha");


            List<String> provincia_Trujillo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Trujillo.add("Seleccionar un Distrito");
            }else{
                provincia_Trujillo.add(var_Distrito);
            }
            provincia_Trujillo.add("Trujillo");
            provincia_Trujillo.add("El Porvenir");
            provincia_Trujillo.add("Florencia De Mora");
            provincia_Trujillo.add("Huanchaco");
            provincia_Trujillo.add("La Esperanza");
            provincia_Trujillo.add("Laredo");
            provincia_Trujillo.add("Moche");
            provincia_Trujillo.add("Poroto");
            provincia_Trujillo.add("Salaverry");
            provincia_Trujillo.add("Simbal");
            provincia_Trujillo.add("Victor Larco Herrera");


            List<String> provincia_Ascope = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ascope.add("Seleccionar un Distrito");
            }else{
                provincia_Ascope.add(var_Distrito);
            }
            provincia_Ascope.add("Ascope");
            provincia_Ascope.add("Chicama");
            provincia_Ascope.add("Chocope");
            provincia_Ascope.add("Magdalena De Cao");
            provincia_Ascope.add("Paijan");
            provincia_Ascope.add("Razuri");
            provincia_Ascope.add("Santiago De Cao");
            provincia_Ascope.add("Casa Grande");


            List<String> provincia_Bolivar = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Bolivar.add("Seleccionar un Distrito");
            }else{
                provincia_Bolivar.add(var_Distrito);
            }
            provincia_Bolivar.add("Bolivar");
            provincia_Bolivar.add("Bambamarca");
            provincia_Bolivar.add("Condormarca");
            provincia_Bolivar.add("Longotea");
            provincia_Bolivar.add("Uchumarca");
            provincia_Bolivar.add("Ucuncha");


            List<String> provincia_Chepen = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chepen.add("Seleccionar un Distrito");
            }else{
                provincia_Chepen.add(var_Distrito);
            }
            provincia_Chepen.add("Chepen");
            provincia_Chepen.add("Pacanga");
            provincia_Chepen.add("Pueblo Nuevo");


            List<String> provincia_Julcan = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Julcan.add("Seleccionar un Distrito");
            }else{
                provincia_Julcan.add(var_Distrito);
            }
            provincia_Julcan.add("Julcan");
            provincia_Julcan.add("Calamarca");
            provincia_Julcan.add("Carabamba");
            provincia_Julcan.add("Huaso");


            List<String> provincia_Otuzco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Otuzco.add("Seleccionar un Distrito");
            }else{
                provincia_Otuzco.add(var_Distrito);
            }
            provincia_Otuzco.add("Otuzco");
            provincia_Otuzco.add("Agallpampa");
            provincia_Otuzco.add("Charat");
            provincia_Otuzco.add("Huaranchal");
            provincia_Otuzco.add("La Cuesta");
            provincia_Otuzco.add("Mache");
            provincia_Otuzco.add("Paranday");
            provincia_Otuzco.add("Salpo");
            provincia_Otuzco.add("Sinsicap");
            provincia_Otuzco.add("Usquil");


            List<String> provincia_Pacasmayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pacasmayo.add("Seleccionar un Distrito");
            }else{
                provincia_Pacasmayo.add(var_Distrito);
            }
            provincia_Pacasmayo.add("San Pedro De Lloc");
            provincia_Pacasmayo.add("Guadalupe");
            provincia_Pacasmayo.add("Jequetepeque");
            provincia_Pacasmayo.add("Pacasmayo");
            provincia_Pacasmayo.add("San Jose");


            List<String> provincia_Pataz = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pataz.add("Seleccionar un Distrito");
            }else{
                provincia_Pataz.add(var_Distrito);
            }
            provincia_Pataz.add("Tayabamba");
            provincia_Pataz.add("Buldibuyo");
            provincia_Pataz.add("Chillia");
            provincia_Pataz.add("Huancaspata");
            provincia_Pataz.add("Huaylillas");
            provincia_Pataz.add("Huayo");
            provincia_Pataz.add("Ongon");
            provincia_Pataz.add("Parcoy");
            provincia_Pataz.add("Pataz");
            provincia_Pataz.add("Pias");
            provincia_Pataz.add("Santiago De Challas");
            provincia_Pataz.add("Taurija");
            provincia_Pataz.add("Urpay");


            List<String> provincia_SanchezCarrion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanchezCarrion.add("Seleccionar un Distrito");
            }else{
                provincia_SanchezCarrion.add(var_Distrito);
            }
            provincia_SanchezCarrion.add("Huamachuco");
            provincia_SanchezCarrion.add("Chugay");
            provincia_SanchezCarrion.add("Cochorco");
            provincia_SanchezCarrion.add("Curgos");
            provincia_SanchezCarrion.add("Marcabal");
            provincia_SanchezCarrion.add("Sanagoran");
            provincia_SanchezCarrion.add("Sarin");
            provincia_SanchezCarrion.add("Sartimbamba");


            List<String> provincia_SantiagoDeChuco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SantiagoDeChuco.add("Seleccionar un Distrito");
            }else{
                provincia_SantiagoDeChuco.add(var_Distrito);
            }
            provincia_SantiagoDeChuco.add("Santiago De Chuco");
            provincia_SantiagoDeChuco.add("Angasmarca");
            provincia_SantiagoDeChuco.add("Cachicadan");
            provincia_SantiagoDeChuco.add("Mollebamba");
            provincia_SantiagoDeChuco.add("Mollepata");
            provincia_SantiagoDeChuco.add("Quiruvilca");
            provincia_SantiagoDeChuco.add("Santa Cruz De Chuca");
            provincia_SantiagoDeChuco.add("Sitabamba");


            List<String> provincia_GranChimu = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_GranChimu.add("Seleccionar un Distrito");
            }else{
                provincia_GranChimu.add(var_Distrito);
            }
            provincia_GranChimu.add("Cascas");
            provincia_GranChimu.add("Lucma");
            provincia_GranChimu.add("Marmot");
            provincia_GranChimu.add("Sayapullo");


            List<String> provincia_Viru = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Viru.add("Seleccionar un Distrito");
            }else{
                provincia_Viru.add(var_Distrito);
            }
            provincia_Viru.add("Viru");
            provincia_Viru.add("Chao");
            provincia_Viru.add("Guadalupito");


            List<String> provincia_Chiclayo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chiclayo.add("Seleccionar un Distrito");
            }else{
                provincia_Chiclayo.add(var_Distrito);
            }
            provincia_Chiclayo.add("Chiclayo");
            provincia_Chiclayo.add("Chongoyape");
            provincia_Chiclayo.add("Eten");
            provincia_Chiclayo.add("Eten Puerto");
            provincia_Chiclayo.add("Jose Leonardo Ortiz");
            provincia_Chiclayo.add("La Victoria");
            provincia_Chiclayo.add("Lagunas");
            provincia_Chiclayo.add("Monsefu");
            provincia_Chiclayo.add("Nueva Arica");
            provincia_Chiclayo.add("Oyotun");
            provincia_Chiclayo.add("Picsi");
            provincia_Chiclayo.add("Pimentel");
            provincia_Chiclayo.add("Reque");
            provincia_Chiclayo.add("Santa Rosa");
            provincia_Chiclayo.add("Saña");
            provincia_Chiclayo.add("Cayalti");
            provincia_Chiclayo.add("Patapo");
            provincia_Chiclayo.add("Pomalca");
            provincia_Chiclayo.add("Pucala");
            provincia_Chiclayo.add("Tuman");


            List<String> provincia_Ferreñafe = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ferreñafe.add("Seleccionar un Distrito");
            }else{
                provincia_Ferreñafe.add(var_Distrito);
            }
            provincia_Ferreñafe.add("Ferreñafe");
            provincia_Ferreñafe.add("Cañaris");
            provincia_Ferreñafe.add("Incahuasi");
            provincia_Ferreñafe.add("Manuel Antonio Mesones");
            provincia_Ferreñafe.add("o");
            provincia_Ferreñafe.add("Pitipo");
            provincia_Ferreñafe.add("Pueblo Nuevo");


            List<String> provincia_Lambayeque = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lambayeque.add("Seleccionar un Distrito");
            }else{
                provincia_Lambayeque.add(var_Distrito);
            }
            provincia_Lambayeque.add("Lambayeque");
            provincia_Lambayeque.add("Chochope");
            provincia_Lambayeque.add("Illimo");
            provincia_Lambayeque.add("Jayanca");
            provincia_Lambayeque.add("Mochumi");
            provincia_Lambayeque.add("Morrope");
            provincia_Lambayeque.add("Motupe");
            provincia_Lambayeque.add("Olmos");
            provincia_Lambayeque.add("Pacora");
            provincia_Lambayeque.add("Salas");
            provincia_Lambayeque.add("San Jose");
            provincia_Lambayeque.add("Tucume");


            List<String> provincia_Lima = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lima.add("Seleccionar un Distrito");
            }else{
                provincia_Lima.add(var_Distrito);
            }
            provincia_Lima.add("Lima");
            provincia_Lima.add("Ancon");
            provincia_Lima.add("Ate");
            provincia_Lima.add("Barranco");
            provincia_Lima.add("Breña");
            provincia_Lima.add("Carabayllo");
            provincia_Lima.add("Chaclacayo");
            provincia_Lima.add("Chorrillos");
            provincia_Lima.add("Cieneguilla");
            provincia_Lima.add("Comas");
            provincia_Lima.add("El Agustino");
            provincia_Lima.add("Independencia");
            provincia_Lima.add("Jesus Maria");
            provincia_Lima.add("La Molina");
            provincia_Lima.add("La Victoria");
            provincia_Lima.add("Lince");
            provincia_Lima.add("Los Olivos");
            provincia_Lima.add("Lurigancho");
            provincia_Lima.add("Lurin");
            provincia_Lima.add("Magdalena Del Mar");
            provincia_Lima.add("Pueblo Libre");
            provincia_Lima.add("Miraflores");
            provincia_Lima.add("Pachacamac");
            provincia_Lima.add("Pucusana");
            provincia_Lima.add("Puente Piedra");
            provincia_Lima.add("Punta Hermosa");
            provincia_Lima.add("Punta Negra");
            provincia_Lima.add("Rimac");
            provincia_Lima.add("San Bartolo");
            provincia_Lima.add("San Borja");
            provincia_Lima.add("San Isidro");
            provincia_Lima.add("San Juan De Lurigancho");
            provincia_Lima.add("San Juan De Miraflores");
            provincia_Lima.add("San Luis");
            provincia_Lima.add("San Martin De Porres");
            provincia_Lima.add("San Miguel");
            provincia_Lima.add("Santa Anita");
            provincia_Lima.add("Santa Maria Del Mar");
            provincia_Lima.add("Santa Rosa");
            provincia_Lima.add("Santiago De Surco");
            provincia_Lima.add("Surquillo");
            provincia_Lima.add("Villa El Salvador");
            provincia_Lima.add("Villa Maria Del Triunfo");


            List<String> provincia_Barranca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Barranca.add("Seleccionar un Distrito");
            }else{
                provincia_Barranca.add(var_Distrito);
            }
            provincia_Barranca.add("Barranca");
            provincia_Barranca.add("Paramonga");
            provincia_Barranca.add("Pativilca");
            provincia_Barranca.add("Supe");
            provincia_Barranca.add("Supe Puerto");


            List<String> provincia_Cajatambo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cajatambo.add("Seleccionar un Distrito");
            }else{
                provincia_Cajatambo.add(var_Distrito);
            }
            provincia_Cajatambo.add("Cajatambo");
            provincia_Cajatambo.add("Copa");
            provincia_Cajatambo.add("Gorgor");
            provincia_Cajatambo.add("Huancapon");
            provincia_Cajatambo.add("Manas");


            List<String> provincia_Canta = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Canta.add("Seleccionar un Distrito");
            }else{
                provincia_Canta.add(var_Distrito);
            }
            provincia_Canta.add("Canta");
            provincia_Canta.add("Arahuay");
            provincia_Canta.add("Huamantanga");
            provincia_Canta.add("Huaros");
            provincia_Canta.add("Lachaqui");
            provincia_Canta.add("San Buenaventura");
            provincia_Canta.add("Santa Rosa De Quives");


            List<String> provincia_Cañete = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Cañete.add("Seleccionar un Distrito");
            }else{
                provincia_Cañete.add(var_Distrito);
            }
            provincia_Cañete.add("San Vicente De Cañete");
            provincia_Cañete.add("Asia");
            provincia_Cañete.add("Calango");
            provincia_Cañete.add("Cerro Azul");
            provincia_Cañete.add("Chilca");
            provincia_Cañete.add("Coayllo");
            provincia_Cañete.add("Imperial");
            provincia_Cañete.add("Lunahuana");
            provincia_Cañete.add("Mala");
            provincia_Cañete.add("Nuevo Imperial");
            provincia_Cañete.add("Pacaran");
            provincia_Cañete.add("Quilmana");
            provincia_Cañete.add("San Antonio");
            provincia_Cañete.add("San Luis");
            provincia_Cañete.add("Santa Cruz De Flores");
            provincia_Cañete.add("Zuñiga");


            List<String> provincia_Huaral = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huaral.add("Seleccionar un Distrito");
            }else{
                provincia_Huaral.add(var_Distrito);
            }
            provincia_Huaral.add("Huaral");
            provincia_Huaral.add("Atavillos Alto");
            provincia_Huaral.add("Atavillos Bajo");
            provincia_Huaral.add("Aucallama");
            provincia_Huaral.add("Chancay");
            provincia_Huaral.add("Ihuari");
            provincia_Huaral.add("Lampian");
            provincia_Huaral.add("Pacaraos");
            provincia_Huaral.add("San Miguel De Acos");
            provincia_Huaral.add("Santa Cruz De Andamarca");
            provincia_Huaral.add("Sumbilca");
            provincia_Huaral.add("Veintisiete De Noviembre");


            List<String> provincia_Huarochiri = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huarochiri.add("Seleccionar un Distrito");
            }else{
                provincia_Huarochiri.add(var_Distrito);
            }
            provincia_Huarochiri.add("Matucana");
            provincia_Huarochiri.add("Antioquia");
            provincia_Huarochiri.add("Callahuanca");
            provincia_Huarochiri.add("Carampoma");
            provincia_Huarochiri.add("Chicla");
            provincia_Huarochiri.add("Cuenca");
            provincia_Huarochiri.add("Huachupampa");
            provincia_Huarochiri.add("Huanza");
            provincia_Huarochiri.add("Huarochiri");
            provincia_Huarochiri.add("Lahuaytambo");
            provincia_Huarochiri.add("Langa");
            provincia_Huarochiri.add("Laraos");
            provincia_Huarochiri.add("Mariatana");
            provincia_Huarochiri.add("Ricardo Palma");
            provincia_Huarochiri.add("San Andres De Tupicocha");
            provincia_Huarochiri.add("San Antonio");
            provincia_Huarochiri.add("San Bartolome");
            provincia_Huarochiri.add("San Damian");
            provincia_Huarochiri.add("San Juan De Iris");
            provincia_Huarochiri.add("San Juan De Tantaranche");
            provincia_Huarochiri.add("San Lorenzo De Quinti");
            provincia_Huarochiri.add("San Mateo");
            provincia_Huarochiri.add("San Mateo De Otao");
            provincia_Huarochiri.add("San Pedro De Casta");
            provincia_Huarochiri.add("San Pedro De Huancayre");
            provincia_Huarochiri.add("Sangallaya");
            provincia_Huarochiri.add("Santa Cruz De Cocachacra");
            provincia_Huarochiri.add("Santa Eulalia");
            provincia_Huarochiri.add("Santiago De Anchucaya");
            provincia_Huarochiri.add("Santiago De Tuna");
            provincia_Huarochiri.add("Santo Domingo De Los Ollero");
            provincia_Huarochiri.add("Surco");


            List<String> provincia_Huaura = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huaura.add("Seleccionar un Distrito");
            }else{
                provincia_Huaura.add(var_Distrito);
            }
            provincia_Huaura.add("Huacho");
            provincia_Huaura.add("Ambar");
            provincia_Huaura.add("Caleta De Carquin");
            provincia_Huaura.add("Checras");
            provincia_Huaura.add("Hualmay");
            provincia_Huaura.add("Huaura");
            provincia_Huaura.add("Leoncio Prado");
            provincia_Huaura.add("Paccho");
            provincia_Huaura.add("Santa Leonor");
            provincia_Huaura.add("Santa Maria");
            provincia_Huaura.add("Sayan");
            provincia_Huaura.add("Vegueta");


            List<String> provincia_Oyon = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Oyon.add("Seleccionar un Distrito");
            }else{
                provincia_Oyon.add(var_Distrito);
            }
            provincia_Oyon.add("Oyon");
            provincia_Oyon.add("Andajes");
            provincia_Oyon.add("Caujul");
            provincia_Oyon.add("Cochamarca");
            provincia_Oyon.add("Navan");
            provincia_Oyon.add("Pachangara");


            List<String> provincia_Yauyos = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Yauyos.add("Seleccionar un Distrito");
            }else{
                provincia_Yauyos.add(var_Distrito);
            }
            provincia_Yauyos.add("Yauyos");
            provincia_Yauyos.add("Alis");
            provincia_Yauyos.add("Ayauca");
            provincia_Yauyos.add("Ayaviri");
            provincia_Yauyos.add("Azangaro");
            provincia_Yauyos.add("Cacra");
            provincia_Yauyos.add("Carania");
            provincia_Yauyos.add("Catahuasi");
            provincia_Yauyos.add("Chocos");
            provincia_Yauyos.add("Cochas");
            provincia_Yauyos.add("Colonia");
            provincia_Yauyos.add("Hongos");
            provincia_Yauyos.add("Huampara");
            provincia_Yauyos.add("Huancaya");
            provincia_Yauyos.add("Huangascar");
            provincia_Yauyos.add("Huantan");
            provincia_Yauyos.add("Huaðec");
            provincia_Yauyos.add("Laraos");
            provincia_Yauyos.add("Lincha");
            provincia_Yauyos.add("Madean");
            provincia_Yauyos.add("Miraflores");
            provincia_Yauyos.add("Omas");
            provincia_Yauyos.add("Putinza");
            provincia_Yauyos.add("Quinches");
            provincia_Yauyos.add("Quinocay");
            provincia_Yauyos.add("San Joaquin");
            provincia_Yauyos.add("San Pedro De Pilas");
            provincia_Yauyos.add("Tanta");
            provincia_Yauyos.add("Tauripampa");
            provincia_Yauyos.add("Tomas");
            provincia_Yauyos.add("Tupe");
            provincia_Yauyos.add("Viñac");
            provincia_Yauyos.add("Vitis");


            List<String> provincia_Maynas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Maynas.add("Seleccionar un Distrito");
            }else{
                provincia_Maynas.add(var_Distrito);
            }
            provincia_Maynas.add("Iquitos");
            provincia_Maynas.add("Alto Nanay");
            provincia_Maynas.add("Fernando Lores");
            provincia_Maynas.add("Indiana");
            provincia_Maynas.add("Las Amazonas");
            provincia_Maynas.add("Mazan");
            provincia_Maynas.add("Napo");
            provincia_Maynas.add("Punchana");
            provincia_Maynas.add("Putumayo");
            provincia_Maynas.add("Torres Causana");
            provincia_Maynas.add("Belen");
            provincia_Maynas.add("San Juan Bautista");


            List<String> provincia_AltoAmazonas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_AltoAmazonas.add("Seleccionar un Distrito");
            }else{
                provincia_AltoAmazonas.add(var_Distrito);
            }
            provincia_AltoAmazonas.add("Yurimaguas");
            provincia_AltoAmazonas.add("Balsapuerto");
            provincia_AltoAmazonas.add("Barranca");
            provincia_AltoAmazonas.add("Cahuapanas");
            provincia_AltoAmazonas.add("Jeberos");
            provincia_AltoAmazonas.add("Lagunas");
            provincia_AltoAmazonas.add("Manseriche");
            provincia_AltoAmazonas.add("Morona");
            provincia_AltoAmazonas.add("Pastaza");
            provincia_AltoAmazonas.add("Santa Cruz");
            provincia_AltoAmazonas.add("Teniente Cesar Lopez Rojas");


            List<String> provincia_Loreto = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Loreto.add("Seleccionar un Distrito");
            }else{
                provincia_Loreto.add(var_Distrito);
            }
            provincia_Loreto.add("Nauta");
            provincia_Loreto.add("Parinari");
            provincia_Loreto.add("Tigre");
            provincia_Loreto.add("Trompeteros");
            provincia_Loreto.add("Urarinas");


            List<String> provincia_MariscalRamonCastilla = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_MariscalRamonCastilla.add("Seleccionar un Distrito");
            }else{
                provincia_MariscalRamonCastilla.add(var_Distrito);
            }
            provincia_MariscalRamonCastilla.add("Ramon Castilla");
            provincia_MariscalRamonCastilla.add("Pebas");
            provincia_MariscalRamonCastilla.add("Yavari");
            provincia_MariscalRamonCastilla.add("San Pablo");


            List<String> provincia_Requena = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Requena.add("Seleccionar un Distrito");
            }else{
                provincia_Requena.add(var_Distrito);
            }
            provincia_Requena.add("Requena");
            provincia_Requena.add("Alto Tapiche");
            provincia_Requena.add("Capelo");
            provincia_Requena.add("Emilio San Martin");
            provincia_Requena.add("Maquia");
            provincia_Requena.add("Puinahua");
            provincia_Requena.add("Saquena");
            provincia_Requena.add("Soplin");
            provincia_Requena.add("Tapiche");
            provincia_Requena.add("Jenaro Herrera");
            provincia_Requena.add("Yaquerana");


            List<String> provincia_Ucayali = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ucayali.add("Seleccionar un Distrito");
            }else{
                provincia_Ucayali.add(var_Distrito);
            }
            provincia_Ucayali.add("Contamana");
            provincia_Ucayali.add("Inahuaya");
            provincia_Ucayali.add("Padre Marquez");
            provincia_Ucayali.add("Pampa Hermosa");
            provincia_Ucayali.add("Sarayacu");
            provincia_Ucayali.add("Vargas Guerra");


            List<String> provincia_Tambopata = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tambopata.add("Seleccionar un Distrito");
            }else{
                provincia_Tambopata.add(var_Distrito);
            }
            provincia_Tambopata.add("Tambopata");
            provincia_Tambopata.add("Inambari");
            provincia_Tambopata.add("Las Piedras");
            provincia_Tambopata.add("Laberinto");


            List<String> provincia_Manu = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Manu.add("Seleccionar un Distrito");
            }else{
                provincia_Manu.add(var_Distrito);
            }
            provincia_Manu.add("Manu");
            provincia_Manu.add("Fitzcarrald");
            provincia_Manu.add("Madre De Dios");
            provincia_Manu.add("Huepetuhe");


            List<String> provincia_Tahuamanu = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tahuamanu.add("Seleccionar un Distrito");
            }else{
                provincia_Tahuamanu.add(var_Distrito);
            }
            provincia_Tahuamanu.add("Iñapari");
            provincia_Tahuamanu.add("Iberia");
            provincia_Tahuamanu.add("Tahuamanu");


            List<String> provincia_MariscalNieto = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_MariscalNieto.add("Seleccionar un Distrito");
            }else{
                provincia_MariscalNieto.add(var_Distrito);
            }
            provincia_MariscalNieto.add("Moquegua");
            provincia_MariscalNieto.add("Carumas");
            provincia_MariscalNieto.add("Cuchumbaya");
            provincia_MariscalNieto.add("Samegua");
            provincia_MariscalNieto.add("San Cristobal");
            provincia_MariscalNieto.add("Torata");


            List<String> provincia_GeneralSanchezCerro = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_GeneralSanchezCerro.add("Seleccionar un Distrito");
            }else{
                provincia_GeneralSanchezCerro.add(var_Distrito);
            }
            provincia_GeneralSanchezCerro.add("Omate");
            provincia_GeneralSanchezCerro.add("Chojata");
            provincia_GeneralSanchezCerro.add("Coalaque");
            provincia_GeneralSanchezCerro.add("Ichuña");
            provincia_GeneralSanchezCerro.add("La Capilla");
            provincia_GeneralSanchezCerro.add("Lloque");
            provincia_GeneralSanchezCerro.add("Matalaque");
            provincia_GeneralSanchezCerro.add("Puquina");
            provincia_GeneralSanchezCerro.add("Quinistaquillas");
            provincia_GeneralSanchezCerro.add("Ubinas");
            provincia_GeneralSanchezCerro.add("Yunga");


            List<String> provincia_Ilo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ilo.add("Seleccionar un Distrito");
            }else{
                provincia_Ilo.add(var_Distrito);
            }
            provincia_Ilo.add("Ilo");
            provincia_Ilo.add("El Algarrobal");
            provincia_Ilo.add("Pacocha");


            List<String> provincia_Pasco = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Pasco.add("Seleccionar un Distrito");
            }else{
                provincia_Pasco.add(var_Distrito);
            }
            provincia_Pasco.add("Chaupimarca");
            provincia_Pasco.add("Huachon");
            provincia_Pasco.add("Huariaca");
            provincia_Pasco.add("Huayllay");
            provincia_Pasco.add("Ninacaca");
            provincia_Pasco.add("Pallanchacra");
            provincia_Pasco.add("Paucartambo");
            provincia_Pasco.add("San Fco.De As is De Yarusyac");
            provincia_Pasco.add("Simon Bolivar");
            provincia_Pasco.add("Ticlacayan");
            provincia_Pasco.add("Tinyahuarco");
            provincia_Pasco.add("Vicco");
            provincia_Pasco.add("Yanacancha");


            List<String> provincia_DanielAlcidesCarrion = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_DanielAlcidesCarrion.add("Seleccionar un Distrito");
            }else{
                provincia_DanielAlcidesCarrion.add(var_Distrito);
            }
            provincia_DanielAlcidesCarrion.add("Yanahuanca");
            provincia_DanielAlcidesCarrion.add("Chacayan");
            provincia_DanielAlcidesCarrion.add("Goyllarisquizga");
            provincia_DanielAlcidesCarrion.add("Paucar");
            provincia_DanielAlcidesCarrion.add("San Pedro De Pillao");
            provincia_DanielAlcidesCarrion.add("Santa Ana De Tusi");
            provincia_DanielAlcidesCarrion.add("Tapuc");
            provincia_DanielAlcidesCarrion.add("Vilcabamba");


            List<String> provincia_Oxapampa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Oxapampa.add("Seleccionar un Distrito");
            }else{
                provincia_Oxapampa.add(var_Distrito);
            }
            provincia_Oxapampa.add("Oxapampa");
            provincia_Oxapampa.add("Chontabamba");
            provincia_Oxapampa.add("Huancabamba");
            provincia_Oxapampa.add("Palcazu");
            provincia_Oxapampa.add("Pozuzo");
            provincia_Oxapampa.add("Puerto Bermudez");
            provincia_Oxapampa.add("Villa Rica");


            List<String> provincia_Piura = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Piura.add("Seleccionar un Distrito");
            }else{
                provincia_Piura.add(var_Distrito);
            }
            provincia_Piura.add("Piura");
            provincia_Piura.add("Castilla");
            provincia_Piura.add("Catacaos");
            provincia_Piura.add("Cura Mori");
            provincia_Piura.add("El Tallan");
            provincia_Piura.add("La Arena");
            provincia_Piura.add("La Union");
            provincia_Piura.add("Las Lomas");
            provincia_Piura.add("Tambo Grande");


            List<String> provincia_Ayabaca = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Ayabaca.add("Seleccionar un Distrito");
            }else{
                provincia_Ayabaca.add(var_Distrito);
            }
            provincia_Ayabaca.add("Ayabaca");
            provincia_Ayabaca.add("Frias");
            provincia_Ayabaca.add("Jilili");
            provincia_Ayabaca.add("Lagunas");
            provincia_Ayabaca.add("Montero");
            provincia_Ayabaca.add("Pacaipampa");
            provincia_Ayabaca.add("Paimas");
            provincia_Ayabaca.add("Sapillica");
            provincia_Ayabaca.add("Sicchez");
            provincia_Ayabaca.add("Suyo");


            List<String> provincia_Huancabamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huancabamba.add("Seleccionar un Distrito");
            }else{
                provincia_Huancabamba.add(var_Distrito);
            }
            provincia_Huancabamba.add("Huancabamba");
            provincia_Huancabamba.add("Canchaque");
            provincia_Huancabamba.add("El Carmen De La Frontera");
            provincia_Huancabamba.add("Huarmaca");
            provincia_Huancabamba.add("Lalaquiz");
            provincia_Huancabamba.add("San Miguel De El Faique");
            provincia_Huancabamba.add("Sondor");
            provincia_Huancabamba.add("Sondorillo");


            List<String> provincia_Morropon = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Morropon.add("Seleccionar un Distrito");
            }else{
                provincia_Morropon.add(var_Distrito);
            }
            provincia_Morropon.add("Chulucanas");
            provincia_Morropon.add("Buenos Aires");
            provincia_Morropon.add("Chalaco");
            provincia_Morropon.add("La Matanza");
            provincia_Morropon.add("Morropon");
            provincia_Morropon.add("Salitral");
            provincia_Morropon.add("San Juan De Bigote");
            provincia_Morropon.add("Santa Catalina De Mossa");
            provincia_Morropon.add("Santo Domingo");
            provincia_Morropon.add("Yamango");


            List<String> provincia_Paita = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Paita.add("Seleccionar un Distrito");
            }else{
                provincia_Paita.add(var_Distrito);
            }
            provincia_Paita.add("Paita");
            provincia_Paita.add("Amotape");
            provincia_Paita.add("Arenal");
            provincia_Paita.add("Colan");
            provincia_Paita.add("La Huaca");
            provincia_Paita.add("Tamarindo");
            provincia_Paita.add("Vichayal");


            List<String> provincia_Sullana = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Sullana.add("Seleccionar un Distrito");
            }else{
                provincia_Sullana.add(var_Distrito);
            }
            provincia_Sullana.add("Sullana");
            provincia_Sullana.add("Bellavista");
            provincia_Sullana.add("Ignacio Escudero");
            provincia_Sullana.add("Lancones");
            provincia_Sullana.add("Marcavelica");
            provincia_Sullana.add("Miguel Checa");
            provincia_Sullana.add("Querecotillo");
            provincia_Sullana.add("Salitral");


            List<String> provincia_Talara = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Talara.add("Seleccionar un Distrito");
            }else{
                provincia_Talara.add(var_Distrito);
            }
            provincia_Talara.add("Pariñas");
            provincia_Talara.add("El Alto");
            provincia_Talara.add("La Brea");
            provincia_Talara.add("Lobitos");
            provincia_Talara.add("Los Organos");
            provincia_Talara.add("Mancora");


            List<String> provincia_Sechura = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Sechura.add("Seleccionar un Distrito");
            }else{
                provincia_Sechura.add(var_Distrito);
            }
            provincia_Sechura.add("Sechura");
            provincia_Sechura.add("Bellavista De La Union");
            provincia_Sechura.add("Bernal");
            provincia_Sechura.add("Cristo Nos Valga");
            provincia_Sechura.add("Vice");
            provincia_Sechura.add("Rinconada Llicuar");


            List<String> provincia_Puno = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Puno.add("Seleccionar un Distrito");
            }else{
                provincia_Puno.add(var_Distrito);
            }
            provincia_Puno.add("Puno");
            provincia_Puno.add("Acora");
            provincia_Puno.add("Amantani");
            provincia_Puno.add("Atuncolla");
            provincia_Puno.add("Capachica");
            provincia_Puno.add("Chucuito");
            provincia_Puno.add("Coata");
            provincia_Puno.add("Huata");
            provincia_Puno.add("Mañazo");
            provincia_Puno.add("Paucarcolla");
            provincia_Puno.add("Pichacani");
            provincia_Puno.add("Plateria");
            provincia_Puno.add("San Antonio");
            provincia_Puno.add("Tiquillaca");
            provincia_Puno.add("Vilque");


            List<String> provincia_Azangaro = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Azangaro.add("Seleccionar un Distrito");
            }else{
                provincia_Azangaro.add(var_Distrito);
            }
            provincia_Azangaro.add("Azangaro");
            provincia_Azangaro.add("Achaya");
            provincia_Azangaro.add("Arapa");
            provincia_Azangaro.add("Asillo");
            provincia_Azangaro.add("Caminaca");
            provincia_Azangaro.add("Chupa");
            provincia_Azangaro.add("Jose Domingo Choquehuanca");
            provincia_Azangaro.add("Muñani");
            provincia_Azangaro.add("Potoni");
            provincia_Azangaro.add("Saman");
            provincia_Azangaro.add("San Anton");
            provincia_Azangaro.add("San Jose");
            provincia_Azangaro.add("San Juan De Salinas");
            provincia_Azangaro.add("Santiago De Pupuja");
            provincia_Azangaro.add("Tirapata");


            List<String> provincia_Carabaya = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Carabaya.add("Seleccionar un Distrito");
            }else{
                provincia_Carabaya.add(var_Distrito);
            }
            provincia_Carabaya.add("Macusani");
            provincia_Carabaya.add("Ajoyani");
            provincia_Carabaya.add("Ayapata");
            provincia_Carabaya.add("Coasa");
            provincia_Carabaya.add("Corani");
            provincia_Carabaya.add("Crucero");
            provincia_Carabaya.add("Ituata");
            provincia_Carabaya.add("Ollachea");
            provincia_Carabaya.add("San Gaban");
            provincia_Carabaya.add("Usicayos");


            List<String> provincia_Chucuito = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Chucuito.add("Seleccionar un Distrito");
            }else{
                provincia_Chucuito.add(var_Distrito);
            }
            provincia_Chucuito.add("Juli");
            provincia_Chucuito.add("Desaguadero");
            provincia_Chucuito.add("Huacullani");
            provincia_Chucuito.add("Kelluyo");
            provincia_Chucuito.add("Pisacoma");
            provincia_Chucuito.add("Pomata");
            provincia_Chucuito.add("Zepita");


            List<String> provincia_ElCollao = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_ElCollao.add("Seleccionar un Distrito");
            }else{
                provincia_ElCollao.add(var_Distrito);
            }
            provincia_ElCollao.add("Ilave");
            provincia_ElCollao.add("Capazo");
            provincia_ElCollao.add("Pilcuyo");
            provincia_ElCollao.add("Santa Rosa");
            provincia_ElCollao.add("Conduriri");

            List<String> provincia_Huancane = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huancane.add("Seleccionar un Distrito");
            }else{
                provincia_Huancane.add(var_Distrito);
            }
            provincia_Huancane.add("Huancane");
            provincia_Huancane.add("Cojata");
            provincia_Huancane.add("Huatasani");
            provincia_Huancane.add("Inchupalla");
            provincia_Huancane.add("Pusi");
            provincia_Huancane.add("Rosaspata");
            provincia_Huancane.add("Taraco");
            provincia_Huancane.add("Vilque Chico");

            List<String> provincia_Lampa = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lampa.add("Seleccionar un Distrito");
            }else{
                provincia_Lampa.add(var_Distrito);
            }
            provincia_Lampa.add("Lampa");
            provincia_Lampa.add("Cabanilla");
            provincia_Lampa.add("Calapuja");
            provincia_Lampa.add("Nicasio");
            provincia_Lampa.add("Ocuviri");
            provincia_Lampa.add("Palca");
            provincia_Lampa.add("Paratia");
            provincia_Lampa.add("Pucara");
            provincia_Lampa.add("Santa Lucia");
            provincia_Lampa.add("Vilavila");


            List<String> provincia_Melgar = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Melgar.add("Seleccionar un Distrito");
            }else{
                provincia_Melgar.add(var_Distrito);
            }
            provincia_Melgar.add("Ayaviri");
            provincia_Melgar.add("Antauta");
            provincia_Melgar.add("Cupi");
            provincia_Melgar.add("Llalli");
            provincia_Melgar.add("Macari");
            provincia_Melgar.add("Nuñoa");
            provincia_Melgar.add("Orurillo");
            provincia_Melgar.add("Santa Rosa");
            provincia_Melgar.add("Umachiri");


            List<String> provincia_Moho = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Moho.add("Seleccionar un Distrito");
            }else{
                provincia_Moho.add(var_Distrito);
            }
            provincia_Moho.add("Moho");
            provincia_Moho.add("Conima");
            provincia_Moho.add("Huayrapata");
            provincia_Moho.add("Tilali");


            List<String> provincia_SanAntonioDePutina = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanAntonioDePutina.add("Seleccionar un Distrito");
            }else{
                provincia_SanAntonioDePutina.add(var_Distrito);
            }
            provincia_SanAntonioDePutina.add("Putina");
            provincia_SanAntonioDePutina.add("Ananea");
            provincia_SanAntonioDePutina.add("Pedro Vilca Apaza");
            provincia_SanAntonioDePutina.add("Quilcapuncu");
            provincia_SanAntonioDePutina.add("Sina");


            List<String> provincia_SanRoman = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanRoman.add("Seleccionar un Distrito");
            }else{
                provincia_SanRoman.add(var_Distrito);
            }
            provincia_SanRoman.add("Juliaca");
            provincia_SanRoman.add("Cabana");
            provincia_SanRoman.add("Cabanillas");
            provincia_SanRoman.add("Caracoto");


            List<String> provincia_Sandia = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Sandia.add("Seleccionar un Distrito");
            }else{
                provincia_Sandia.add(var_Distrito);
            }
            provincia_Sandia.add("Sandia");
            provincia_Sandia.add("Cuyocuyo");
            provincia_Sandia.add("Limbani");
            provincia_Sandia.add("Patambuco");
            provincia_Sandia.add("Phara");
            provincia_Sandia.add("Quiaca");
            provincia_Sandia.add("San Juan Del Oro");
            provincia_Sandia.add("Yanahuaya");
            provincia_Sandia.add("Alto Inambari");


            List<String> provincia_Yunguyo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Yunguyo.add("Seleccionar un Distrito");
            }else{
                provincia_Yunguyo.add(var_Distrito);
            }
            provincia_Yunguyo.add("Yunguyo");
            provincia_Yunguyo.add("Anapia");
            provincia_Yunguyo.add("Copani");
            provincia_Yunguyo.add("Cuturapi");
            provincia_Yunguyo.add("Ollaraya");
            provincia_Yunguyo.add("Tinicachi");
            provincia_Yunguyo.add("Unicachi");


            List<String> provincia_Moyobamba = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Moyobamba.add("Seleccionar un Distrito");
            }else{
                provincia_Moyobamba.add(var_Distrito);
            }
            provincia_Moyobamba.add("Moyobamba");
            provincia_Moyobamba.add("Calzada");
            provincia_Moyobamba.add("Habana");
            provincia_Moyobamba.add("Jepelacio");
            provincia_Moyobamba.add("Soritor");
            provincia_Moyobamba.add("Yantalo");


            List<String> provincia_Bellavista = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Bellavista.add("Seleccionar un Distrito");
            }else{
                provincia_Bellavista.add(var_Distrito);
            }
            provincia_Bellavista.add("Bellavista");
            provincia_Bellavista.add("Alto Biavo");
            provincia_Bellavista.add("Bajo Biavo");
            provincia_Bellavista.add("Huallaga");
            provincia_Bellavista.add("San Pablo");
            provincia_Bellavista.add("San Rafael");


            List<String> provincia_ElDorado = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_ElDorado.add("Seleccionar un Distrito");
            }else{
                provincia_ElDorado.add(var_Distrito);
            }
            provincia_ElDorado.add("San Jose De Sisa");
            provincia_ElDorado.add("Agua Blanca");
            provincia_ElDorado.add("San Martin");
            provincia_ElDorado.add("Santa Rosa");
            provincia_ElDorado.add("Shatoja");


            List<String> provincia_Huallaga = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Huallaga.add("Seleccionar un Distrito");
            }else{
                provincia_Huallaga.add(var_Distrito);
            }
            provincia_Huallaga.add("Saposoa");
            provincia_Huallaga.add("Alto Saposoa");
            provincia_Huallaga.add("El Eslabon");
            provincia_Huallaga.add("Piscoyacu");
            provincia_Huallaga.add("Sacanche");
            provincia_Huallaga.add("Tingo De Saposoa");


            List<String> provincia_Lamas = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Lamas.add("Seleccionar un Distrito");
            }else{
                provincia_Lamas.add(var_Distrito);
            }
            provincia_Lamas.add("Lamas");
            provincia_Lamas.add("Alonso De Alvarado");
            provincia_Lamas.add("Barranquita");
            provincia_Lamas.add("Caynarachi 1/");
            provincia_Lamas.add("Cuñumbuqui");
            provincia_Lamas.add("Pinto Recodo");
            provincia_Lamas.add("Rumisapa");
            provincia_Lamas.add("San Roque De Cumbaza");
            provincia_Lamas.add("Shanao");
            provincia_Lamas.add("Tabalosos");
            provincia_Lamas.add("Zapatero");


            List<String> provincia_MariscalCaceres = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_MariscalCaceres.add("Seleccionar un Distrito");
            }else{
                provincia_MariscalCaceres.add(var_Distrito);
            }
            provincia_MariscalCaceres.add("Juanjui");
            provincia_MariscalCaceres.add("Campanilla");
            provincia_MariscalCaceres.add("Huicungo");
            provincia_MariscalCaceres.add("Pachiza");
            provincia_MariscalCaceres.add("Pajarillo");


            List<String> provincia_Picota = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Picota.add("Seleccionar un Distrito");
            }else{
                provincia_Picota.add(var_Distrito);
            }
            provincia_Picota.add("Picota");
            provincia_Picota.add("Buenos Aires");
            provincia_Picota.add("Caspisapa");
            provincia_Picota.add("Pilluana");
            provincia_Picota.add("Pucacaca");
            provincia_Picota.add("San Cristobal");
            provincia_Picota.add("San Hilarion");
            provincia_Picota.add("Shamboyacu");
            provincia_Picota.add("Tingo De Ponasa");
            provincia_Picota.add("Tres Unidos");


            List<String> provincia_Rioja = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Rioja.add("Seleccionar un Distrito");
            }else{
                provincia_Rioja.add(var_Distrito);
            }
            provincia_Rioja.add("Rioja");
            provincia_Rioja.add("Awajun");
            provincia_Rioja.add("Elias Soplin Vargas");
            provincia_Rioja.add("Nueva Cajamarca");
            provincia_Rioja.add("Pardo Miguel");
            provincia_Rioja.add("Posic");
            provincia_Rioja.add("San Fernando");
            provincia_Rioja.add("Yorongos");
            provincia_Rioja.add("Yuracyacu");


            List<String> provincia_SanMartin = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_SanMartin.add("Seleccionar un Distrito");
            }else{
                provincia_SanMartin.add(var_Distrito);
            }
            provincia_SanMartin.add("Tarapoto");
            provincia_SanMartin.add("Alberto Leveau");
            provincia_SanMartin.add("Cacatachi");
            provincia_SanMartin.add("Chazuta");
            provincia_SanMartin.add("Chipurana");
            provincia_SanMartin.add("El Porvenir");
            provincia_SanMartin.add("Huimbayoc");
            provincia_SanMartin.add("Juan Guerra");
            provincia_SanMartin.add("La Banda De Shilcayo");
            provincia_SanMartin.add("Morales");
            provincia_SanMartin.add("Papaplaya");
            provincia_SanMartin.add("San Antonio");
            provincia_SanMartin.add("Sauce");
            provincia_SanMartin.add("Shapaja");


            List<String> provincia_Tocache = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tocache.add("Seleccionar un Distrito");
            }else{
                provincia_Tocache.add(var_Distrito);
            }
            provincia_Tocache.add("Tocache");
            provincia_Tocache.add("Nuevo Progreso");
            provincia_Tocache.add("Polvora");
            provincia_Tocache.add("Shunte");
            provincia_Tocache.add("Uchiza");


            List<String> provincia_Tacna = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tacna.add("Seleccionar un Distrito");
            }else{
                provincia_Tacna.add(var_Distrito);
            }
            provincia_Tacna.add("Tacna");
            provincia_Tacna.add("Alto De La Alianza");
            provincia_Tacna.add("Calana");
            provincia_Tacna.add("Ciudad Nueva");
            provincia_Tacna.add("Inclan");
            provincia_Tacna.add("Pachia");
            provincia_Tacna.add("Palca");
            provincia_Tacna.add("Pocollay");
            provincia_Tacna.add("Sama");
            provincia_Tacna.add("Coronel Gregorio Albarracín Lanchipa");


            List<String> provincia_Candarave = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Candarave.add("Seleccionar un Distrito");
            }else{
                provincia_Candarave.add(var_Distrito);
            }
            provincia_Candarave.add("Candarave");
            provincia_Candarave.add("Cairani");
            provincia_Candarave.add("Camilaca");
            provincia_Candarave.add("Curibaya");
            provincia_Candarave.add("Huanuara");
            provincia_Candarave.add("Quilahuani");


            List<String> provincia_JorgeBasadre = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_JorgeBasadre.add("Seleccionar un Distrito");
            }else{
                provincia_JorgeBasadre.add(var_Distrito);
            }
            provincia_JorgeBasadre.add("Locumba");
            provincia_JorgeBasadre.add("Ilabaya");
            provincia_JorgeBasadre.add("Ite");


            List<String> provincia_Tarata = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tarata.add("Seleccionar un Distrito");
            }else{
                provincia_Tarata.add(var_Distrito);
            }
            provincia_Tarata.add("Tarata");
            provincia_Tarata.add("Chucatamani");
            provincia_Tarata.add("Estique");
            provincia_Tarata.add("Estique-Pampa");
            provincia_Tarata.add("Sitajara");
            provincia_Tarata.add("Susapaya");
            provincia_Tarata.add("Tarucachi");
            provincia_Tarata.add("Ticaco");


            List<String> provincia_Tumbes = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Tumbes.add("Seleccionar un Distrito");
            }else{
                provincia_Tumbes.add(var_Distrito);
            }
            provincia_Tumbes.add("Tumbes");
            provincia_Tumbes.add("Corrales");
            provincia_Tumbes.add("La Cruz");
            provincia_Tumbes.add("Pampas De Hospital");
            provincia_Tumbes.add("San Jacinto");
            provincia_Tumbes.add("San Juan De La Virgen");


            List<String> provincia_ContralmiranteVillar = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_ContralmiranteVillar.add("Seleccionar un Distrito");
            }else{
                provincia_ContralmiranteVillar.add(var_Distrito);
            }
            provincia_ContralmiranteVillar.add("Zorritos");
            provincia_ContralmiranteVillar.add("Casitas");


            List<String> provincia_Zarumilla = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Zarumilla.add("Seleccionar un Distrito");
            }else{
                provincia_Zarumilla.add(var_Distrito);
            }
            provincia_Zarumilla.add("Zarumilla");
            provincia_Zarumilla.add("Aguas Verdes");
            provincia_Zarumilla.add("Matapalo");
            provincia_Zarumilla.add("Papayal");


            List<String> provincia_CoronelPortillo = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_CoronelPortillo.add("Seleccionar un Distrito");
            }else{
                provincia_CoronelPortillo.add(var_Distrito);
            }
            provincia_CoronelPortillo.add("Calleria");
            provincia_CoronelPortillo.add("Campoverde");
            provincia_CoronelPortillo.add("Iparia");
            provincia_CoronelPortillo.add("Masisea");
            provincia_CoronelPortillo.add("Yarinacocha");
            provincia_CoronelPortillo.add("Nueva Requena");


            List<String> provincia_Atalaya = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Atalaya.add("Seleccionar un Distrito");
            }else{
                provincia_Atalaya.add(var_Distrito);
            }
            provincia_Atalaya.add("Raymondi");
            provincia_Atalaya.add("Sepahua");
            provincia_Atalaya.add("Tahuania");
            provincia_Atalaya.add("Yurua");


            List<String> provincia_PadreAbad = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_PadreAbad.add("Seleccionar un Distrito");
            }else{
                provincia_PadreAbad.add(var_Distrito);
            }
            provincia_PadreAbad.add("Padre Abad");
            provincia_PadreAbad.add("Irazola");
            provincia_PadreAbad.add("Curimana");


            List<String> provincia_Purus = new ArrayList<String>();
            if(var_Distrito.isEmpty()){
                provincia_Purus.add("Seleccionar un Distrito");
            }else{
                provincia_Purus.add(var_Distrito);
            }
            provincia_Purus.add("Puru");




            parent.getItemAtPosition(pos);

            if (pos != 0) {

                if(parent.getItemAtPosition(pos).toString().equals("Abancay")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Abancay);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                }else  if(parent.getItemAtPosition(pos).toString().equals("Acobamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Acobamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                }else  if(parent.getItemAtPosition(pos).toString().equals("Acomayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Acomayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Aija")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Aija);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Alto Amazonas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_AltoAmazonas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ambo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ambo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Andahuaylas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Andahuaylas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Angaraes")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Angaraes);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Anta")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Anta);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Antabamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Antabamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Antonio Raymondi")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_AntonioRaymondi);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Arequipa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Arequipa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ascope")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ascope);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Asuncion")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Asuncion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Atalaya")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Atalaya);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ayabaca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ayabaca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Aymaraes")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Aymaraes);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Azangaro")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Azangaro);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Bagua")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Bagua);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Barranca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Barranca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Bellavista")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Bellavista);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Bolivar")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Bolivar);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Bolognesi")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Bolognesi);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Bongara")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Bongara);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cajabamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cajabamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cajamarca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cajamarca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cajatambo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cajatambo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Calca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Calca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Callao")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Callao);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Camana")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Camana);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Canas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Canas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Canchis")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Canchis);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Candarave")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Candarave);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cangallo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cangallo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Canta")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Canta);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cañete")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cañete);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Carabaya")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Carabaya);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Caraveli")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Caraveli);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Carhuaz")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Carhuaz);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Carlos F. Fitzcarrald")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_CarlosFFitzcarrald);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Casma")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Casma);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Castilla")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Castilla);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Castrovirreyna")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Castrovirreyna);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Caylloma")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Caylloma);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Celendin")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Celendin);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chachapoyas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chachapoyas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chanchamayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chanchamayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chepen")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chepen);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chiclayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chiclayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chincha")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chincha);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chincheros")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chincheros);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chota")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chota);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chucuito")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chucuito);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chumbivilcas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chumbivilcas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Chupaca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Chupaca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Churcampa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Churcampa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Concepcion")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Concepcion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Condesuyos")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Condesuyos);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Condorcanqui")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Condorcanqui);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Contralmirante Villar")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_ContralmiranteVillar);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Contumaza")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Contumaza);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Coronel Portillo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_CoronelPortillo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Corongo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Corongo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cotabambas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cotabambas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cusco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cusco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Cutervo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Cutervo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Daniel Alcides Carrion")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_DanielAlcidesCarrion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Dos De Mayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_DosDeMayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("El Collao")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_ElCollao);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("El Dorado")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_ElDorado);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Espinar")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Espinar);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ferreñafe")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ferreñafe);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("General Sanchez Cerro")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_GeneralSanchezCerro);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Gran Chimu")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_GranChimu);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Grau")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Grau);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huacaybamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huacaybamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Hualgayoc")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Hualgayoc);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huallaga")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huallaga);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huamalies")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huamalies);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huamanga")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huamanga);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huanca Sancos")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_HuancaSancos);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huancabamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huancabamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huancane")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huancane);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huancavelica")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huancavelica);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huancayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huancayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huanta")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huanta);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huanuco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huanuco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huaral")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huaral);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huaraz")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huaraz);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huari")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huari);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huarmey")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huarmey);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huarochiri")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huarochiri);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huaura")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huaura);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huaylas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huaylas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Huaytara")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Huaytara);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ica")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ica);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ilo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ilo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Islay")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Islay);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Jaen")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Jaen);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Jauja")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Jauja);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Jorge Basadre")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_JorgeBasadre);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Julcan")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Julcan);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Junin")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Junin);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("La Convencion")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_LaConvencion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("La Mar")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_LaMar);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("La Union")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_LaUnion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lamas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lamas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lambayeque")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lambayeque);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lampa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lampa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lauricocha")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lauricocha);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Leoncio Prado")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_LeoncioPrado);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lima")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lima);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Loreto")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Loreto);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Lucanas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Lucanas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Luya")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Luya);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Manu")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Manu);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Marañon")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Marañon);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Mariscal Caceres")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_MariscalCaceres);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Mariscal Luzuriaga")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_MariscalLuzuriaga);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Mariscal Nieto")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_MariscalNieto);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Mariscal Ramon Castilla")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_MariscalRamonCastilla);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Maynas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Maynas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Melgar")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Melgar);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Moho")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Moho);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Morropon")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Morropon);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Moyobamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Moyobamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Nazca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Nazca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ocros")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ocros);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Otuzco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Otuzco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Oxapampa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Oxapampa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Oyon")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Oyon);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pacasmayo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pacasmayo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pachitea")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pachitea);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Padre Abad")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_PadreAbad);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Paita")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Paita);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pallasca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pallasca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Palpa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Palpa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Parinacochas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Parinacochas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Paruro")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Paruro);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pasco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pasco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pataz")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pataz);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Paucar Del Sara Sara")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_PaucarDelSaraSara);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Paucartambo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Paucartambo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Picota")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Picota);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pisco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pisco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Piura")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Piura);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Pomabamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Pomabamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Puerto Inca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_PuertoInca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Puno")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Puno);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Purus")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Purus);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Quispicanchi")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Quispicanchi);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Recuay")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Recuay);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Requena")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Requena);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Rioja")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Rioja);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Rodriguez De Mendo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_RodriguezDeMendo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Antonio De Putina")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanAntonioDePutina);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Ignacio")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanIgnacio);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Marcos")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanMarcos);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Martin")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanMartin);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Miguel")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanMiguel);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Pablo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanPablo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("San Roman")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanRoman);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sanchez Carrion")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SanchezCarrion);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sandia")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Sandia);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Santa")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Santa);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Santa Cruz")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SantaCruz);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Santiago De Chuco")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_SantiagoDeChuco);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Satipo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Satipo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sechura")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Sechura);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sihuas")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Sihuas);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sucre")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Sucre);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Sullana")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Sullana);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tacna")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tacna);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tahuamanu")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tahuamanu);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Talara")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Talara);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tambopata")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tambopata);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tarata")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tarata);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tarma")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tarma);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tayacaja")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tayacaja);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tocache")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tocache);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Trujillo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Trujillo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Tumbes")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Tumbes);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Ucayali")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Ucayali);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Urubamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Urubamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Utcubamba")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Utcubamba);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Victor Fajardo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_VictorFajardo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Vilcas Huaman")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_VilcasHuaman);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Viru")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Viru);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Yarowilca")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Yarowilca);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Yauli")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Yauli);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Yauyos")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Yauyos);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Yungay")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Yungay);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Yunguyo")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Yunguyo);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                } else  if(parent.getItemAtPosition(pos).toString().equals("Zarumilla")){
                    ArrayAdapter<String> s1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provincia_Zarumilla);
                    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(s1);
                }


            }


        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }










}
