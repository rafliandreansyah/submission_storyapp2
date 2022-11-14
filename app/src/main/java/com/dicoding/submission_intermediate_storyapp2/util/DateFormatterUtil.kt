package com.dicoding.submission_intermediate_storyapp2.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun changeFormatDate(date: String): String {
    var orderDate: Date? = null
    var dateString1 = ""
    var dateString2 = ""
    var dateString = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val dateFormat1 = SimpleDateFormat("dd MMM yyyy")
    val dateFormat2 = SimpleDateFormat("HH:mm")
    try {
        orderDate = sdf.parse(date)
        dateString1 = dateFormat1.format(orderDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    try {
        orderDate = sdf.parse(date)
        dateString2 = dateFormat2.format(orderDate)
        dateString = "$dateString1  |  $dateString2"
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateString
}