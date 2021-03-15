package com.example.a99hub.model

class BetsModel(
    private var notional_profit: Int,
    private var ip: String,
    private var name: String,
    private var team: String,
    private var size: String,
    private var notional_loss: Int,
    private var parent_id: Int,
    private var rate: Int,
    private var action: String,
    private var created: String,
    private var amount: String,
    private var client_id: String,
    private var market_id: String,
    private var ledger: Int,
    private var type: Int,
) {
    fun getRate(): Int {
        return rate
    }

    fun getName(): String {
        return name
    }

    fun getSize(): String {
        return size
    }

    fun getAction(): String {

        if (action.equals("1"))
            return "LAGAI"
        else

            return "KHAI"
    }

    fun getAmount(): String {
        return amount
    }

    fun getTeam(): String {
        return team
    }

    fun getMode(): String {

        if (type==1)
            return "YES"
        else

            return "NOT"
    }
}