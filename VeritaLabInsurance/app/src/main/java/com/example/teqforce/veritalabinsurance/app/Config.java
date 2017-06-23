package com.example.teqforce.veritalabinsurance.app;

/**
 * Created by vikash.verma on 06/12/2017.
 */

public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://10.2.0.129:8080/android_sms/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://10.2.0.129:8080/android_sms/verify_otp.php";
    public static final String URL_VERIFY_EXISTING_USER = "http://10.2.0.129:8080/android_sms/Login.php";
    public static final String URL_GENERATE_OTP_EXISTING_USER = "http://10.2.0.129:8080/android_sms/GenerateOtp.php";
    public static final String FILE_UPLOAD_URL = "http://10.2.0.129:8080/AndroidImageUpload/fileUpload.php";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String URL_INSERT_NEW_USER_REG_DETAILS = "http://10.2.0.129:8080/android_sms/InsertNewUserVehicleDetails.php";
    public static final String URL_GET_MANUFACTURE_LIST = "http://10.2.0.129:8080/android_sms/get_categories.php";
    public static final String URL_GET_MODEL_LIST = "http://10.2.0.129:8080/android_sms/GetModelVarCascading.php";
    public static final String URL_GET_INSPECTION_ID = "http://10.2.0.129:8080/android_sms/GenerateinspectionID.php";
    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "ANHIVE";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}

