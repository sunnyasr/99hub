package com.example.a99hub.model

class LedgerModel(
    private var event_id: String,
    private var market_id: String,
    private var long_name: String,
    private var short_name: String,
    private var winner: String,
    private var start_time: String,
    private var transaction: String,
    private var lost: String,
    private var won: String,
    private var balance: String,
) {
    fun getEventID(): String {
        return event_id
    }

    fun setEventID(event_id: String) {
        this.event_id = event_id
    }

    fun getMarketID(): String {
        return market_id
    }

    fun setMarketID(market_id: String) {
        this.market_id = market_id
    }

    fun getLongName(): String {
        return long_name
    }

    fun setLongName(long_name: String) {
        this.long_name = long_name
    }

    fun getShortName(): String {
        return short_name
    }

    fun setShortName(short_name: String) {
        this.short_name = short_name
    }

    fun getWinner()
            : String {
        return winner
    }

    fun setWinner(winner: String) {
        this.winner = winner
    }

    fun getStartTime(): String {
        return start_time
    }

    fun setStartTime(start_time: String) {
        this.start_time = start_time
    }

    fun getTransaction(): String {
        return transaction
    }

    fun setTransaction(transaction: String) {
        this.transaction = transaction
    }

    fun getLost(): String {
        return lost
    }

    fun setLost(lost: String) {
        this.lost = lost
    }

    fun getWon(): String {
        return won
    }

    fun setWon(won: String) {
        this.won = won
    }

    fun getBalance(): String {
        return balance
    }

    fun setBalance(balance: String) {
        this.balance = balance
    }


}