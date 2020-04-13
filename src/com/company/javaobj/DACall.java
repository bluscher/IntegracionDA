//
// Main class for performing a DA call
// @version 1.0
// @author Experian
// 
// 19/9/2017 Version 1.1 A. Burt Updated for Decision Agent 2.2 SP5 and later
//

package com.company.javaobj;

import java.util.*;

// DA libraries
import com.experian.stratman.datasources.runtime.IData;
//import com.experian.stratman.decisionagent.business.NT.NTJSEMObjectInterface;
import com.experian.stratman.decisionagent.business.NT.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DACall
{
	static final int TRACE_LEVEL = 25;		// Trace level to use for DA call
	static final int TOTAL_DATA_AREAS = 4;          // Total number of data areas to pass to DA
        static final int TOTAL_DA_CALLS = 10;           // Total number of DA calls to perform
        

	// Create data areas
	DataArea controlData_ = new DataArea("OCONTROL");
	DataArea inputData_ = new DataArea("OINPUT");
	DataArea inputData2_ = new DataArea("OINPUT2"); 
	DataArea resultsData_ = new DataArea("ORESULTS");

	// Create data block to be passed to the DA (will contain all data areas)
	IData[] dataAreas_ = new IData[TOTAL_DATA_AREAS];


	//
	// Main method
	//
	public static void main(String[] args) 
	{
		DACall myCall = new DACall();	// Object used to perform DA call
                int errors = 0;                 // Number of errors returned by DA call
                int callCount = 0;              // Number of DA calls performed, so far
                
		try 
		{
                        // Loop to perform DA calls
                        do
                        {
                            // Initialise data & call Decision Agent
                            myCall.initialiseData();
                            errors = myCall.performCall();
                            
                            callCount++;
                        }
                        while ((errors == 0) && (callCount < TOTAL_DA_CALLS));
		}
                catch (Exception e) 
		{
			System.out.println("Exception: " + e);
		} // end catch
	}



	//
	// Initialise data ready for DA call
	// All input data, including the Control Area, must be initialised
	// Any temporary or results data, does not have to be initialised
	//
	private void initialiseData() throws ParseException 
	{
		// initialise OCONTROL, OINPUT and OINPUT2 data areas
		initialiseControl();
		initialiseInput();
		initialiseInput2();

		// N.B. RESULTS data area does not have to be initialised
                			
		// Put each data area into the data block
		dataAreas_[0] = (IData) controlData_;	// OCONTROL data area must be first
		dataAreas_[1] = (IData) inputData_;
		dataAreas_[2] = (IData) inputData2_;
		dataAreas_[3] = (IData) resultsData_;
	}



	//
        // Initialise the OCONTROL data area
	//
	private void initialiseControl() 
	{
                //  Always set the Alias and Signature
		controlData_.setValue("ALIAS", "DEMO");		// Same as *.SER filename
		controlData_.setValue("SIGNATURE", "DEMOSIG");
                
                // All other values must be initialised, but can be set to an empty string
		controlData_.setValue("OBJECTIVENAME", "");
		controlData_.setValue("EDITIONDATE", "");
		controlData_.setValue("ERRORCOUNT", "");
		controlData_.setValue("ERRORTABLE", "");
		controlData_.setValue("EDITION", "");
		controlData_.setValue("EDITIONTIME", "");
	}

        

	//
        // Initialise the OINPUT data area
	//
	private void initialiseInput() throws ParseException 
	{                
                // All values must be initialised, but can be set to an empty string or zero (as appropriate)
                // 1st parameter is External Name defined in SDS Physical Definition
                inputData_.setValue("Address", "10, Baker Street, London");

                // Create date to use in characteristic with External Name "DOB"
                Calendar cal = new GregorianCalendar(1965, Calendar.OCTOBER, 17);
                Date dob = cal.getTime();
                
                //*******************************
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String dateInString = "31-08-1982 10:20:56";
                Date dob2 = sdf.parse(dateInString); 
                
               // Date dob3 = new Date("31-08-1982 10:20:56");
                //******************************
                        
                // Set DOB (date of birth)
                inputData_.setValue("DOB", dob);                

                // Set salaries
		inputData_.setValue("Salary[1]", new Double(25000) );
		inputData_.setValue("Salary[2]", new Double(10000) );
	}



	//
        // Initialise the OINPUT2 data area
        //
	private void initialiseInput2() 
	{
                // All values must be initialised, but can be set to an empty string or zero (as appropriate)
                // 1st parameter is External Name defined in SDS Physical Definition
		inputData2_.setValue("MortgageToLet", "N");
                
                // Set house & mortage values
                // Uses random numbers, purely to give different results for each DA call
                // Normally you would not use random numbers
                Random rnd = new Random();

		inputData2_.setValue("RequestedMortgage", new Integer( rnd.nextInt(125000)) );
		inputData2_.setValue("HouseValue", new Integer( rnd.nextInt(100000)) );
	}



	//
	// Performs a DA call
        // @return error count (0 if successful)
	//
	private int performCall() 
	{
		int errors = 0;	// Error count returned by DA

                // Call the Decision Agent
                NTJSEMObjectInterface.instance().execute(dataAreas_, TRACE_LEVEL);      
                

                // Get the error count
                String errorStr = (String)controlData_.getValue("ERRORCOUNT");
				if (errorStr.length() > 0)
				{
							errors = Integer.parseInt(errorStr);
				}

                // Check for errors
                if (errors > 0) 
                {
                	System.out.println("DA call gave error\n\n");
                        
                        // Display OCONTROL data area (for debug use only)
                        displayDataArea(controlData_);
                }
                else 
                {
                	System.out.println("DA call successful\n\n");
                } // end if	
                
                return (errors);
	}

        
        
	//
        //  Displays contents of a specified data area (for debug use only)
	//  @param DataArea to be displayed
	//
        private void displayDataArea(DataArea data) 
	{
        	HashMap map = data.getMap();
        	Set keys = map.keySet();
        	Iterator itr = keys.iterator();
            
        	// Loop through data area & output it's values
        	while (itr.hasNext()) 
		{
                	String key = (String) itr.next();
                
                	System.out.println("key=" + key + 
                                   " value=" + data.getValue(key)); 
        	}   // end while
        }

}
