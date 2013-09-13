package com.ircclouds.irc.api.domain;

import java.util.*;

public final class ChannelModes
{
	private Set<Character> TYPE_A;
	private Set<Character> TYPE_B;
	private Set<Character> TYPE_C;
	private Set<Character> TYPE_D;
	
	public ChannelModes(Set<Character> aTypeA, Set<Character> aTypeB, Set<Character> aTypeC, Set<Character> aTypeD)
	{
		TYPE_A = aTypeA;
		TYPE_B = aTypeB;
		TYPE_C = aTypeC;
		TYPE_D = aTypeD;
	}
	
	public boolean isEmpty()
	{
		return TYPE_A.isEmpty() && TYPE_B.isEmpty() && TYPE_C.isEmpty() && TYPE_D.isEmpty();
	}
	
	public boolean isOfTypeA(Character aChar)
	{
		return TYPE_A.contains(aChar);
	}
	
	public boolean isOfTypeB(Character aChar)
	{
		return TYPE_B.contains(aChar);
	}
	
	public boolean isOfTypeC(Character aChar)
	{
		return TYPE_C.contains(aChar);
	}
	
	public boolean isOfTypeD(Character aChar)
	{
		return TYPE_D.contains(aChar);
	}
	
	public Set<Character> getTypeA()
	{
		return Collections.unmodifiableSet(TYPE_A);
	}
	
	public Set<Character> getTypeB()
	{
		return Collections.unmodifiableSet(TYPE_B);
	}
	
	public Set<Character> getTypeC()
	{
		return Collections.unmodifiableSet(TYPE_C);
	}
	
	public Set<Character> getTypeD()
	{
		return Collections.unmodifiableSet(TYPE_D);
	}
}
