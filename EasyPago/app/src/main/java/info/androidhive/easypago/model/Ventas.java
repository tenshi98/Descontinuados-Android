package info.androidhive.easypago.model;

public class Ventas {
    int _id;
    String _fecha;
    String _monto;
    String _fono;


    public Ventas(){   }
    public Ventas(String _fecha, String _monto, String _fono){
        this._fecha = _fecha;
        this._monto = _monto;
        this._fono = _fono;
    }
    public Ventas(int id, String _fecha, String _monto, String _fono){
        this._id = id;
        this._fecha = _fecha;
        this._monto = _monto;
        this._fono = _fono;
    }


    public int getID(){
        return this._id;
    }
    public String getFecha(){
        return this._fecha;
    }
    public String getMonto(){
        return this._monto;
    }
    public String getFono(){
        return this._fono;
    }



    public void setID(int id){
        this._id = id;
    }
    public void setFecha(String fecha){
        this._fecha = fecha;
    }
    public void setMonto(String monto){
        this._monto = monto;
    }
    public void setFono(String fono){
        this._fono = fono;
    }

}