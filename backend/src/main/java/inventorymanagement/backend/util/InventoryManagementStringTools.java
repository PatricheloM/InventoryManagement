package inventorymanagement.backend.util;

import inventorymanagement.backend.util.constants.MessageConstants;
import inventorymanagement.backend.util.constants.RedisConstants;

public class InventoryManagementStringTools {
    public static String getAccountSetKey() {
        return RedisConstants.ACCOUNT_NAMES_KEY;
    }
    public static String getBadRequestMsg() {
        return MessageConstants.BAD_REQUEST_MSG;
    }
    public static String getInternalServerErrorMsg() {
        return MessageConstants.INTERNAL_SERVER_ERROR_MSG;
    }
    public static String getOKMsg() {
        return MessageConstants.OK_MSG;
    }
    public static String getUsernameAlreadyExistsMsg() {
        return MessageConstants.USERNAME_ALREADY_EXISTS_MSG;
    }
    public static String getUnauthorizedMsg() {
        return MessageConstants.UNAUTHORIZED_MSG;
    }
    public static String getForbiddenMsg() {
        return MessageConstants.FORBIDDEN_MSG;
    }
    public static String getLoggedOutMsg() {
        return MessageConstants.LOGGED_OUT_MSG;
    }
    public static String getSuccessfulRegistrationMsg() {
        return MessageConstants.SUCCESSFUL_REGISTRATION_MSG;
    }
    public static String getSuccessfulPasswordChangeMsg() {
        return MessageConstants.SUCCESSFUL_PASSWORD_CHANGE_MSG;
    }
    public static String getUserDoesNotExistMsg() {
        return MessageConstants.USER_DOES_NOT_EXIST_MSG;
    }
    public static String getUserDeletedMsg() {
        return MessageConstants.USER_DELETED_MSG;
    }
}
