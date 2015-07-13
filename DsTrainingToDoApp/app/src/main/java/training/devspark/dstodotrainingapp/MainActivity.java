package training.devspark.dstodotrainingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import training.devspark.dstodotrainingapp.adapter.ToDoAdapter;
import training.devspark.dstodotrainingapp.model.ToDoItem;

public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

	private static final int NEW_EDIT_TODO = 1;
	public static final String NEW_TODO_RESULT_FIELD = "result";
	public static final String TODO_ITEM = "todoitem";
	public static final String TODO_POSITION = "todoposition";

	private List<ToDoItem> toDoList;
	private ToDoAdapter toDoAdapter;
	private ListView toDoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindViews();
		toDoList = new ArrayList<>();
		toDoAdapter = new ToDoAdapter(this, toDoList);

		toDoListView.setAdapter(toDoAdapter);

		// ASYNC CALL REQUIRED
		loadToDoList();
	}

	private void bindViews() {
		toDoListView = (ListView)findViewById(R.id.toDolistView);

		toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NewEditToDo.class);
				intent.putExtra(TODO_ITEM, toDoList.get(position));
				intent.putExtra(TODO_POSITION, position);
				startActivityForResult(intent, NEW_EDIT_TODO);
			}
		});

		toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.delete_entry)).setMessage(getString(R.string.are_sure_delete) + toDoList.get(position).getName() + " ?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MainActivity.this, getString(R.string.removing_to_do) + toDoList.get(position).getName(), Toast.LENGTH_SHORT).show();
						deleteFromRealm(toDoList.get(position));
						toDoList.remove(position);
						toDoAdapter.notifyDataSetChanged();
					}
				}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MainActivity.this, getString(R.string.user_cancel), Toast.LENGTH_SHORT).show();
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
				return true;
			}
		});
	}

	@Override
	protected void onPause() {

		// ASYNC CALL REQUIRED
		saveTodoList(toDoList);

		super.onPause();
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
			intent.putExtra(TODO_ITEM, new ToDoItem());
			startActivityForResult(intent, NEW_EDIT_TODO);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NEW_EDIT_TODO) {
			if (resultCode == RESULT_OK) {
				ToDoItem result = (ToDoItem)data.getSerializableExtra(NEW_TODO_RESULT_FIELD);
				int position = data.getIntExtra(TODO_POSITION, -1);
				if (result != null && !result.getName().isEmpty()) {
					if (position >= 0) {
						toDoList.set(position, result);
					} else {
						toDoList.add(result);
					}
					toDoAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
				}
			}
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.user_cancel), Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Called when the checked state of a compound button has changed.
	 *
	 * @param buttonView
	 *            The compound button view whose state has changed.
	 * @param isChecked
	 *            The new checked state of buttonView.
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int pos = toDoListView.getPositionForView(buttonView);
		toDoList.get(pos).setIsTaskFinished(isChecked);
		toDoAdapter.notifyDataSetChanged();

		String toDoState = isChecked ? " Finished" : " Un-finished";
		Toast.makeText(this, toDoList.get(pos).getName() + toDoState, Toast.LENGTH_SHORT).show();
	}

	private void loadToDoList() {
		if (toDoList == null) return;
		toDoList.clear();
		Realm realm = null;
		try {
			realm = Realm.getInstance(this);

			for (ToDoItem item : realm.allObjects(ToDoItem.class)) {
				//Copy the ToDoItem in order to insert to the local list
				ToDoItem itemToInsert = new ToDoItem();
				itemToInsert.setId(item.getId());
				itemToInsert.setName(item.getName());
				itemToInsert.setIsTaskFinished(item.isTaskFinished());
				toDoList.add(itemToInsert);
			}

			toDoAdapter.notifyDataSetChanged();
		} finally {
			if (realm != null) {
				realm.close();
			}
		}
	}

	private void saveTodoList(List<ToDoItem> list) {
		if (list == null) return;
		Realm realm = null;
		try {
			realm = Realm.getInstance(this);

			// Copy the object to Realm. Any further changes must happen on realmUser
			realm.beginTransaction();
			realm.copyToRealmOrUpdate(list);
			realm.commitTransaction();
		} finally {
			if (realm != null) {
				realm.close();
			}
		}
	}

	private void deleteFromRealm(ToDoItem item) {
		if (item == null) return;
		Realm realm = null;
		try {
			realm = Realm.getInstance(this);
			realm.beginTransaction();

			//Query the ToDoItem and then remove from Realm
			realm.where(ToDoItem.class).equalTo("id", item.getId()).findFirst().removeFromRealm();

			realm.commitTransaction();
		} finally {
			if (realm != null) {
				realm.close();
			}
		}
	}

}
