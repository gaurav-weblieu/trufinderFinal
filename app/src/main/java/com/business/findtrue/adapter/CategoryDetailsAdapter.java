package com.business.findtrue.adapter;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marcoscg.dialogsheet.DialogSheet;
import com.business.findtrue.R;
import com.business.findtrue.ViewProfileActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.model.CategoryData;
import com.business.findtrue.model.SendEnquiry;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryDetailsAdapter extends RecyclerView.Adapter<CategoryDetailsAdapter.ViewHolder> {
    Context mContext;
    private List<CategoryData> categoryDataList;
    int mYear, mMonth, mDay, mHour, mMinute;
    String vndrbox, category_id, city_id, areapinid, dateTime;
    String checked;
    AlertDialog materialAlertDialogBuilder;



    RegularTextInputLayout inputLayoutName,inputLayoutEmail,inputLayoutMobile,inputLayoutBookingDate;
    RegularTextInputEditText name,email,mobile,bookingDate;
    ApiInterface apiInterface;

    public CategoryDetailsAdapter(Context mContext, List<CategoryData> categoryDataList){
        this.mContext = mContext;
        this.categoryDataList = categoryDataList;
    }

    public void setList(List<CategoryData> categoryDataList){
        //this.categoryDataList.addAll(categoryDataList);
        this.categoryDataList = categoryDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.category_details_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryData categoryData = categoryDataList.get(position);
        Glide.with(mContext).load(categoryData.getFullProfilePic()).centerCrop().placeholder(R.drawable.no_img).into(holder.ivImage);
        holder.tvVenderName.setText(categoryData.getVendorName());
        holder.tvAddress.setText(categoryData.getAddress());

        if (categoryData.getPackagePrice()!=null){
        if (categoryData.getPackagePrice().equals("0")){
            holder.tvPrice.setText("PACKAGE PRICE : " +"\u20B9" +"Ask for Price");
        }else {
            holder.tvPrice.setText("PACKAGE PRICE : " +"\u20B9" +categoryData.getPackagePrice());
        }
        }

        vndrbox = categoryData.getId();
        category_id = categoryData.getCategoryId();
        city_id = categoryData.getCityId();
        areapinid = categoryData.getPincode();


        holder.btnEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSheet dialogSheet = new DialogSheet(mContext, true);
                View view = View.inflate(mContext, R.layout.fragment_enquiry_dialog, null);
                dialogSheet.setView(view);
                dialogSheet.show();
                //ImageView btnClose = view.findViewById(R.id.btnClose);
                RegularButton btnSubmit = view.findViewById(R.id.submit);
                ImageView ivClose = (ImageView)view.findViewById(R.id.ivClose);
                inputLayoutName = view.findViewById(R.id.inputLayoutName);
                inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
                inputLayoutMobile = view.findViewById(R.id.inputLayoutMobile);
                inputLayoutBookingDate = view.findViewById(R.id.inputLayoutBookingDate);
                name = view.findViewById(R.id.name);
                email = view.findViewById(R.id.email);
                mobile = view.findViewById(R.id.mobile);
                bookingDate = view.findViewById(R.id.bookingDate);
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.whatsupcheckBox);

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

                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
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
                        if (validateForm()){
                            SweetAlertDialog pDialog = new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            String id = categoryDataList.get(position).getId();
                            String city_id = categoryDataList.get(position).getCityId();
                            String areapinid = categoryDataList.get(position).getPincode();
                            apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                            SendEnquiry sendEnquiry = new SendEnquiry();
                            sendEnquiry.setVndrbox(id);
                            sendEnquiry.setCategory_id(category_id);
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

                            apiInterface.sendEnquiry(vndrbox, category_id, city_id, areapinid, name.getText().toString(), email.getText().toString(), country_code, mobile.getText().toString(), dateTime, checked, AppConstant.PUBLIC_IP_ADDRESS).enqueue(new Callback<ResponseRequest>() {
                                @Override
                                public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                                    if (response.isSuccessful()){
                                        pDialog.hide();
                                        System.out.println("-------------------------------------"+response.body().getMessage());
                                        //Toast.makeText(mContext, "Enquiry send successfully", Toast.LENGTH_LONG).show();
                                        dialogSheet.dismiss();
                                        showDialog("Enquiry send successfully. Do you want to contact vendor on this number : "+response.body().getData(),response.body().getData());


                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseRequest> call, Throwable t) {
                                    pDialog.hide();
                                    Toast.makeText(mContext, "Enquiry failed", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        //dialogSheet.dismiss();
                    }
                });
                //Navigation.createNavigateOnClickListener(R.id.action_categoryDetailsFragment_to_enquiryDialogFragment).onClick(holder.itemView);


            }

        });

        holder.linear_cat_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewProfileActivity.class);
                intent.putExtra("PROFILE_IMAGE", categoryData.getProfilepic());
                intent.putExtra("VENDOR_NAME", categoryData.getVendorName());
                CommonUtils.savePreferenceString(mContext, AppConstant.VENDOR_EMAIL, categoryDataList.get(position).getEmail());
                mContext.startActivity(intent);
//
            }
        });

        holder.image_shortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isLoggedIn = CommonUtils.getPreferencesString(mContext, AppConstant.IS_LOGGED_IN);
                if (isLoggedIn.equals("true")){

                    holder.image_shortlist.setImageResource(R.drawable.ic_round_favorite_24);

                    apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                    String USER_ID = CommonUtils.getPreferencesString(mContext, AppConstant.USER_ID);
                    String id = categoryDataList.get(position).getId();
                    String category_id = categoryDataList.get(position).getCategoryId();
                    System.out.println("id----------------------------"+id);
                    System.out.println("category_id----------------------------"+category_id);
                    apiInterface.addWishList(USER_ID, id, category_id).enqueue(new Callback<ResponseRequest>() {
                        @Override
                        public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                            if (response.isSuccessful()){

                                System.out.println("message----------------------"+response.body().getMessage());
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRequest> call, Throwable t) {

                        }
                    });
//                    //

                }else {
                    Toast.makeText(mContext, "Please Login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivImage;
        TitleTextView tvVenderName,btnEnquiry;
        RegularTextView tvAddress, tvPrice,btnProfile;
        LinearLayout linear_cat_details;
        ImageView image_shortlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnEnquiry = itemView.findViewById(R.id.btnSendEnquery);
            btnProfile = itemView.findViewById(R.id.btnProfile);
            ivImage = (RoundedImageView)itemView.findViewById(R.id.ivImage);
            tvVenderName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            linear_cat_details = (LinearLayout)itemView.findViewById(R.id.linear_cat_details);
            image_shortlist = (ImageView)itemView.findViewById(R.id.image_shortlist);

        }
    }

    private void showDialog(String message,String number) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_enquire_successfull, null, false);
        MaterialButton ok = view.findViewById(R.id.ok);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        RegularTextView messageTv = view.findViewById(R.id.message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            messageTv.setText(message);
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(mContext)
                    .setView(view)
                    .setBackground(mContext.getDrawable(R.drawable.grid_bg))
                    .setCancelable(false)
                    .show();
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    String p = "tel:" + number;
                    i.setData(Uri.parse(p));
                    mContext.startActivity(i);
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

    private boolean validateForm(){
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

}
