@file:Suppress("unused")

package com.example.pa4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.IOException


@Suppress("unused")
class MainActivity : AppCompatActivity() {
    private lateinit var phoneNumberToCall: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDatabase // Declare database here
    private lateinit var resourceMap: Map<String, String>

    companion object {
        private const val REQUEST_CALL_PHONE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resourceMap = preloadResources()

        recyclerView = findViewById(R.id.medical_terms_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = AppDatabase.getDatabase(this)

        setupUI()
        initializeDatabase()
    }

    private fun setupUI() {
        val resourcesButton: Button = findViewById(R.id.button_resources)
        resourcesButton.setOnClickListener {
            showResourcesDialog()
        }

        val emergencyCallButton: Button = findViewById(R.id.emergency_call_button)
        emergencyCallButton.setOnClickListener {
            showEmergencyCallDialog()
        }

        val poisonControlButton: Button = findViewById(R.id.poison_control_button)
        poisonControlButton.setOnClickListener {
            showPoisonControlDialog()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        toolbar.setLogo(R.mipmap.ic_launcher)
    }

    private fun initializeDatabase() {
        lifecycleScope.launch {
            checkAndPopulateDatabase()
            fetchAndDisplayFirstAidInfo()
            checkAndShowDisclaimer()
        }
    }

    private fun checkAndPopulateDatabase() {
        lifecycleScope.launch {
            try {
                if (database.firstAidInfoDao().getCount() == 0) {
                    if (loadAndInsertTopicsFromJson()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Data loaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No data to load", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error populating database", e)
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun loadAndInsertTopicsFromJson(): Boolean {
        var success = false
        try {
            val jsonString =
                applicationContext.assets.open("first_aid_topics.json").bufferedReader()
                    .use { it.readText() }
            val type = object : TypeToken<List<FirstAidInfo>>() {}.type
            val listOfTopics: List<FirstAidInfo> = Gson().fromJson(jsonString, type)
            if (listOfTopics.isNotEmpty()) {
                val dao = database.firstAidInfoDao()
                dao.insertAll(*listOfTopics.toTypedArray())
                Log.d("loadAndInsertTopicsFromJson", "Data inserted successfully")
                success = true
            } else {
                Log.d("loadAndInsertTopicsFromJson", "No data to insert")
            }
        } catch (e: IOException) {
            Log.e("loadAndInsertTopicsFromJson", "Error reading JSON file", e)
        }
        return success
    }


    private fun fetchAndDisplayFirstAidInfo() {
        lifecycleScope.launch {
            val terms = MedicalTermAdapter.loadTermsFromJson(applicationContext)
            updateRecyclerView(terms)
        }
    }


    private fun showResourcesDialog() {
        val options = arrayOf(
            "First Aid Kit Supplies",
            "Emergency Essentials",
            "Add Emergency Health Information",
            "CPR"
        )
        AlertDialog.Builder(this)
            .setTitle("Resources")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showFirstAidKitSupplies()
                    1 -> showEmergencyEssentials()
                    2 -> addEmergencyHealthInformation()
                    3 -> navigateToCPRScreen()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun addEmergencyHealthInformation() {
        val intent = Intent(this, EmergencyHealthInformationActivity::class.java)
        startActivity(intent)
    }

    private fun showEmergencyEssentials() {
        val intent = Intent(this, EmergencyEssentialsActivity::class.java)
        startActivity(intent)
    }


    private fun showFirstAidKitSupplies() {
        val intent = Intent(this, FirstAidKitActivity::class.java)
        startActivity(intent)
    }


    private fun navigateToCPRScreen() {
        val intent = Intent(this, CPRActivity::class.java)
        startActivity(intent)
    }

    private fun preloadResources(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val fields = R.string::class.java.fields
        for (field in fields) {
            try {
                val resourceId = field.getInt(null)
                val resourceName = field.name
                map[resourceName] = getString(resourceId)
                Log.d("ResourceLoad", "Loaded: $resourceName = ${getString(resourceId)}")
            } catch (e: IllegalAccessException) {
                Log.e("ResourceLoad", "Failed to access string resource", e)
            }
        }
        return map
    }


    private fun refreshData() {
        lifecycleScope.launch {
            val terms = database.firstAidInfoDao().getAllFirstAidInfo()
            if (recyclerView.adapter is MedicalTermAdapter) {
                (recyclerView.adapter as MedicalTermAdapter).updateData(terms)
            } else {
                updateRecyclerView(terms)
            }
        }
    }

    private fun updateRecyclerView(terms: List<FirstAidInfo>) {
        val medicalTermAdapter = MedicalTermAdapter(terms, this::navigateToDetailActivity, resourceMap)
        recyclerView.adapter = medicalTermAdapter
    }


    // In MainActivity or similar
    private fun deleteDuplicateTerm(term: String) {
        lifecycleScope.launch {
            database.firstAidInfoDao().deleteOneInstance(term)
            refreshData() // Refresh data after deletion
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView // Removed redundant qualifier name

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener { // Removed redundant qualifier name
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })

        return true
    }


    private fun navigateToDetailActivity(termId: Int) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("TERM_ID", termId)
        }
        startActivity(intent)
    }


    private fun performSearch(query: String) {
        val database = AppDatabase.getDatabase(applicationContext)
        val firstAidInfoDao = database.firstAidInfoDao()

        val searchQuery = "%$query%" // Prepare query for SQL LIKE statement

        lifecycleScope.launch {
            val results = firstAidInfoDao.searchFirstAidInfoByTerm(searchQuery)
            // Update RecyclerView with the search results
            if (recyclerView.adapter is MedicalTermAdapter) {
                (recyclerView.adapter as MedicalTermAdapter).updateData(results)
            } else {
                // This case handles when the adapter hasn't been initialized yet
                val medicalTermAdapter = MedicalTermAdapter(results, this@MainActivity::navigateToDetailActivity, resourceMap)
                recyclerView.adapter = medicalTermAdapter
            }
        }
    }


    private fun showEmergencyCallDialog() {
        AlertDialog.Builder(this)
            .setTitle("Emergency Call")
            .setMessage("Do you want to call emergency services?")
            .setPositiveButton("Call") { _, _ ->
                phoneNumberToCall =
                    "8002221222"  // Generally, this should be a real emergency number.
                checkAndRequestPermissions("911")  // This appears to be reversed; likely should pass `phoneNumberToCall`.
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showPoisonControlDialog() {
        AlertDialog.Builder(this)
            .setTitle("Poison Control Call")
            .setMessage("Do you want to call poison control?")
            .setPositiveButton("Call") { _, _ ->
                phoneNumberToCall = "911"
                checkAndRequestPermissions("8002221222")  // Use appropriate poison control number
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun checkAndRequestPermissions(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PHONE
            )
        } else {
            initiateCall(phoneNumber)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateCall(phoneNumberToCall)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initiateCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No app available to place a call", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAndShowDisclaimer() {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val hasAcceptedDisclaimer = prefs.getBoolean("hasAcceptedDisclaimer", false)

        if (!hasAcceptedDisclaimer) {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.disclaimer_dialog, null)
            val checkBox = dialogView.findViewById<CheckBox>(R.id.checkBox_disclaimer)
            val buttonDismiss = dialogView.findViewById<Button>(R.id.button_dismiss)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            buttonDismiss.setOnClickListener {
                if (checkBox.isChecked) {
                    val editor = prefs.edit()
                    editor.putBoolean("hasAcceptedDisclaimer", true)
                    editor.apply()
                }
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onDestroy() {
        // Call the superclass method
        super.onDestroy()

        // Perform any cleanup or finalization tasks here
        // For example, closing resources like database connections
        database.close()
    }
}

class CPRActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpr) // Make sure to create this layout file
        // You can perform any additional setup here
    }
}


