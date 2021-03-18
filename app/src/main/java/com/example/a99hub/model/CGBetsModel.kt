package com.example.a99hub.model

class CGBetsModel(
    private var bet_amount: String,
    private var bet_type: String,
    private var market_id: String,
    private var rate: String,
    private var team: String,
    private var action: String,
    private var created: String,
    private var client_id: String,
    private var transaction_reference: String,
    private var transaction_amount: String,
    private var transaction_type: String,

) {
    fun getRate(): String {
        return rate
    }



    fun getAction(): String {

        if (action.equals("1"))
            return "LAGAI"
        else

            return "KHAI"
    }

    fun getBetAmount(): String {
        return bet_amount
    }
    fun getTransAmount(): String {
        return transaction_amount
    }

    fun getTeam(): String {
        return team
    }

//    fun getMode(): String {
//
//        if (type==1)
//            return "YES"
//        else
//
//            return "NOT"
//    }
}