package com.business.findtrue.user;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.business.findtrue.CategoryDetailsActivity;
import com.business.findtrue.R;
import com.business.findtrue.SearchActivity;
import com.business.findtrue.adapter.CategoryAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.listner.ProductListner;
import com.business.findtrue.model.Category;
import com.business.findtrue.model.CityList;
import com.business.findtrue.model.CityModel;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.mrapp.android.dialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoryActivity extends BaseActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    MaterialDialog.Builder builder;
    private SpinnerCityAdapter spinnerCityAdapter;
    List<Category> categoryItemList = null;
    private ApiInterface apiInterface;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvLocation)
    RegularTextView tvLocation;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    List<CityList> cityLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        ButterKnife.bind(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        showDialog();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        apiInterface.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    categoryItemList = response.body();
                    if (categoryItemList != null) {

                        //CategoryAdapter categoryAdapter = new CategoryAdapter(AllCategoryActivity.this, categoryItemList,true);
                        CategoryAdapter categoryAdapter = new CategoryAdapter(AllCategoryActivity.this, categoryItemList, true, new ProductListner() {
                            @Override
                            public void productClick(int position, @NonNull Category category) {
                                boolean isMatch = false;
                                CityList currentCity = null;
                                for (CityList city : cityLists) {
                                    if (city.getCity().equalsIgnoreCase(AppConstant.CITY_NAME) || city.getCity_alias().equalsIgnoreCase(AppConstant.CITY_NAME)) {
                                        Log.e("city_name", city.getCity() + " : " + city.getCity_alias() + ": " + AppConstant.CITY_NAME);
                                        currentCity = city;
                                        isMatch = true;
                                    }
                                }
                                if (isMatch) {
                                    Intent intent = new Intent(AllCategoryActivity.this, CategoryDetailsActivity.class);
                                    intent.putExtra("CATEGORY_NAME", category.getId());
                                    intent.putExtra("CITY_NAME", currentCity.getCity());
                                    startActivity(intent);
                                }
                                else showCityList();


                            }
                        });
                        mRecyclerView.setAdapter(categoryAdapter);

                    }
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                hideDialog();
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityList();
            }
        });
        editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCategoryActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showCityList(){
        builder = new MaterialDialog.Builder(AllCategoryActivity.this);
        builder.setView(R.layout.change_city_dialog);
        builder.show();
        mRecyclerView = (RecyclerView) builder.create().findViewById(R.id.recyclerViewCityName);
        ProgressBar mProgressBar = (ProgressBar)builder.create().findViewById(R.id.mProgressBar);
        EditText editSearch = (EditText)builder.create().findViewById(R.id.editSearch);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AllCategoryActivity.this));
        mRecyclerView.setHasFixedSize(true);
        ImageView imageClose = (ImageView)builder.create().findViewById(R.id.imageViewClose);
        spinnerCityAdapter = new SpinnerCityAdapter(AllCategoryActivity.this, cityLists);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.getCity().enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.isSuccessful()){
                    mProgressBar.setVisibility(View.GONE);
                    System.out.println("type=========------------------------"+response.body().getType());
                    List<CityList> models = response.body().getData();
                    for (CityList list : models){
                        cityLists.add(list);
                        spinnerCityAdapter = new SpinnerCityAdapter(AllCategoryActivity.this, cityLists);
                        mRecyclerView.setAdapter(spinnerCityAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.create().dismiss();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerCityAdapter.getFilter().filter(s);
            }
        });
    }



    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            System.out.println("latitude-----------------" + location.getLatitude());
                            System.out.println("longitute-----------------" + location.getLongitude());
                            String latlong = location.getLatitude() + "," + location.getLongitude();
                            System.out.println("latlong 1-------------------" + latlong);


                            geocoder = new Geocoder(AllCategoryActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String current_address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                                String city = addresses.get(0).getLocality();
                                System.out.println("city--------------------------" + city);
                                tvLocation.setText(city);
                                String str = city.trim();
                                //String cityName = str.substring(0, 1).toLowerCase() + str.substring(1);
                                String cityName = str.replaceAll(" ", "-").toLowerCase();
                                System.out.println("city--------------------------" + cityName);
                                AppConstant.CITY_NAME = cityName;

                                // System.out.println("city_name-------------------"+AppConstant.CITY_NAME);

                            } catch (IOException e) {

                            }
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            System.out.println("mLatitude-----------------------"+mLastLocation.getLatitude());
            System.out.println("mLongitude-----------------------"+mLastLocation.getLongitude());
            //latTextView.setText(mLastLocation.getLatitude()+"");
            //lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void back(View view) {
        finish();
    }


    public class SpinnerCityAdapter extends RecyclerView.Adapter<SpinnerCityAdapter.ViewHolder> implements Filterable {
        Context context;
        List<CityList> cityLists;
        private List<CityList> cityListFull;

        public SpinnerCityAdapter(Context context, List<CityList> cityLists) {
            this.context = context;
            this.cityLists = cityLists;
            cityListFull = new ArrayList<>(cityLists);
        }

        @NonNull
        @Override
        public SpinnerCityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.city_list, parent, false);
            SpinnerCityAdapter.ViewHolder viewHolder = new SpinnerCityAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SpinnerCityAdapter.ViewHolder holder, int position) {
            CityList cityList = cityLists.get(position);
            System.out.println("city_name-------------------------"+cityList.getCity());
            holder.tvCityName.setText(cityList.getCity());

            //city name in on click event
            holder.tvCityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, cityList.getCity_alias(), Toast.LENGTH_LONG).show();
                    builder.create().dismiss();
                    AppConstant.CITY_NAME = cityList.getCity_alias();
                    AppConstant.SEARCH_CITY_NAME = cityList.getCity_alias();
                    tvLocation.setText(cityList.getCity());
                    //System.out.println("AppCityName------------------"+AppConstant.CITY_NAME);


                }
            });
        }

        @Override
        public int getItemCount() {
            return cityLists.size();
        }

        @Override
        public Filter getFilter() {
            return cityNameFilter;
        }

        private Filter cityNameFilter = new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CityList> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(cityListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (CityList item : cityListFull) {
                        if (item.getCity().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cityLists.clear();
                cityLists.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvCityName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCityName = (TextView)itemView.findViewById(R.id.tvCityName);
            }
        }
    }

}