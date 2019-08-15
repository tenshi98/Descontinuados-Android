package info.androidhive.supegaofertador.model;

public class Alarms {
    int _id;
    String _texto;
    String _hora;
    String _horaini;


    public Alarms(){   }
    public Alarms(String _texto, String _hora, String _horaini){
        this._texto = _texto;
        this._hora = _hora;
        this._horaini = _horaini;
    }
    public Alarms(int id, String _texto, String _hora, String _horaini){
        this._id = id;
        this._texto = _texto;
        this._hora = _hora;
        this._horaini = _horaini;
    }


    public int getID(){
        return this._id;
    }
    public String getTexto(){
        return this._texto;
    }
    public String getHora(){
        return this._hora;
    }
    public String getHoraini(){
        return this._horaini;
    }


    public void setID(int id){
        this._id = id;
    }
    public void setTexto(String texto){
        this._texto = texto;
    }
    public void setHora(String hora){
        this._hora = hora;
    }
    public void setHoraini(String horaini){
        this._horaini = horaini;
    }

}