package it.polimi.tagonto.mapper;

import java.util.Collection;
import java.util.Vector;

/**
 * This class represents a tag.
 * 
 * @author Mauro Luigi Drago.
 *
 */
public class Tag 
{
	private String tag = null;
	private Collection<Tag> friends = new Vector<Tag>();
	
	public Tag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return this.tag;
	}
	
	public Collection<Tag> getFriends()
	{
		return this.friends;
	}
	
	public void setFriends(Collection<Tag> friends)
	{
		this.friends = new Vector<Tag>(friends);
	}
	
	public void addFriend(String tag)
	{
		this.friends.add(new Tag(tag));
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (!(obj instanceof Tag)) return false;
		Tag otherTag = (Tag) obj;
		
		if (!this.tag.equals(otherTag)) return false;
		else return true;
	}

	@Override
	public int hashCode() {
		return this.tag.hashCode();
	}
}
