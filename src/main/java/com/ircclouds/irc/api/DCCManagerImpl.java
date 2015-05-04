package com.ircclouds.irc.api;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

import org.slf4j.*;

import com.ircclouds.irc.api.ctcp.*;

public class DCCManagerImpl implements DCCManager
{
	private static final Logger LOG = LoggerFactory.getLogger(DCCManagerImpl.class);

	public static final int DCC_SEND_TIMEOUT = 10000;

	private IRCApi api;

	private Map<Integer, DCCSender> sendersMap = new HashMap<Integer, DCCSender>();
	private List<DCCReceiver> dccReceivers = new ArrayList<DCCReceiver>();
	
	public DCCManagerImpl(IRCApi aApi)
	{
		api = aApi;
	}

	void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout, DCCSendCallback aCallback)
	{
		DCCSender _dccSender = new DCCSender(aListeningPort, aTimeout, addManagerDCCSendCallback(aCallback, aListeningPort));

		registerSender(aListeningPort, _dccSender);

		_dccSender.send(aFile);

		api.message(aNick, '\001' + "DCC SEND " + aFile.getName() + " " + getLocalAddressRepresentation() + " " + aListeningPort + " " + aFile.length() + '\001');
	}

	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout, DCCSendCallback aCallback)
	{
		DCCSender _dccSender = new DCCSender(aTimeout, aPort, aResumePosition, addManagerDCCSendCallback(aCallback, aPort));

		if (isWaitingForConnection(aPort))
		{
			sendersMap.get(aPort).setResumePosition(aResumePosition);
		}
		else
		{
			registerSender(aPort, _dccSender);
			_dccSender.send(aFile);
		}

		api.message(aNick, '\001' + "DCC ACCEPT " + aFile.getName() + " " + aPort + " " + aResumePosition + '\001');
	}

	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback)
	{
		DCCReceiver _dccReceiver = new DCCReceiver(addManagerDCCReceiveCallback(aCallback), null);
		
		registerReceiver(_dccReceiver);
		
		_dccReceiver.receive(aFile, aResumePosition, aSize, aAddress);
	}

	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback, Proxy aProxy)
	{
		DCCReceiver _dccReceiver = new DCCReceiver(addManagerDCCReceiveCallback(aCallback), aProxy);

		registerReceiver(_dccReceiver);

		_dccReceiver.receive(aFile, aResumePosition, aSize, aAddress);
	}

	@Override
	public int activeDCCSendsCount()
	{
		return sendersMap.size();
	}

	@Override
	public int activeDCCReceivesCount()
	{
		return dccReceivers.size();
	}
	
	private String getLocalAddressRepresentation()
	{
		try
		{
			InetAddress _localHost = InetAddress.getLocalHost();
			byte[] _address = _localHost.getAddress();
			if (_address.length == 4)
			{
				return new BigInteger(1, _address).toString();
			}
			else
			{
				return _localHost.getHostAddress();
			}
		}
		catch (UnknownHostException aExc)
		{
			LOG.error("", aExc);
			throw new ApiException(aExc);
		}
	}

	private DCCReceiveCallback addManagerDCCReceiveCallback(final DCCReceiveCallback aCallback)
	{
		if (aCallback instanceof DCCReceiveProgressCallback)
		{
			return new DCCReceiveProgressCallback()
			{				
				@Override
				public void onSuccess(DCCReceiveResult aU)
				{
					dccReceivers.remove(aU);
					aCallback.onSuccess(aU);
				}
				
				@Override
				public void onFailure(DCCReceiveException aV)
				{
					dccReceivers.remove(aV);
					aCallback.onFailure(aV);
				}
				
				@Override
				public void onProgress(int aBytesTransferred)
				{
					((DCCReceiveProgressCallback) aCallback).onProgress(aBytesTransferred);
				}
			};
		}
		
		return new DCCReceiveCallback()
		{			
			@Override
			public void onSuccess(DCCReceiveResult aU)
			{
				dccReceivers.remove(aU);
				aCallback.onSuccess(aU);
			}
			
			@Override
			public void onFailure(DCCReceiveException aV)
			{
				dccReceivers.remove(aV);
				aCallback.onFailure(aV);
			}
		};
	}
	
	private DCCSendCallback addManagerDCCSendCallback(final DCCSendCallback aCallback, final int aPort)
	{
		if (aCallback instanceof DCCSendProgressCallback)
		{
			return new DCCSendProgressCallback()
			{				
				@Override
				public void onSuccess(DCCSendResult aU)
				{					
					sendersMap.remove(aPort);
					
					aCallback.onSuccess(aU);					
				}
				
				@Override
				public void onFailure(DCCSendException aV)
				{					
					sendersMap.remove(aPort);
					
					aCallback.onFailure(aV);
				}
				
				@Override
				public void onProgress(int aBytesTransferred)
				{
					((DCCSendProgressCallback) aCallback).onProgress(aBytesTransferred);
				}
			};
		}
		
		return new DCCSendCallback()
		{
			@Override
			public void onSuccess(DCCSendResult aU)
			{
				sendersMap.remove(aPort);
			
				aCallback.onSuccess(aU);				
			}
			
			@Override
			public void onFailure(DCCSendException aV)
			{
				sendersMap.remove(aPort);
				
				aCallback.onFailure(aV);
			}
		};
	}
	
	private void registerSender(Integer aListeningPort, DCCSender _dccSender)
	{
		sendersMap.put(aListeningPort, _dccSender);
	}

	private void registerReceiver(DCCReceiver aDCCReceiver)
	{
		dccReceivers.add(aDCCReceiver);
	}
	
	private boolean isWaitingForConnection(Integer aPort)
	{
		return sendersMap.containsKey(aPort);
	}
}
