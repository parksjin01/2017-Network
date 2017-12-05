package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Word {


	private final StringProperty Name;
	private final StringProperty Kind;

	public Word(String Name, String Kind){
		this.Name = new SimpleStringProperty(Name);
		this.Kind = new SimpleStringProperty(Kind);
	}

	public String getName() {
		return Name.get();
	}

	public void SetName(String Name) {
		this.Name.set(Name);
	}

	public StringProperty getNameProperty(){
		return Name;
	}

	public String getKind() {
		return Kind.get();
	}

	public void SetKind(String Kind) {
		this.Name.set(Kind);
	}

	public StringProperty getKindProperty(){
		return Kind;
	}
}
