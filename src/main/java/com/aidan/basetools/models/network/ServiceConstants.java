package com.aidan.basetools.models.network;

import com.aidan.basetools.utils.LogHelper;
import com.aidan.basetools.models.event.ServiceEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Aidan on 2018/3/10.
 */


public class ServiceConstants {
    public static final String JSON_KEY_ERROR = "error";
    public static final String JSON_KEY_CODE = "code";
    public static final String JSON_KEY_RESULT = "result";
    public static final String JSON_KEY_DATA = "data";
    public static final String API_CHECK_NEW_VERSION = "repositories/getNew";


    /* JSON KEY */
    public static final String JSON_KEY_IS_SUCCESS = "isSuccess";
    public static final String JSON_KEY_ERROR_CODE = "errorCode";
    public static final String JSON_KEY_ERROR_MEESAGE = "errorMessage";
    public static final String JSON_KEY_ERROR_DATA = "errorData";

    /* Error Code & Data */
    public static final String ERROR_DATA_SESSION_FAIL = "SESSION_FAIL";
    public static final int ERROR_CODE_404 = 404;
    public static final int ERROR_CODE_403 = 403;
    public static final int ERROR_CODE_400 = 400;

    public static class ErrorHandler {

        public static final int CODE_UNKNOWN = 0;

        public int errorCode = 0;
        public String errorMessage = "";
        public String errorData = "";

        public ErrorHandler() {
        }

        public ErrorHandler(int errorCode, String errorMessage, String errorData) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.errorData = errorData;
        }

        public ErrorHandler(JSONObject jsonObject) {
            try {
                errorCode = jsonObject.getInt(JSON_KEY_ERROR_CODE);
                errorMessage = jsonObject.getString(JSON_KEY_ERROR_MEESAGE);
                errorData = jsonObject.getString(JSON_KEY_ERROR_DATA);
                LogHelper.log("error occured: " + errorMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* News */
    public interface JSONObjectCallback {
        void onResult(Boolean isSuccess, JSONObject data, ErrorHandler errorHandler);
    }

    public interface JSONArrayCallback {
        void onResult(Boolean isSuccess, JSONArray data, ErrorHandler errorHandler);
    }

    public interface IntCallback {
        void onResult(Boolean isSuccess, int data, ErrorHandler errorHandler);
    }

    public interface BoolCallback {
        void onResult(Boolean isSuccess, Boolean data, ErrorHandler errorHandler);
    }

    public interface StringCallback {
        void onResult(Boolean isSuccess, String data, ErrorHandler errorHandler);
    }

    public static void checkAndGetInt(String input_data, IntCallback callback) {

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(input_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (input_data == null || jsonObj == null || (!jsonObj.has(JSON_KEY_RESULT))) {
            LogHelper.log("error occured input data is not a jsonobject");
            callback.onResult(false, 0, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            return;
        }

        try {
            JSONObject responseResult = jsonObj.getJSONObject(JSON_KEY_RESULT);
            Boolean isSuccess = responseResult.getBoolean(JSON_KEY_IS_SUCCESS);

            if (!isSuccess) {
                ErrorHandler errorHandler = new ErrorHandler(responseResult);
                callback.onResult(false, 0, errorHandler);
                //check Login Token
                checkLoginToken(errorHandler);
            } else {
                int result = responseResult.getInt(JSON_KEY_RESULT);
                callback.onResult(true, result, new ErrorHandler());
            }

        } catch (JSONException e) {
            callback.onResult(false, 0, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            e.printStackTrace();
        }
    }

    public static void checkAndGetBoolean(String input_data, BoolCallback callback) {

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(input_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (input_data == null || jsonObj == null || (!jsonObj.has(JSON_KEY_RESULT))) {
            LogHelper.log("error occured input data is not a jsonobject");
            callback.onResult(false, false, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            return;
        }

        try {
            JSONObject responseResult = jsonObj.getJSONObject(JSON_KEY_RESULT);
            Boolean isSuccess = responseResult.getBoolean(JSON_KEY_IS_SUCCESS);

            if (!isSuccess) {
                ErrorHandler errorHandler = new ErrorHandler(responseResult);
                callback.onResult(false, false, errorHandler);
                //check Login Token
                checkLoginToken(errorHandler);
            } else {
                Boolean result = responseResult.getBoolean(JSON_KEY_RESULT);
                callback.onResult(true, result, new ErrorHandler());
            }

        } catch (JSONException e) {
            callback.onResult(false, false, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            e.printStackTrace();
        }
    }

    public static void checkAndGetString(String input_data, StringCallback callback) {

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(input_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (input_data == null || jsonObj == null) {
            LogHelper.log("error occured input data is not a jsonobject");
            callback.onResult(false, "", new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            return;
        }

        try {
            String result = jsonObj.getString(JSON_KEY_DATA);
            callback.onResult(true, result, new ErrorHandler());
        } catch (JSONException e) {
            callback.onResult(false, "", new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            e.printStackTrace();
        }
    }

    public static void checkAndGetData(String input_data, JSONObjectCallback callback) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(input_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (input_data == null || jsonObj == null) {
            LogHelper.log("error occured input data is not a jsonobject");
            callback.onResult(false, null, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            return;
        }

        try {
            callback.onResult(jsonObj.getBoolean(JSON_KEY_RESULT), jsonObj.getJSONObject(JSON_KEY_DATA), new ErrorHandler(jsonObj));
        } catch (JSONException e) {
            callback.onResult(false, null, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            e.printStackTrace();
        }

    }

    public static void checkAndGetArray(String input_data, JSONArrayCallback callback) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(input_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (input_data == null || jsonObj == null || (!jsonObj.has(JSON_KEY_RESULT))) {
            LogHelper.log("error occured input data is not a jsonobject");
            callback.onResult(false, null, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            return;
        }

        try {
            callback.onResult(jsonObj.getBoolean(JSON_KEY_RESULT), jsonObj.getJSONArray(JSON_KEY_DATA), new ErrorHandler(jsonObj));
        } catch (JSONException e) {
            callback.onResult(false, null, new ErrorHandler(ErrorHandler.CODE_UNKNOWN, "", ""));
            e.printStackTrace();
        }

    }

    private static void checkLoginToken(ErrorHandler errorHandler) {
        if ((errorHandler.errorCode == ERROR_CODE_404) &&
                (errorHandler.errorData.equals(ERROR_DATA_SESSION_FAIL))) {
            EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_MSG_TOKEN_INVALID));
        }
    }

}