package com.business.findtrue.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.business.findtrue.R;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;

import java.math.BigDecimal;
import java.text.Format;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseFragment extends Fragment {

    protected View root;
    protected Unbinder unbinder;
    protected static NavController navController=null;
    protected ApiInterface apiInterface;
    SweetAlertDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        pDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE);
    }

    public BaseFragment() {

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected void showDialog(){
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    protected void showSuccessMessage(String title,String message){
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    protected void showErrorMessage(String title,String message){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    protected static void showPopDialog(Context context, String title, String message){
        new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    protected static void showPopDialogWithConfirmation(Context context, String title, String message){
        new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no,(dialog, which) -> dialog.dismiss())
                .show();
    }

    protected static void showSnackbar(View view, String message){
        /*Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();*/
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
        snackbar.setTextColor(ContextCompat.getColor(view.getContext(), R.color.purple_500));
       /*TextView textView = (TextView) snackBarView.findViewById(android.R.id.text1);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));*/
        snackbar.show();

    }

    protected static int getLoanAmount(String number){
        return (int) (((Integer.parseInt(number) * 0.6)/850) * 100000);
    }

    protected static String convertIndianFormat(String number){
        String finalAmount=null;
        Format format = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            finalAmount = format.format(new BigDecimal(convertStringToInt(number)));
        }
        return finalAmount;
    }

    protected static String convertStringToInt(String number){
        return number.toString().replaceAll("[^0-9]+","");
    }

}
