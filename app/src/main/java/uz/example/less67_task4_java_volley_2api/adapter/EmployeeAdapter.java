package uz.example.less67_task4_java_volley_2api.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import uz.example.less67_task4_java_volley_2api.MainActivity;
import uz.example.less67_task4_java_volley_2api.R;
import uz.example.less67_task4_java_volley_2api.activity.EditActivity;
import uz.example.less67_task4_java_volley_2api.model.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    MainActivity activity;
    ArrayList<Employee> items;

    public EmployeeAdapter(MainActivity activity, ArrayList<Employee> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Employee item = items.get(position);

            SwipeLayout sl_swipe = holder.sl_swipe;

            sl_swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
            sl_swipe.addDrag(SwipeLayout.DragEdge.Left,holder.linear_left);
            sl_swipe.addDrag(SwipeLayout.DragEdge.Right,holder.linear_right);

            holder.name.setText("Employee Name: "+ item.getEmployee_name().toUpperCase());
            holder.salary.setText("Employee Salary: : "+ item.getEmployee_salary());
            holder.age.setText("Employee Age: "+ item.getEmployee_age());
            int id = item.getId();

            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.dialogEmployee(item);
                }
            });
            holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity.getBaseContext(), EditActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("name",item.getEmployee_name());
                    intent.putExtra("salary",item.getEmployee_salary());
                    intent.putExtra("age",item.getEmployee_age());
                    //activity.setResult(88,intent);
                    activity.startActivity(intent);

                }
            });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;
        public TextView salary;
        public TextView age;
        public LinearLayout linear_left;
        public LinearLayout linear_right;
        public SwipeLayout sl_swipe;
        TextView tv_delete;
        TextView tv_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = view.findViewById(R.id.tv_name);
            salary = view.findViewById(R.id.tv_salary);
            age = view.findViewById(R.id.tv_age);
            linear_right = view.findViewById(R.id.ll_linear_right);
            linear_left = view.findViewById(R.id.ll_linear_left);
            sl_swipe = view.findViewById(R.id.sl_swipe);
            tv_delete = view.findViewById(R.id.tv_delete);
            tv_edit = view.findViewById(R.id.tv_edit);
        }
    }
}
