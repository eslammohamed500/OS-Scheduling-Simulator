import java.util.ArrayList;

public class RoundRobinEngine {

    ArrayList<ScheduleLog> logs = new ArrayList<>();

    public ArrayList<ScheduleLog> calculate(ArrayList<Process> processList, int quantum) {

        int time = 0;
        int done = 0;
        int total = processList.size();
        ArrayList<Integer> readyQueue = new ArrayList<>();
        boolean[] inQueue = new boolean[total];

        for (int i = 0; i < total; i++) {
            if (processList.get(i).getArrivalTime() == 0) {
                readyQueue.add(i);
                inQueue[i] = true;
            }
        }

        while (done < total) {

            if (readyQueue.isEmpty()) {
                time++;

                for (int i = 0; i < total; i++) {
                    if (processList.get(i).getArrivalTime() <= time && !inQueue[i]
                            && processList.get(i).getRemainingTime() > 0) {
                        readyQueue.add(i);
                        inQueue[i] = true;
                    }
                }
                continue;
            }

            int currentIndex = readyQueue.remove(0);
            Process current = processList.get(currentIndex);

            int start = time;
            int runTime = Math.min(quantum, current.getRemainingTime());

            current.setRemainingTime(current.getRemainingTime() - runTime);
            time += runTime;

            logs.add(new ScheduleLog(current.getId(), start, time));

            for (int i = 0; i < total; i++) {
                if (processList.get(i).getArrivalTime() <= time && !inQueue[i]
                        && processList.get(i).getRemainingTime() > 0) {
                    readyQueue.add(i);
                    inQueue[i] = true;
                }
            }

            if (current.getRemainingTime() == 0) {
                done++;
            } else {
                readyQueue.add(currentIndex);
            }
        }

        return logs;
    }
}