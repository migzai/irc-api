package com.ircclouds.irc.api;

public interface Callback<T> extends ICallback<T, String>
{
	void onSuccess(T aObject);

    void onFailure(String aErrorMessage);
}
