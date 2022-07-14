package ru.oepak22.simpleweatherpatterna.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.simpleweatherpatterna.R;
import ru.oepak22.simpleweatherpatterna.model.City;

import java.util.List;

// класс адаптера для списка городов
public class CitiesAdapter extends RecyclerView.Adapter<CityHolder> {

    // список городов
    private final List<City> mCities;

    // выбор города из списка по нажатию
    private final OnItemClick mOnItemClick;

    // обработчик нажатия на элемент списка
    private final View.OnClickListener mInternalListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            City city = (City) view.getTag();
            mOnItemClick.onItemClick(city);
        }
    };

    // конструктор
    public CitiesAdapter(@NonNull List<City> cities, @NonNull OnItemClick onItemClick) {

        mCities = cities;                   // инициализация списка городов
        mOnItemClick = onItemClick;         // инициализация переданного обработчика нажатия
    }

    // ? пересоздание списка ?
    public void changeDataSet(@NonNull List<City> cities) {
        mCities.clear();
        mCities.addAll(cities);
        notifyDataSetChanged();
    }

    // создание поля для элемента списка
    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_city, parent, false);
        return new CityHolder(itemView);
    }

    // отображение объекта (города) в элементе списка
    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        City city = mCities.get(position);
        holder.bind(city);
        holder.itemView.setTag(city);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    // размер списка
    @Override
    public int getItemCount() {
        return mCities.size();
    }

    // интерфейс обработчика нажатия на элемент списка (для активности)
    public interface OnItemClick {

        void onItemClick(@NonNull City city);

    }
}
