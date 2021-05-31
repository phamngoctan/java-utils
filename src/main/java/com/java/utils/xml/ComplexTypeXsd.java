package com.java.utils.xml;

public class ComplexTypeXsd {
    private String nameType;
    private String Type;
    private boolean isRequired;
    
    public ComplexTypeXsd() {
    }
    
    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public static ComplexTypeXsd createNew(){
        return new ComplexTypeXsd();
    }
    
    public ComplexTypeXsd withNameType(String name){
        this.setNameType(name);
        return this;
    }
    
    public ComplexTypeXsd withType(String type){
        this.setType(type);
        return this;
    }
    public ComplexTypeXsd isRequired(boolean isRequired){
        this.setRequired(isRequired);
        return this;
    }
    
}
