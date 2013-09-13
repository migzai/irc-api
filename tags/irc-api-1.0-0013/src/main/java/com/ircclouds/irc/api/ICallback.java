package com.ircclouds.irc.api;

public interface ICallback<U, V>
{
	void onSuccess(U aU);
	
	void onFailure(V aV);
}
