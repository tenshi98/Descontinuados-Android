package info.androidhive.supegatrab.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.supegatrab.R;
import info.androidhive.supegatrab.helper.DB_usuarios;
import info.androidhive.supegatrab.helper.SessionManager;


public class View_Contacts_Add_Fragment extends Fragment {


    //declaracion de texto
    private TextView txtName;
    private TextView txtPhone;
    //declaracion de botones
    private Button btnContact;
    private Button btnVolver;
    private Button btnCompartir;
    private Spinner spinner1;
    //declaracion de variables
    private final static int REQUEST_CONTACTPICKER = 1;
    private static final int PICK_CONTACT_REQ = 1;
    //declaracion de variables siempre vacias
    private String phoneNumber = "";
    private String phoneName = "";
    public String Error = "" ;
    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos con los datos del usuario
    private DB_usuarios dataUsuarios;

    public View_Contacts_Add_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_add, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_contacts_add));

        //Se toman los botones y textos del layout
        txtName = (TextView) rootView.findViewById(R.id.Nombre);
        txtPhone = (TextView) rootView.findViewById(R.id.Fono);

        btnContact     = (Button) rootView.findViewById(R.id.btnOpenContact);
        btnVolver      = (Button) rootView.findViewById(R.id.btnVolver);
        btnCompartir   = (Button) rootView.findViewById(R.id.btnCompartir);

        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Seleccionar tipo de contacto");
        list.add("Familiar");
        list.add("Otro");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // se conecta a la base de datos interna
        dataUsuarios = new DB_usuarios(getActivity());
        //obtengo la id del cliente
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String var_idCliente       = user.get("idCliente");


        // boton buscar contactos
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    // Abrir el selector de contactos
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CONTACTPICKER);

            }
        });

        //boton de invitacion
        btnCompartir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //se vacia la variable error
                Error = "" ;

                String tipoContacto = spinner1.getSelectedItem().toString().trim();

                //Se comprueban datos vacios
                if (tipoContacto.isEmpty()  ) {
                    Error = "No ha seleccionado el tipo de contacto" ;
                }
                if (tipoContacto.equals("Seleccionar tipo de contacto")  ) {
                    Error = "No ha seleccionado el tipo de contacto" ;
                }
                if (phoneNumber.isEmpty()  ) {
                    Error = "El contacto seleccionado no posee un numero telefonico" ;
                }
                if (phoneName.isEmpty()  ) {
                    Error = "El contacto seleccionado no posee un nombre" ;
                }


               //si el numero existe o es distinto de vacio
                if (Error.isEmpty()) {
                    //se crea el nuevo contacto
                    ((MainActivity)getActivity()).addContact(var_idCliente, phoneName, phoneNumber, tipoContacto);

                //si el numero seleccionado no tiene un valor asignado
                } else {
                    Toast.makeText(getActivity(), Error, Toast.LENGTH_LONG).show();
                }

            }
        });

        // boton volver a la pantalla de lista de contactos
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Contacts_Fragment();
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



    //al volvel de la pantalla de seleccion de contactos
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CONTACT_REQ) {
                Uri contentUri = data.getData();
                String contactId = contentUri.getLastPathSegment();


                //Busco en la agenda en relacion a lo que presione
                Cursor cursor = getActivity().getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null, CommonDataKinds.Phone._ID + "=?",
                        new String[]{contactId}, null);

                //recorro los datos
                while (cursor.moveToNext()) {
                    //verifico el tipo de telefono
                    int type = cursor.getInt(cursor.getColumnIndex(CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        //casa
                        case CommonDataKinds.Phone.TYPE_HOME:
                            Toast.makeText(getActivity(), "Seleccione un contacto con un telefono movil", Toast.LENGTH_LONG).show();
                            break;
                        //movil
                        case CommonDataKinds.Phone.TYPE_MOBILE:

                            //guardo el numero y el nombre
                            phoneNumber = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER));
                            phoneName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                            phoneNumber = phoneNumber.trim();


                            if (!phoneNumber.equals("")) {
                                txtName.setText(phoneName);
                                txtPhone.setText(phoneNumber);
                            }else{
                                Toast.makeText(getActivity(), "El contacto seleccionado no posee un telefono movil", Toast.LENGTH_LONG).show();
                            }


                            break;
                        //trabajo
                        case CommonDataKinds.Phone.TYPE_WORK:
                            Toast.makeText(getActivity(), "Seleccione un contacto con un telefono movil", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "Seleccione un contacto con un telefono valido", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                cursor.close();

            }
        }
    }




}
