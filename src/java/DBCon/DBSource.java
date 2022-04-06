/**
 *
 * @author NIC-TRSC
 */
package DBCon;

public class DBSource {

    private boolean dsFlag = true;

//    public Connect connectToDBJami() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("landrecords");
//            return dbc.connect();
//        }
//    }
//    public Connect connectToLandRecord() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("landrecords");
//            return dbc.connect();
//        }
//    }
//    public Connect connectToLandImage() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("landimage");
//            return dbc.connect();
//        }
//    }
//    public Connect connectToNicWsDirectory() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("nic_ws_directory");
//            return dbc.connect();
//        }
//    }
//    public Connect connectToSQLServer() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("lrdb");
//            return dbc.connect();
//        }
//    }
//    public Connect connectToEstampDB() {
//        if(dsFlag==false){
//            return new DBCon.DBConnection("c:/database.properties").connect();
//        } else {
//            DBCon.DBConnection dbc=new DBCon.DBConnection("");
//            dbc.setDatasource("estamp_staging");
//            return dbc.connect();
//        }
//    }
    public Connect connectToAgriDbtDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            //dbc.setDatasource("agriculture");
            dbc.setDatasource("bms");
            return dbc.connect();
        }
    }
    
    public Connect connectToAgriDbtFilesDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("bms_files");
            return dbc.connect();
        }
    }

    public Connect connectToRCDataDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("rcdata");
            return dbc.connect();
        }
    }

    public Connect connectToRCDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("rc_nd");
            return dbc.connect();
        }
    }
    
    public Connect connectToRSAClientDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("rsaclient");
            return dbc.connect();
        }
    }
    
    public Connect connectToBMSDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("bms");
            return dbc.connect();
        }
    }
    
    public Connect connectToBMSEsignFilesDB() {
        if (dsFlag == false) {
            return new DBCon.DBConnection("c:/database.properties").connect();
        } else {
            DBCon.DBConnection dbc = new DBCon.DBConnection("");
            dbc.setDatasource("bms_esign_files");
            return dbc.connect();
        }
    }

}
