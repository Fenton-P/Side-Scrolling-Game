package gameCode;

import java.util.*;

public class DataInfo {
	private String propertyName;
    private String attribute;
    private Map<String, DataInfo> childNodes;
    private boolean attrBool;
    private DataInfo parent;
    
    public DataInfo(String name, String attr) {
        propertyName = name;
        attribute = attr;
        attrBool = true;
    }
    
    public DataInfo(String name, Map<String, DataInfo> children) {
        propertyName = name;
        childNodes = children;
        attrBool = false;
    }
    
    public boolean isAttr() {
        return attrBool;
    }
    
    public String getName() {
        return propertyName;
    }
    
    public void setName(String name) {
        propertyName = name;
    }
    
    public String getVal() {
        return attribute;
    }
    
    public void setVal(String attr) {
        attribute = attr;
    }
    
    public Map<String, DataInfo> getChildren() {
        return childNodes;
    }
    
    public void deleteChild(String name) {
        childNodes.remove(name);
    }

	public static DataInfo getChildNode(String[] path, DataInfo parentNode) {
		if(path.length == 1) return parentNode.getChildren().get(path[0]);
		
		return getChildNode(LevelReader.exclude(path, 0), parentNode.getChildren().get(path[0]));
	}
}
