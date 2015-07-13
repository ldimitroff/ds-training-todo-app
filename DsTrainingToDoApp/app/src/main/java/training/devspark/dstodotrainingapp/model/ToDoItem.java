package training.devspark.dstodotrainingapp.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by guillermo on 7/13/15.
 */

public class ToDoItem extends RealmObject implements Serializable {

	@PrimaryKey
	private long id = System.nanoTime();
	private String name = "";

	private boolean isTaskFinished;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTaskFinished() {
		return isTaskFinished;
	}

	public void setIsTaskFinished(boolean isTaskFinished) {
		this.isTaskFinished = isTaskFinished;
	}
}
