package com.project.thetechnewsapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {


    private LinearLayout linearLayoutEditProfile;
    private ImageView profilePicture;
    private LinearLayout imageEditProfile;
    private ImageView imageSelect;
    private EditText fullNameEditProfileEt;
    private EditText emailIdEditProfileEt;
    private EditText phoneNumberEditProfileEt;
    private EditText dateOfBirthEditProfile;
    private TextView submitButtonEditProfile;
    private TextView logoutButtonEditProfile;

    private File proImageFile;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        viewProfileApiCall(userId);

        submitButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateApiCall(userId);

            }
        });
        imageEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });

        logoutButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alert");
                builder.setMessage("Do You Want To Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sP = getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
                                SharedPreferences.Editor speditor = sP.edit();
                                speditor.putBoolean("session", false);
                                speditor.commit();

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        return view;
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);

                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                profilePicture.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
    }

    void viewProfileApiCall(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEWPROFILE(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        try {
                            Glide.with(getContext()).load(root.userDetails.get(0).photo).into(profilePicture);
                        } catch (Exception e) {

                        }
                        fullNameEditProfileEt.setText(root.userDetails.get(0).name);
                        emailIdEditProfileEt.setText(root.userDetails.get(0).email);
                        phoneNumberEditProfileEt.setText(root.userDetails.get(0).phone);


                    } else {
                        Toast.makeText(getActivity(), root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        linearLayoutEditProfile = view.findViewById(R.id.linear_layout_edit_profile);
        profilePicture = view.findViewById(R.id.profile_picture);
        imageEditProfile = view.findViewById(R.id.image_edit_profile);
        imageSelect = view.findViewById(R.id.image_select);
        fullNameEditProfileEt = view.findViewById(R.id.fullName_editProfile_et);
        emailIdEditProfileEt = view.findViewById(R.id.emailId_editProfile_et);
        phoneNumberEditProfileEt = view.findViewById(R.id.phoneNumber_editProfile_et);
        dateOfBirthEditProfile = view.findViewById(R.id.dateOfBirth_editProfile);
        submitButtonEditProfile = view.findViewById(R.id.submit_button_editProfile);
        logoutButtonEditProfile = view.findViewById(R.id.logout_button_editProfile);
    }

    private void updateApiCall(String userId) {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), fullNameEditProfileEt.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneNumberEditProfileEt.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailIdEditProfileEt.getText().toString());
        RequestBody rUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        MultipartBody.Part proImageFilePart = null;

        try {
            proImageFilePart = MultipartBody.Part.createFormData("image",
                    proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

        } catch (NullPointerException e) {

        }

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.UPDATE_USER_DETAILS(name, email, phone, proImageFilePart, rUserId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Server Error!!Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), "Server Error!!Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

}