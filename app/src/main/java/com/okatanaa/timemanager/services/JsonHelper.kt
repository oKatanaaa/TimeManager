package com.okatanaa.timemanager.services

import android.content.Context
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.utilities.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class JsonHelper {
    companion object {

        fun readJSON(context: Context): JSONObject {
            // Read json as a string
            var jsonString: String? = null

            val input: InputStream = context.assets.open("json_file.json")
            val size = input.available()
            val buffer: ByteArray = ByteArray(size)
            input.read(buffer)
            input.close()
            jsonString = String(buffer)

            // Convert json string to a JSON object

            val json = JSONObject(jsonString)
            return json
        }

        // Temporary function. Need it for testing
        fun readFirstWeekFromJson(json: JSONObject): Week {
            val jsonWeekArray = json.getJSONArray(JSON_WEEKS)
            return weekFromJson(jsonWeekArray[0] as JSONObject)
        }

        fun weekFromJson(json: JSONObject): Week {
            val jsonDayArray = json.getJSONArray(JSON_DAYS)

            val dayList = arrayListOf<Day>()
            for (i in 0..jsonDayArray.length()) {
                dayList.add(dayFromJson(jsonDayArray[i] as JSONObject))
            }
            return Week(dayList)
        }

        fun dayFromJson(json: JSONObject): Day {
            val dayName = json.get(JSON_NAME) as String
            val jsonEventArray = json.getJSONArray(JSON_EVENTS)

            val eventList = arrayListOf<Event>()
            for (i in 0..jsonEventArray.length()) {
                eventList.add(eventFromJson(jsonEventArray[i] as JSONObject))
            }
            return Day(eventList, dayName)
        }

        fun eventFromJson(json: JSONObject): Event {
            val eventName = json.get(JSON_NAME) as String
            val eventDescription = json.get(JSON_DESCRIPTION) as String
            return Event(eventName, eventDescription)
        }

    }
}

