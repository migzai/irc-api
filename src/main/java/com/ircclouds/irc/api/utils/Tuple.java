package com.ircclouds.irc.api.utils;

public class Tuple<K, V>
{
	private K k;
	private V v;
	
	public Tuple(K aK, V aV)
	{
		k = aK;
		v = aV;
	}
	
	public boolean equals(Tuple<K, V> aTuple)
	{
		if (aTuple instanceof Tuple)
		{
			return k.equals(aTuple.k) && v.equals(aTuple.v);
		}
		
		return false;
	}
}
