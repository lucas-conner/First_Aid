**First Aid App
**

The First Aid App is a tool designed to provide essential first aid information and resources. The app includes features like emergency contact assistance, CPR guidance, poison control, and a searchable database of first aid topics. This app serves as a vital resource for emergency preparedness and quick response.

**Features**

	•	Searchable Database: Easily find first aid information from a curated list of topics sourced from the Mayo Clinic's publicly available website.
	•	Emergency Call Assistance: Direct access to emergency services and poison control numbers.
	•	CPR Guidance: Navigate to CPR instructions for critical situations.
	•	Resource Dialogs: Information on first aid kit supplies, emergency essentials, and adding emergency health information.
	•	Disclaimer Modal: Users must accept a disclaimer upon first use, ensuring awareness of the app's scope and limitations.

**Technical Details
**

Key Components:
  
	•	Database:
	◦	Powered by Room Database.
	◦	Preloaded with first aid information from a JSON file.
	◦	Includes a DAO for querying and managing data efficiently.
	•	RecyclerView:
	◦	Displays first aid topics dynamically.
	◦	Supports real-time updates and search functionality.
	•	UI Elements:
	◦	Toolbar for navigation and branding.
	◦	AlertDialog for resource selection and emergency calls.
	◦	SearchView for filtering first aid topics.
	•	Permissions:
	◦	Handles CALL_PHONE permission dynamically for initiating emergency calls.
	•	Coroutines:
	◦	Uses lifecycleScope to ensure smooth data loading and UI updates.

**External Dependencies:
**

  •	Gson: For parsing JSON data into Kotlin objects.
	•	Room: For local database management.

**Installation
**

1. Clone the repository:
 	git clone https://github.com/username/FirstAidApp.git
	cd FirstAidApp
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

**Usage Instructions
**

  1	Launch the app.
	2	Accept the disclaimer to proceed.
	3	Use the navigation menu and buttons to:
  	◦	Access first aid resources.
	  ◦	Search for topics in the database.
	  ◦	Make emergency calls to services like poison control or 911.
	4	Update or add emergency health information as needed.

**Known Issues & Limitations
**

  1.	Data Source: Information is preloaded from a JSON file based on publicly available data from the Mayo Clinic. Regular updates to this source may require app updates.
	2.	Permissions: Users must grant phone permissions for emergency call functionality.
	3.	Localization: Currently supports only English.

**Future Enhancements
**

  •	Add support for additional languages.
	•	Enable live updates of the first aid database from online sources.
	•	Integrate with wearable devices for quick emergency responses.
	•	Incorporate video tutorials for CPR and other life-saving techniques.

**Contributing
**

Contributions are welcome. Please fork the repository and submit a pull request for review. Ensure your changes align with the project's goals and standards.

**License
**

This project is licensed under the MIT License.
