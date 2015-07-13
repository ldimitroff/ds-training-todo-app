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
    private Boolean finished;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
