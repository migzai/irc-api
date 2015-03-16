Usage of the API is very simple.  The main interfaces you should know about are:

- IRCApi
- IMessage
- IIRCState
- Callback<>

Here's an example connecting one client:

```

import java.util.*;

import com.ircclouds.irc.api.*;
import com.ircclouds.irc.api.domain.*;
import com.ircclouds.irc.api.state.*;

public class IRCClient
{
	public static void main(String[] aArgs)
	{
		IRCApi _api = new IRCApiImpl(true);
		_api.connect(getServerParams("nick", Arrays.asList("altNick1", "altNick2"), "IRC Api", "ident", "localhost", true), new Callback<IIRCState>()
		{
			@Override
			public void onSuccess(final IIRCState aIRCState)
			{
				// Connected!
			}

			@Override
			public void onFailure(Exception aErrorMessage)
			{
				throw new RuntimeException(aErrorMessage);
			}
		});
	}

	private static IServerParameters getServerParams(final String aNickname, final List<String> aAlternativeNicks, final String aRealname, final String aIdent,
			final String aServerName, final Boolean aIsSSLServer)
	{
		return new IServerParameters()
		{
			@Override
			public IRCServer getServer()
			{
				return new IRCServer(aServerName, aIsSSLServer);
			}

			@Override
			public String getRealname()
			{
				return aRealname;
			}

			@Override
			public String getNickname()
			{
				return aNickname;
			}

			@Override
			public String getIdent()
			{
				return aIdent;
			}

			@Override
			public List<String> getAlternativeNicknames()
			{
				return aAlternativeNicks;
			}
		};
	}
}
```