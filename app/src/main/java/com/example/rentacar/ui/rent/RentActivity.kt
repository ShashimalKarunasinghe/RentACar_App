package com.example.rentacar.ui.rent

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rentacar.R
import com.example.rentacar.data.Car
import com.example.rentacar.data.CarRepository
import com.example.rentacar.data.CreditManager
import com.example.rentacar.databinding.ActivityRentBinding

class RentActivity : AppCompatActivity() {

    companion object { const val EXTRA_CAR = "extra_car" }

    private lateinit var b: ActivityRentBinding
    private lateinit var car: Car
    private var days = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRentBinding.inflate(layoutInflater)
        setContentView(b.root)


        car = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_CAR, Car::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Car>(EXTRA_CAR)!!
        }

        bindCar()

        b.sliderDays.addOnChangeListener { _, value, _ ->
            days = value.toInt()
            b.tvDays.text = getString(R.string.days_total_fmt, days * car.dailyCost, days)
            validate()
        }

        b.btnSave.setOnClickListener {
            if (validate()) {
                CreditManager.charge(days, car.dailyCost)
                CarRepository.markRented(car)
                setResult(RESULT_OK)
                finish()
            }
        }

        b.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun bindCar() {
        b.imgCar.setImageResource(car.imageRes)
        // "Aurora LX (2021)"
        b.tvTitle.text = getString(R.string.car_name_model_year, car.name, car.model, car.year)
        b.tvCost.text  = getString(R.string.cost_per_day, car.dailyCost)
        b.tvDays.text  = getString(R.string.days_total_fmt, days * car.dailyCost, days)
    }

    private fun validate(): Boolean {
        val ok = CreditManager.canAfford(days, car.dailyCost)
        b.tvError.visibility = if (ok) View.GONE else View.VISIBLE
        return ok
    }
}
