package training.devspark.dstodotrainingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewEditToDo extends ActionBarActivity {

	private Button addToDoButton;
	private EditText toDoEditText;
	private String toDoText;
	private int toDoPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_to_do);

		Intent i = getIntent();
		toDoText = i.getStringExtra(MainActivity.TODO_TEXT);
		toDoPosition = i.getIntExtra(MainActivity.TODO_POSITION, -1);
		bindViews();
	}

	private void bindViews() {
		toDoEditText = (EditText)findViewById(R.id.toDoEditText);

		if (toDoText != null && !toDoText.isEmpty()) {
			toDoEditText.setText(toDoText);
		}

		addToDoButton = (Button)findViewById(R.id.addToDoButton);
		addToDoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(MainActivity.TODO_POSITION, toDoPosition);
				returnIntent.putExtra(MainActivity.NEW_TODO_RESULT_FIELD, toDoEditText.getText().toString());
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}
}
