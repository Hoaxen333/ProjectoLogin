package View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Utilizador;
import Controler.Encry;
import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {
    private ActivityMain3Binding binding;
    String nome,password,email,contacto,passwold;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Encry encry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView3.setText(new Recaptcha().recaptchaCode());

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity3.this, R.color.purp));

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(binding.editTextTextPersonName4.getText()).equals(String.valueOf(binding.textView3.getText()))){
                    try{
                        binding.progressBar.setVisibility(View.VISIBLE);
                        nome = String.valueOf(binding.editTextTextPersonName.getText());
                        password = String.valueOf(binding.editTextTextPassword.getText());
                        passwold  = Encry.doHashing(password);
                        email = String.valueOf(binding.editTextTextPersonName2.getText());
                        contacto = "+258"+String.valueOf(binding.editTextPhone2.getText());

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(nome).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity3.this, "O Username ja existe", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Utilizador user = new Utilizador(nome,passwold,email,contacto);
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference("Users");
                                        databaseReference.child(nome).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                binding.progressBar.setVisibility(View.GONE);
                                                binding.editTextTextPersonName.setText("");
                                                binding.editTextTextPassword.setText("");
                                                binding.editTextPhone2.setText("");
                                                binding.editTextTextPersonName2.setText("");
                                                binding.textView3.setText(new Recaptcha().recaptchaCode());
                                                binding.editTextTextPersonName4.setText("");
                                                Toast.makeText(MainActivity3.this, "Usuario Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity3.this, "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }else{
                                    Toast.makeText(MainActivity3.this, "A Leitura Falhou !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(MainActivity3.this, "Preencha todos os Campos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity3.this, "Erro", Toast.LENGTH_SHORT).show();
                    binding.textView3.setText(new Recaptcha().recaptchaCode());
                    binding.editTextTextPersonName4.setText("");
                }
            }
        });
    }
}