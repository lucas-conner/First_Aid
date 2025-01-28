package com.example.pa4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EmergencyEssentialsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirstAidKitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_essentials)

        recyclerView = findViewById(R.id.recyclerViewEmergencyEssentials)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            FirstAidItem("Small, waterproof flashlight or headlamp and extra batteries"),
            FirstAidItem("Waterproof matches"),
            FirstAidItem("Water, 1 gallon per person per day"),
            FirstAidItem("Food that won't spoil, including baby food if needed"),
            FirstAidItem("Manual can opener for food"),
            FirstAidItem("Pet food and supplies, such as a leash, if needed"),
            FirstAidItem("Small notepad and waterproof writing instrument"),
            FirstAidItem("Blanket"),
            FirstAidItem("Cellphone with solar charger"),
            FirstAidItem("Battery-powered or hand-cranked radio and a weather radio with tone alert and extra batteries for both"),
            FirstAidItem("Insect repellent"),
            FirstAidItem("Whistle"),
            FirstAidItem("First-aid kit"),
            FirstAidItem("Dust mask"),
            FirstAidItem("Plastic sheeting and duct tape for improvised shelter"),
            FirstAidItem("Wrench or pliers to turn off utilities"),
            FirstAidItem("Medicine, a week's supply"),
            FirstAidItem("Extra medical supplies or equipment, as needed"),
            FirstAidItem("Soap, toothbrush, feminine supplies and other personal care items"),
            FirstAidItem("Moist towelettes, garbage bags, and plastic ties for personal sanitation"),
            FirstAidItem("Emergency health information for you and your family"),
            FirstAidItem("Phone numbers for professional emergency contacts, such as your family doctor and pediatrician"),
            FirstAidItem("Phone numbers for a personal emergency contact, such as a friend or a family member you've asked to serve in this role"),
            FirstAidItem("Copy of insurance cards"),
            FirstAidItem("Cash or traveler's checks and change"),
            FirstAidItem("Maps of the area"),
            FirstAidItem("An extra set of car keys and house keys")
        )

        adapter = FirstAidKitAdapter(items)
        recyclerView.adapter = adapter
    }
}