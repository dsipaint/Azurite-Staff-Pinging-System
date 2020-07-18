import java.util.List;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter
{
	static final String helpmsg = "<member> needs your help!";
	static final String[] hierarchy = {
			"455900530766643211", //junior staff
			"455900351560679426", //chatmod
			"455900130596356097", //mod
			"455899796201537538", //admin
			"455900680943829022", //network admin
			"455899100697591808" //owner
	};
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();

		if(msg.equalsIgnoreCase(Main.PREFIX + "staff"))
		{
			int i = getHierarchyPosition(e.getMember()) + 1;
			
			for(; i < hierarchy.length; i++)
			{
				if(pingGroup(hierarchy[i], e))
					return; 
			}

			e.getChannel().sendMessage("No staff are available!").queue();
		}
		
		if(msg.equalsIgnoreCase(Main.PREFIX + "robocop") && isStaff(e.getMember()))
		{
			e.getChannel().sendMessage("***Shutting down...***").queue();
			Main.jda.shutdown();
			System.exit(0);
		}
	}
	
	
	private boolean pingGroup(String groupID, GuildMessageReceivedEvent e)
	{
		String pings = "";
		List<Member> group = e.getGuild().getMembersWithRoles(e.getGuild().getRoleById(groupID));
		if(areMembersOnline(group))
		{
			for(int i = 0; i < group.size(); i++)
			{
				//if a member is online ping them
				if(isMemberOnline(group.get(i)))
					pings += " " + group.get(i).getAsMention();
			}
			
			e.getChannel().sendMessage(pings + "! " + helpmsg.replace("<member>", e.getMember().getEffectiveName())).queue();
			return true;
		}
		
		return false;
	}
	
	private boolean isStaff(Member member)
	{
		List<Role> roles = member.getRoles();
		for(int i = 0; i < roles.size(); i++)
		{
			if(roles.get(i).getId().equals("489519546471022593") || roles.get(i).getId().equals("602863595767726122"))
				return true;
		}
		
		return false;
	}
	
	private boolean areMembersOnline(List<Member> staffgroup)
	{
		for(int i = 0; i < staffgroup.size(); i++)
		{
			if(isMemberOnline(staffgroup.get(i)))
				return true;
		}
		
		return false;
	}
	
	private boolean isMemberOnline(Member member)
	{
		if(member.getOnlineStatus() != OnlineStatus.INVISIBLE && member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getOnlineStatus() != OnlineStatus.IDLE)
			return true;
		
		return false;
	}
	
	private int getHierarchyPosition(Member member)
	{
		int pos = -1;
		List<Role> roles = member.getRoles();
		
		//cycle through the hierarchy
		for(int i = 0; i < hierarchy.length; i++)
		{
			//cycle through member's roles
			for(int j = 0; j < roles.size(); j++)
			{
				if(roles.get(j).getId().equals(hierarchy[i]))
				{
					pos = i; //hierarchy position is recalculated if a match is found
					break;
				}
			}
		}
		
		return pos;
	}
}
