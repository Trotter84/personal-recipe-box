package csc180.trotter.daniel.personalrecipebox.model;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class ListRowData {

	private final VBox container;
	private final Supplier<HBox> rowFactory;
	private final List<HBox> rows = new ArrayList<>();
	private Consumer<Void> onRowsChanged;

	public ListRowData(VBox container, Supplier<HBox> rowFactory) {
		this.container = container;
		this.rowFactory = rowFactory;
	}

	public void setOnRowsChanged(Consumer<Void> callback) {
		this.onRowsChanged = callback;
	}

	public HBox addRow() {
		HBox row = rowFactory.get();
		rows.add(row);
		container.getChildren().add(row);
		notifyChanged();
		return row;
	}

	public void removeRow(HBox row) {
		rows.remove(row);
		container.getChildren().remove(row);
		notifyChanged();
	}

	public void clear() {
		rows.clear();
		container.getChildren().clear();
	}

	public List<HBox> getRows() {
		return rows;
	}

	private void notifyChanged() {
		if (onRowsChanged != null) {
			onRowsChanged.accept(null);
		}
	}
}