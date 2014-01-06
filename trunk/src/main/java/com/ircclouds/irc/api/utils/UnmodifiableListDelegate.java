package com.ircclouds.irc.api.utils;

import java.lang.reflect.*;
import java.util.*;

public abstract class UnmodifiableListDelegate<T, D> implements List<D>
{
	private List<T> list;
	
	protected abstract D newInstance(T aT);
	
	public UnmodifiableListDelegate(List<T> aT)
	{
		list = aT;
	}

	@Override
	public D get(int index)
	{
		return newInstance(list.get(index));
	}

	@Override
	public ListIterator<D> listIterator()
	{
		return newListIterator(0);
	}
	
	@Override
	public ListIterator<D> listIterator(int aIndex)
	{
		return newListIterator(aIndex);
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public Iterator<D> iterator()
	{
		return newIterator();
	}

	@Override
	public List<D> subList(int fromIndex, int toIndex)
	{
		List<D> _subDelegateList = new ArrayList<D>();
		List<T> _subList = list.subList(fromIndex, toIndex);
		for (T _t : _subList)
		{
			_subDelegateList.add(newInstance(_t));
		}
		return _subDelegateList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] toArray()
	{
		Object[] _arr = list.toArray();
		Object[] _sDArr = new Object[_arr.length];
		for (int _i = 0; _i < _arr.length; _i++)
		{
			_sDArr[_i] = newInstance((T) _arr[_i]);
		}
		return _sDArr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K[] toArray(K[] aArray)
	{
		int _size = list.size();
		if (aArray.length < _size)
		{
			aArray = (K[]) Array.newInstance(aArray.getClass().getComponentType(), _size);
		}
				
		Iterator<T> _iter = list.iterator();
		int _i = 0;
		while (_iter.hasNext())
		{
			aArray[_i++] = (K) newInstance(_iter.next());
		}
		
		return aArray;
	}

	@Override
	public boolean contains(Object o)
	{
		return list.contains(o);
	}	
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return list.containsAll(c);
	}
	
	@Override
	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}	

	@Override
	public boolean addAll(Collection<? extends D> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
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
	public D set(int index, D element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, D element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public D remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends D> c)
	{
		throw new UnsupportedOperationException();
	}
	
	private ListIterator<D> newListIterator(int aIndex)
	{
		final ListIterator<T> iterator = list.listIterator(aIndex);
		return new ListIterator<D>()
		{
			@Override
			public boolean hasNext()
			{
				return iterator.hasNext();
			}

			@Override
			public D next()
			{
				return newInstance(iterator.next());
			}

			@Override
			public boolean hasPrevious()
			{
				return iterator.hasPrevious();
			}

			@Override
			public D previous()
			{
				return newInstance(iterator.previous());
			}

			@Override
			public int nextIndex()
			{
				return iterator.nextIndex();
			}

			@Override
			public int previousIndex()
			{
				return iterator.previousIndex();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(D e)
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(D e)
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	private Iterator<D> newIterator()
	{
		final Iterator<T> _iterator = list.iterator();
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
				return newInstance(_iterator.next());
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}	
}