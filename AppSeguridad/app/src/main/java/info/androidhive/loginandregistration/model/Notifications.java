package info.androidhive.loginandregistration.model;

public class Notifications {
    int _id;
    String _fecha;
    String _hora;
    String _latitud;
    String _longitud;
    String _tipo_alerta;
    String _fono;
    String _id_tipo_alerta;
    String _texto;
    String _web;
    String _idAlerta;


    public Notifications(){   }
    public Notifications(String _fecha, String _hora, String _latitud, String _longitud, String _tipo_alerta, String _fono, String _id_tipo_alerta, String _texto, String _web, String _idAlerta){
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._tipo_alerta = _tipo_alerta;
        this._fono = _fono;
        this._id_tipo_alerta = _id_tipo_alerta;
        this._texto = _texto;
        this._web = _web;
        this._idAlerta = _idAlerta;
    }
    public Notifications(int id, String _fecha, String _hora, String _latitud, String _longitud, String _tipo_alerta, String _fono, String _id_tipo_alerta, String _texto, String _web, String _idAlerta){
        this._id = id;
        this._fecha = _fecha;
        this._hora = _hora;
        this._latitud = _latitud;
        this._longitud = _longitud;
        this._tipo_alerta = _tipo_alerta;
        this._fono = _fono;
        this._id_tipo_alerta = _id_tipo_alerta;
        this._texto = _texto;
        this._web = _web;
        this._idAlerta = _idAlerta;
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
    public String getTipoNotificacion(){
        return this._tipo_alerta;
    }
    public String getFono(){
        return this._fono;
    }
    public String getTipoAlerta(){
        return this._id_tipo_alerta;
    }
    public String getTexto(){
        return this._texto;
    }
    public String getWeb(){
        return this._web;
    }
    public String getIdAlert(){
        return this._idAlerta;
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
    public void setTipoNotificacion(String tipo_alerta){
        this._tipo_alerta = tipo_alerta;
    }
    public void setFono(String fono){
        this._fono = fono;
    }
    public void settipoAlerta(String tipoAlerta){
        this._id_tipo_alerta = tipoAlerta;
    }
    public void setTexto(String texto){
        this._texto = texto;
    }
    public void setWeb(String web){
        this._web = web;
    }
    public void setIdAlert(String idAlerta){
        this._idAlerta = idAlerta;
    }

}