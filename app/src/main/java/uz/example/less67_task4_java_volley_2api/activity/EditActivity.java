package uz.example.less67_task4_java_volley_2api.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import uz.example.less67_task4_java_volley_2api.MainActivity;
import uz.example.less67_task4_java_volley_2api.R;

public class EditActivity extends AppCompatActivity {
    EditText et_name;
    EditText et_salary;
    EditText et_age;
    Button btn_edit;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    void initViews(){
        Bundle extras = getIntent().getExtras();
        et_name = findViewById(R.id.et_nameEdit);
        et_salary = findViewById(R.id.et_SalaryEdit);
        et_age = findViewById(R.id.et_ageEdit);
        btn_edit = findViewById(R.id.btn_submitEdit);
        if (extras !=null){
            Log.d("###","extras not NULL - ");
            id = extras.getInt("id");
            et_name.setText(extras.getString("name") );
            et_salary.setText(""+extras.getInt("salary"));
            et_age.setText(""+extras.getInt("age"));
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                int salary = Integer.parseInt(et_salary.getText().toString().trim());
                int age = Integer.parseInt(et_age.getText().toString().trim());
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("salary",salary);
                intent.putExtra("age",age);
                intent.putExtra("id",id);

                startActivity(intent);
            }
        });

    }
}