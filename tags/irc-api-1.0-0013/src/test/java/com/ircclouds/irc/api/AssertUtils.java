package com.ircclouds.irc.api;

import java.util.*;

import org.junit.*;

public class AssertUtils
{
	public static <T> void assertSets(Set<T> aSetOne, Set<T> aSetTwo)
	{
		for (T _c : aSetOne)
		{
			Assert.assertTrue(aSetTwo.contains(_c));
		}
		for (T _c : aSetTwo)
		{
			Assert.assertTrue(aSetOne.contains(_c));
		}
	}
}
