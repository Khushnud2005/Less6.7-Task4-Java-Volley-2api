package uz.example.less67_task4_java_volley_2api.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import uz.example.less67_task4_java_volley_2api.R;

public class CreateActivity extends AppCompatActivity {
    EditText et_name;
    EditText et_salary;
    EditText et_age;
    Button btn_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }
    void initViews(){
        et_name = findViewById(R.id.et_nameCreate);
        et_salary = findViewById(R.id.et_SalaryCreate);
        et_age = findViewById(R.id.et_ageCreate);
        btn_create = findViewById(R.id.btn_submitCreate);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String salary = et_salary.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("salary",salary);
                intent.putExtra("age",age);
                setResult(78,intent);
                CreateActivity.super.onBackPressed();
            }
        });
    }


}