package com.ircclouds.irc.api;

public interface Callback<T> extends ICallback<T, Exception>
{
	void onSuccess(T aObject);

    void onFailure(Exception aExc);
}
