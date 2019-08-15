package info.androidhive.easypago.model;

public class Alert {
    int _id;
    String _fecha;
    String _hora;
    String _latitud;
    String _longitud;
    String _tipo_alerta;


    public Alert(){   }
    public Alert(String _fecha, String _hora, String _latitud, String _longitud, String _tipo_alerta){
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._tipo_alerta = _tipo_alerta;
    }
    public Alert(int id, String _fecha, String _hora, String _latitud, String _longitud, String _tipo_alerta){
        this._id = id;
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._tipo_alerta = _tipo_alerta;
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
    public String getTipoAlerta(){
        return this._tipo_alerta;
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
    public void setTipoAlerta(String tipo_alerta){
        this._tipo_alerta = tipo_alerta;
    }

}