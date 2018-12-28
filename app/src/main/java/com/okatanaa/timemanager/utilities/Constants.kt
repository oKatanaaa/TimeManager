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
const val EXTRA_TOP_TIME_BORDER = "top time border"
const val EXTRA_BOTTOM_TIME_BORDER = "bottom time border"
const val EXTRA_EVENT_POSITION = "event_position"

// JSON DATA
const val JSON_NAME = "name"
const val JSON_EVENT_DESCRIPTION = "description"
const val JSON_EVENTS = "events"
const val JSON_DAYS = "days"
const val JSON_WEEKS = "week_list"
const val JSON_EVENT_START_TIME = "start_time"
const val JSON_EVENT_END_TIME = "end_time"
const val JSON_IN_DAY = "in_day"
const val JSON_GLOBAL_START_TIME = "global_start_time"
const val JSON_SETTINGS = "settings"
const val JSON_COLOR = "color"

// JSON FILENAME
const val JSON_PRIMARY_DATA_WEEK_FILE = "primary_data.json"
const val JSON_DEFAULT_DATA_WEEK_FILE = R.raw.default_week

// REQUEST CODES
const val RC_EVENT_ACTIVITY = 0
const val RC_TEXT_EDITOR_ACTIVITY = 1

// BUNDLE STRING KEYS

const val BUNDLE_TIME = "time"