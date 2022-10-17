package uz.example.less67_task4_java_volley_2api;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uz.example.less67_task4_java_volley_2api.activity.CreateActivity;
import uz.example.less67_task4_java_volley_2api.adapter.EmployeeAdapter;
import uz.example.less67_task4_java_volley_2api.model.BaseModel;
import uz.example.less67_task4_java_volley_2api.model.Employee;
import uz.example.less67_task4_java_volley_2api.network.VolleyHandler;
import uz.example.less67_task4_java_volley_2api.network.VolleyHttp;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Employee> employees = new ArrayList<>();
    ProgressBar pb_loading;
    FloatingActionButton floating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    void initViews(){
        pb_loading = findViewById(R.id.pb_loading);
        floating = findViewById(R.id.floating);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        apiEmployeeList();
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateActivity();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            Log.d("###","extras not NULL - ");
            String edit_name = extras.getString("name");
            int edit_salary = extras.getInt("salary");
            int edit_age = extras.getInt("age");
            int edit_id = extras.getInt("id");
            Employee employee = new Employee(edit_id,edit_name,edit_salary,edit_age);
            Toast.makeText(MainActivity.this, "Employee Prepared to Edit", Toast.LENGTH_LONG).show();

            apiEmployeeEdit(employee);

        }

    }


    public ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == 78) {
                        Intent data = result.getData();

                        if (data !=null){
                            String edit_name = data.getStringExtra("name");
                            String edit_salary = data.getStringExtra("salary");
                            String edit_age = data.getStringExtra("age");
                            Employee employee = new Employee(edit_name,Integer.parseInt(edit_salary),Integer.parseInt(edit_age));
                            Toast.makeText(MainActivity.this, "Title modified", Toast.LENGTH_LONG).show();

                            apiEmployeeCreate(employee);
                        }
                        // your operation....
                    }else {
                        Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                    }

                }
            });
    void refreshAdapter(ArrayList<Employee> employees) {
        EmployeeAdapter adapter = new EmployeeAdapter(this, employees);
        recyclerView.setAdapter(adapter);
    }

    void openCreateActivity(){
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        launchSomeActivity.launch(intent);
    }

    public void dialogEmployee(Employee employee) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apiEmployeeDelete(employee);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void apiEmployeeList() {
        pb_loading.setVisibility(View.VISIBLE);
        VolleyHttp.get(VolleyHttp.API_LIST_EMPLOYEE, VolleyHttp.paramsEmpty(), new VolleyHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d("@@@onResponse ", "" + employees.size());
                Log.d("@@@onResponse ", "" + response);
                pb_loading.setVisibility(View.GONE);
                employees.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("employee_name");
                            String salary = jsonObject1.getString("employee_salary");
                            String age = jsonObject1.getString("employee_age");
                            String id = jsonObject1.getString("id");
                            Employee employee = new Employee(Integer.parseInt(id),name,Integer.parseInt(salary),Integer.parseInt(age));
                            employees.add(employee);
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                 refreshAdapter(employees);
            }

            @Override
            public void onError(String error) {
                Log.d("@@@onErrorResponse ", error);
                apiEmployeeList();
            }
        });
    }

    private void apiEmployeeCreate(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);
        VolleyHttp.post(VolleyHttp.API_CREATE_EMPLOYEE, VolleyHttp.paramsCreate(employee), new VolleyHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d("@@@onResponse ", "" + response);
                Toast.makeText(MainActivity.this, employee.getEmployee_name()+" Created", Toast.LENGTH_LONG).show();
                apiEmployeeList();
            }

            @Override
            public void onError(String error) {
                Log.d("@@@onErrorResponse ", error);
                apiEmployeeCreate(employee);
            }
        });
    }
    private void apiEmployeeEdit(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);
        VolleyHttp.put(VolleyHttp.API_UPDATE_EMPLOYEE+employee.getId(), VolleyHttp.paramsUpdate(employee), new VolleyHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(MainActivity.this, employee.getEmployee_name()+" Edited", Toast.LENGTH_LONG).show();
                Log.d("@@@onResponse ", response);
                apiEmployeeList();
            }

            @Override
            public void onError(String error) {
                Log.d("@@@onErrorResponse ", error);
                apiEmployeeEdit(employee);
            }
        });
    }
    private void apiEmployeeDelete(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);

        VolleyHttp.del(VolleyHttp.API_DELETE_EMPLOYEE + employee.getId(), new VolleyHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d("@@@",response);
                Toast.makeText(MainActivity.this, "Employer "+employee.getId()+" Deleted", Toast.LENGTH_LONG).show();
                apiEmployeeList();
                //pb_loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Log.d("@@@onErrorResponse ", error);
                apiEmployeeDelete(employee);
            }
        });
    }
}