package com.company.javaobj;

import com.experian.stratman.datasources.runtime.IData;

import java.util.HashMap;


public class DataArea implements IData 
{
	
	String layoutName_ = null;	// Contains the name for the data source that the Data Area represents E.g. OCONTROL

	HashMap areaContents_ = null;	// HashMap used to provide a Name/Value pair data table


	//
	// Creates a new instance of the Data Area
	// @param layoutName contains the name for the Data Source E.g. OCONTROL
	//
	public DataArea(String layoutName) 
	{
		layoutName_ = layoutName;
		areaContents_ = new HashMap();
	}


 
	//
	// Returns the layout name
	// @return returns a String of the layout name
	//
	public String getLayout() 
	{
		return layoutName_;
	}



	//
	// Gets the value for specified external name
	// @param externalName the external name of the required data value
	// @return returns a Number, String or Date object
	//
	public Object getValue(String externalName) 
	{
		return areaContents_.get(externalName);
	}



	//
	// Stores a value against specified external name
	// @param externalName the external name of the data value to be changed
	// @param value the data item as a Number, String or Date object
	//
	public void setValue(String externalName, Object value) 
	{
		areaContents_.put(externalName, value);
	}



	//
	// Gets the Hash map which contains the name/value pair data table
	// @return returns a HashMap containing name/value pairs
	//
	public HashMap getMap() 
	{
		return areaContents_;
	}

}
