package com.java.utils.xml;

import java.util.ArrayList;
import java.util.List;

public class SimpleTypeXsd {
    private String name;
    private String type;

    private String pattern;
    private String minInclusive;
    private String maxInclusive;
    private String minLength;
    private String maxLength;

    private List<String> enumerics;

    public SimpleTypeXsd() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMinInclusive() {
        return minInclusive;
    }

    public void setMinInclusive(String minInclusive) {
        this.minInclusive = minInclusive;
    }

    public String getMaxInclusive() {
        return maxInclusive;
    }

    public void setMaxInclusive(String maxInclusive) {
        this.maxInclusive = maxInclusive;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> getEnumerics() {
        return enumerics;
    }

    public void setEnumerics(List<String> enumerics) {
        this.enumerics = enumerics;
    }

    public static SimpleTypeXsd createNew() {
        return new SimpleTypeXsd();
    }

    public SimpleTypeXsd withName(String name) {
        this.setName(name);
        return this;
    }

    public SimpleTypeXsd withType(String type) {
        this.setType(type);
        return this;
    }

    public SimpleTypeXsd withPattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public SimpleTypeXsd withMinLength(String number) {
        this.setMinLength(number);
        return this;
    }

    public SimpleTypeXsd withMaxLength(String number) {
        this.setMinLength(number);
        return this;
    }

    public SimpleTypeXsd withMinInclusive(String number) {
        this.setMinInclusive(number);
        return this;
    }

    public SimpleTypeXsd withMaxInclusive(String number) {
        this.setMaxInclusive(number);
        return this;
    }

    public SimpleTypeXsd withEnumerics(List<String> enums) {
        this.setEnumerics(enums);
        return this;
    }

    public SimpleTypeXsd addEnumerics(String enumeric) {
        if (this.getEnumerics() == null) {
            enumerics = new ArrayList<>();
        }
        enumerics.add(enumeric);
        return this;
    }

    public SimpleTypeXsd withValue(String tag, String value) {
        if ("enumeration".equals(tag)) {
            this.addEnumerics(value);
        }
        if ("minInclusive".equals(tag)) {
            this.setMinInclusive(value);
        }
        if ("maxInclusive".equals(tag)) {
            this.setMaxInclusive(value);
        }
        if ("pattern".equals(tag)) {
            this.setPattern(value);
        }
        if ("minLength".equals(tag)) {
            this.setMinLength(value);
        }
        if ("maxLength".equals(tag)) {
            this.setMaxLength(value);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("base=" + type);
        if (minInclusive != null && minInclusive.length() > 0) {
            string.append(" minInclusive=\"" + minInclusive + "\"");
        }
        if (maxInclusive != null && maxInclusive.length() > 0) {
            string.append(" maxInclusive=\"" + maxInclusive + "\"");
        }
        if (pattern != null && pattern.length() > 0) {
            string.append(" pattern=\"" + pattern + "\"");
        }
        if (minLength != null && minLength.length() > 0) {
            string.append(" minLength=\"" + minLength + "\"");
        }
        if (maxLength != null && maxLength.length() > 0) {
            string.append(" maxLength=\"" + maxLength + "\"");
        }
        if (enumerics != null && !enumerics.isEmpty()) {
            string.append(" enumerations=\"" + enumerics.toString() + "\"");
        }
        return string.toString();
    }

}