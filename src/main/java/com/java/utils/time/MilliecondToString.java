package com.java.utils.time;

import java.time.Duration;
import java.time.Instant;

public class MilliecondToString {
	public static void main(String[] args) {
		Instant start = Instant.now();
		Instant end = Instant.now();
		System.out.println(formatMillisecond(start, end));
	}
	
	static String formatMillisecond(Instant start, Instant end) {
		long millis = Duration.between(start, end).toMillis();
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		String time = String.format("Time to calculate %02d:%02d:%02d:%d", hour, minute, second, millis % 1000);
		return time;
	}
}
