package com.mycompany.app;

public class MyBotMain {

	public static void main(String[] args) throws Exception
	{
		// Defining variables
		String server = "irc.freenode.net";
		String channel = "#demobot";
		
		// Start the bot
		MyBot bot = new MyBot();
		
		// Enable debugging
		bot.setVerbose(true);
		
		// Connect the bot to the server
		bot.connect(server);
		
		// Connect the bot to the channel
		bot.joinChannel(channel);
		
		// Send startup message
		bot.sendMessage(channel, "Hello! Type \"!commands\" to get a list of commands that are available!");
	}

}