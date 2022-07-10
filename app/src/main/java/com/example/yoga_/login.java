package com.example.yoga_;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class login extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    EditText phone_number;
    TextView resendOtpText;
    TextInputLayout otpTextField_;
    TextInputEditText otpTextField;
    String phoneN = "";
    Button getOtpButton, clearInputButton, voiceInputButton;
    ProgressBar progressBar;
    FirebaseAuth FAuth;
    FirebaseFirestore fStore;
    CountryCodePicker ccp;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;
    Boolean getOtpClicked = false;
    RelativeLayout loginMainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("  Register/Login"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        loginMainContainer = (RelativeLayout) findViewById(R.id.loginMainContainer);
        phone_number = (EditText) findViewById(R.id.phoneNumberTextField);
        otpTextField_ = (TextInputLayout) findViewById(R.id.otpTextField_);
        otpTextField = (TextInputEditText) findViewById(R.id.otpTextField);
        resendOtpText = (TextView) findViewById(R.id.resendOtpText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        voiceInputButton = findViewById(R.id.voiceInputButton);
        clearInputButton = findViewById(R.id.clearInputButton);
        getOtpButton = findViewById(R.id.getOtpButton);
        getOtpButton.setEnabled(false);
        ccp = findViewById(R.id.ccp);


        // Firebase auth
        FAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // To validate the phone number
        phone_number.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable ss) {


            }

            public void beforeTextChanged(CharSequence ss, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence ss, int start,
                                      int before, int count) {

                String s = ss.toString();

                //s = s.replaceAll("[^0-9]", "");
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(s);
                boolean b = m.find();

                if (s.length() < 10) {
                    getOtpButton.setEnabled(false);
                    phone_number.setError(null);
                }
                if (s.length() == 10 && b) {
                    phone_number.setError("invalid phone number");
                    getOtpButton.setEnabled(false);
                }
                if (s.length() == 10 && !b) {
                    phone_number.setError(null);
                    getOtpButton.setEnabled(true);
                }
                if (s.length() > 10) {
                    getOtpButton.setEnabled(false);
                    phone_number.requestFocus();
                    phone_number.setError("invalid phone number");

                }
            }
        });


        voiceInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakNow(view);

            }
        });

        clearInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneN = "";
                if (verificationInProgress == true) {
                    verificationInProgress = false;
                }

                phone_number.setText(phoneN);
                getOtpButton.setText("GET OTP");
                if (getOtpClicked) {
                    getOtpButton.setEnabled(true);
                } else
                    getOtpButton.setEnabled(false);
                otpTextField_.setVisibility(View.GONE);
                phone_number.requestFocus();
            }
        });
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtpText.setVisibility(View.VISIBLE);
                getOtpClicked = true;
                if (getOtpButton.getText().toString().contains("VERIFY")) {

                    Snackbar.make(loginMainContainer, "Verifying otp...", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.green))
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                }

                if (!verificationInProgress) {
                    // Integer len = phone_number.getText().toString().length();
                    String phoneN = "+" + ccp.getSelectedCountryCode() + phone_number.getText().toString();
                    Log.d(TAG, "On Click  " + phoneN);
                    getOtpButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    Snackbar.make(loginMainContainer, "Sending OTP...", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.red_500))
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                    //state.setText("Sending otp...");
                    // state.setVisibility(View.VISIBLE);
                    requestOTP(phoneN);
                    getOtpButton.setEnabled(false);
                } else {
                    String userOTP = otpTextField.getText().toString();
                    if (!userOTP.isEmpty() && userOTP.length() == 6) {
                        getOtpButton.setEnabled(true);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);
                        verifyAuth(credential);
                    } else {

                        otpTextField.setError("Invalid OTP");
                    }
                }

            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FAuth.getCurrentUser() != null) {
            progressBar.setVisibility(View.GONE);
            checkUserProfile();
        }
    }


    private void verifyAuth(PhoneAuthCredential credential) {
        FAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUserProfile();
                } else {

                    Snackbar.make(loginMainContainer, "OTP verification failed!", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                }
            }
        });
    }


    private void checkUserProfile() {
        DocumentReference docRef = fStore.collection("users").document(FAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String visibilityIssue = documentSnapshot.getString("visibilityIssue");
                String physicallyHandicapped = documentSnapshot.getString("physicallyHandicapped");
                if (documentSnapshot.exists()) {
//                       intent i = new intent(login.this, mainactivity.class);
//                       i.putextra("vissue",visibilityissue);
//                       i.putextra("pissue", physicallyhandicapped);
//                       startActivity(i);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), addDetails.class));
                }
                finish();
            }
        });
    }

    private void requestOTP(String phoneN) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneN, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                progressBar.setVisibility(View.GONE);
                otpTextField_.setVisibility(View.VISIBLE);
                otpTextField_.requestFocus();
                //state.setVisibility(View.INVISIBLE);
                verificationId = s;
                token = forceResendingToken;
                getOtpButton.setText("Verify");
                getOtpButton.setEnabled(true);
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);

                getOtpButton.setText("    Re-Send OTP");
                getOtpButton.setEnabled(true);
                verificationInProgress = false;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Snackbar.make(loginMainContainer, "OTP, verification failed!", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.red))
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
                getOtpButton.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void speakNow(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 200000000);
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000000);
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000000);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak ");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast
                    .makeText(login.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                phoneN += TextUtils.join(", ", Collections.singleton(Objects.requireNonNull(result).get(0)));
                String b = phoneN.toString();
                String s = b.replaceAll("[^\\d.]", "");
                phone_number.setText(s);

            }
        }
    }
}



