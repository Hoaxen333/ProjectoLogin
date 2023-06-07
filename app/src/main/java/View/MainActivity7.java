package View;

import static mz.ac.isutc.i32.projecto.R.color.purp;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import mz.ac.isutc.i32.projecto.R;
import mz.ac.isutc.i32.projecto.databinding.ActivityMain7Binding;

public class MainActivity7 extends AppCompatActivity {
    private ActivityMain7Binding binding;
    HomeFragment homeFragment = new HomeFragment();
    SettingFragment settingFragment = new SettingFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain7Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity7.this, purp));

        //binding.bottom.setBackgroundColor(Color.MAGENTA);
        //binding.bottom.setItemActiveIndicatorColor(ColorStateList.valueOf(Color.MAGENTA));

        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(),homeFragment).commit();
        binding.bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(),homeFragment).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(),settingFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}