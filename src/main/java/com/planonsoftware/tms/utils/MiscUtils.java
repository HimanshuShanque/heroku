package com.planonsoftware.tms.utils;

public class MiscUtils {
	public static boolean isBlank(String str) {
		if(str==null||str.trim().isEmpty()) {
			return true;
		}
		return false;
	}
}
