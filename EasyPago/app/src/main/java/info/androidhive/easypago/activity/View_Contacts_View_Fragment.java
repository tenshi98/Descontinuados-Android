package info.androidhive.easypago.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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


public class View_Contacts_View_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_Nombre;
    private TextView txt_Fono;
    private TextView txt_Estado;
    private TextView txt_Tipo;

    //Icono del usuario
    private ImageView user_icon;

    //declaracion de botones
    private Button btnDeleteContact;
    private Button btnInviteContact;
    private Button btnLlamar;
    private Button btnBack;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_contactos dataContactos;
    private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Contacts_View_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_view, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_contacts_view));

        //Se toman los botones y textos del layout
        user_icon         = (ImageView) rootView.findViewById(R.id.icon_user);
        txt_Nombre        = (TextView) rootView.findViewById(R.id.Nombre);
        txt_Fono          = (TextView) rootView.findViewById(R.id.Fono);
        txt_Estado        = (TextView) rootView.findViewById(R.id.Estado);
        txt_Tipo          = (TextView) rootView.findViewById(R.id.TipoContacto);

        btnDeleteContact  = (Button) rootView.findViewById(R.id.btn_eliminar);
        btnInviteContact  = (Button) rootView.findViewById(R.id.btn_compartir);
        btnLlamar         = (Button) rootView.findViewById(R.id.btnLlamar);
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

        //obtengo la id del cliente
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String var_idCliente       = user.get("idCliente");

        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();
        //la transformo a integer
        int foo = Integer.parseInt(itemID);
        //busco los datos del contacto a traves de su id
        Contact contacts = dataContactos.getContact(foo);

        final int idContacto        = contacts.getID();
        final String Nombre         = contacts.getName();
        final String Fono           = contacts.getPhoneNumber();
        final String Estado         = contacts.getEstado();
        final String TipoContacto   = contacts.getTipoContacto();

        // se escriben los datos en los respectivos elementos
        txt_Nombre.setText(Nombre);
        txt_Fono.setText(Fono);
        txt_Estado.setText(Estado);
        txt_Tipo.setText(TipoContacto);

        //Se setea el icono
        if(Estado.equals("Registrado")){
            user_icon.setImageResource(R.drawable.ic_user_registrado);
            //oculto el boton invitar contacto en caso de que el contacto si este registrado
            btnInviteContact.setVisibility(View.GONE);
        }else{
            user_icon.setImageResource(R.drawable.ic_user_no_registrado);
        }


        //boton eliminar contacto
        btnDeleteContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Cuadro de dialogo de eliminacion del contacto
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Eliminacion de Datos");
                alertDialogBuilder.setMessage("¿Confirma que desea eliminar este contacto?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).delContact(var_idCliente, idContacto, Fono);
                                    }
                                });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();




            }
        });

        //boton de invitacion
        btnInviteContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Se abre la ventana de aplicaciones para compartir
                Compartir(Nombre, Fono);

            }
        });

        // boton llamar
        btnLlamar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Fono, null)));

            }


        });

        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

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

    //al presionar boton compartir, envia algunos datos a las distintas aplicaciones
    public void Compartir(String phoneName,String phoneNumber){
        Intent intentCompartir = new Intent(Intent.ACTION_SEND);
        intentCompartir.setType("text/plain");
        intentCompartir.putExtra(Intent.EXTRA_SUBJECT, phoneNumber);
        intentCompartir.putExtra(Intent.EXTRA_TEXT, "Texto compartir");
        startActivity(Intent.createChooser(intentCompartir, "Comparte App Seguridad"));
    }




}
