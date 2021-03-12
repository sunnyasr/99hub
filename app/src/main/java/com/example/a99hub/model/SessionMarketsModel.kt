package com.example.a99hub.model

 class SessionMarketsModel(
    private var event_id: String,
    private var transaction: String,
    private var commission: String
) {
     fun getEventID(): String {
         return event_id
     }

     fun getTransaction(): String {
         return transaction
     }
     fun getCommission(): String {
         return commission
     }
}