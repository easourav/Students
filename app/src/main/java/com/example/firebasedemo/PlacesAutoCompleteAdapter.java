package com.example.firebasedemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.firebasedemo.PlaceAPI.Prediction;
import com.example.firebasedemo.PlaceAPI.Predictions;
import com.example.firebasedemo.databinding.ModelLocationItemBinding;
import com.example.firebasedemo.retrofit.ApiService;
import com.example.firebasedemo.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;


public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Prediction> predictions;
    private PredictionInterface predictionInterface;

    public PlacesAutoCompleteAdapter(Context context, List<Prediction> predictions, PredictionInterface predictionInterface) {
        this.context = context;
        this.predictions = predictions;
        this.predictionInterface = predictionInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ModelLocationItemBinding modelLocationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),R.layout.model_location_item,viewGroup,false);
        return new ViewHolder(modelLocationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (predictions != null && predictions.size() > 0) {
            final Prediction prediction = predictions.get(i);
            viewHolder.binding.locationNameTV.setText(prediction.getDescription());
            viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    predictionInterface.getPrediction(prediction);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    @Override
    public Filter getFilter() {
        return new PlacesAutoCompleteFilter(this,context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ModelLocationItemBinding binding;
        public ViewHolder(ModelLocationItemBinding modelLocationItemBinding) {
            super(modelLocationItemBinding.getRoot());
            binding = modelLocationItemBinding;
        }
    }




    private class PlacesAutoCompleteFilter extends Filter {

        private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
        private Context context;

        public PlacesAutoCompleteFilter(PlacesAutoCompleteAdapter placesAutoCompleteAdapter, Context context) {
            super();
            this.placesAutoCompleteAdapter = placesAutoCompleteAdapter;
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            try {
                placesAutoCompleteAdapter.predictions.clear();
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = new ArrayList<Prediction>();
                    filterResults.count = 0;
                } else {
                    ApiService googleMapAPI = RetrofitInstance.getRetrofitInstanceForPlaceAPI().create(ApiService.class);
                    Predictions predictions = googleMapAPI.getPlacesAutoComplete(charSequence.toString(), "establishment", "23.7808875,90.2792371", "500", context.getString(R.string.place_api_key)).execute().body();
                    filterResults.values = predictions.getPredictions();
                    filterResults.count = predictions.getPredictions().size();
                }
                return filterResults;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            placesAutoCompleteAdapter.predictions.clear();
            if (filterResults!=null){
                placesAutoCompleteAdapter.predictions.addAll((List<Prediction>) filterResults.values);
                placesAutoCompleteAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Prediction prediction = (Prediction) resultValue;
            return prediction.getDescription();
        }
    }


}
