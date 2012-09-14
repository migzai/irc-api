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
	
	public boolean equals(Object aTuple)
	{
		if (aTuple instanceof Tuple)
		{
			Tuple<?, ?> _t = (Tuple<?, ?>) aTuple;
			return k.equals(_t.k) && v.equals(_t.v);
		}
		
		return false;
	}
	
	public int hashCode()
	{
		return k.hashCode() + v.hashCode();
	}
}
