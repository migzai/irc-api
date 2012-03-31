package com.ircclouds.irc.api;

public interface Callback<T>
{
	void onSuccess(T aObject);

    void onFailure(String aErrorMessage);
}
