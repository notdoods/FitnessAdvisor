package com.example.fitnessadvisor;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.INTERNET;

/**
 * Activity for testing {@link PlacesClient#findCurrentPlace(FindCurrentPlaceRequest)}.
 */
public class findPlaces extends AppCompatActivity {

    private PlacesClient placesClient;
    private TextView responseView;
    private FieldSelector fieldSelector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_places);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDwAlXJ6Vlxo6bVzvQGSttNWbCSwix7HhA");
            Toast.makeText(this, "Places initialized!", Toast.LENGTH_SHORT).show();
        }
        placesClient = Places.createClient(this);

        // Set view objects
        List<Field> placeFields =
                FieldSelector.allExcept(
                        Field.ADDRESS_COMPONENTS,
                        Field.OPENING_HOURS,
                        Field.PHONE_NUMBER,
                        Field.UTC_OFFSET,
                        Field.WEBSITE_URI,
                        Field.PRICE_LEVEL,
                        Field.RATING,
                        Field.USER_RATINGS_TOTAL);
        fieldSelector =
                new FieldSelector(
                        findViewById(R.id.use_custom_fields),
                        findViewById(R.id.custom_fields_list),
                        placeFields,
                        savedInstanceState);
        responseView = findViewById(R.id.response);
        setLoading(false);

        // Set listeners for programmatic Find Current Place
        findViewById(R.id.find_current_place_button).setOnClickListener((view) -> findCurrentPlace());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        fieldSelector.onSaveInstanceState(bundle);
    }

    /**
     * Fetches a list of {@link PlaceLikelihood} instances that represent the Places the user is
     * most
     * likely to be at currently.
     */
    private void findCurrentPlace() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"after if",Toast.LENGTH_SHORT).show();
        }

        if (checkPermission(ACCESS_FINE_LOCATION)) {
            findCurrentPlaceWithPermissions();
            Toast.makeText(this, "findCurrentPlaceWithPermissions()",Toast.LENGTH_SHORT).show();
            Log.d("fine_loc granted", "access_fine_loc granted!");
        }else{
            Toast.makeText(this,"ACCESS_FINE_LOCATION needed",Toast.LENGTH_SHORT).show();
            Log.d("fine_loc denied","access_fine_loc needed!");
        }
    }

    /**
     * Fetches a list of {@link PlaceLikelihood} instances that represent the Places the user is
     * most
     * likely to be at currently.
     */
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void findCurrentPlaceWithPermissions() {
        setLoading(true);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(getPlaceFields());
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail", "currentPlaceTask failed");
            }
        });

        currentPlaceTask.addOnSuccessListener(
                (response) -> responseView.setText(StringUtil.stringify(response, isDisplayRawResultsChecked()))
        );

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    responseView.setText(exception.getMessage());
        });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }

    //////////////////////////
    // Helper methods below //
    //////////////////////////

    private List<Field> getPlaceFields() {
        if (((CheckBox) findViewById(R.id.use_custom_fields)).isChecked()) {
            return fieldSelector.getSelectedFields();
        } else {
            return fieldSelector.getAllFields();
        }
    }

    private boolean checkPermission(String permission) {
        boolean hasPermission =
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
        return hasPermission;
    }

    private boolean isDisplayRawResultsChecked() {
        return ((CheckBox) findViewById(R.id.display_raw_results)).isChecked();
    }

    private void setLoading(boolean loading) {
        findViewById(R.id.loading).setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }
}