package com.khumbu.dailyplanner.constants;

public interface DailyPlannerConstants {

    String FIELDS_MISSING = "Enter all required fields.";
    String USER_ALREADY_EXISTS = "User with provided username already exists. ";

    String BOTH_START_END_TIME_REQUIRED = "Start or End time cannot be empty when start time is set";

    String START_TIME_AFTER_END_TIME = "Start time cannot be after end time";
    String SAME_START_END_TIME = "Invalid time: Start and end time cannot be the same";

    String NO_EMAIL = "Email cannot be empty, login and try again";
}
