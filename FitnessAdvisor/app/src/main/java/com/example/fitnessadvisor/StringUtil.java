package com.example.fitnessadvisor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.libraries.places.api.model.Place.*;

/**
 * Utility class for converting objects to viewable strings and back.
 */
public final class StringUtil {

    private static final String FIELD_SEPARATOR = "\n\t";
    private static final String RESULT_SEPARATOR = "\n---\n\t";

    static void prepend(TextView textView, String prefix) {
        textView.setText(prefix + "\n\n" + textView.getText());
    }

    @Nullable
    static LatLngBounds convertToLatLngBounds(
            @Nullable String southWest, @Nullable String northEast) {
        LatLng soundWestLatLng = convertToLatLng(southWest);
        LatLng northEastLatLng = convertToLatLng(northEast);
        if (soundWestLatLng == null || northEast == null) {
            return null;
        }

        return new LatLngBounds(soundWestLatLng, northEastLatLng);
    }

    @Nullable
    static LatLng convertToLatLng(@Nullable String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        String[] split = value.split(",", -1);
        if (split.length != 2) {
            return null;
        }

        try {
            return new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    static List<String> countriesStringToArrayList(String countriesString) {
        // Allow these delimiters: , ; | / \
        List<String> countries = Arrays.asList(countriesString
                .replaceAll("\\s", "|")
                .split("[,;|/\\\\]",-1));

        return countries;
    }

    static String stringify(FindAutocompletePredictionsResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder
                .append(response.getAutocompletePredictions().size())
                .append(" Autocomplete Predictions Results:");

        if (raw) {
            builder.append(RESULT_SEPARATOR);
            appendListToStringBuilder(builder, response.getAutocompletePredictions());
        } else {
            for (AutocompletePrediction autocompletePrediction : response.getAutocompletePredictions()) {
                builder
                        .append(RESULT_SEPARATOR)
                        .append(autocompletePrediction.getFullText(/* matchStyle */ null));
            }
        }

        return builder.toString();
    }

    static String stringify(FetchPlaceResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append("Fetch Place Result:").append(RESULT_SEPARATOR);
        if (raw) {
            builder.append(response.getPlace());
        } else {
            builder.append(stringify(response.getPlace()));
        }

        return builder.toString();
    }

    static String stringify(FindCurrentPlaceResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append(response.getPlaceLikelihoods().size()).append(" Current Place Results:");

        if (raw) {
            builder.append(RESULT_SEPARATOR);
            appendListToStringBuilder(builder, response.getPlaceLikelihoods());
        } else {
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                List<Place.Type> t = placeLikelihood.getPlace().getTypes();
                for(int i = 0; i < t.size(); i++){
                    if(t.get(i) == Type.BICYCLE_STORE || t.get(i) == Type.PARK || t.get(i) == Type.AMUSEMENT_PARK ||
                        t.get(i) == Type.SHOPPING_MALL || t.get(i) == Type.AQUARIUM || t.get(i) == Type.ART_GALLERY ||
                        t.get(i) == Type.BOWLING_ALLEY ||t.get(i) == Type.TOURIST_ATTRACTION || t.get(i) == Type.SPA ||
                        t.get(i) == Type.NIGHT_CLUB || t.get(i) == Type.LIBRARY || t.get(i) == Type.CAMPGROUND || t.get(i) == Type.CASINO ||
                        t.get(i) == Type.CEMETERY || t.get(i) == Type.COLLOQUIAL_AREA || t.get(i) == Type.NEIGHBORHOOD || t.get(i) == Type.NATURAL_FEATURE ||
                        t.get(i) == Type.GYM || t.get(i) == Type.HINDU_TEMPLE || t.get(i) == Type.MUSEUM || t.get(i) == Type.STADIUM ||
                        t.get(i) == Type.TOWN_SQUARE || t.get(i) == Type.UNIVERSITY || t.get(i) == Type.ZOO || t.get(i) == Type.DEPARTMENT_STORE ||
                        t.get(i) == Type.ELECTRONICS_STORE || t.get(i) == Type.MOVIE_THEATER || t.get(i) == Type.ARCHIPELAGO || t.get(i) == Type.PET_STORE){

                        builder
                            .append(RESULT_SEPARATOR)
                            .append("Likelihood: ")
                            .append(placeLikelihood.getLikelihood())
                            .append(FIELD_SEPARATOR)
                            .append("Type: ")
                            .append(placeLikelihood.getPlace().getTypes())
                            .append(FIELD_SEPARATOR)
                            .append("Place: ")
                            .append(stringify(placeLikelihood.getPlace()));
                        break;
                    }
                }
            }
        }

        return builder.toString();
    }

    static String stringifyF(FindCurrentPlaceResponse response, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append(response.getPlaceLikelihoods().size()).append(" Current Place Results:");

        if (raw) {
            builder.append(RESULT_SEPARATOR);
            appendListToStringBuilder(builder, response.getPlaceLikelihoods());
        } else {
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                List<Place.Type> t = placeLikelihood.getPlace().getTypes();
                for(int i = 0; i < t.size(); i++){
                    if(t.get(i) == Type.FOOD || t.get(i) == Type.RESTAURANT || t.get(i) == Type.CAFE || t.get(i) == Type.GROCERY_OR_SUPERMARKET ||
                            t.get(i) == Type.SUPERMARKET || t.get(i) == Type.BAKERY || t.get(i) == Type.BAR || t.get(i) == Type.GAS_STATION ||
                            t.get(i) == Type.LIQUOR_STORE || t.get(i) == Type.MEAL_DELIVERY || t.get(i) == Type.MEAL_TAKEAWAY){

                        builder
                                .append(RESULT_SEPARATOR)
                                .append("Likelihood: ")
                                .append(placeLikelihood.getLikelihood())
                                .append(FIELD_SEPARATOR)
                                .append("Type: ")
                                .append(placeLikelihood.getPlace().getTypes())
                                .append(FIELD_SEPARATOR)
                                .append("Place: ")
                                .append(stringify(placeLikelihood.getPlace()));
                        break;
                    }
                }
            }
        }

        return builder.toString();
    }

    static String stringify(Place place) {
        return place.getName()
                + " ("
                + place.getAddress()
                + ")"
                + " is open now? "
                + place.isOpen(System.currentTimeMillis());
    }

    static String stringify(Bitmap bitmap) {
        StringBuilder builder = new StringBuilder();

        builder
                .append("Photo size (width x height)")
                .append(RESULT_SEPARATOR)
                .append(bitmap.getWidth())
                .append(", ")
                .append(bitmap.getHeight());

        return builder.toString();
    }

    public static String stringifyAutocompleteWidget(Place place, boolean raw) {
        StringBuilder builder = new StringBuilder();

        builder.append("Autocomplete Widget Result:").append(RESULT_SEPARATOR);

        if (raw) {
            builder.append(place);
        } else {
            builder.append(stringify(place));
        }

        return builder.toString();
    }

    private static <T> void appendListToStringBuilder(StringBuilder builder, List<T> items) {
        if (items.isEmpty()) {
            return;
        }

        builder.append(items.get(0));
        for (int i = 1; i < items.size(); i++) {
            builder.append(RESULT_SEPARATOR);
            builder.append(items.get(i));
        }
    }
}
