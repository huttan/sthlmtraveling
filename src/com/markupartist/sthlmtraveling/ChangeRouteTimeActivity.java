/*
 * Copyright (C) 2009 Johan Nilsson <http://markupartist.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.markupartist.sthlmtraveling;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class ChangeRouteTimeActivity extends Activity {
    static final String TAG = "ChangeRouteTimeActivity"; 
    static final int DIALOG_DATE = 0;
    static final int DIALOG_TIME = 1;

    private Time mTime;
    private Button mDateButton;
    private Button mTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_route_time);
        setTitle(getText(R.string.change_date_and_time));

        Bundle extras = getIntent().getExtras();
        final String startPoint = extras.getString(RoutesActivity.EXTRA_START_POINT);
        final String endPoint = extras.getString(RoutesActivity.EXTRA_END_POINT);
        String timeString = extras.getString(RoutesActivity.EXTRA_TIME);

        mTime = new Time();
        mTime.parse(timeString);

        mDateButton = (Button) findViewById(R.id.change_route_date);
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        mTimeButton = (Button) findViewById(R.id.change_route_time);
        mTimeButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });

        Button changeButton = (Button) findViewById(R.id.change_route_time_change);
        changeButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_OK, (new Intent())
                        .putExtra(RoutesActivity.EXTRA_TIME, mTime.format2445())
                        .putExtra(RoutesActivity.EXTRA_START_POINT, startPoint)
                        .putExtra(RoutesActivity.EXTRA_END_POINT, endPoint));
                finish();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.change_route_time_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        updateDisplay();
    }

    /**
     * Update time on the buttons.
     */
    private void updateDisplay() {
        String formattedDate = mTime.format("%x");
        String formattedTime = mTime.format("%R");
        mDateButton.setText(formattedDate);
        mTimeButton.setText(formattedTime);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DATE:
                return new DatePickerDialog(this, mDateSetListener,
                        mTime.year, mTime.month, mTime.monthDay);
            case DIALOG_TIME:
                // TODO: Base 24 hour on locale, same with the format.
                return new TimePickerDialog(this, mTimeSetListener,
                        mTime.hour, mTime.minute, true);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_DATE:
                ((DatePickerDialog) dialog).updateDate(mTime.year, mTime.month, mTime.monthDay);
                break;
            case DIALOG_TIME:
                ((TimePickerDialog) dialog).updateTime(mTime.hour, mTime.minute);
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                mTime.year = year;
                mTime.month = monthOfYear;
                mTime.monthDay = dayOfMonth;
                updateDisplay();
            }
        };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mTime.hour = hourOfDay;
                mTime.minute = minute;
                updateDisplay();
            }
        };
}
