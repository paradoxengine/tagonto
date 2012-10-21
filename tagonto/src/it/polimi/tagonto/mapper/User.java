package it.polimi.tagonto.mapper;

public class User 
{
	private int id = -1;
	private String name = null;
	
	public User(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() 
	{
		return name;
	}
	
	public String getUri()
	{
		return name;
	}
}
