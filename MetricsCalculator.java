import java.util.ArrayList;

public class MetricsCalculator {

    public double[] calculate(ArrayList<Process> processList, ArrayList<ScheduleLog> logs) {

        double totalWT = 0, totalTAT = 0, totalRT = 0;

        for (Process p : processList) {

            int finishTime = 0;
            int firstStart = -1;

            for (ScheduleLog log : logs) {

                if (log.getProcessName().equals(p.getId())) {

                    if (firstStart == -1)
                        firstStart = log.getStartTime();

                    finishTime = Math.max(finishTime, log.getEndTime());
                }
            }
            int tat = finishTime - p.getArrivalTime();
            int wt = tat - p.getBurstTime();
            int rt = firstStart - p.getArrivalTime();

            if (wt < 0)
                wt = 0;
            if (tat < 0)
                tat = 0;
            if (rt < 0)
                rt = 0;

            totalWT += wt;
            totalTAT += tat;
            totalRT += rt;

            System.out.println("Process: " + p.getId());
            System.out.println("  Waiting Time   : " + wt);
            System.out.println("  Turnaround Time: " + tat);
            System.out.println("  Response Time  : " + rt);
        }

        int total = processList.size();
        double avgWT = totalWT / total;
        double avgTAT = totalTAT / total;
        double avgRT = totalRT / total;

        System.out.println("\nAverage Waiting Time   : " + avgWT);
        System.out.println("Average Turnaround Time: " + avgTAT);
        System.out.println("Average Response Time  : " + avgRT);

        return new double[] { avgWT, avgTAT, avgRT };
    }
}