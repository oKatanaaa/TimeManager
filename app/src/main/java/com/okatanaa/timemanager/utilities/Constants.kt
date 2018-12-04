package com.okatanaa.timemanager.utilities

import com.okatanaa.timemanager.R
import org.json.JSONObject

// ACTIONS
const val ACTION_DELETE = "delete"
const val ACTION_SAVE = "save"

// EXTRAS
const val EXTRA_EVENT_JSON = "event_json"
const val EXTRA_EDITED_NAME = "edited_name"
const val EXTRA_EDITED_VALUE = "edited_value"
const val EXTRA_ACTION = "action"
// JSON DATA
const val JSON_NAME = "name"
const val JSON_EVENT_DESCRIPTION = "description"
const val JSON_EVENTS = "events"
const val JSON_DAYS = "days"
const val JSON_WEEKS = "weeks"
const val JSON_EVENT_START_TIME = "start_time"
const val JSON_EVENT_END_TIME = "end_time"
const val JSON_IN_DAY = "in_day"
// JSON FILENAME
const val JSON_PRIMARY_DATA_WEEK_FILE = "primary_data.json"
const val JSON_DEFAULT_DATA_WEEK_FILE = R.raw.default_week