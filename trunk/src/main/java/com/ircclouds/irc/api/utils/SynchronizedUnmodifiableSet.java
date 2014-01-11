package com.ircclouds.irc.api.utils;

import java.util.*;

public class SynchronizedUnmodifiableSet<T> implements Set<T>
{
	private Set<T> set;
	private Object mutex;

	public SynchronizedUnmodifiableSet(Set<T> aSet)
	{
		set = aSet;
		mutex = this;
	}

	@Override
	public int size()
	{
		synchronized (mutex)
		{
			return set.size();
		}
	}

	@Override
	public boolean isEmpty()
	{
		synchronized (mutex)
		{
			return set.isEmpty();
		}
	}

	@Override
	public boolean contains(Object o)
	{
		synchronized (mutex)
		{
			return set.contains(o);
		}
	}

	@Override
	public Iterator<T> iterator()
	{
		synchronized (mutex)
		{
			return new Iterator<T>()
			{
				private Iterator<T> iter = set.iterator();

				@Override
				public boolean hasNext()
				{
					return iter.hasNext();
				}

				@Override
				public T next()
				{
					return iter.next();
				}

				@Override
				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		}
	}

	@Override
	public Object[] toArray()
	{
		synchronized (mutex)
		{
			return set.toArray();
		}
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a)
	{
		synchronized (mutex)
		{
			return set.toArray(a);
		}
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		synchronized (mutex)
		{
			return set.containsAll(c);
		}
	}
	
	@Override
	public boolean add(T e)
	{
		throw new UnsupportedOperationException();
	}

	public boolean addElement(T e)
	{
		return set.add(e);
	}
	
	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}

	public boolean removeElement(Object o)
	{
		return set.remove(o);
	}	
	
	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
}
