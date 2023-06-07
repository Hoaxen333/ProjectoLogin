package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import Controler.Encry;
import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain6Binding;

public class MainActivity6 extends AppCompatActivity {
    private ActivityMain6Binding binding;
    String verificationCode,phone,nome;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    Long timeoutSeconds =100L;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity6.this, R.color.purp));

        nome = getIntent().getExtras().getString("nome");
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
                Toast.makeText(MainActivity6.this,"A verificacao falhou",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                resendingToken = forceResendingToken;
                Toast.makeText(MainActivity6.this,"OTP enviado com Sucesso",Toast.LENGTH_LONG).show();
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
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(nome).child("password").setValue(Encry.doHashing(String.valueOf(binding.editTextTextPassword2.getText())));
                    Toast.makeText(MainActivity6.this, "Password Mofificada com Sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity6.this, "A verificacao do OTP Falhou", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClick(View v) {
        if(v.getId() == binding.button6.getId()){
            String enteredOTP = String.valueOf(binding.editTextNumber4.getText());
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOTP);
            signIn(credential);
        }
        if(v.getId() == binding.textView15.getId()){
            sendOTP(phone,true);
        }
    }
    void startResendTimer(){
        binding.textView15.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                binding.textView15.setText("Reenviar OTP em "+timeoutSeconds+" s");
                if(timeoutSeconds==0){
                    timeoutSeconds = 100L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        binding.textView15.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}