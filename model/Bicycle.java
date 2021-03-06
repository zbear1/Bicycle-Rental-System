package model;

//System imports
//GUI Imports
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Bicycle extends EntityBase implements IView {
        
    private static final String myTableName = "Bicycle";
    protected Properties persistentState;
    protected Properties dependencies;
	Properties bikeInfo;
	public String make, model, color, condition,serialNumber, locationOnCampus, description, status;
    
    // GUI Components
    private String updateStatusMessage = "";

    //Initilize Bicycle
    public Bicycle(Properties props) {

        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }

    }

    //Takes an array of strings to insert bicycle into Database
    public void addBicycle(String[] Values) {
        String insertStatusMessage;
        try {
            if (persistentState.getProperty("bikeId") != null) {
                insertPersistentState(mySchema, persistentState);
                insertStatusMessage = "Account data for account number : " + persistentState.getProperty("bikeId") + " Inserted successfully!";
                System.out.println(insertStatusMessage);
            }
        }   
        catch (SQLException e) {
            updateStatusMessage = "Error in Adding Bicycle Data To Database!";
        }
            
     }

     protected void initializeSchema(String tableName) {
         if (mySchema == null) {
             mySchema = getSchemaInfo(tableName);
         }
     }

    public boolean update() {
    	try {
            if (persistentState.getProperty("bikeId") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("bikeId",
                persistentState.getProperty("bikeId"));
                updatePersistentState(mySchema, persistentState, whereClause);
            } else {
                Integer bikeId =
                    insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("bikeId", "" + bikeId.intValue());
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing bicycle data in database!";
			System.out.println(ex);
            return false;
        }
    	return true;
    }
	
	public void changeStatus() {
		try {
			Properties whereClause = new Properties();
			
			whereClause.setProperty("bikeId", persistentState.getProperty("bikeId"));
			updatePersistentState(mySchema, persistentState, whereClause);
			updateStatusMessage = " Updated Bike!";
		}
		catch(SQLException ex) {
			updateStatusMessage = "Failed to update!";
		}
		System.out.println(updateStatusMessage);
	}
	
	//-------------------------------------------------------------------
	public void getBikeInfo(Properties props) {
		String authQuery = "SELECT * FROM `" + myTableName + "` WHERE (`bikeId` = '" + props.getProperty("bikeId") + "');";
		Vector allDataRetrieved = getSelectQueryResult(authQuery);
		int size = allDataRetrieved.size();
		if(size == 1) {
			Properties retrievedBikeData = (Properties)allDataRetrieved.elementAt(0);
			bikeInfo = new Properties();
			
			Enumeration allKeys = retrievedBikeData.propertyNames();
			while(allKeys.hasMoreElements() == true) {
				String nextKey = (String)allKeys.nextElement();
				String nextValue = retrievedBikeData.getProperty(nextKey);
				
				if(nextValue != null) {
					bikeInfo.setProperty(nextKey, nextValue);
				}
			}
			
			make = bikeInfo.getProperty("make");
			model = bikeInfo.getProperty("model");
			condition = bikeInfo.getProperty("bikeCondition");
			color = bikeInfo.getProperty("color");
			serialNumber = bikeInfo.getProperty("serialNumber");
			locationOnCampus = bikeInfo.getProperty("locationOnCampus");
			description = bikeInfo.getProperty("description");
			status = bikeInfo.getProperty("status");
			
		} else {
			System.out.println("No Bike Found");
		}
		
	}
	//GETTERS FOR BIKE INFO
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public String getCondition() {
		return condition;
	}
	public String getColor() {
		return color;
	}
	public String getSerial() {
		return serialNumber;
	}
	public String getLocation() {
		return locationOnCampus;
	}
	public String getDescription() {
		return description;
	}
	public String getStatus() {
		return status;
	}

//-----------------------------------------------------------------------
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage") == true) {
            return updateStatusMessage;
        }

        return persistentState.getProperty(key);
    }

    public void save() {
        try {
            if(persistentState.getProperty("bikeId") != null) {
                update();
            }
        }
        catch(Exception ex) {
            System.out.println("Error in save: Unable to insert or update.");
        }
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    private void setDependencies() {   
        myRegistry.setDependencies(persistentState);
    }

}