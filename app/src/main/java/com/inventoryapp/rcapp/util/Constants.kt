package com.inventoryapp.rcapp.util

object FireStoreCollection{
    const val INTERNALSTOCKTRANSACTION = "InternalStockTransaction"
    const val AGENTUSER = "AgentUser"
    const val INTERNALUSER = "InternalUser"
    const val OFFERINGFORAGENT = "OfferingForAgent"
    const val INTERNALPRODUCT = "InternalProduct"
    const val AGENTPRODUCT = "AgentProduct"
    const val AGENTTRANSACTION = "AgentTransaction"
    const val SALESORDER = "SalesOrder"
    const val CARTDATA = "CartData"

}


object SharedPrefConstants {
    const val LOCAL_SHARED_PREF = "local_shared_pref"
    //SHARED PREF FOR AGENT
    const val USER_SESSION = "user_session"
    const val USER_STATUS = "user_status"
    const val USER_ID = "user_id"
    const val USER_NAME = "user_name"
    const val USER_EMAIL = "user_email"

    //SHARED PREF FOR INTERNAL
    const val USER_SESSION_INTERNAL = "user_session_internal"
    const val USER_ROLE_INTERNAL = "user_role"
    const val USER_ID_INTERNAL = "user_id_internal"
    const val USER_NAME_INTERNAL = "user_name_internal"
    const val USER_EMAIL_INTERNAL = "user_email_internal"
}
