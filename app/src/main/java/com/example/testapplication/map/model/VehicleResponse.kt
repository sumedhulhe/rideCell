package com.example.testapplication.map.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleResponse(
    var vehicleList: ArrayList<VehicleList>
):Parcelable

@Parcelize
data class VehicleList(
    val id : Int,
    val is_active : Boolean,
    val is_available : Boolean,
    val lat : Double,
    val lng : Double,
    val license_plate_number : String,
    val remaining_mileage : String,
    val remaining_range_in_meters : String,
    val transmission_mode : String,
    val vehicle_make : String,
    val vehicle_pic_absolute_url : String,
    val vehicle_type : String,
    val vehicle_type_id : String
):Parcelable

data class VehicleResponseWrapper(
    var isSuccess:Boolean,
    var message:String,
    var error:Int,
    var serviceID:String,
    var result:ArrayList<VehicleList>?=null
)