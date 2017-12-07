package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Group_member {

	private final StringProperty group_ID;
	private final StringProperty user_ID;

	public Group_member(String group_ID, String user_ID){
		this.group_ID = new SimpleStringProperty(group_ID);
		this.user_ID = new SimpleStringProperty(user_ID);
	}

	public String getGroup_ID() {
		return group_ID.get();
	}

	public void SetGroup_ID(String group_ID) {
		this.group_ID.set(group_ID);
	}

	public StringProperty getGroup_IDProperty(){
		return group_ID;
	}

	public String getUser_ID() {
		return user_ID.get();
	}

	public void SetUser_ID(String user_ID) {
		this.user_ID.set(user_ID);
	}

	public StringProperty getUser_IDProperty(){
		return user_ID;
	}
}
