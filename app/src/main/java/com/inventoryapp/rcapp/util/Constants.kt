package com.inventoryapp.rcapp.util

object FireStoreCollection{
    val NOTE = "note"
    val AGENTUSER = "AgentUser"
    val INTERNALUSER = "InternalUser"
    val OFFERINGBYSALES = "OfferingBySales"
    val INTERNALPRODUCT = "InternalProduct"
    val AGENTPRODUCT = "AgentProduct"
    val AGENTTRANSACTION = "AgentTransaction"
}

object FireDatabase{
    val TASK = "task"
}



object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
    val USER_STATUS = "user_status"
    val USER_ID = "user_id"
    val USER_NAME = "user_name"

}

object FirebaseStorageConstants {
    val ROOT_DIRECTORY = "app"
    val NOTE_IMAGES = "note"
}

enum class HomeTabs(val index: Int, val key: String) {
    NOTES(0, "notes"),
    TASKS(1, "tasks"),
}