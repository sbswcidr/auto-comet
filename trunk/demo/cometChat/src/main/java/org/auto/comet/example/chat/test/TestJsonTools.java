package org.auto.comet.example.chat.test;

public class TestJsonTools {
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	private java.util.List<TestJsonTools> data;
	
	private TestJsonTools child;
	
	
	
	public TestJsonTools getChild() {
		return child;
	}
	public void setChild(TestJsonTools child) {
		this.child = child;
	}
	public java.util.List<TestJsonTools> getData() {
		return data;
	}
	public void setData(java.util.List<TestJsonTools> data) {
		this.data = data;
	}
	
	public static void main(String[] argv) throws Exception{
	
		String jsonString = "{name:\"your name\",value:\"This is value\", data:[{name:\"this is list one name\",value:\"This is list one value\"},{name:\"this is list two name\",value:\"This is list two value\"},{name:\"this is list three name\",value:\"This is list three value\"}], child:{name:\"this is child name\",value:\"This is child value\"}}";
		
		//Simple way to parse
		java.util.Map obj = JsonTools.parseJsonString(java.util.Map.class, jsonString);
		
		//user defined way to parse
		TestJsonTools obj1 = JsonTools.parseJsonString(TestJsonTools.class, jsonString);
		
		System.out.println(obj);
		
		
	}
}

