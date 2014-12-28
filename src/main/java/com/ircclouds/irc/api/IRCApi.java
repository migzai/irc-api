package com.ircclouds.irc.api;

import java.io.*;
import java.net.*;

import com.ircclouds.irc.api.ctcp.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.filters.*;
import com.ircclouds.irc.api.listeners.*;
import com.ircclouds.irc.api.state.*;

/** 
 * The main interface of IRC-API, where all IRC methods are defined.
 * 
 * There are 2 types of IRC methods available, synchronous and asynchronous.  When the type is asynchronous, a callback should be provided.
 * 
 * Moreover, this interface accepts IRC message listeners/filters, and offers a useful set of DCC Commands.
 * 
 *  @author miguel@lebane.se  
 */
public interface IRCApi
{
	/**
	 * Asynchronous connect
	 * 
	 * @param aServerParameters
	 *            The IRC Server connection parameters
	 * @param aCallback
	 *            A callback that will be invoked when the connection is
	 *            established, and will return an {@link IIRCState} on success,
	 *            or an {@link Exception} in case of failure
	 * @param negotiator
	 *            CAP negotiator instance used when establishing the IRC
	 *            connection. <b>Note</b> that the negotiator is expected to
	 *            take over (transparently, irc-api will not signal the
	 *            instance) when CAP negotiation has started. For this purpose,
	 *            the provided negotiator instance is added to the private
	 *            listeners collection such that it will receive communications.
	 *            After CAP END has been sent, as by spec the server will resume
	 *            the normal registration process starting with message 001 and
	 *            irc-api will then continue its standard procedure.
	 */
	void connect(IServerParameters aServerParameters, Callback<IIRCState> aCallback, CapabilityNegotiator negotiator);

	/**
	 * Synchronous disconnect
	 */
	void disconnect();

	/**
	 * Synchronous disconnect
	 * 
	 * @param aQuitMessage The Quit message
	 */
	void disconnect(String aQuitMessage);

	/**
	 * Synchronous channel join
	 * 
	 * @param aChannelName A channel name
	 */
	void joinChannel(String aChannelName);

	/**
	 * Asynchronous channel join
	 * 
	 * @param aChannelName A Channel name
	 * @param aCallback A callback that will return an {@link IRCChannel} on success, or an {@link Exception} in case of failure
	 */
	void joinChannel(String aChannelName, Callback<IRCChannel> aCallback);

	/**
	 * Synchronous channel join
	 * 
	 * @param aChannelName A Channel name
	 * @param aKey A channel key
	 */
	void joinChannel(String aChannelName, String aKey);

	/**
	 * Asynchronous channel join
	 * 
	 * @param aChannelName A Channel name
	 * @param aKey A channel key
	 * @param aCallback A callback that will return an {@link IRCChannel} on success, or an {@link Exception} in case of failure
	 */
	void joinChannel(String aChannelName, String aKey, Callback<IRCChannel> aCallback);

	/**
	 * Synchronous channel leave
	 * @param aChannelName A Channel name
	 */
	void leaveChannel(String aChannelName);

	/**
	 * Asynchronous channel leave
	 * 
	 * @param aChannelName A Channel name
	 * @param aCallback A callback that will return the left channel name in case of success, or an {@link Exception} in case of failure
	 */
	void leaveChannel(String aChannelName, Callback<String> aCallback);

	/**
	 * Synchronous channel leave
	 * 
	 * @param aChannelName A Channel name
	 * @param aPartMessage A part message
	 */
	void leaveChannel(String aChannelName, String aPartMessage);

	/**
	 * Asynchronous channel leave
	 * 
	 * @param aChannelName A channel name
	 * @param aPartMessage A part message
	 * @param aCallback A callback that will return the left channel name in case of success, or an {@link Exception} in case of failure
	 */
	void leaveChannel(String aChannelName, String aPartMessage, Callback<String> aCallback);

	/**
	 * Synchronous nick change
	 * 
	 * @param aNewNick A new nickname
	 */
	void changeNick(String aNewNick);

	/**
	 * Asynchronous nick change
	 * 
	 * @param aNewNick A new nickname
	 * @param aCallback A callback that returns the new nick on success, or an {@link Exception} in case of failure
	 */
	void changeNick(String aNewNick, Callback<String> aCallback);	

	/**
	 * Synchronous Private message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message 
	 */
	void message(String aTarget, String aMessage);

	/**
	 * Asynchronous Private message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message 
	 * @param aCallback A callback that will return the sent message on success, or an {@link Exception} in case of failure
	 */
	void message(String aTarget, String aMessage, Callback<String> aCallback);

	/**
	 * Synchronous Action message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message
	 */
	void act(String aTarget, String aMessage);

	/**
	 * Asynchronous Action message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message 
	 * @param aCallback A callback that will return the sent action message on success, or an {@link Exception} in case of failure
	 */
	void act(String aTarget, String aMessage, Callback<String> aCallback);

	/**
	 * Synchronous Notice message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message 
	 */
	void notice(String aTarget, String aMessage);

	/**
	 * Asynchronous Notice message
	 * 
	 * @param aTarget Can be a channel or a nickname
	 * @param aMessage A message 
	 * @param aCallback A callback that will return the sent notice message on success, or an {@link Exception} in case of failure
	 */
	void notice(String aTarget, String aMessage, Callback<String> aCallback);

	/**
	 * Synchronous kick message
	 * 
	 * @param aChannel A channel name
	 * @param aNick A nick to be kicked
	 */
	void kick(String aChannel, String aNick);

	/**
	 * Synchronous kick message
	 * 
	 * @param aChannel A channel name
	 * @param aNick A nick to be kicked
	 * @param aKickMessage
	 */
	void kick(String aChannel, String aNick, String aKickMessage);

	/**
	 * Asynchronous kick message
	 * 
	 * @param aChannel A channel name
	 * @param aNick A nick to be kicked
	 * @param aCallback A callback that will return an empty message on success, or an {@link Exception} in case of failure
	 */
	void kick(String aChannel, String aNick, Callback<String> aCallback);

	/**
	 * Asynchronous kick message
	 * 
	 * @param aChannel A channel name
	 * @param aNick A nick to be kicked
	 * @param aKickMessage A kick message
	 * @param aCallback A callback that will return an empty message on success, or an {@link Exception} in case of failure
	 */
	void kick(String aChannel, String aNick, String aKickMessage, Callback<String> aCallback);

	/**
	 * Synchronous change topic
	 * 
	 * @param aChannel A channel name
	 * @param aTopic A new topic
	 */
	void changeTopic(String aChannel, String aTopic);

	/**
	 * Synchronous change mode
	 * 
	 * @param aModeString This will basically execute a 'mode ' + aModeString
	 */
	void changeMode(String aModeString);

	/**
	 * Synchronous raw message
	 * 
	 * @param aMessage A raw text message to be sent to the IRC server
	 */
	void rawMessage(String aMessage);

	/**
	 * 
	 * @param aNick A nick to send the file to
	 * @param aFile A file resource
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccSend(String aNick, File aFile, DCCSendCallback aCallback);

	/**
	 * @param aNick A nick to send the file to
	 * @param aFile A file resource to send
	 * @param aTimeout A timeout in milliseconds for destination to reply
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccSend(String aNick, File aFile, Integer aTimeout, DCCSendCallback aCallback);

	/**
	 * @param aNick A nick to send the file to
	 * @param aListeningPort A port to listen on
	 * @param aFile A file resource to send
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccSend(String aNick, Integer aListeningPort, File aFile, DCCSendCallback aCallback);

	/**
	 * 
	 * @param aNick A nick to send the file to
	 * @param aFile A file resource to send
	 * @param aListeningPort A port to listen on for incoming DCC connections
	 * @param aTimeout A timeout in milliseconds for destination to reply
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccSend(String aNick, File aFile, Integer aListeningPort, Integer aTimeout, DCCSendCallback aCallback);

	/**
	 * 
	 * @param aNick A nick to accept the file from
	 * @param aFile A file resource to receive to
	 * @param aPort A port to advertise and to listen on for DCC senders to send us to the file
	 * @param aResumePosition A file resume position in bytes
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, DCCSendCallback aCallback);

	/**
	 * 
	 * @param aNick A nick to accept the file from
	 * @param aFile A file resource to receive to
	 * @param aPort A port to advertise and to listen on for DCC senders to send us to the file
	 * @param aResumePosition A file resume position in bytes
	 * @param aTimeout A timeout in milliseconds for destination to reply
	 * @param aCallback A callback that will return a {@link DCCSendResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccAccept(String aNick, File aFile, Integer aPort, Integer aResumePosition, Integer aTimeout, DCCSendCallback aCallback);

	/**
	 * 
	 * @param aFile A file resource
	 * @param aSize A file size.  Used to denote how much to receive to file
	 * @param aAddress A socket address to connect to and get the file
	 * @param aCallback A callback that will return a {@link DCCReceiveResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccReceive(File aFile, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback);

	/**
	 * 
	 * @param aFile A file resource
	 * @param aSize A file size.  Used to denote how much to receive to file
	 * @param aAddress A socket address to connect to and get the file
	 * @param aCallback A callback that will return a {@link DCCReceiveResult} on success, or a {@link DCCSendException} on failure
	 * @param aProxy A SOCKS proxy
	 */
	void dccReceive(File aFile, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback, Proxy aProxy);

	/**
	 *
	 * @param aFile A file resource
	 * @param aResumePosition A resume position in bytes
	 * @param aSize A size in bytes.  Used to denote how much to receive to file
	 * @param aAddress A socket address to connect to and get the file
	 * @param aCallback A callback that will return a {@link DCCReceiveResult} on success, or a {@link DCCSendException} on failure
	 */
	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback);

	/**
	 *
	 * @param aFile A file resource
	 * @param aResumePosition A resume position in bytes
	 * @param aSize A size in bytes.  Used to denote how much to receive to file
	 * @param aAddress A socket address to connect to and get the file
	 * @param aCallback A callback that will return a {@link DCCReceiveResult} on success, or a {@link DCCSendException} on failure
	 * @param aProxy The proxy server to use for connecting.
	 */
	void dccResume(File aFile, Integer aResumePosition, Integer aSize, SocketAddress aAddress, DCCReceiveCallback aCallback, Proxy aProxy);

	/**
	 * Returns the DCC manager
	 * 
	 * @return {@link DCCManager}
	 */
	DCCManager getDCCManager();

	/**
	 * Adds a message listener
	 * 
	 * @param aListener A message listener
	 */
	void addListener(IMessageListener aListener);

	/**
	 * Deletes a message listener
	 * 
	 * @param aListener A message listener
	 */
	void deleteListener(IMessageListener aListener);

	/**
	 * Sets a message filter
	 * 
	 * @param aFilter A message filter
	 */
	void setMessageFilter(IMessageFilter aFilter);
}
