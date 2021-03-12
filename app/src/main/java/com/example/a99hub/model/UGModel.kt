package com.example.a99hub.model

class UGModel(
    private var sport_id: String,
    private var sport_name: String,
    private var sport_picture: String,
    private var event_id: String,
    private var market_id: String,
    private var long_name: String,
    private var short_name: String,
    private var start_time: String,
    private var competition_name: String,
    private var display_picture: String,
    private var inactive: String
) {
    fun getSportID(): String {
        return sport_id
    }

    fun setSportID(sport_id: String) {
        this.sport_id = sport_id
    }

    fun getSportName(): String {
        return sport_name
    }

    fun setSportName(sport_name: String) {
        this.sport_name = sport_name
    }

    fun getEventID(): String {
        return event_id
    }


    fun setEventID(event_id: String) {
        this.event_id = event_id
    }

    fun getLongName(): String {
        return long_name
    }

    fun setLongName(long_name: String) {
        this.long_name = long_name
    }

    fun getStartTime(): String {
        return start_time
    }

    fun setStartTime(start_time: String) {
        this.start_time = start_time
    }
    fun getInactive():String {
        return inactive
    }

    fun setInactive(inactive: String) {
        this.inactive = inactive
    }


}