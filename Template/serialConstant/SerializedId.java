package serialConstant;

public enum SerializedId {
	SERIALIZED_ID("serialVersionUID");
	
	private String value;
	
	SerializedId(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
