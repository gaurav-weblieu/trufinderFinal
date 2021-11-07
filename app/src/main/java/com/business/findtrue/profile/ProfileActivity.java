package com.business.findtrue.profile;

import androidx.annotation.Nullable;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marcoscg.dialogsheet.DialogSheet;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private ImageView imageView6, ivEdit;
    private TextView  tvEmailAddress, tvMobileNumber, tvGender, tvUserName, tvUserEmail, tvUserGender;
    private RoundedImageView imageView5;
    String fName, fLastName, emailAddress,mobileNo, gender;
    String Gender;
    String USER_ID;
    private ImageView mImageViewProfileUpdate, imageView4;
    Button uploads;
    File file;
    String imageURL;

    private static final int INTENT_REQUEST_CODE = 100;
    private String mImageUrl = "";

    @BindView(R.id.tvFirstName)
    TitleTextView tvFirstName;
    @BindView(R.id.mobile)
    RegularTextView mobile;
    @BindView(R.id.email)
    RegularTextView email;
    @BindView(R.id.gender)
    RegularTextView genderTV;
    @BindView(R.id.editProfile)
    ImageView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        imageView6 = (ImageView)findViewById(R.id.imageView6);
        imageView5 = (RoundedImageView)findViewById(R.id.imageView5);
        //tvFirstName = (TextView)findViewById(R.id.textView13);
        tvEmailAddress = (TextView)findViewById(R.id.tvEmailAddress);
        tvMobileNumber = (TextView)findViewById(R.id.tvMobileNumber);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvUserEmail = (TextView)findViewById(R.id.tvUserEmail);
        tvUserGender = (TextView)findViewById(R.id.tvUserGender);
        ivEdit = (ImageView)findViewById(R.id.ivEdit);
        mImageViewProfileUpdate = (ImageView)findViewById(R.id.imageView2);
        uploads = (Button)findViewById(R.id.uploads);
        imageView4 = (ImageView)findViewById(R.id.imageView4);



        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        USER_ID = CommonUtils.getPreferencesString(ProfileActivity.this, AppConstant.USER_ID);
        System.out.println("USER_ID================================"+USER_ID);

        //function call get profile
        getProfile();
        //-------------------
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSheet dialogSheet = new DialogSheet(ProfileActivity.this, true);
                View view = View.inflate(ProfileActivity.this, R.layout.edit_profile, null);
                dialogSheet.setView(view);
                dialogSheet.show();
                ArrayList<String> genderList = new ArrayList<>();
                ImageView ivClose = (ImageView)view.findViewById(R.id.ivClose);
                RegularTextInputEditText editFName = view.findViewById(R.id.firstname);
                RegularTextInputEditText editLName = view.findViewById(R.id.lastname);
                RegularTextInputEditText mobile = view.findViewById(R.id.mobile);
                RegularTextInputEditText editEmail = view.findViewById(R.id.email);
                AutoCompleteTextView genderDD = view.findViewById(R.id.gender);
                RegularButton btnSubmit = view.findViewById(R.id.submit);

                genderList.add("Male");
                genderList.add("Female");
                ArrayAdapter arrayAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_1,genderList);
                genderDD.setAdapter(arrayAdapter);
                genderDD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(RegistationActivity.this, ""+i+genderList.get(i), Toast.LENGTH_SHORT).show();
                        gender = genderList.get(i);

                    }
                });
                /*AppCompatButton btnClose = (AppCompatButton)view.findViewById(R.id.btnClose);

                EditText editFName = (EditText)view.findViewById(R.id.editFName);
                EditText editLName = (EditText) view.findViewById(R.id.editLName);
                EditText editEmail = (EditText)view.findViewById(R.id.editEmail);
                RadioGroup rgGroup = (RadioGroup)view.findViewById(R.id.rgGroup);
                RadioButton rbMale = (RadioButton)view.findViewById(R.id.rbMale);
                RadioButton rbFemale = (RadioButton)view.findViewById(R.id.rbFemale);
                AppCompatButton btnSubmit = (AppCompatButton)view.findViewById(R.id.btnSubmit);*/

                editFName.setText(fName);
                editLName.setText(fLastName);
                editEmail.setText(emailAddress);
                if (Gender.equalsIgnoreCase("Male")){
                    //genderDD.setListSelection(0);
                    genderDD.setText(genderDD.getAdapter().getItem(0).toString(), false);
                    gender = "Male";
                }else {
                    genderDD.setText(genderDD.getAdapter().getItem(1).toString(), false);
                    gender = "Female";
                }

                /*rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.rbMale) {
                            gender = "Male";
                            //System.out.println("gender------------------------"+gender);
                        }else if (checkedId == R.id.rbFemale) {
                            gender = "Female";
                            //System.out.println("gender------------------------" + gender);
                        }
                    }
                });*/
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSheet.dismiss();
                    }
                });

                /*btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSheet.dismiss();
                    }
                });*/

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        apiInterface.updateProfile(editFName.getText().toString(), editLName.getText().toString(), editEmail.getText().toString(), gender, USER_ID).enqueue(new Callback<UpdateProfile>() {
                            @Override
                            public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                                System.out.println("--------------------------"+response.body().getMessage());
                                if (response.isSuccessful()){
                                    System.out.println("--------------------------"+response.body().getMessage());
                                    dialogSheet.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                                    getProfile();
                                }
                            }

                            @Override
                            public void onFailure(Call<UpdateProfile> call, Throwable t) {
                                dialogSheet.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile updated failed", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }
        });

        uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("====================++++++++++++++++++++++");
            }
        });

        mImageViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
               // onSelectImageClick(v);
            }
        });
    }

    public void onSelectImageClick(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    uploadFile(USER_ID, getBytes(is), imageURL);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void uploadFile(String user_id, byte[] imageBytes, String image_url){

        RequestBody idBody = RequestBody.create(okhttp3.MultipartBody.FORM, user_id);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", "image.jpg", requestFile);
        RequestBody urlBody = RequestBody.create(okhttp3.MultipartBody.FORM, image_url);

        System.out.println("================================="+idBody.toString()+","+requestFile.toString()+","+urlBody.toString());
        apiInterface.uploadProfilePic(idBody, body, urlBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ProfileActivity.this, "Image Updated Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }



    private void getProfile(){
        apiInterface.getUserProfile(USER_ID).enqueue(new Callback<GetProfile>() {
            @Override
            public void onResponse(Call<GetProfile> call, Response<GetProfile> response) {
                if (response.isSuccessful()){
                    //System.out.println("message========================="+response.body().getMessage());

                    List<LoginData> loginData = response.body().getData();
                    for (LoginData login : loginData){
                        //System.out.println("fname============================"+login.getFname());
                        tvFirstName.setText(login.getFname()+" "+login.getLname());
                        email.setText(login.getEmail());
                        mobile.setText(login.getMobileno());
                        genderTV.setText(login.getGender());
                        System.out.println("");
                        Glide.with(ProfileActivity.this).load(login.getNewPofileImage()).centerCrop().placeholder(R.drawable.user_placeholder).into(imageView5);
                        Glide.with(ProfileActivity.this).load(login.getNewPofileImage()).centerCrop().placeholder(R.drawable.profile_photo).into(imageView4);

                        /*imageURL = login.getNewPofileImage();
                        System.out.println("imageURL----------------------------"+imageURL);
                        tvUserName.setText(login.getFname()+" "+login.getLname());
                        tvUserEmail.setText(login.getEmail());
                        tvUserGender.setText(login.getGender());*/

                        fName = login.getFname();
                        fLastName = login.getLname();
                        emailAddress = login.getEmail();
                        mobileNo = login.getMobileno();

                        Gender = login.getGender();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfile> call, Throwable t) {

            }
        });
    }
}