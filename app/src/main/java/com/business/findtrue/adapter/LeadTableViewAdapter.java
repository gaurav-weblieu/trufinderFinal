package com.business.findtrue.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.business.findtrue.R;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.model.TotalLeadsResponseModel;
import com.business.findtrue.model.UserLeads;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;
import com.business.findtrue.vendor.PackageActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadTableViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Context mContext;
    List<UserLeads> userLeadsList;
    private ApiInterface apiInterface;
    private LeadViewSuccess listner;

    public LeadTableViewAdapter(Context mContext, List<UserLeads> userLeadsList, LeadViewSuccess listner) {
        this.mContext = mContext;
        this.userLeadsList = userLeadsList;
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        this.listner = listner;

    }

    public void setList(List<UserLeads> userLeadsList) {
        //this.userLeadsList.addAll(userLeadsList);
        this.userLeadsList = userLeadsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_item, parent, false);
        return new ItemViewHolder(itemView);
//        if (viewType == TYPE_ITEM){
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_item, parent, false);
//            return new ItemViewHolder(itemView);
//
//        }else if (viewType == TYPE_HEADER) {
//            //Inflating header view
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leads_header, parent, false);
//            return new HeaderViewHolder(itemView);
//        }else
//            return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        UserLeads userLeads = userLeadsList.get(position);
        itemViewHolder.txtName.setText(userLeads.getName());
        itemViewHolder.txtBookingDate.setText("Booking Date : " + userLeads.getBookingdate());

        if (userLeads.getStatus().equals("Viewed")) {
            itemViewHolder.txtEmail.setText("Email Id : " + userLeads.getEmail());
            itemViewHolder.txtContactNo.setText("Contact No : " + userLeads.getContactno());
            itemViewHolder.viewDetail.setVisibility(View.GONE);
            itemViewHolder.call.setVisibility(View.VISIBLE);
        } else {
            String starEmail = userLeads.getEmail().replaceAll("(^[^@]{2}|(?!^)\\G)[^@]", "$1*");
            String inputPhoneNum = userLeads.getContactno().replaceAll(".(?=.{4})", "*");
            itemViewHolder.txtEmail.setText("Email Id : " + starEmail);
            itemViewHolder.txtContactNo.setText("Contact No : " + inputPhoneNum);
            itemViewHolder.viewDetail.setVisibility(View.VISIBLE);
            itemViewHolder.call.setVisibility(View.GONE);
        }
        itemViewHolder.viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemViewHolder.call.setVisibility(View.VISIBLE);
                itemViewHolder.txtContactNo.setText("Contact No : " + userLeads.getContactno());
                itemViewHolder.txtEmail.setText("Contact No : " + userLeads.getEmail());
            }
        });

        itemViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + userLeads.getContactno();
                i.setData(Uri.parse(p));
                mContext.startActivity(i);

            }
        });

        itemViewHolder.viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isDigitsOnly(CommonUtils.getPreferencesString(holder.itemView.getContext(), AppConstant.TOTAL_LEADS))) {
                    if (Integer.parseInt(CommonUtils.getPreferencesString(holder.itemView.getContext(), AppConstant.TOTAL_LEADS)) > 0)
                        leadView(itemViewHolder, userLeads, holder.itemView.getContext(), userLeads.getId());
                    else {
                        new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warring")
                                .setContentText("You don't have leads to view. Please purchase package.")
                                .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), PackageActivity.class));
                                    }
                                })
                                .show();
                    }
                } else {
                    new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warring")
                            .setContentText("You don't have leads to view. Please purchase package.")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), PackageActivity.class));
                                }
                            })
                            .show();
                }
            }
        });


//        itemViewHolder.txtBookingDate.setText(userLeads.getBookingdate());
//        if (holder instanceof HeaderViewHolder){
//            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
//
//        }else if (holder instanceof ItemViewHolder){
//
//        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return TYPE_HEADER;
//        }
//        return TYPE_ITEM;
//    }

    @Override
    public int getItemCount() {
        return userLeadsList.size();
        //return userLeadsList.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            //headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TitleTextView txtName;
        RegularTextView txtEmail, txtContactNo, txtBookingDate, txtView, viewDetail;
        ImageView call, view;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtContactNo = itemView.findViewById(R.id.txtContactNo);
            txtBookingDate = itemView.findViewById(R.id.txtBookingDate);
            call = itemView.findViewById(R.id.call);
            viewDetail = itemView.findViewById(R.id.viewDetail);
            view = itemView.findViewById(R.id.view);
            //txtView = (TextView)itemView.findViewById(R.id.txtView);
        }
    }

    private void leadView(ItemViewHolder itemView, UserLeads userLeads, Context context, String id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        apiInterface.leadsView(CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID), id).enqueue(new Callback<TotalLeadsResponseModel>() {
            @Override
            public void onResponse(Call<TotalLeadsResponseModel> call, Response<TotalLeadsResponseModel> response) {
                pDialog.dismiss();
                itemView.txtEmail.setText("Email Id : " + userLeads.getEmail());
                itemView.txtContactNo.setText("Contact No : " + userLeads.getContactno());
                itemView.viewDetail.setVisibility(View.GONE);
                itemView.call.setVisibility(View.VISIBLE);
                CommonUtils.savePreferenceString(context, AppConstant.TOTAL_LEADS, response.body().getData().getLastUpdtTotlLeads());
                listner.onLeadViewSuccess(response.body().getData().getLastUpdtTotlLeads());
            }

            @Override
            public void onFailure(Call<TotalLeadsResponseModel> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface LeadViewSuccess {
        void onLeadViewSuccess(String leads);
    }
}
