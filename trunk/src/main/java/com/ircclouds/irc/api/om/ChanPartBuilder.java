package com.ircclouds.irc.api.om;

import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.utils.*;

public class ChanPartBuilder implements IBuilder<ChanPartMessage>
{
	// //:aae!aaf@bot.lebane.se PART #botcode :aSS

	public ChanPartMessage build(String aMessage)
	{
		String[] _cmpnts = aMessage.split(" ");

		WritableIRCUser _info = ParseUtils.getUser(_cmpnts[0]);
		String _chanName = _cmpnts[2].toLowerCase();
		if (_chanName.startsWith(":"))
		{
			_chanName = _chanName.substring(1);
		}

		ChanPartMessage _msg = null;
		if (_cmpnts.length > 3)
		{
			 _msg = new ChanPartMessage(_chanName, _info, aMessage.substring(aMessage.indexOf(" :") + 2));
		}
		else
		{
			 _msg = new ChanPartMessage(_chanName, _info);
		}

		return _msg;
	}
}
