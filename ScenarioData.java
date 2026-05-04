import java.util.ArrayList;
import java.util.List;

public class ScenarioData {

        public static class ProcessEntry {
                public String id;
                public int arrival;
                public int burst;

                public ProcessEntry(String id, int arrival, int burst) {
                        this.id = id;
                        this.arrival = arrival;
                        this.burst = burst;
                }
        }

        public static class Scenario {
                public String name;
                public String description;
                public String color;
                public List<ProcessEntry> processes;
                public int quantum;

                public Scenario(String name, String description, String color, int quantum) {
                        this.name = name;
                        this.description = description;
                        this.color = color;
                        this.quantum = quantum;
                        this.processes = new ArrayList<>();
                }

                public void addProcess(String id, int arrival, int burst) {
                        processes.add(new ProcessEntry(id, arrival, burst));
                }
        }

        /**
         * Returns all 5 predefined scenarios.
         */
        public static List<Scenario> getAllScenarios() {
                List<Scenario> scenarios = new ArrayList<>();

                // ─── Scenario A: Basic Mixed Workload ───────────────────────────────────
                Scenario a = new Scenario(
                                "A — Basic Mixed Workload",
                                "A normal workload with several processes of varying burst times.\n" +
                                                "Tests general correctness of both schedulers.",
                                "#7C3AED", // violet
                                4);
                a.addProcess("P1", 0, 8);
                a.addProcess("P2", 1, 4);
                a.addProcess("P3", 2, 9);
                a.addProcess("P4", 3, 5);
                scenarios.add(a);

                // ─── Scenario B: Quantum Sensitivity ────────────────────────────────────
                Scenario b = new Scenario(
                                "B — Quantum Sensitivity",
                                "A workload run with a small quantum (2) to show how quantum size\n" +
                                                "dramatically changes Round Robin's behavior and fairness.",
                                "#2563EB", // blue
                                2);
                b.addProcess("P1", 0, 10);
                b.addProcess("P2", 0, 6);
                b.addProcess("P3", 2, 4);
                b.addProcess("P4", 4, 8);
                scenarios.add(b);

                // ─── Scenario C: Short-Job-Heavy ────────────────────────────────────────
                Scenario c = new Scenario(
                                "C — Short-Job-Heavy Case",
                                "Many short jobs so SRTF's advantage becomes very visible.\n" +
                                                "SRTF should minimise waiting time significantly here.",
                                "#6D28D9", // purple
                                3);
                c.addProcess("P1", 0, 2);
                c.addProcess("P2", 1, 1);
                c.addProcess("P3", 2, 3);
                c.addProcess("P4", 3, 1);
                c.addProcess("P5", 4, 2);
                scenarios.add(c);

                // ─── Scenario D: Interactive Fairness ───────────────────────────────────
                Scenario d = new Scenario(
                                "D — Interactive Fairness Case",
                                "Workload designed to show whether Round Robin gives faster\n" +
                                                "first response to multiple processes arriving together.",
                                "#1D4ED8", // deeper blue
                                3);
                d.addProcess("P1", 0, 7);
                d.addProcess("P2", 0, 5);
                d.addProcess("P3", 0, 3);
                d.addProcess("P4", 0, 6);
                scenarios.add(d);

                // ─── Scenario E: Validation (includes edge / invalid awareness) ──────────
                Scenario e = new Scenario(
                                "E — Validation Case",
                                "Single long job plus late arrivals. Tests scheduler correctness\n" +
                                                "when the CPU sits idle waiting for new processes.",
                                "#4F46E5",
                                5);
                e.addProcess("P1", 0, 12);
                e.addProcess("P2", 15, 4);
                e.addProcess("P3", 15, 3);
                e.addProcess("P4", -1, 5);  
                e.addProcess("P5", 5, 0);    
                scenarios.add(e);

                return scenarios;
        }
}