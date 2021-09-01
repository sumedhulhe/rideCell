package com.example.testapplication.map.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.testapplication.R
import com.example.testapplication.baseclasses.BaseFragment
import com.example.testapplication.map.viewmodel.MapsViewModel
import com.example.testapplication.utils.Constants
import com.example.testapplication.utils.Constants.Companion.FETCH_VEHICLES_SERVICE_ID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MapsFragment : BaseFragment() {
lateinit var  mGoogleMap:GoogleMap
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mapsViewModel: MapsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
    }
    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap=googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onRetry(serviceID: String) {
        when (serviceID) {
            FETCH_VEHICLES_SERVICE_ID->  mapsViewModel.fetchVehiclesApiCall()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        if (!mapsViewModel.apiResponse.hasObservers()) observeApiResponse()
        if (!mapsViewModel.getErrorResponse().hasObservers()) observeErrorResponse(
            mapsViewModel,
            requireView()
        )
        showProgressDialog("Loading...")
        mapsViewModel.fetchVehiclesApiCall()

    }

    private fun observeApiResponse() {
        mapsViewModel.apiResponse.observe(requireActivity(), Observer { apiResponse ->
            dismissProgressDialog()
            if (!apiResponse.isSuccess) {
                showToast(apiResponse.message)
            } else {
                when (apiResponse.serviceID) {
                    Constants.FETCH_VEHICLES_SERVICE_ID -> {
                    apiResponse.result?.forEach {
                        mGoogleMap.addMarker(MarkerOptions().position(LatLng(it.lat,it.lng)).title(it.vehicle_type))


                    }
                    }
                }
            }
        })
    }
    private fun getMarkerBitmapFromView(@DrawableRes resId: Int): Bitmap? {
        val customMarkerView: View =
            LayoutInflater.from(context).inflate(
                R.layout.view_custom_marker,
                null
            )
        val markerImageView: ImageView =
            customMarkerView.findViewById<View>(R.id.profile_image) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }
}