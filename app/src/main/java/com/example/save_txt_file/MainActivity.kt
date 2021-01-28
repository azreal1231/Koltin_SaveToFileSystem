package com.example.save_txt_file


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.save_json_file.Dog
import com.example.save_json_file.Owner
import org.json.JSONArray
import org.json.JSONObject
import java.io.*


class MainActivity : AppCompatActivity() {

    val fileName:String = "TestData.txt"
    val data:String = "hello there"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val fileName = findViewById<EditText>(R.id.editFile)


        val fileData = findViewById<EditText>(R.id.editData)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnView = findViewById<Button>(R.id.btnView)

        btnSave.setOnClickListener(View.OnClickListener {

            val json = JSONObject()

            val owner = Owner("Edward", "Nicholls", "CPT", 21)
            json.put("owner", addOwner(owner))
            json.put("numberOfDogs", 3)
            json.put("dogBreeds", JSONArray()
                .put("Sheepdog")
                .put("Beagle")
                .put("Rat"))
            val dogs = arrayListOf<Dog>(
                Dog("Buck", "Beagle", 2, 15.1),
                Dog("Hag", "Rat", 5, 5.0)
            )

            json.put("dogs", addDogs(dogs))

            val jsonString:String = json.toString()

            val fileOutputStream:FileOutputStream
            try {
                fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                fileOutputStream.write(jsonString.toByteArray())
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()
            }catch (e: Exception){
                e.printStackTrace()
            }
            Toast.makeText(applicationContext,"Data Saved",Toast.LENGTH_LONG).show()
        })

        btnView.setOnClickListener(View.OnClickListener {
//            val filename = fileName.text.toString()
            if(fileName.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(fileName)
                val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                //Displaying data on EditText
                fileData.setText(stringBuilder.toString()).toString()

                val jsonObject = JSONObject(stringBuilder.toString())
                print(jsonObject)

                val owner = JSONObject(jsonObject.getString("owner"))
                val owner2 = getOwner(owner)

                val dogBreeds = jsonObject.getJSONArray("dogBreeds")
                val dogBreeds2 = getDogBreeds(dogBreeds)

                val dogs = jsonObject.getJSONArray("dogs")
                val dogs2 = getDogs(dogs)

                val numDogs = jsonObject.getInt("numberOfDogs")
                val numDogs2 = numDogs

            }else{
                Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
            }
        })

//

    }

    private fun addOwner(_owner: Owner):JSONObject{

        return JSONObject()
            .put("firstName",_owner.firstName)
            .put("lastname",_owner.lastName)
            .put("city",_owner.city)
            .put("age",_owner.age)
    }

    private fun addDogs(_dogs: ArrayList<Dog>): JSONArray{
        val dogsJson = JSONArray()

        _dogs.forEach{
            dogsJson.put(
                JSONArray()
                    .put(it.name)
                    .put(it.breed)
                    .put(it.age)
                    .put(it.weight)
            )
        }

        return dogsJson
    }

    private fun getOwner(_jsonObj:JSONObject):Owner{

        return Owner(
            _jsonObj.getString("firstName"),
            _jsonObj.getString("lastname"),
            _jsonObj.getString("city"),
            _jsonObj.getInt("age")
        )
    }

    private fun getDogBreeds(_jsonArray: JSONArray):ArrayList<String>{
        val dogBreeds = ArrayList<String>()

        for (i in 0 until _jsonArray.length()){
            dogBreeds.add(_jsonArray[i].toString())
        }
        return dogBreeds
    }

    private fun getDogs(_jsonArray: JSONArray): ArrayList<Dog>{
        val dogs = ArrayList<Dog>()

        for (i in 0 until _jsonArray.length()){
            dogs.add(Dog(
                _jsonArray.getJSONArray(i).getString(0),
                _jsonArray.getJSONArray(i).getString(1),
                _jsonArray.getJSONArray(i).getInt(2),
                _jsonArray.getJSONArray(i).getString(3).toDouble()
            ))
        }
        return dogs
    }
}