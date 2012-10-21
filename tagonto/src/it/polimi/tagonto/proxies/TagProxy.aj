package it.polimi.tagonto.proxies;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.mapper.Tag;

/**
 * The purpose of this proxy is to load friend tags invoking an external service is a seamless way.
 * 
 * @author Mauro Luigi Drago
 */
public privileged aspect TagProxy 
{
	private boolean Tag.areFriendsLoaded = false;
	
	pointcut gettingFriends(Tag tag) :
		execution(* Tag.getFriends()) &&
		this(tag) &&
		within(Tag);
	
	before(Tag tag) : gettingFriends(tag)
	{
		if (tag.areFriendsLoaded == true) return;
		else{ // we need to load friends
			Map<String, Tag> friendTags = new HashMap<String,Tag>(); // we use an hash map to avoid inserting duplicate entries
			
			Collection<Tag> currentFriends = tag.friends;
			for(Tag currentFriend : currentFriends){
				friendTags.put(currentFriend.getTag(), currentFriend);
			}
			
			Collection<String> retrievedFriends = this.retrieveFriendTags(tag);
			for(String friend : retrievedFriends){
				Tag newTag = new Tag(friend);
				friendTags.put(newTag.getTag(), newTag);
			}
			
			tag.setFriends(friendTags.values());
			
			tag.areFriendsLoaded = true;
		}
	}
	
	private Collection<String> retrieveFriendTags(Tag tag)
	{
		Collection<String> result = new HashSet<String>();
		
		Client client = new Client(Protocol.HTTP);
		String url = Configuration.getInstance().getRELATED_TAG_SERVICE_URL();
		url += tag.getTag();
		try {
			URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return result;
		}
		
		Request request = new Request(Method.GET, url);
		Response response = client.handle(request);
		
		// now interpret the response
		DomRepresentation rep = response.getEntityAsDom();
		
		Document doc;
		try {
			doc = rep.getDocument();
		} catch (IOException e) {
			return result;
		}
		
		NodeList nodesList = doc.getElementsByTagName("tag");
		for(int i = 0; i < Configuration.getInstance().getRELATED_TAG_SERVICE_MAX_TAGS(); i++){
			if (nodesList.getLength() <= i) break;
			Element elem = (Element)nodesList.item(i);
			result.add(elem.getAttribute("label"));
		}
		
		return result;
	}
}
