package com.example.rentacar.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentacar.R
import com.example.rentacar.data.Car
import com.example.rentacar.data.CreditManager
import com.example.rentacar.databinding.ActivityMainBinding
import com.example.rentacar.ui.adapters.FavAdapter
import com.example.rentacar.ui.rent.RentActivity

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val vm = MainViewModel()
    private lateinit var favAdapter: FavAdapter

    private val rentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        vm.refresh()
        updateUI(vm.current())
        updateCredit()
        updateFavs()
        Toast.makeText(
            this,
            if (result.resultCode == RESULT_OK) "Booked!" else "Booking cancelled",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)

        // Overflow "Sort" menu
        b.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_rating -> { vm.sortByRating(); updateUI(vm.current()); true }
                R.id.sort_year   -> { vm.sortByYear();   updateUI(vm.current()); true }
                R.id.sort_cost   -> { vm.sortByCost();   updateUI(vm.current()); true }
                else -> false
            }
        }

        // Dark mode toggle
        b.switchDark.setOnCheckedChangeListener { _, on ->
            AppCompatDelegate.setDefaultNightMode(
                if (on) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Search
        b.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                vm.applySearch(newText ?: "")
                updateUI(vm.current())
                return true
            }
        })

        // Buttons on main card
        b.btnNext.setOnClickListener { updateUI(vm.next()) }
        b.btnRent.setOnClickListener { vm.current()?.let { openRent(it) } }
        b.btnFav.setOnClickListener {
            vm.current()?.let {
                vm.toggleFavourite(it.id)
                updateUI(it)
                updateFavs()
            }
        }
        b.imgCar.setOnLongClickListener {
            vm.current()?.let {
                vm.toggleFavourite(it.id)
                updateUI(it)
                updateFavs()
            }
            true
        }

        // Favourites RecyclerView — tap selects (does NOT toggle/remove)
        favAdapter = FavAdapter(emptyList()) { id ->
            val selected = vm.select(id)
            if (selected != null) {
                updateUI(selected)
            } else {
                Toast.makeText(this, "Car not available", Toast.LENGTH_SHORT).show()
            }
        }
        b.rvFavs.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = favAdapter
        }

        // Initial state
        vm.refresh()
        updateUI(vm.current())
        updateCredit()
        updateFavs()
    }

    private fun openRent(car: Car) {
        val i = Intent(this, RentActivity::class.java).apply {
            putExtra(RentActivity.EXTRA_CAR, car) // Parcelable extra
        }
        rentLauncher.launch(i)
    }

    private fun updateUI(car: Car?) {
        if (car == null) {
            b.tvNameModel.text = getString(R.string.no_cars)
            b.imgCar.setImageDrawable(null)
            b.tvYearKm.text = ""
            b.tvDaily.text = ""
            b.ratingBar.rating = 0f
            b.btnFav.setImageResource(R.drawable.ic_heart_outline)
            return
        }
        b.imgCar.setImageResource(car.imageRes)
        b.tvNameModel.text = getString(R.string.car_name_model, car.name, car.model)
        b.tvYearKm.text     = getString(R.string.year_km, car.year, car.kilometres)
        b.tvDaily.text      = getString(R.string.cost_per_day, car.dailyCost)
        b.ratingBar.rating  = car.rating
        b.btnFav.setImageResource(
            if (car.isFavourite) R.drawable.ic_heart_filled
            else R.drawable.ic_heart_outline
        )
    }

    private fun updateCredit() {
        b.tvCredit.text = getString(R.string.credit_value, CreditManager.balance)
    }

    private fun updateFavs() {
        favAdapter.replace(vm.favourites())
    }
}
