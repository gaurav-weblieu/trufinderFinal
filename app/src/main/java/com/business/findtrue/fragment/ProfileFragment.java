package com.business.findtrue.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.marcoscg.dialogsheet.DialogSheet;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseFragment;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.model.CategoryProfile;
import com.business.findtrue.model.Data1;
import com.business.findtrue.model.Datum;
import com.business.findtrue.model.SendEnquiry;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {


    private ApiInterface apiInterface;
    private TitleTextView tvVendorName,tvBusiness,tvOfficeAddress;
    private RegularTextView  tvAddress, tvVendorAddress, tvEstablishedYear, tvCash, tvFullOfficeAddress, tvOfficeHour, tvCategory_Name;
    private RegularButton sendEnqiry;
    RegularTextInputLayout inputLayoutName,inputLayoutEmail,inputLayoutMobile,inputLayoutBookingDate;
    RegularTextInputEditText name,email,mobile,bookingDate;
    int mYear, mMonth, mDay, mHour, mMinute;
    String dateTime,checked,vndrbox, category_id;
    Datum categoryData;
    AlertDialog materialAlertDialogBuilder;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvVendorName = view.findViewById(R.id.tvVendorName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvBusiness = view.findViewById(R.id.tvBusiness);
        tvEstablishedYear = view.findViewById(R.id.tvEstablishedYear);
        tvCash = view.findViewById(R.id.tvCash);
        tvFullOfficeAddress = view.findViewById(R.id.tvFullOfficeAddress);
        tvOfficeHour = view.findViewById(R.id.tvOfficeHour);
        tvCategory_Name = view.findViewById(R.id.tvCategory_Name);
        tvOfficeAddress = view.findViewById(R.id.tvOfficeAddress);
        sendEnqiry = view.findViewById(R.id.sendEnquiry);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        getCategoryProfile();

        sendEnqiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        DialogSheet dialogSheet = new DialogSheet(getContext(), true);
                        View view1 = View.inflate(getContext(), R.layout.fragment_enquiry_dialog, null);
                        dialogSheet.setView(view1);
                        dialogSheet.show();
                        //ImageView btnClose = view.findViewById(R.id.btnClose);
                        RegularButton btnSubmit = view1.findViewById(R.id.submit);
                        ImageView ivClose = (ImageView)view1.findViewById(R.id.ivClose);
                        inputLayoutName = view1.findViewById(R.id.inputLayoutName);
                        inputLayoutEmail = view1.findViewById(R.id.inputLayoutEmail);
                        inputLayoutMobile = view1.findViewById(R.id.inputLayoutMobile);
                        inputLayoutBookingDate = view1.findViewById(R.id.inputLayoutBookingDate);
                        name = view1.findViewById(R.id.name);
                        email = view1.findViewById(R.id.email);
                        mobile = view1.findViewById(R.id.mobile);
                        bookingDate = view1.findViewById(R.id.bookingDate);
                        CheckBox checkBox = (CheckBox)view1.findViewById(R.id.whatsupcheckBox);

                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSheet.dismiss();
                            }
                        });


                        bookingDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get Current Date
                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                bookingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                //dateTime = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                                dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                System.out.println("dateTime----------------------"+dateTime);

                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked){
                                    checked = "1";
                                    System.out.println("checked---------------------"+checked);
                                }else {
                                    //boolean check = false;
                                    checked = "0";
                                    System.out.println("checked---------------------"+checked);
                                }
                            }
                        });

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validationCheck()){
                                    showDialog();
                                    String id = categoryData.getId();
                                    String city_id = categoryData.getCity();
                                    String areapinid = categoryData.getPincode();
                                    apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                                    SendEnquiry sendEnquiry = new SendEnquiry();
                                    sendEnquiry.setVndrbox(id);
                                    sendEnquiry.setCategory_id(categoryData.getCategoryId());
                                    sendEnquiry.setCity_id(city_id);
                                    sendEnquiry.setAreapinid(areapinid);
                                    sendEnquiry.setName(name.getText().toString());
                                    sendEnquiry.setEmail(email.getText().toString());
                                    sendEnquiry.setMobile_country_code(91);
                                    sendEnquiry.setContactno(mobile.getText().toString());
                                    sendEnquiry.setBookingdate(dateTime);
                                    sendEnquiry.setWhatsapp_message_status(checked);
                                    sendEnquiry.setIpaddress(AppConstant.PUBLIC_IP_ADDRESS);



                                    //System.out.println("output------------------"+sendEnquiry);
                                    String country_code = "91";

                                    apiInterface.sendEnquiry(categoryData.getId(), categoryData.getCategoryId(), city_id, areapinid, name.getText().toString(), email.getText().toString(), country_code, mobile.getText().toString(), dateTime, checked, AppConstant.PUBLIC_IP_ADDRESS).enqueue(new Callback<ResponseRequest>() {
                                        @Override
                                        public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                                            hideDialog();
                                            if (response.isSuccessful()){
                                                System.out.println("-------------------------------------"+response.body().getMessage());
                                                dialogSheet.dismiss();
                                                showDialog("Enquiry send successfully. Do you want to contact vendor on this number : "+response.body().getData(),response.body().getData());

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseRequest> call, Throwable t) {
                                            hideDialog();
                                            Toast.makeText(getContext(), "Enquiry failed", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                                //dialogSheet.dismiss();
                            }
                        });
                        //Navigation.createNavigateOnClickListener(R.id.action_categoryDetailsFragment_to_enquiryDialogFragment).onClick(holder.itemView);
                    }



        });


    }

    private void showDialog(String message,String number) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_enquire_successfull, null, false);
        MaterialButton ok = view.findViewById(R.id.ok);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        RegularTextView messageTv = view.findViewById(R.id.message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            messageTv.setText(message);
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                    .setView(view)
                    .setBackground(getContext().getDrawable(R.drawable.grid_bg))
                    .setCancelable(false)
                    .show();
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    String p = "tel:" + number;
                    i.setData(Uri.parse(p));
                    startActivity(i);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialAlertDialogBuilder.dismiss();
                }
            });
        }
    }

    private boolean validationCheck(){
        if(TextUtils.isEmpty(name.getText().toString())){
            inputLayoutName.setErrorEnabled(true);
            inputLayoutName.setError("Please enter your name");
            return false;
        }else if(TextUtils.isEmpty(mobile.getText().toString())){
            inputLayoutMobile.setErrorEnabled(true);
            inputLayoutMobile.setError("Please enter your mobile number");
            return false;
        }else return true;
    }

    private void getCategoryProfile(){
        //String vid = "Birthday-Party-Organizer-In-Delhi";
        String vid = CommonUtils.getPreferencesString(getContext(), AppConstant.VENDOR_EMAIL);
        apiInterface.getCategoryProfile(vid).enqueue(new Callback<CategoryProfile>() {
            @Override
            public void onResponse(Call<CategoryProfile> call, Response<CategoryProfile> response) {
                if (response.isSuccessful()){
                    String type = response.body().getType();
                    System.out.println("type---------------------------"+type);
                    List<Datum> results = response.body().getData();
                    categoryData = response.body().getData().get(0);
                    for (Datum data : results){
                        tvVendorName.setText("About "+data.getVendorName());
                        tvAddress.setText(data.getAddress());
                        tvBusiness.setText("Business Info "+data.getVendorName()+" :");
                        tvFullOfficeAddress.setText(data.getAddress()+", "+data.getLocalarea()+", "+data.getPincode()+", "+data.getCountry());
                        tvCategory_Name.setText(AppConstant.CATEGORY_NAME);
                        tvOfficeAddress.setText(data.getVendorName() +"Office Address:");
                    }
                    List<Data1> data = response.body().getData1();
                    for (Data1 data1 : data){
                        tvEstablishedYear.setText(data1.getEstablish());
                        tvCash.setText(data1.getAccepts());
                        tvOfficeHour.setText(data1.getOfficeHour());
                    }
                }

            }

            @Override
            public void onFailure(Call<CategoryProfile> call, Throwable t) {

            }
        });

    }
}