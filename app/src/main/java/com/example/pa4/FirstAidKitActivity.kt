package com.example.pa4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView





class FirstAidKitActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirstAidKitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid_kit)

        recyclerView = findViewById(R.id.recyclerViewFirstAidKit)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            FirstAidItem("Adhesive tape"),
            FirstAidItem("Elastic wrap bandages"),
            FirstAidItem("Bandage strips and 'butterfly' bandages in assorted sizes"),
            FirstAidItem("Super glue"),
            FirstAidItem("Rubber tourniquet or 16 French catheter"),
            FirstAidItem("Nonstick sterile bandages and roller gauze in assorted sizes"),
            FirstAidItem("Eye shield or pad"),
            FirstAidItem("Large triangular bandage (may be used as a sling)"),
            FirstAidItem("Aluminum finger splint"),
            FirstAidItem("Instant cold packs"),
            FirstAidItem("Cotton balls and cotton-tipped swabs"),
            FirstAidItem("Disposable non-latex examination gloves, several pairs"),
            FirstAidItem("Duct tape"),
            FirstAidItem("Petroleum jelly or other lubricant"),
            FirstAidItem("Plastic bags, assorted sizes"),
            FirstAidItem("Safety pins in assorted sizes"),
            FirstAidItem("Scissors and tweezers"),
            FirstAidItem("Hand sanitizer"),
            FirstAidItem("Antibiotic ointment"),
            FirstAidItem("Antiseptic solution and towelettes"),
            FirstAidItem("Eyewash solution"),
            FirstAidItem("Thermometer"),
            FirstAidItem("Turkey baster or other bulb suction device for flushing wounds"),
            FirstAidItem("Sterile saline for irrigation, flushing"),
            FirstAidItem("Breathing barrier (surgical mask)"),
            FirstAidItem("Syringe, medicine cup or spoon"),
            FirstAidItem("First-aid manual"),
            FirstAidItem("Hydrogen peroxide to disinfect"),
            FirstAidItem("Aloe vera gel"),
            FirstAidItem("Calamine lotion"),
            FirstAidItem("Anti-diarrhea medication"),
            FirstAidItem("Laxative"),
            FirstAidItem("Antacids"),
            FirstAidItem("Antihistamine, such as diphenhydramine"),
            FirstAidItem("Hydrocortisone cream"),
            FirstAidItem("Cough and cold medications"),
            FirstAidItem("Personal medications that don't need refrigeration"),
            FirstAidItem("Auto-injector of epinephrine, if prescribed by your doctor"),
            FirstAidItem("Pain relievers, such as acetaminophen (Tylenol, others), ibuprofen (Advil, Motrin IB, others)"),
            FirstAidItem("Emergency space blanket"),
            FirstAidItem("Medical consent forms for each family member"),
            FirstAidItem("Medical history forms for each family member"),
            FirstAidItem("Small, waterproof flashlight or headlamp and extra batteries"),
            FirstAidItem("Waterproof matches"),
            FirstAidItem("Small notepad and waterproof writing instrument"),
            FirstAidItem("Cell phone with solar charger"),
            FirstAidItem("Insect repellent"),
            )

        adapter = FirstAidKitAdapter(items)
        recyclerView.adapter = adapter
    }
}

data class FirstAidItem(var name: String, var isChecked: Boolean = false)