package com.business.findtrue.vendor;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.business.findtrue.R;
import com.business.findtrue.adapter.FaqAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.model.FAQData;
import com.business.findtrue.model.GetFAQ;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFAQActivity extends BaseActivity {

    private Context context = AddFAQActivity.this;
    private ImageView back;
    private EditText editQuestion, editAnswer;
    private RelativeLayout btnSubmit;
    String VENDOR_ID;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewFAQ;
    private FaqAdapter faqAdapter;
    private List<FAQData> listFaqList;
    private ProgressBar progressBar3;
    private FloatingActionButton addFAQ;
    AlertDialog materialAlertDialogBuilder;

    RegularTextInputLayout inputLayoutQuestion,inputLayoutAnswer;
    RegularTextInputEditText question,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_f_a_q);

        back = (ImageView)findViewById(R.id.back);
//        editQuestion = (EditText)findViewById(R.id.editQuestion);
//        editAnswer = (EditText)findViewById(R.id.editAnswer);
 //       btnSubmit = (RelativeLayout) findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        recyclerViewFAQ = (RecyclerView)findViewById(R.id.recyclerViewFAQ);
        progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);
        addFAQ = findViewById(R.id.addFAQ);
        listFaqList = new ArrayList<>();

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        //getFAQAnswerQuestion();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(AddFAQActivity.this).inflate(R.layout.dialog_add_faq, null, false);
                RegularButton submit = view1.findViewById(R.id.submit);
                ImageView close = view1.findViewById(R.id.close);
                inputLayoutQuestion= view1.findViewById(R.id.inputLayoutQuestion);
                inputLayoutAnswer = view1.findViewById(R.id.inputLayoutAnswer);
                question = view1.findViewById(R.id.question);
                answer = view1.findViewById(R.id.answer);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AddFAQActivity.this)
                            .setView(view1)
                            .setBackground(getDrawable(R.drawable.grid_bg))
                            .setCancelable(false)
                            .show();
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog();
                            apiInterface.updateFAQ(question.getText().toString(), answer.getText().toString(), VENDOR_ID).enqueue(new Callback<ResponseRequest>() {
                                @Override
                                public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                                    hideDialog();
                                    if (response.isSuccessful()){
                                        getFAQAnswerQuestion();
                                        materialAlertDialogBuilder.dismiss();
                                        Toast.makeText(context, "Add Successfully", Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseRequest> call, Throwable t) {
                                    hideDialog();
                                }
                            });
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialAlertDialogBuilder.dismiss();
                        }
                    });

                }
            }
        });

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                apiInterface.updateFAQ(editQuestion.getText().toString(), editAnswer.getText().toString(), VENDOR_ID).enqueue(new Callback<ResponseRequest>() {
//                    @Override
//                    public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
//                        if (response.isSuccessful()){
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(context, "Add Successfully", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseRequest> call, Throwable t) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });
    }

    private void getFAQAnswerQuestion(){

        apiInterface.getVendorFAQ(CommonUtils.getPreferencesString(context, AppConstant.TOKEN),VENDOR_ID).enqueue(new Callback<GetFAQ>() {
            @Override
            public void onResponse(Call<GetFAQ> call, Response<GetFAQ> response) {
                if (response.isSuccessful()){
                    progressBar3.setVisibility(View.GONE);
                    System.out.println("----------------------"+response.body().getMessage()+"---------"+response.body().getData().size());
                    listFaqList.clear();
                    List<FAQData> results = response.body().getData();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerViewFAQ.setLayoutManager(linearLayoutManager);
                    listFaqList.addAll(results);
                    faqAdapter = new FaqAdapter(context, listFaqList);
                    recyclerViewFAQ.setAdapter(faqAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetFAQ> call, Throwable t) {
                progressBar3.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFAQAnswerQuestion();
    }
}