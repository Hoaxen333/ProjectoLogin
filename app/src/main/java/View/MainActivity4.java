package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain4Binding;

public class MainActivity4 extends AppCompatActivity {
    private ActivityMain4Binding binding;
    String verificationCode,phone;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    Long timeoutSeconds =30L;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity4.this, R.color.purp));

        phone = getIntent().getExtras().getString("phone");
        sendOTP(phone,false);
    }
    void sendOTP(String phoneNumber,boolean isResend){
        startResendTimer();
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(timeoutSeconds, TimeUnit.SECONDS).setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity4.this,"A verificacao falhou",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                resendingToken = forceResendingToken;
                Toast.makeText(MainActivity4.this,"OTP enviado com Sucesso",Toast.LENGTH_LONG).show();
            }
        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity4.this, "Sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity4.this,MainActivity7.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity4.this, "A verificacao do OTP Falhou", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClick(View v) {
        if(v.getId() == binding.button5.getId()){
            String enteredOTP = String.valueOf(binding.editTextNumber3.getText());
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOTP);
            signIn(credential);
        }
        if(v.getId() == binding.textView11.getId()){
            sendOTP(phone,true);
        }
    }
    void startResendTimer(){
        binding.textView11.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                binding.textView11.setText("Reenviar OTP em "+timeoutSeconds+" s");
                if(timeoutSeconds==0){
                    timeoutSeconds = 30L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        binding.textView11.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}