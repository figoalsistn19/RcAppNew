package com.inventoryapp.rcapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

class AgentUserRepositoryImp(
    val database: FirebaseFirestore
): AgentUserRepository{
    override fun createAgentUser(): AgentUser {
        TODO("Not yet implemented")
    }

    override fun getAgentUser(): List<AgentUser> {
        return arrayListOf(
            AgentUser(
                idAgent = "dwqomqd",
                name = "Gox",
                email="dqwd@gmail.com",
                phoneNumber = "0291039123",
                address = "jl.hibrida",
                verificationStatus = VerifAccountStatus.APPROVED,
                createAt = Date()
            ),
            AgentUser(
                idAgent = "dwqomqd",
                name = "Gox",
                email="dqwd@gmail.com",
                phoneNumber = "0291039123",
                address = "jl.hibrida",
                verificationStatus = VerifAccountStatus.APPROVED,
                createAt = Calendar.getInstance().time
            ),
            AgentUser(
                idAgent = "dwqomqd",
                name = "Gox",
                email="dqwd@gmail.com",
                phoneNumber = "0291039123",
                address = "jl.hibrida",
                verificationStatus = VerifAccountStatus.APPROVED,
                createAt = Calendar.getInstance().time
            )
        )
    }

}