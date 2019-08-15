package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.helper.DB_alarms;
import info.androidhive.clickfarma.helper.SessionManager;
import info.androidhive.clickfarma.model.Alarms;


public class View_AgendaEdit_Fragment extends Fragment {

    //declaracion de botones
    private Button btn_editar,btn_borrar,btn_volver;
    private TextView input_Texto;
    private TextView input_Hora;
    private Spinner spinner_hora;
    public String Error = "" ;


    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
    private SessionManager session;
    private DB_alarms dataAlarmas;

    public View_AgendaEdit_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agendaedit, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_agendaedit));


        //Se toman los botones
        btn_editar     = (Button) rootView.findViewById(R.id.btn_editar);
        btn_borrar     = (Button) rootView.findViewById(R.id.btn_borrar);
        btn_volver     = (Button) rootView.findViewById(R.id.btn_volver);
        input_Texto    = (EditText) rootView.findViewById(R.id.Texto);
        input_Hora     = (EditText) rootView.findViewById(R.id.HoraIni);
        spinner_hora   = (Spinner) rootView.findViewById(R.id.hora);



        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataAlarmas   = new DB_alarms(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        input_Hora.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    input_Hora.clearFocus();
                    spinner_hora.requestFocus();
                    spinner_hora.performClick();
                    spinner_hora.requestFocusFromTouch();
                }
                return false;
            }
        });



        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();
        //la transformo a integer
        final int foo = Integer.parseInt(itemID);
        //busco los datos del contacto a traves de su id
        Alarms alarmas = dataAlarmas.getNoti(foo);
        final String Hora       = alarmas.getHora();
        final String Texto      = alarmas.getTexto();
        final String HoraIni    = alarmas.getHoraini();

        // se escriben los datos en los respectivos elementos
        input_Texto.setText(Texto);
        input_Hora.setText(HoraIni);

        //Se ingresan manualmente los datos del primer select
        List<String> list = new ArrayList<String>();
        if(Hora.isEmpty()){
            list.add("Seleccione el intervalo");
        }else{
            list.add(Hora);
        }
        list.add("1 Hora");
        list.add("2 Horas");
        list.add("3 Horas");
        list.add("4 Horas");
        list.add("5 Horas");
        list.add("6 Horas");
        list.add("7 Horas");
        list.add("8 Horas");
        list.add("9 Horas");
        list.add("10 Horas");
        list.add("11 Horas");
        list.add("12 Horas");
        list.add("13 Horas");
        list.add("14 Horas");
        list.add("15 Horas");
        list.add("16 Horas");
        list.add("17 Horas");
        list.add("18 Horas");
        list.add("19 Horas");
        list.add("20 Horas");
        list.add("21 Horas");
        list.add("22 Horas");
        list.add("23 Horas");
        list.add("24 Horas");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hora.setAdapter(dataAdapter);



        //
        btn_editar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String int_texto    = input_Texto.getText().toString().trim();
                String int_hora     = spinner_hora.getSelectedItem().toString().trim();
                String int_HoraIni  = input_Hora.getText().toString().trim();

                //VALIDACIONES
                long horas = 0;
                int Hora = -1;
                int Minuto = -1;


                //se verifica si los datos estan llenados correctamente

                //se verifica el ingreso de datos de el campo hora
                if (int_hora.isEmpty() ) {
                    Error = "Seleccione el rango de repeticion de la alarma" ;
                }else{
                    //se condiciona para ajustar la variable hora
                    switch (int_hora) {
                        case "Seleccione el intervalo":    horas = 0;  break;
                        case "1 Hora":    horas = 1;  break;
                        case "2 Horas":   horas = 2;  break;
                        case "3 Horas":   horas = 3;  break;
                        case "4 Horas":   horas = 4;  break;
                        case "5 Horas":   horas = 5;  break;
                        case "6 Horas":   horas = 6;  break;
                        case "7 Horas":   horas = 7;  break;
                        case "8 Horas":   horas = 8;  break;
                        case "9 Horas":   horas = 9;  break;
                        case "10 Horas":  horas = 10; break;
                        case "11 Horas":  horas = 11; break;
                        case "12 Horas":  horas = 12; break;
                        case "13 Horas":  horas = 13; break;
                        case "14 Horas":  horas = 14; break;
                        case "15 Horas":  horas = 15; break;
                        case "16 Horas":  horas = 16; break;
                        case "17 Horas":  horas = 17; break;
                        case "18 Horas":  horas = 18; break;
                        case "19 Horas":  horas = 19; break;
                        case "20 Horas":  horas = 20; break;
                        case "21 Horas":  horas = 21; break;
                        case "22 Horas":  horas = 22; break;
                        case "23 Horas":  horas = 23; break;
                        case "24 Horas":  horas = 24; break;
                    }
                }
                //se verifica el ingreso de datos de el campo texto
                if (int_HoraIni.isEmpty()  ) {
                    Error = "Ingrese la hora de inicio de la alarma" ;
                }else{
                    String[] parts = int_HoraIni.split(":");
                    Hora    = Integer.parseInt(parts[0]);
                    Minuto  = Integer.parseInt(parts[1]);
                    //se verifica los rangos de datos esten correctos
                    //verificacion de rango de la hora
                    if (Hora>-1 && Hora<24  ) {
                        Error = "" ;
                    }else{
                        Error = "Ingrese el formato de hora correcto (1 a 24)" ;
                    }
                    //verificacion del rango de minutos
                    if (Minuto>-1 && Minuto<60  ) {
                        Error = "" ;
                    }else{
                        Error = "Ingrese el formato de minuto correcto (00 a 59)" ;
                    }
                    //se verifica si es el dato correcto
                    try {
                        DateFormat sdf = new SimpleDateFormat("HH:mm");
                        sdf.setLenient(false);
                        Date time = sdf.parse(int_HoraIni);
                        Error = "" ;
                    } catch (Exception e) {
                        Error = "Ingrese un formato correcto para la hora (HH:mm)" ;
                    }

                }
                //se verifica el ingreso de datos de el campo texto
                if (int_texto.isEmpty()  ) {
                    Error = "Ingrese la descripcion de la alerta" ;
                }

                //Si no hay errores ejecuto el codigo
                if (Error.isEmpty() ) {

                    //se actualiza el registro en la base de datos interna
                    dataAlarmas.updateNoti(new Alarms(foo, int_texto, int_hora, int_HoraIni));

                    //Se modifica la alarma en el sistema
                    setAlarm(getActivity(), foo, horas, Hora, Minuto);

                    //notificacion
                    Toast.makeText(getActivity(), "Alerta editada correctamente", Toast.LENGTH_SHORT).show();

                    //se redirige a la pantalla principal
                    Fragment fragment = new View_Agenda_Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), Error , Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_borrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                dataAlarmas.deleteNoti(foo);

                cancelAlarm(getActivity(), foo);

                Toast.makeText(getActivity(), "Alerta eliminada correctamente", Toast.LENGTH_SHORT).show();

                Fragment fragment = new View_Agenda_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        //
        btn_volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Agenda_Fragment();
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



    public void setAlarm(Context context, int pk, long time, int Hora, int Minuto) {

        /* intervalos
        1000 * 60 * 60 * 1 = 1 hora
        1000 * 60 * 60 * 24 = 1 dia
        1000 * 60 * 60 * 24 * 7 = 1 semana
        */
        //Inidico la hora de inicio en base a la hora ingresada


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Hora);
        calendar.set(Calendar.MINUTE, Minuto);


        //Se crea la hora correcta para el sistema
        long newTime = 1000 * 60 * 60 * time;
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarma", pk);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pk, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newTime, pendingIntent);



    }

    public void cancelAlarm(Context context, int pk) {


        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pk, alarmIntent, PendingIntent.FLAG_NO_CREATE);
        manager.cancel(pendingIntent);

    }


}
