package com.business.findtrue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.business.findtrue.R;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.listner.PackageListner;
import com.business.findtrue.model.PackageDetails;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder>{
    Context mContext;
    List<PackageDetails> packageDetailsList;
    PackageListner listner;

    public PackageAdapter(Context mContext, List<PackageDetails> packageDetailsList, PackageListner listner){
        this.mContext = mContext;
        this.packageDetailsList = packageDetailsList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.package_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageDetails packageDetails = packageDetailsList.get(position);

        holder.packageName.setText(packageDetails.getPackageName());
        holder.packagePrice.setText("\u20B9 " + packageDetails.getPrice());
        holder.packageExpDate.setText("Validity : " +packageDetails.getExpiryDay()+" Days");
        holder.packageleadCount.setText("Total leads : "+ packageDetails.getLeadsCount());
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext, PaymentActivity.class));
                /*new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("success")
                        .setContentText("Your enquire has been sent. We will contact you soon. Thank You")
                        .show();*/
                listner.packageClick(position,packageDetails);

            }
        });

    }

    @Override
    public int getItemCount() {
        return packageDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TitleTextView packageName;
        RegularTextView  packagePrice, packageExpDate, packageleadCount;
        RegularButton buy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            packageName = itemView.findViewById(R.id.packageName);
            packagePrice = itemView.findViewById(R.id.packagePrice);
            packageExpDate = itemView.findViewById(R.id.tvPackageExpireTitle);
            packageleadCount = itemView.findViewById(R.id.packageleadCount);
            buy = itemView.findViewById(R.id.buy);
        }
    }
}
