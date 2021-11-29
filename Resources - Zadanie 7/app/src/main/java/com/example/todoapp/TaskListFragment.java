package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    TextView nameTextView;
    TextView dateTextView;
    ImageView iconImageView;
    private boolean subtitleVisible;

    public static final String KEY_EXTRA_TASK_ID = "com.example.todoapp.tasklistfragment.task_id";
    public static final String subtitleVisibleKey = "subtitleVisible";

    public TaskListFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();

    }

    @Override
    public void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(subtitleVisibleKey);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(subtitleVisibleKey, subtitleVisible);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_task_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getID());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        return recyclerView;
    }

    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if (adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    public void updateSubtitle() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount = 0;
        for (Task task : tasks) {
            if (!task.getDone()) {
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format, todoTasksCount);
        if (!subtitleVisible) {
            Log.d("debugging", "hi");
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            iconImageView = itemView.findViewById(R.id.task_item_icon);
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
        }

        public void bind(Task task) {
            Log.d("debugging", "Binding " + task.getName());
            this.task = task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
            if (task.getDone()) {
                iconImageView.setImageResource(R.drawable.ic_check_box_positive);
            } else {
                iconImageView.setImageResource((R.drawable.ic_check_box_negative));
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getID());
            startActivity(intent);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, final int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return  tasks.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}
