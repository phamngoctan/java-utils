package com.java.utils.xml;

public enum PersonInstitutionSupportedTag {
	AHV_AVS_SALARY("AHV-AVS-Salary", "AHV-AVS"), UVG_LAA_SALARY("UVG-LAA-Salary", "UVG-LAA"), UVGZ_LAAC_SALARY(
			"UVGZ-LAAC-Salary", "UVGZ-LAAC"), KTG_AMC_SALARY("KTG-AMC-Salary", "KTG-AMC"), FAK_CAF_SALARY("FAK-CAF-Salary", "FAK-CAF");
	private String value;
	private String institutionTag;

	PersonInstitutionSupportedTag(String tagValue, String institutionTag) {
		this.value = tagValue;
		this.institutionTag = institutionTag;
	}

	public boolean isEquals(String strToCompare) {
		if (this.value == strToCompare) {
			return true;
		}
		return false;
	}

	public static PersonInstitutionSupportedTag fromString(String text) {
		if (text != null) {
			for (PersonInstitutionSupportedTag supportedTag : PersonInstitutionSupportedTag.values()) {
				if (text.equalsIgnoreCase(supportedTag.value)) {
					return supportedTag;
				}
			}
		}
		return null;
	}
	
	public String toReferenceTag() {
		return this.institutionTag;
	}

}
