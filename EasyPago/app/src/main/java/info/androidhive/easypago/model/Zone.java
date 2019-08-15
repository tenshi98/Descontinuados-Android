package info.androidhive.easypago.model;

public class Zone {
    int _id;
    String _idZona;
    String _Nombre;
    String _ColorCode;
    String _Peligrosidad;
    String _Latitud;
    String _Longitud;


    public Zone(){   }
    public Zone(String _idZona, String _Nombre, String _ColorCode, String _Peligrosidad, String _Latitud, String _Longitud){
        this._idZona = _idZona;
        this._Nombre = _Nombre;
        this._ColorCode = _ColorCode;
        this._Peligrosidad = _Peligrosidad;
        this._Latitud = _Latitud;
        this._Longitud = _Longitud;
    }
    public Zone(int id, String _idZona, String _Nombre, String _ColorCode, String _Peligrosidad, String _Latitud, String _Longitud){
        this._id = id;
        this._idZona = _idZona;
        this._Nombre = _Nombre;
        this._ColorCode = _ColorCode;
        this._Peligrosidad = _Peligrosidad;
        this._Latitud = _Latitud;
        this._Longitud = _Longitud;
    }


    public int getID(){
        return this._id;
    }
    public String getidZona(){
        return this._idZona;
    }
    public String getNombre(){
        return this._Nombre;
    }
    public String getColorCode(){
        return this._ColorCode;
    }
    public String getPeligrosidad(){
        return this._Peligrosidad;
    }
    public String getLatitud(){
        return this._Latitud;
    }
    public String getLongitud(){
        return this._Longitud;
    }




    public void setID(int id){
        this._id = id;
    }
    public void setidZona(String idZona){
        this._idZona = idZona;
    }
    public void setNombre(String Nombre){
        this._Nombre = Nombre;
    }
    public void setColorCode(String ColorCode){
        this._ColorCode = ColorCode;
    }
    public void setPeligrosidad(String Peligrosidad){
        this._Peligrosidad = Peligrosidad;
    }
    public void setLatitud(String Latitud){
        this._Latitud = Latitud;
    }
    public void setLongitud(String Longitud){
        this._Longitud = Longitud;
    }

}