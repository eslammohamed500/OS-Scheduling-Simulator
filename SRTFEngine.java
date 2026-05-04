import java.util.ArrayList;

public class SRTFEngine {

    ArrayList<ScheduleLog> logs = new ArrayList<>();

    public ArrayList<ScheduleLog> calculate(ArrayList<Process> processList) {

        int time = 0;
        int done = 0;
        int total = processList.size();

        while (done < total) {

            int shortestIndex = -1;

            for (int i = 0; i < total; i++) {
                if (processList.get(i).getArrivalTime() <= time && processList.get(i).getRemainingTime() > 0) {
                    if (shortestIndex == -1 || processList.get(i).getRemainingTime() < processList.get(shortestIndex)
                            .getRemainingTime()) {
                        shortestIndex = i;
                    }
                }
            }

            if (shortestIndex == -1) {
                time++;
                continue;
            }

            int start = time;
            processList.get(shortestIndex).setRemainingTime(processList.get(shortestIndex).getRemainingTime() - 1);
            time++;

            if (!logs.isEmpty()
                    && logs.get(logs.size() - 1).getProcessName().equals(processList.get(shortestIndex).getId())
                    && logs.get(logs.size() - 1).getEndTime() == start) {
                logs.get(logs.size() - 1).setEndTime(time);
            } else {
                logs.add(new ScheduleLog(processList.get(shortestIndex).getId(), start, time));
            }

            if (processList.get(shortestIndex).getRemainingTime() == 0) {
                done++;
            }
        }

        return logs;
    }
}
