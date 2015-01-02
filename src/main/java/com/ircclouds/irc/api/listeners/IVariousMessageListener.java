package com.ircclouds.irc.api.listeners;

import com.ircclouds.irc.api.domain.messages.*;

public interface IVariousMessageListener extends IMessageListener
{
	void onChannelMessage(ChannelPrivMsg aMsg);
	
	void onChannelJoin(ChanJoinMessage aMsg);
	
	void onChannelPart(ChanPartMessage aMsg);
	
	void onChannelNotice(ChannelNotice aMsg);
	
	void onChannelAction(ChannelActionMsg aMsg);
	
	void onChannelKick(ChannelKick aMsg);
	
	void onTopicChange(TopicMessage aMsg);
	
	void onUserPrivMessage(UserPrivMsg aMsg);
	
	void onUserNotice(UserNotice aMsg);
	
	void onUserAction(UserActionMsg aMsg);
	
	void onServerNumericMessage(ServerNumericMessage aMsg);
	
	void onServerNotice(ServerNotice aMsg);
	
	void onNickChange(NickMessage aMsg);
	
	void onUserQuit(QuitMessage aMsg);
	
	void onError(ErrorMessage aMsg);

	void onClientError(ClientErrorMessage aMsg);
	
	void onChannelMode(ChannelModeMessage aMsg);
	
	void onUserPing(UserPing aMsg);
	
	void onUserVersion(UserVersion aMsg);
	
	void onServerPing(ServerPing aMsg);
}
