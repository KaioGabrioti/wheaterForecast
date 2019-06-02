package br.com.usjt_ads3anmca_weatherforecast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeatherAdapter extends
        RecyclerView.Adapter <WeatherAdapter.MeuViewHolder> {

    private List<Weather> previsoes;
    private Context context;

    public WeatherAdapter (List <Weather> previsoes, Context context){
        this.previsoes = previsoes;
        this.context = context;
    }

    class MeuViewHolder extends RecyclerView.ViewHolder{
        private ImageView conditionImageView;
        private TextView dayTextView;
        private TextView lowTextView;
        private TextView highTextView;
        private TextView humidityTextView;
        private TextView latitudeTextView;
        private TextView longitudeTextView;

        MeuViewHolder (View raiz){
            super (raiz);
            conditionImageView =
                    raiz.findViewById(R.id.conditionImageView);
            dayTextView =
                    raiz.findViewById(R.id.dayTextView);
            lowTextView =
                    raiz.findViewById(R.id.lowTextView);
            highTextView =
                    raiz.findViewById(R.id.highTextView);
            humidityTextView =
                    raiz.findViewById(R.id.humidityTextView);
            latitudeTextView =
                    raiz.findViewById(R.id.latitudeTextView);
            longitudeTextView =
                    raiz.findViewById(R.id.longitudeTextView);
        }
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                            int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View raiz = inflater.inflate(
                R.layout.list_item, viewGroup,
                false);
        return new MeuViewHolder(raiz);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder viewHolder, int i) {
        Weather item = previsoes.get(i);
        viewHolder.dayTextView.setText(
                context.getString(
                        R.string.day_description,
                        item.dayOfWeek,
                        item.description
                )
        );
        viewHolder.lowTextView.setText(
                context.getString(
                        R.string.low_temp,
                        item.minTemp
                )
        );
        weatherViewHolder.highTextView.setText(
                context.getString(
                        R.string.high_temp,
                        dadoDeInteresse.maxTemp
                )
        );
        weatherViewHolder.humidityTextView.setText(
                context.getString(
                        R.string.humidity,
                        dadoDeInteresse.humidity
                )
        );
        weatherViewHolder.latitudeTextView.setText(
                context.getString(
                        R.string.latitude,
                        dadoDeInteresse.latitude
                )
        );
        weatherViewHolder.longitudeTextView.setText(
                context.getString(
                        R.string.longitude,
                        dadoDeInteresse.longitude
                )
        );

        Glide.with(context).load(item.iconURL).into(viewHolder.conditionImageView);
    }

    @Override
    public int getItemCount() {
        return previsoes.size();
    }
}
