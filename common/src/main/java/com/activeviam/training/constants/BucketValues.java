package com.activeviam.training.constants;

import java.util.Arrays;
import java.util.List;

public class BucketValues {
	
	// Standard buckets from schema gen file, used for comparator
	private static List<String> BUCKETS = Arrays.asList(
			"1D", "2D", "3D", "1W", "2W", "3W", "1M", "2M", "3M", "4M", "5M", "6M", "7M", "8M", "9M", "10M", "11M",
			"1Y", "13M", "14M", "14M", "15M", "16M", "17M", "18M", "19M", "20M", "21M", "22M", "23M", "2Y", "25M", "26M",
			"27M", "28M", "29M", "30M", "31M", "32M", "33M", "34M", "35M", "3Y", "37M", "38M", "39M", "40M", "41M", "42M",
			"43M", "44M", "45M", "46M", "47M", "4Y", "49M", "50M", "51M", "52M", "53M", "54M", "55M", "56M", "57M", "58M",
			"59M", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "11Y", "12Y", "13Y", "14Y", "15Y", "16Y", "17Y", "18Y", "19Y", "20Y",
			"21Y", "22Y", "23Y", "24Y", "25Y", "26Y", "27Y", "28Y", "29Y", "30Y", "31Y", "32Y", "33Y", "34Y", "35Y", "40Y",
			"50Y", "60Y"
	);
	
	private static List<String> SUMMARY_BUCKETS = Arrays.asList(
			"SHORT", "MID", "LONG"
	);
	
	public static List<String> getBuckets() {
		return BUCKETS;
	}
	
	public static List<String> getSummaryBuckets() {
		return SUMMARY_BUCKETS;
	}

}
