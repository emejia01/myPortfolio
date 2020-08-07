// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

// Data Structures Needed
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;


public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

        if (request.getDuration() > TimeRange.WHOLE_DAY.end())
            return new ArrayList<>();
        else if (events == null || events.isEmpty())
            return new ArrayList<>(Collections.singletonList(TimeRange.WHOLE_DAY));
        else{ 

            // Add time range into Arraylist
            ArrayList<TimeRange> meetingTimeRanges = new ArrayList<>();
            for (Event event: events){
                if (!Collections.disjoint(request.getAttendees(), event.getAttendees())){
                    TimeRange eventTimeRange = event.getWhen();

                    if (meetingTimeRanges.size() == 0)
                        meetingTimeRanges.add(eventTimeRange);
                    else{
                        int lastIndex = meetingTimeRanges.size()-1;
                        TimeRange lastMeetingTime = meetingTimeRanges.get(lastIndex);

                        if (eventTimeRange.overlaps(lastMeetingTime))
                            meetingTimeRanges.set(lastIndex, joinTimes(eventTimeRange, lastMeetingTime));
                        else
                            meetingTimeRanges.add(eventTimeRange);
                    }
                }
            }

            ArrayList<TimeRange> freeTimeSlots = new ArrayList<>();
            int startTime = 0;
            int endTime;

            // Loop to find available free time slots
            for (TimeRange singleMeetingTime: meetingTimeRanges){ 

                endTime = singleMeetingTime.start();
                if (endTime - startTime >= request.getDuration())
                    freeTimeSlots.add(TimeRange.fromStartEnd(startTime, endTime, false));
                startTime = singleMeetingTime.end();
            }

            if (startTime < TimeRange.WHOLE_DAY.end())
                freeTimeSlots.add(TimeRange.fromStartEnd(startTime, TimeRange.WHOLE_DAY.end(), false));

            return freeTimeSlots;
        }
    }

    public static TimeRange joinTimes(TimeRange firstMeeting, TimeRange secondMeeting) {
        return TimeRange.fromStartEnd(Math.min(firstMeeting.start(), secondMeeting.start()), Math.max(firstMeeting.end(), secondMeeting.end()), false);
    }
}
