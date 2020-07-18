import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Main
{
	static JDA jda;
	static final String PREFIX = ">";
	
	/*
	 * V1.2 Changelog:
	 * 	-added a hierarchy system- staff who use the commands only ping staff higher up than them
	 */
	
	public static void main(String[] args)
	{
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("").build();
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		
		jda.addEventListener(new CommandListener());
		jda.getPresence().setActivity(Activity.playing("Use >staff for staff assistance!"));
	}
}
