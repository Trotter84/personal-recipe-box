package csc180.trotter.daniel.personalrecipebox.model;

public class IngredientRowData {

	private final String quantity;
	private final String unit;
	private final String itemName;
	private final String note;

	public IngredientRowData(String quantity, String unit, String itemName, String note) {
		this.quantity = quantity;
		this.unit = unit;
		this.itemName = itemName;
		this.note = note;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getUnit() {
		return unit;
	}

	public String getItemName() {
		return itemName;
	}

	public String getNote() {
		return note;
	}

	public String toIngredientLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(quantity.trim());
		if (unit != null && !unit.isBlank()) {
			sb.append(" ").append(unit.trim());
		}
		sb.append(" ").append(itemName.trim());
		if (note != null && !note.isBlank()) {
			sb.append(" ").append(note.trim());
		}
		return sb.toString();
	}
}
