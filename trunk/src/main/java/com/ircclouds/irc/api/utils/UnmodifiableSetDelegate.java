package com.ircclouds.irc.api.utils;

import java.lang.reflect.*;
import java.util.*;

public abstract class UnmodifiableSetDelegate<T, D> implements Set<D>
{
	private Set<T> set;
	private Map<T, D> cache;
	
	protected abstract D newInstance(T aT);
	
	public UnmodifiableSetDelegate(Set<T> aSet)
	{
		set = aSet;
		cache = new HashMap<T, D>();
	}
	
	@Override
	public int size()
	{
		return set.size();
	}

	@Override
	public boolean isEmpty()
	{
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object aObject)
	{
		return set.contains(aObject);
	}

	@Override
	public Iterator<D> iterator()
	{
		final Iterator<T> _iterator = set.iterator();
		return new Iterator<D>()
		{
			@Override
			public boolean hasNext()
			{
				return _iterator.hasNext();
			}

			@Override
			public D next()
			{
				return conditionalNewInstance(_iterator.next());
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Object[] toArray()
	{
		Object[] subObjs = new Object[set.size()];
		Iterator<T> _iter = set.iterator();
		int _i = 0;
		while (_iter.hasNext())
		{
			subObjs[_i++] = conditionalNewInstance(_iter.next());
		}
		return subObjs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K[] toArray(K[] aArray)
	{
		int _size = set.size();
		if (aArray.length < _size)
		{
			aArray = (K[]) Array.newInstance(aArray.getClass().getComponentType(), _size);
		}
				
		Iterator<T> _iter = set.iterator();
		int _i = 0;
		while (_iter.hasNext())
		{
			aArray[_i++] = (K) conditionalNewInstance(_iter.next());
		}
		
		return aArray;
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return set.containsAll(c);
	}

	@Override
	public boolean add(D e)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(Collection<? extends D> c)
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
	
	private D conditionalNewInstance(T aT) 
	{
		if (cache.containsKey(aT))
		{
			return cache.get(aT);
		}
		
		D _d = newInstance(aT);
		cache.put(aT, _d);
		return _d;
	}
}
