package com.uslotter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.uslotter.data.Item;
import com.uslotter.data.Option;

public class MyDefaultHandler extends DefaultHandler {
	private List<Option> options = null;
	private List<Item> items = null;
	private Item item = null;
	private Option option = null;
	private String tagName = "";

	@Override
	public void startDocument() throws SAXException {
		options = new ArrayList<Option>();
		items = new ArrayList<Item>();
		super.startDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("options")){
			return;
		}
		if(localName.equals("item")){
			item = new Item();
			item.setId(attributes.getValue("","id"));
			return;
		}
		if(localName.equals("option")){
			option = new Option();
			option.setId(attributes.getValue("","id"));
			return;
		}
		if(localName.equals("intro")){
			tagName = localName;
			return;
		}
		if(localName.equals("score")){
			tagName = localName;
			return;
		}
		if(localName.equals("childNum")){
			tagName = localName;
			return;
		}
		if(localName.equals("child")){
			tagName = localName;
			return;
		}
		if(localName.equals("cNum")){
			tagName = localName;
			return;
		}
		if(localName.equals("introduction")){
			tagName = localName;
			return;
		}
	//	super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String value = new String(ch,start,length);
		
		if(item!=null&&tagName.equals("child")){
			String[] my_items = value.split(",");
			List<String> _items = new ArrayList<String>();
			for(String str : my_items){
				_items.add(str);
			}
			item.setItems(_items);
			return;
		}
		//cNum
		if(item!=null&&tagName.equals("cNum")){
			item.setChildNum(value);
			return;
		}
		if(item!=null&&tagName.equals("introduction")){
			item.setIntro(value);
			return;
		}
		
		if(option!= null&&tagName.equals("intro")){
			option.setIntro(value);
			return;
		}else if(option!= null&&tagName.equals("score")){
			option.setScore(value);
			return;
		}else if(option!= null&&tagName.equals("childNum")){
			option.setChildnum(value);
			return;
		}
		
		//super.characters(ch, start, length);
	}

	

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(option!=null&&localName.equals("option")){
			
			options.add(option);
			option = null;	
		}
		if(item!=null&&localName.equals("item")){
			items.add(item);
			item = null;
		}
		tagName = "";
	//	super.endElement(uri, localName, qName);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public  List<Option> getOptions(){
		return options;
	}
	
	public  List<Item> getItems(){
		return items;
	}
}