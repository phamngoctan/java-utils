package com.java.utils.reflection;

import java.util.List;

import javax.activation.UnsupportedDataTypeException;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * Feature to be tested. The targeted class should be a class, a static inner class
 * <ul>
 * <li>Should support String type (done)</li>
 * <li>Should support Long type (done)</li>
 * <li>Should support int primitive type type (done)</li>
 * <li>Should support short primitive type type</li>
 * <li>Should support byte primitive type type</li>
 * <li>Should support long primitive type type</li>
 * <li>Should support float primitive type type</li>
 * <li>Should support double primitive type type</li>
 * <li>Should support boolean primitive type type</li>
 * <li></li>
 * </ul>
 *
 */
public class DataUtilsTest {
	
	public static class Employee {
		private Long id;
		private String name;
		private String address;
		private short workingMonth;
		private int workingHour;
		private byte employeePriByte;
		private long employeePriLong;
		private float employeePriFloat;
		private double employeePriDouble;
		private boolean employeePriBoolean;
		private List<Employee> employees;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public short getWorkingMonth() {
			return workingMonth;
		}

		public void setWorkingMonth(short workingMonth) {
			this.workingMonth = workingMonth;
		}

		public int getWorkingHour() {
			return workingHour;
		}

		public void setWorkingHour(int workingHour) {
			this.workingHour = workingHour;
		}

		public byte getEmployeePriByte() {
			return employeePriByte;
		}

		public void setEmployeePriByte(byte employeePriByte) {
			this.employeePriByte = employeePriByte;
		}

		public long getEmployeePriLong() {
			return employeePriLong;
		}

		public void setEmployeePriLong(long employeePriLong) {
			this.employeePriLong = employeePriLong;
		}

		public float getEmployeePriFloat() {
			return employeePriFloat;
		}

		public void setEmployeePriFloat(float employeePriFloat) {
			this.employeePriFloat = employeePriFloat;
		}

		public double getEmployeePriDouble() {
			return employeePriDouble;
		}

		public void setEmployeePriDouble(double employeePriDouble) {
			this.employeePriDouble = employeePriDouble;
		}

		public boolean isEmployeePriBoolean() {
			return employeePriBoolean;
		}

		public void setEmployeePriBoolean(boolean employeePriBoolean) {
			this.employeePriBoolean = employeePriBoolean;
		}

		public List<Employee> getEmployees() {
			return employees;
		}

		public void setEmployees(List<Employee> employees) {
			this.employees = employees;
		}
	}
	
	// when I input a class type then I expect to get the normal data of that
	// class
	@Test
	public void getData_employee_getNormalData() throws IllegalArgumentException, SecurityException, UnsupportedDataTypeException {
		Employee emp = DataUtils.getData(Employee.class);
		Assert.assertNotNull(emp);
		Assert.assertNotNull(emp.getId());
		Assert.assertNotNull(emp.getName());
		Assert.assertNotNull(emp.getAddress());
		Assert.assertNotNull(emp.getWorkingMonth());
		Assert.assertNotNull(emp.getWorkingHour());
		Assert.assertNotNull(emp.getEmployeePriByte());
		Assert.assertNotNull(emp.getEmployeePriDouble());
		Assert.assertNotNull(emp.getEmployeePriFloat());
		Assert.assertNotNull(emp.getEmployeePriLong());
		Assert.assertNotNull(emp.isEmployeePriBoolean());
		Assert.assertNotNull(emp.getEmployees());
	}

}
