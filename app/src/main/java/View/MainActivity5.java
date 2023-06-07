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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain4Binding;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain5Binding;

public class MainActivity5 extends AppCompatActivity {
    private ActivityMain5Binding binding;
    private String nome,phone;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity5.this, R.color.purp));

        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    nome = binding.editTextPhone2.getText().toString();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(nome).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().exists()){
                                    DataSnapshot dataSnapshot = task.getResult();
                                        phone = String.valueOf(dataSnapshot.child("phone").getValue());
                                        Intent intent = new Intent(MainActivity5.this,MainActivity6.class);
                                        intent.putExtra("phone",phone);
                                        intent.putExtra("nome",nome);
                                        startActivity(intent);
                                }else{
                                    Toast.makeText(MainActivity5.this, "O Usuario nao existe.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity5.this, "A Leitura Falhou", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(MainActivity5.this, "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}