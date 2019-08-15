package info.androidhive.clickfarma.model;

public class Notifications {
    int _id;
    String _mensaje;
    String _texto;
    String _fecha;
    String _hora;
    String _Web;
    String _tipo;
    String _room;



    public Notifications(){   }
    public Notifications(String _mensaje, String _texto, String _fecha, String _hora, String _Web, String _tipo, String _room){
        this._mensaje = _mensaje;
        this._texto = _texto;
        this._fecha = _fecha;
        this._hora = _hora;
        this._Web = _Web;
        this._tipo = _tipo;
        this._room = _room;

    }
    public Notifications(int id, String _mensaje, String _texto, String _fecha, String _hora, String _Web, String _tipo, String _room){
        this._id = id;
        this._mensaje = _mensaje;
        this._texto = _texto;
        this._fecha = _fecha;
        this._hora = _hora;
        this._Web = _Web;
        this._tipo = _tipo;
        this._room = _room;

    }


    public int getID(){
        return this._id;
    }
    public String getMensaje(){
        return this._mensaje;
    }
    public String getTexto(){
        return this._texto;
    }
    public String getFecha(){
        return this._fecha;
    }
    public String getHora(){
        return this._hora;
    }
    public String getWeb(){
        return this._Web;
    }
    public String getTipo(){
        return this._tipo;
    }
    public String getRoom(){
        return this._room;
    }


    public void setID(int id){
        this._id = id;
    }
    public void setMensaje(String mensaje){
        this._mensaje = mensaje;
    }
    public void setTexto(String texto){
        this._texto = texto;
    }
    public void setFecha(String fecha){
        this._fecha = fecha;
    }
    public void setHora(String hora){
        this._hora = hora;
    }
    public void setWeb(String Web){
        this._Web = Web;
    }
    public void setTipo(String tipo){
        this._tipo = tipo;
    }
    public void setRoom(String room){
        this._room = room;
    }


}