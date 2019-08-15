package info.androidhive.loginandregistration.model;

public class Eventos {
    int _id;
    String _fecha;
    String _hora;
    String _latitud;
    String _longitud;
    String _nombre;
    String _tipo_evento;


    public Eventos(){   }
    public Eventos(String _fecha, String _hora, String _latitud, String _longitud, String _nombre, String _tipo_evento){
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._nombre = _nombre;
        this._tipo_evento = _tipo_evento;
    }
    public Eventos(int id, String _fecha, String _hora, String _latitud, String _longitud, String _nombre, String _tipo_evento){
        this._id = id;
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._nombre = _nombre;
        this._tipo_evento = _tipo_evento;
    }


    public int getID(){
        return this._id;
    }
    public String getFecha(){
        return this._fecha;
    }
    public String getHora(){
        return this._hora;
    }
    public String getLatitud(){
        return this._latitud;
    }
    public String getLongitud(){
        return this._longitud;
    }
    public String getNombre(){
        return this._nombre;
    }
    public String getTipoEvento(){
        return this._tipo_evento;
    }



    public void setID(int id){
        this._id = id;
    }
    public void setFecha(String fecha){
        this._fecha = fecha;
    }
    public void setHora(String hora){
        this._hora = hora;
    }
    public void setLatitud(String latitud){
        this._latitud = latitud;
    }
    public void setLongitud(String longitud){
        this._longitud = longitud;
    }
    public void setEvento(String nombre){
        this._nombre = nombre;
    }
    public void setTipoEvento(String tipo_evento){
        this._tipo_evento = tipo_evento;
    }

}