package it.polimi.tagonto.mapper.plugins.googlechooser;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.GreedyMapper;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.noise.GoogleNoiseAnalyzer;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.linkchooser.LinkChooser;
import it.polimi.tagonto.mapper.plugins.maxchooser.MaxChooser;
import it.polimi.tagonto.mapper.plugins.thresholdchooser.ThresholdChooser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;

/**
 * This plugin modifies the significance of candidate mappings analyzing the connections
 * of a concept with neighbour concepts derived from a google result analysis.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class GoogleChooser implements IMatchPlugin 
{
	private static final String GOOGLE_URL = "http://www.google.com/search?q=";
	
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	private int maxNeighbourPages = Configuration.getInstance().getGOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES();
	private int maxKeywords = Configuration.getInstance().getGOOGLE_CHOOSER_MAX_KEYWORDS();
	
	private WVTool wvt = new WVTool(false);
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(GoogleChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(GoogleChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(GoogleChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		
		this.isInited = true;
	}
	
	public void setOntology(Ontology ontology) 
	{
		this.ont = ontology;
	}

	public void setTag(Tag tag) 
	{
		this.tag = tag;
	}

	public Ontology getOntology() 
	{
		return this.ont;
	}

	public Tag getTag() 
	{
		return this.tag;
	}
	
	public void setMaxNeighbourPages(int maxNeighbourPages)
	{
		this.maxNeighbourPages = maxNeighbourPages;
	}
	
	public void setMappings(Collection<Mapping> mappings)
	{
		this.mappings = mappings;
	}
	
	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(GoogleChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<String> keywords = null;
		try {
			keywords = this.analyzeGoogleNeighbours();
		} catch (TagontoException e) {
			throw new PluginException(GoogleChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
		
		// now do a greedy match
		Collection<Mapping> keywordMappings = new Vector<Mapping>();
		for(String keyword : keywords){
			try{	
				Collection<Mapping> tempMappings = null;
				GreedyMapper mapper = new GreedyMapper();
				mapper.setTag(new Tag(keyword));
				mapper.setontology(this.ont);
				mapper.setUser(Configuration.getInstance().getDATABASE_TAGONTO_USER());
				mapper.init();
				tempMappings = mapper.run();
				
				MaxChooser chooser = new MaxChooser();
				chooser.setMappings(tempMappings);
				chooser.setMaxMappings(2);
				chooser.setOntology(this.ont);
				chooser.setTag(this.tag);
				chooser.init();
				tempMappings = chooser.run();
				
				keywordMappings.addAll(tempMappings);
			} catch (Exception e) {
				throw new PluginException(GoogleChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
			}
		}
		
		// now purge results
		try{
			ThresholdChooser chooser = new ThresholdChooser();
			chooser.setMappings(keywordMappings);
			chooser.setOntology(this.ont);
			chooser.setTag(this.tag);
			chooser.setThreshold(0.3f);
			chooser.init();
			keywordMappings = chooser.run();
		}catch(PluginException e){
			throw new PluginException(GoogleChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
		
		// and do a simple choose
		try{
			LinkChooser chooser = new LinkChooser();
			chooser.setTag(this.tag);
			chooser.setOntology(this.ont);
			chooser.setMappings(this.mappings);
			chooser.setNeighbourMappings(keywordMappings);
			chooser.setWeight(0.2f);
			chooser.init();
			return chooser.run();
		}catch(PluginException e){
			throw new PluginException(GoogleChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
	}
	
	
	private Collection<String> getGoogleNeighbourPages() throws TagontoException
	{
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(400);
		
		String url = null;
		try {
			url = GoogleChooser.GOOGLE_URL + URLEncoder.encode(this.tag.getTag(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new Vector<String>();
		}
		
		HttpMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(true);
		
		InputStream responseBody = null;
        try{
            client.executeMethod(getMethod);
            responseBody = getMethod.getResponseBodyAsStream();
        } catch (Exception e) {
        	e.printStackTrace();
        	return new Vector<String>();
        }
    
        Collection<String> linkUrls = new HashSet<String>();
        Source source = null;
		try {
			source = new Source(responseBody);
		} catch (Exception e) {
			e.printStackTrace();
			return new Vector<String>();
		}
		
		List<StartTag> anchors = source.findAllStartTags("a");
		for(StartTag tag : anchors){
			if (linkUrls.size() >= this.maxNeighbourPages) break;
			
			String cssClass = tag.getAttributeValue("class");
			if (cssClass == null) continue;
			if (cssClass.equals("l")){
				String linkUrl = tag.getAttributeValue("href");
				linkUrls.add(linkUrl);
			}
		}
        
        return linkUrls;
	}
	
	private Collection<String> analyzeGoogleNeighbours() throws TagontoException
	{
		Collection<String> urls = this.getGoogleNeighbourPages();
		Map<String,Integer> keywords = new HashMap<String,Integer>();
		
		for(String url : urls){
			Collection<String> urlKeywords = this.getPageKeywords(url);
			for(String keyword : urlKeywords){
				if (keywords.containsKey(keyword)){
					keywords.put(keyword, keywords.get(keyword) + 1);
				}else keywords.put(keyword, 1);
			}
		}
		
		int maxCount = 0;
		for(Integer count : keywords.values()){
			if (count > maxCount) maxCount = count;
		}
		
		Collection<String> choosenKeywords = new Vector<String>();
		
		for(int count = maxCount; count > 0; count--){
			if (choosenKeywords.size() >= this.maxKeywords) break;
			
			for(String keyword : keywords.keySet()){
				if (keywords.get(keyword) == count){
					choosenKeywords.add(keyword);
					if (choosenKeywords.size() == this.maxKeywords) break;
				}
			}
		}
		
		return choosenKeywords;
	}

	private Collection<String> getPageKeywords(String url) throws TagontoException
	{
		Collection<String> keywords = new HashSet<String>();
		
		Source source = null;
		try {
			source = new Source(new URL(url));
			source.fullSequentialParse();
		} catch (Exception e) {throw new TagontoException(e);}

		// page keywords
		String metaKeywords = getMetaValue(source,"keywords");
		if (metaKeywords != null){
			return GoogleChooser.parseMetaKeywords(metaKeywords);
		}
		
		// otherwise build the word vector
		WVTFileInputList list = new WVTFileInputList(1);
		list.addEntry(new WVTDocumentInfo(url, "html", "", ""));
		
		WVTWordList wordList = null;
		try {
			wordList = wvt.createWordList(list, new WVTConfiguration());
		} catch (WVToolException e) {
			e.printStackTrace();
			return new Vector<String>();
		}
		
		wordList.pruneByFrequency(2,10);
		for(int i=0; i < wordList.getNumWords(); i++){
			keywords.add(wordList.getWord(i));
		}
		
		return keywords;
	}
	
	private static String getMetaValue(Source source, String key) 
	{
		for (int pos=0; pos<source.length();) {
			StartTag startTag = source.findNextStartTag(pos,"name",key,false);
			if (startTag == null) return null;
			if (startTag.getName() == HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
	
	private static Collection<String> parseMetaKeywords(String metaKeywords)
	{
		Collection<String> keywords = new HashSet<String>();
		
		int currentPos = 0;
		int keywordEnd = metaKeywords.indexOf(",", currentPos);
		
		while(keywordEnd != -1){
			String keyword = metaKeywords.substring(currentPos, keywordEnd);
			keywords.add(keyword);
			currentPos = keywordEnd + 1;
			keywordEnd = metaKeywords.indexOf(",", currentPos);
		}
		
		return keywords;
	}
}
