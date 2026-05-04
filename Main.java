import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int num = scanner.nextInt();

        ArrayList<Process> processList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            System.out.print("Process ID: ");
            String id = scanner.next();
            System.out.print("Arrival Time: ");
            int arrival = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burst = scanner.nextInt();

            if (arrival < 0 || burst <= 0) {
                System.out.println("Invalid input! Arrival must be >= 0 and Burst must be > 0");
                i--;
                continue;
            }
            processList.add(new Process(id, arrival, burst));
        }

        System.out.print("\nEnter Time Quantum: ");
        int quantum = scanner.nextInt();
        while (quantum <= 0) {
            System.out.println("Invalid quantum! Must be > 0");
            System.out.print("Enter Time Quantum: ");
            quantum = scanner.nextInt();
        }

        // نسخ العمليات لكل خوارزمية لوحدها
        ArrayList<Process> RRList = new ArrayList<>();
        ArrayList<Process> SRTFList = new ArrayList<>();
        for (Process p : processList) {
            RRList.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime()));
            SRTFList.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime()));
        }

        // تشغيل الخوارزميات
        RoundRobinEngine RR = new RoundRobinEngine();
        ArrayList<ScheduleLog> RRLogs = RR.calculate(RRList, quantum);

        SRTFEngine SRTF = new SRTFEngine();
        ArrayList<ScheduleLog> SRTFLogs = SRTF.calculate(SRTFList);

        // Gantt Charts
        System.out.println("\n========== Round Robin Gantt Chart ==========");
        for (ScheduleLog log : RRLogs) {
            System.out.print("| " + log.getProcessName() + " (" + log.getStartTime() + "-" + log.getEndTime() + ") ");
        }
        System.out.println("|");

        System.out.println("\n========== SRTF Gantt Chart ==========");
        for (ScheduleLog log : SRTFLogs) {
            System.out.print("| " + log.getProcessName() + " (" + log.getStartTime() + "-" + log.getEndTime() + ") ");
        }
        System.out.println("|");

        // النتائج
        System.out.println("\n========== Round Robin Metrics ==========");
        MetricsCalculator RRMetrics = new MetricsCalculator();
        double[] RRAvg = RRMetrics.calculate(RRList, RRLogs);

        System.out.println("\n========== SRTF Metrics ==========");
        MetricsCalculator SRTFMetrics = new MetricsCalculator();
        double[] SRTFAvg = SRTFMetrics.calculate(SRTFList, SRTFLogs);

        // المقارنة
        System.out.println("\n========== Comparison Summary ==========");

        if (RRAvg[0] < SRTFAvg[0])
            System.out.println("Better Waiting Time   : Round Robin");
        else
            System.out.println("Better Waiting Time   : SRTF");

        if (RRAvg[1] < SRTFAvg[1])
            System.out.println("Better Turnaround Time: Round Robin");
        else
            System.out.println("Better Turnaround Time: SRTF");

        if (RRAvg[2] < SRTFAvg[2])
            System.out.println("Better Response Time  : Round Robin");
        else
            System.out.println("Better Response Time  : SRTF");

        scanner.close();
    }
}
