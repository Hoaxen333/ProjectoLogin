package View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Controler.Encry;
import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    String nome,password,phone,email,passwold;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textView3.setText(new Recaptcha().recaptchaCode());

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity2.this, R.color.purp));
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);


        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(binding.editTextTextPersonName4.getText()).equals(binding.textView3.getText())){
                    nome = String.valueOf(binding.editTextTextPersonName.getText());
                    password = String.valueOf(binding.editTextTextPassword.getText());
                    passwold = Encry.doHashing(password);
                    try{
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(nome).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        DataSnapshot dataSnapshot = task.getResult();
                                        if(passwold.equals(String.valueOf(dataSnapshot.child("password").getValue()))){
                                            phone = String.valueOf(dataSnapshot.child("phone").getValue());
                                            Intent intent = new Intent(MainActivity2.this,MainActivity4.class);
                                            intent.putExtra("phone",phone);
                                            startActivity(intent);
                                            Toast.makeText(MainActivity2.this, "Sucesso", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity2.this, "Senha Invalida", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MainActivity2.this, "O Usuario nao existe.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(MainActivity2.this, "A Leitura Falhou", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(MainActivity2.this, "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity2.this, "Erro", Toast.LENGTH_SHORT).show();
                    binding.textView3.setText(new Recaptcha().recaptchaCode());
                    binding.editTextTextPersonName4.setText("");
                }
            }
        });

        binding.textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,MainActivity5.class);
                startActivity(intent);
            }
        });
    }
    void signIn(){
        Intent sIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(sIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MainActivity7.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}