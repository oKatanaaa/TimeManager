package com.okatanaa.timemanager.utils

import android.content.Context
import com.okatanaa.timemanager.model.Day
import com.okatanaa.timemanager.model.Event
import com.okatanaa.timemanager.model.Time
import com.okatanaa.timemanager.model.Week
import com.okatanaa.timemanager.services.Settings
import com.okatanaa.timemanager.utilities.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class JsonHelper {
    companion object {
        fun readJSON(context: Context): JSONObject {
            // Read json as a string
            var jsonString: String? = null

            // Check if json file with user's data exists
            var input: InputStream? = null
            try {
                input = context.openFileInput(JSON_PRIMARY_DATA_WEEK_FILE)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Json file does not exist. Read data from resources
            if(input == null)
                input = context.resources.openRawResource(JSON_DEFAULT_DATA_WEEK_FILE)

            // Read data
            val size = input?.available()
            val buffer = ByteArray(size!!)
            input?.read(buffer)
            input?.close()
            jsonString = String(buffer)

            return JSONObject(jsonString)
        }


        fun readWeekArr(json: JSONObject): ArrayList<Week> {
            val jsonWeekArray = json.getJSONArray(JSON_WEEKS)

            val weekArray = arrayListOf<Week>()
            for(i in 0 until jsonWeekArray.length())
                weekArray.add(weekFromJson(jsonWeekArray[i] as JSONObject))

            return weekArray
        }

        fun weekFromJson(json: JSONObject): Week {
            val weekName = json.getString(JSON_NAME)
            println(weekName)
            val jsonDayArray = json.getJSONArray(JSON_DAYS)

            val dayList = arrayListOf<Day>()
            for (i in 0 until jsonDayArray.length()) {
                dayList.add(dayFromJson(jsonDayArray[i] as JSONObject))
            }
            return Week(weekName, dayList)
        }

        fun dayFromJson(json: JSONObject): Day {
            val dayName = json.get(JSON_NAME) as String
            val jsonEventArray = json.getJSONArray(JSON_EVENTS)

            val eventList = arrayListOf<Event>()
            val day = Day(eventList, dayName)
            for (i in 0 until jsonEventArray.length()) {
                day.addExistingEvent(eventFromJson(jsonEventArray[i] as JSONObject))
            }
            return Day(eventList, dayName)
        }


        fun eventFromJson(json: JSONObject): Event {
            val eventName = json.getString(JSON_NAME)
            val eventDescription = json.getString(JSON_EVENT_DESCRIPTION)
            val eventInDay = json.getString(JSON_IN_DAY)
            val startTimeJsonArray = json.getJSONArray(JSON_EVENT_START_TIME)
            val startTime = arrayListOf(startTimeJsonArray[0] as Int, startTimeJsonArray[1] as Int)
            val endTimeJsonArray = json.getJSONArray(JSON_EVENT_END_TIME)
            val endTime = arrayListOf(endTimeJsonArray[0] as Int, endTimeJsonArray[1] as Int)
            val eventColor = json.getInt(JSON_COLOR)
            return Event(eventName, eventDescription, startTime, endTime, Day(title = eventInDay), eventColor)
        }

        fun weekToJson(week: Week): JSONObject {
            val json = JSONObject()

            val jsonArray = JSONArray()
            for(i in 0 until week.count()){
                jsonArray.put(dayToJson(week.getDay(i)))
            }
            json.put(JSON_NAME, week.name)
            json.put(JSON_DAYS, jsonArray)
            return json
        }

        fun dayToJson(day: Day): JSONObject {
            val json = JSONObject()
            json.put(JSON_NAME, day.title)

            val jsonArray = JSONArray()
            for(i in 0 until day.eventCount()){
                jsonArray.put(eventToJson(day.getEvent(i)))
            }

            json.put(JSON_EVENTS, jsonArray)
            return json
        }

        fun eventToJson(event: Event): JSONObject {
            val json = JSONObject()
            json.put(JSON_NAME, event.name)
            json.put(JSON_EVENT_DESCRIPTION, event.description)
            json.put(JSON_IN_DAY, event.inDay.toString())

            val jsonArrayStartTime = JSONArray()
            jsonArrayStartTime.put(event.startTimeArr[0])
            jsonArrayStartTime.put(event.startTimeArr[1])
            json.put(JSON_EVENT_START_TIME, jsonArrayStartTime)

            val jsonArrayEndTime = JSONArray()
            jsonArrayEndTime.put(event.endTimeArr[0])
            jsonArrayEndTime.put(event.endTimeArr[1])
            json.put(JSON_EVENT_END_TIME, jsonArrayEndTime)

            json.put(JSON_COLOR, event.color)

            return json
        }

        fun readSettings(json: JSONObject) {
            val settingsJson = json.getJSONObject(JSON_SETTINGS)
            val jsonTimeArray = settingsJson.getJSONArray(JSON_GLOBAL_START_TIME)
            val time = Time(jsonTimeArray[0] as Int, jsonTimeArray[1] as Int)
            Settings.globalStartTime = time
        }
    }
}

