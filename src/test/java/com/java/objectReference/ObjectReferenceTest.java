package com.java.objectReference;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectReferenceTest {
	
	private ScriptContext scriptContext;
	private Employee employee1;
	private Employee employee2;
	
	@Before
	public void init() {
		scriptContext = new ScriptContext();
		employee1 = new Employee("Tan Pham", 25);
		employee2 = new Employee("Vu Tran", 25);
		scriptContext.put("employee1", employee1);
		scriptContext.put("employee2", employee2);
	}
	
	@Test
	public void testVariableContext_changeProcessingContext() {
		VariableContext variableContext = new VariableContext(scriptContext);
		String key = "employee1";
		variableContext.changeProcessingContext(key, new Employee("Thanh Mai", 25));
		Assert.assertNotEquals(employee1, variableContext.getRootScriptContext().get(key));
	}
	
}

class Employee {
	private String name;
	private Integer age;
	public Employee(String name, Integer age) {
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}

class ScriptContext {
	Map<String, Object> variables;

	public ScriptContext() {
		variables = new HashMap<>();
	}

	public void put(String key, Object value) {
		variables.put(key, value);
	}

	public Object get(String key) {
		return variables.get(key);
	}

	public void remove(String key) {
		variables.remove(key);
	}
}

class VariableContext {
	
	private ScriptContext rootScriptContext;
	private ScriptContext processingScriptContext;

	public VariableContext(ScriptContext rootScriptContext) {
		this.rootScriptContext = rootScriptContext;
		this.processingScriptContext = rootScriptContext;
	}

	public ScriptContext getRootScriptContext() {
		return rootScriptContext;
	}

	public void setRootScriptContext(ScriptContext rootScriptContext) {
		this.rootScriptContext = rootScriptContext;
	}

	public ScriptContext getProcessingScriptContext() {
		return processingScriptContext;
	}

	public void setProcessingScriptContext(ScriptContext processingScriptContext) {
		this.processingScriptContext = processingScriptContext;
	}
	
	public void changeProcessingContext(String key, Object objToChange) {
		this.processingScriptContext.put(key, objToChange);
	}
	
}
