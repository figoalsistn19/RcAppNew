package com.inventoryapp.rcapp.data.repository

import com.inventoryapp.rcapp.data.model.AgentUser

interface AgentUserRepository {
    fun createAgentUser():AgentUser

    fun getAgentUser(): List<AgentUser>
}