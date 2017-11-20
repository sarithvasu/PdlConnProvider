package com.effone.pdlconnprovider.common;

/**
 * Created by sarith.vasu on 17-05-2016.
 */
public class PdlApiUtil {
    public static final String BASE_URL="http://asap.pdllabs.com/MAPPAPI/api/";
    public static final String GET_LOCATIONS=BASE_URL+"location";
    public static final String GET_CONTACT_US=BASE_URL+"ContactInformation";
    public static final String GET_ABOUT_US=BASE_URL+"ContentInformation?contentType=8";
    public static final String GET_PRIVACY=BASE_URL+"ContentInformation?contentType=9";
    public static final String GET_FAQ=BASE_URL+"FAQ";


    /*effone internal */

  /*  public static final String GET_TEST_CAT_UPDATE="http://192.168.2.200:86/MAPPAPI/api/TestCatalogInit?SyncAuditDate=";
    public static final String GET_NEEDED_DATA_SYNC="http://192.168.2.200:86/MAPPAPI/api/DataSync";
    public static final String GET_TEST_CAT_DETAILS="http://192.168.2.200:86/MAPPAPI/api/TestCatalogInit?catalogAuditDate=";*/

    public static final String GET_TEST_CAT_UPDATE=BASE_URL+"TestCatalogInit?SyncAuditDate=";
    public static final String GET_NEEDED_DATA_SYNC=BASE_URL+"DataSync";
    public static final String GET_TEST_CAT_DETAILS=BASE_URL+"TestCatalogInit?catalogAuditDate=";








    public static final String PDL_WEB_URL="http://www.pdllabs.com";

}
