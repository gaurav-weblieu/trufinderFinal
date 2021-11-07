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
import com.business.findtrue.model.SendEnquiry;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.search.Vendor;
import com.business.findtrue.service.AppConfig;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchingAdapter extends RecyclerView.Adapter<SearchingAdapter.ViewHolder>{
    Context mContext;
    private List<Vendor> vendorList;

    int mYear, mMonth, mDay;
    String vndrbox, category_id, city_id, areapinid, dateTime;
    String checked;

    RegularTextInputLayout inputLayoutName,inputLayoutEmail,inputLayoutMobile,inputLayoutBookingDate;
    RegularTextInputEditText name,email,mobile,bookingDate;
    ApiInterface apiInterface;
    AlertDialog materialAlertDialogBuilder;

    public SearchingAdapter(Context mContext, List<Vendor> vendorList){
        this.mContext = mContext;
        this.vendorList = vendorList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Vendor vendorName = vendorList.get(position);
        System.out.println("id-------------------------"+vendorName.getId());
        Glide.with(mContext).load(AppConfig.CATEGORY_DETAILS_IMAGE+vendorName.getProfilepic()).centerCrop().placeholder(R.drawable.no_img).into(holder.ivImage);
        holder.tvName.setText(vendorName.getVendorName()+", "+vendorName.getCity());
        holder.tvAddress.setText(vendorName.getAddress());
        holder.tvPrice.setText("PACKAGE PRICE : " +"\u20B9" +vendorName.getPackagePrice());

        holder.linear_cat_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewProfileActivity.class);
                intent.putExtra("PROFILE_IMAGE", vendorList.get(position).getProfilepic());
                intent.putExtra("VENDOR_NAME", vendorList.get(position).getVendorName());
                CommonUtils.savePreferenceString(mContext, AppConstant.VENDOR_EMAIL, vendorList.get(position).getEmail());
                mContext.startActivity(intent);
            }
        });

        holder.btnSendEnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSheet dialogSheet = new DialogSheet(mContext, true);
                View view = View.inflate(mContext, R.layout.fragment_enquiry_dialog, null);
                dialogSheet.setView(view);
                dialogSheet.show();
                //AppCompatButton btnClose = (AppCompatButton)view.findViewById(R.id.btnClose);
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
                        if (validationCheck()){
                            SweetAlertDialog pDialog = new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            String id = vendorList.get(position).getId();
                            String city_id = vendorList.get(position).getCityId();
                            String areapinid = vendorList.get(position).getPincode();
                            category_id = vendorList.get(position).getCategoryId();
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

                            apiInterface.sendEnquiry(id, category_id, city_id, areapinid, name.getText().toString(), email.getText().toString(), country_code, mobile.getText().toString(), dateTime, checked, AppConstant.PUBLIC_IP_ADDRESS).enqueue(new Callback<ResponseRequest>() {
                                @Override
                                public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                                    pDialog.hide();
                                    if (response.isSuccessful()){
                                        System.out.println("-------------------------------------"+response.body().getMessage());
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

    @Override
    public int getItemCount() {
        return vendorList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView ivImage;
        TitleTextView tvName;
        TitleTextView btnSendEnquery;
        RegularTextView tvPrice,tvAddress;
        LinearLayout linear_cat_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = (RoundedImageView)itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnSendEnquery =  itemView.findViewById(R.id.btnSendEnquery);
            linear_cat_details = (LinearLayout)itemView.findViewById(R.id.linear_cat_details);

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
    
}
