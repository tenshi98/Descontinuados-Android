package info.androidhive.loginandregistration.model;

public class Contact {
    int _id;
    String _name;
    String _phone_number;
    String _estado;
    String _gsm;
    String _idcliente;
    String _tipoContacto;


    public Contact(){   }
    public Contact( String name, String _phone_number, String _estado, String _gsm, String _idcliente, String _tipoContacto){
        this._name = name;
        this._phone_number = _phone_number;
        this._estado = _estado;
        this._gsm = _gsm;
        this._idcliente = _idcliente;
        this._tipoContacto = _tipoContacto;
    }
    public Contact(int id, String name, String _phone_number, String _estado, String _gsm, String _idcliente, String _tipoContacto){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._estado = _estado;
        this._gsm = _gsm;
        this._idcliente = _idcliente;
        this._tipoContacto = _tipoContacto;
    }


    public int getID(){
        return this._id;
    }
    public String getName(){
        return this._name;
    }
    public String getPhoneNumber(){
        return this._phone_number;
    }
    public String getEstado(){
        return this._estado;
    }
    public String getGSM(){
        return this._gsm;
    }
    public String getidCliente(){
        return this._idcliente;
    }
    public String getTipoContacto(){
        return this._tipoContacto;
    }

    public void setID(int id){
        this._id = id;
    }
    public void setName(String name){
        this._name = name;
    }
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
    public void setEstado(String estado){
        this._estado = estado;
    }
    public void setGSM(String gsm){
        this._gsm = gsm;
    }
    public void setidCliente(String idcliente){
        this._idcliente = idcliente;
    }
    public void setTipoContacto(String tipoContacto){
        this._tipoContacto = tipoContacto;
    }
}