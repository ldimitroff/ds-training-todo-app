package training.devspark.dstodotrainingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import training.devspark.dstodotrainingapp.adapter.ToDoAdapter;

public class MainActivity extends ActionBarActivity {

	private static final int NEW_EDIT_TODO = 1;
	public static final String NEW_TODO_RESULT_FIELD = "result";
	public static final String TODO_TEXT = "todotext";
	public static final String TODO_POSITION = "todoposition";

	private List<String> toDoList;
	private ToDoAdapter toDoAdapter;
	private ListView toDoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindViews();
		toDoList = new ArrayList<String>();
		toDoAdapter = new ToDoAdapter(this, toDoList);

		toDoListView.setAdapter(toDoAdapter);
	}

	private void bindViews() {
		toDoListView = (ListView)findViewById(R.id.toDolistView);

		toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NewEditToDo.class);
				intent.putExtra(TODO_TEXT, toDoList.get(position));
				intent.putExtra(TODO_POSITION, position);
				startActivityForResult(intent, NEW_EDIT_TODO);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_add_task) {
			Intent intent = new Intent(this, NewEditToDo.class);
			startActivityForResult(intent, NEW_EDIT_TODO);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NEW_EDIT_TODO) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra(NEW_TODO_RESULT_FIELD);
				int position = data.getIntExtra(TODO_POSITION, -1);
				if (result != null && !result.isEmpty()) {
					if (position >= 0) {
                        toDoList.set(position, result);
					} else {
						toDoList.add(result);
					}
					toDoAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "Empty Input", Toast.LENGTH_SHORT).show();
				}
			}
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "User Cancelled", Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
