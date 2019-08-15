package info.androidhive.supegaofertador.adapter;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.app.AppController;
import info.androidhive.supegaofertador.model.Movie;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.target_ofertas, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView titulo        = (TextView) convertView.findViewById(R.id.titulo);
        TextView descripcion   = (TextView) convertView.findViewById(R.id.descripcion);
        TextView valor         = (TextView) convertView.findViewById(R.id.valor);

        // getting movie data for the row
        Movie m = movieItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        titulo.setText(m.getTitulo());

        // rating
        descripcion.setText("Descripcion : " + String.valueOf(m.getDescripcion()));

        // release year
        String subvalor = "Valor : $ " + String.valueOf(m.getValor());
        valor.setText(subvalor);

        return convertView;
    }

}
