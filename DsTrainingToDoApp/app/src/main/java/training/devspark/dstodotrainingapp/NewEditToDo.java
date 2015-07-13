package training.devspark.dstodotrainingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import training.devspark.dstodotrainingapp.model.ToDoItem;

public class NewEditToDo extends ActionBarActivity {

	private Button addToDoButton;
	private EditText toDoEditText;
	private ToDoItem toDoItem;
	private int toDoPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_to_do);

		Intent i = getIntent();
		toDoItem = (ToDoItem) i.getSerializableExtra(MainActivity.TODO_ITEM);
		toDoPosition = i.getIntExtra(MainActivity.TODO_POSITION, -1);
		bindViews();
	}

	private void bindViews() {
		toDoEditText = (EditText)findViewById(R.id.toDoEditText);

		if (toDoItem != null && !toDoItem.getName().isEmpty()) {
			toDoEditText.setText(toDoItem.getName());
		}

		addToDoButton = (Button)findViewById(R.id.addToDoButton);
		addToDoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(MainActivity.TODO_POSITION, toDoPosition);
				toDoItem.setName(toDoEditText.getText().toString());
				returnIntent.putExtra(MainActivity.NEW_TODO_RESULT_FIELD, toDoItem);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

		if (toDoPosition >= 0) {
			setTitle(getString(R.string.modify_to_do));
			addToDoButton.setText(getString(R.string.save_to_do));
		}
	}
}
