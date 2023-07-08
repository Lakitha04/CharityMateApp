package com.example.charitymate

class HungerDetails(
    var id: String,
    var title: String,
    var description: String,
    var location: String,
    var amountNeeded: String,
    var contact: String,
    var startDate: String,
    var endDate: String,
    var pic: String
)
//{
//    fun copy(
//        title: String = this.title,
//        description: String = this.description,
//        location: String = this.location,
//        amountNeeded: String = this.amountNeeded,
//        contact: String = this.contact,
//        startDate: String = this.startDate,
//        endDate: String = this.endDate,
//        pic: String = this.pic
//    ): HungerDetails {
//        return HungerDetails(id, title, description, location, amountNeeded, contact, startDate, endDate, pic)
//    }
//}