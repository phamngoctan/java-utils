package com.java.utils.xml;

public enum InstitutionSupportedTag {
	AHV_AVS("AHV-AVS"), UVG_LAA("UVG-LAA"), UVGZ_LAAC("UVGZ-LAAC"), KTG_AMC("KTG-AMC"), BVG_LPP("BVG-LPP"), FAK_CAF(
			"FAK-CAF");
	private String value;

	InstitutionSupportedTag(String value) {
		this.value = value;
	}

	public boolean isEquals(String strToCompare) {
		if (this.value == strToCompare) {
			return true;
		}
		return false;
	}

	public static InstitutionSupportedTag fromString(String text) {
		if (text != null) {
			for (InstitutionSupportedTag supportedTag : InstitutionSupportedTag.values()) {
				if (text.equalsIgnoreCase(supportedTag.value)) {
					return supportedTag;
				}
			}
		}
		return null;
	}

}
