package com.ircclouds.irc.api.utils;

import java.util.*;

public class SynchronizedUnmodifiableList<T> implements List<T>
{
	private List<T> list;
	private Object mutex;

	public SynchronizedUnmodifiableList(List<T> aList)
	{
		list = aList;
		mutex = this;
	}

	@Override
	public int size()
	{
		synchronized (mutex)
		{
			return list.size();
		}
	}

	@Override
	public boolean isEmpty()
	{
		synchronized (mutex)
		{
			return list.isEmpty();
		}
	}

	@Override
	public boolean contains(Object o)
	{
		synchronized (mutex)
		{
			return list.contains(o);
		}
	}

	@Override
	public Iterator<T> iterator()
	{
		synchronized (this)
		{
			return new Iterator<T>()
			{
				final Iterator<T> iter = list.iterator();

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
			return list.toArray();
		}
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a)
	{
		synchronized (mutex)
		{
			return list.toArray(a);
		}
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		synchronized (mutex)
		{
			return list.containsAll(c);
		}
	}

	@Override
	public T get(int index)
	{
		synchronized (mutex)
		{
			return list.get(index);
		}
	}

	@Override
	public int indexOf(Object o)
	{
		synchronized (mutex)
		{
			return list.indexOf(o);
		}
	}

	@Override
	public int lastIndexOf(Object o)
	{
		synchronized (mutex)
		{
			return list.lastIndexOf(o);
		}
	}

	@Override
	public ListIterator<T> listIterator()
	{
		synchronized (this)
		{
			return listIterator0(0);
		}
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		synchronized (this)
		{
			return listIterator(index);
		}
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return new SynchronizedUnmodifiableList<T>(list.subList(fromIndex, toIndex));
	}

	public boolean addElement(T e)
	{
		synchronized (mutex)
		{
			return list.add(e);
		}
	}

	public boolean removeElement(String aElement)
	{
		synchronized (mutex)
		{
			return list.remove(aElement);
		}
	}

	public boolean removeElement(T e)
	{
		synchronized (mutex)
		{
			return list.remove(e);
		}
	}

	@Override
	public boolean add(T e)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
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

	private ListIterator<T> listIterator0(final int index)
	{
		return new ListIterator<T>()
		{
			private final ListIterator<T> li = list.listIterator(index);

			public boolean hasNext()
			{
				return li.hasNext();
			}

			public T next()
			{
				return li.next();
			}

			public boolean hasPrevious()
			{
				return li.hasPrevious();
			}

			public T previous()
			{
				return li.previous();
			}

			public int nextIndex()
			{
				return li.nextIndex();
			}

			public int previousIndex()
			{
				return li.previousIndex();
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}

			public void set(T e)
			{
				throw new UnsupportedOperationException();
			}

			public void add(T e)
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}