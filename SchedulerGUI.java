import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchedulerGUI {

    JFrame frame;

    // ===== SCENARIO COMPONENTS =====
    List<ScenarioData.Scenario> scenarios;

    // ===== INPUT COMPONENTS =====
    JTextField nameField, arrivalField, burstField, quantumField;
    JButton addBtn, runBtn, clearBtn;
    JTable inputTable;
    DefaultTableModel inputModel;
    ArrayList<Process> processList;

    // ===== RR COMPONENTS =====
    JTable rrTable;
    DefaultTableModel rrModel;
    GanttPanel rrGantt;
    JTextArea rrReadyQueueArea;
    JLabel rrMetricsLabel;

    // ===== SRTF COMPONENTS =====
    JTable srtfTable;
    DefaultTableModel srtfModel;
    GanttPanel srtfGantt;
    JLabel srtfMetricsLabel;

    // ===== CONCLUSION COMPONENT =====
    JTextArea conclusionArea;

    // ===== COLORS & FONTS =====
    Color bgColor = new Color(28, 28, 40);
    Color panelBg = new Color(38, 38, 55);
    Color inputBg = new Color(55, 55, 75);
    Color accentColor = new Color(0, 120, 215);
    Color scenarioBtnColor = new Color(200, 100, 0);
    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    Font normalFont = new Font("Segoe UI", Font.PLAIN, 13);

    public SchedulerGUI() {
        processList = new ArrayList<>();
        scenarios = ScenarioData.getAllScenarios();

        // ================= FRAME =================
        frame = new JFrame("CPU Scheduling Comparison: RR vs SRTF");
        frame.setSize(1200, 950);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(bgColor);

        // ================= NORTH: CONTROLS & INPUT PANEL =================
        JPanel topContainer = new JPanel(new GridLayout(2, 1, 5, 5));
        topContainer.setBackground(bgColor);

        JPanel scenarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        scenarioPanel.setBackground(panelBg);
        scenarioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.ORANGE),
                "Click a Scenario to Auto-Run", 0, 0, boldFont, Color.ORANGE));

        scenarioPanel.add(createWhiteLabel("Test Scenarios:"));

        for (ScenarioData.Scenario s : scenarios) {
            JButton sBtn = createStyledButton(s.name, scenarioBtnColor);

            sBtn.addActionListener(e -> loadAndRunScenario(s));
            scenarioPanel.add(sBtn);
        }

        // 2. Dynamic Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBackground(panelBg);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor),
                "Dynamic Input Panel (Manual Testing)", 0, 0, boldFont, Color.WHITE));

        nameField = new JTextField(5);
        arrivalField = new JTextField(5);
        burstField = new JTextField(5);
        quantumField = new JTextField(5);

        addBtn = createStyledButton("Add Process", accentColor);
        runBtn = createStyledButton("Run & Compare", accentColor);
        clearBtn = createStyledButton("Clear All", new Color(200, 50, 50));

        inputPanel.add(createWhiteLabel("Process ID:"));
        inputPanel.add(nameField);
        inputPanel.add(createWhiteLabel("Arrival Time:"));
        inputPanel.add(arrivalField);
        inputPanel.add(createWhiteLabel("Burst Time:"));
        inputPanel.add(burstField);
        inputPanel.add(addBtn);
        inputPanel.add(new JLabel("  |  "));
        inputPanel.add(createWhiteLabel("Time Quantum (RR):"));
        inputPanel.add(quantumField);
        inputPanel.add(runBtn);
        inputPanel.add(clearBtn);

        topContainer.add(scenarioPanel);
        topContainer.add(inputPanel);

        // Input Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(bgColor);
        inputModel = new DefaultTableModel(new String[] { "Process ID", "Arrival Time", "Burst Time" }, 0);
        inputTable = createStyledTable(inputModel);
        JScrollPane inputScroll = new JScrollPane(inputTable);
        inputScroll.setPreferredSize(new Dimension(1200, 90));
        inputScroll.getViewport().setBackground(bgColor);
        tablePanel.add(inputScroll, BorderLayout.CENTER);

        JPanel fullNorthPanel = new JPanel(new BorderLayout());
        fullNorthPanel.add(topContainer, BorderLayout.NORTH);
        fullNorthPanel.add(tablePanel, BorderLayout.CENTER);

        frame.add(fullNorthPanel, BorderLayout.NORTH);

        // ================= CENTER: ALGORITHMS PANELS =================
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(bgColor);

        // --- Round Robin Panel ---
        JPanel rrPanel = new JPanel(new BorderLayout(5, 5));
        rrPanel.setBackground(panelBg);
        rrPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor),
                "Round Robin Results", 0, 0, boldFont, Color.WHITE));

        rrModel = new DefaultTableModel(new String[] { "Process", "Arrival", "Burst", "WT", "TAT", "RT" }, 0);
        rrTable = createStyledTable(rrModel);
        JScrollPane rrScroll = new JScrollPane(rrTable);
        rrScroll.getViewport().setBackground(bgColor);

        rrGantt = new GanttPanel();
        rrGantt.setPreferredSize(new Dimension(800, 90));

        rrReadyQueueArea = new JTextArea(2, 20);
        rrReadyQueueArea.setEditable(false);
        rrReadyQueueArea.setBackground(inputBg);
        rrReadyQueueArea.setForeground(Color.WHITE);
        rrReadyQueueArea.setFont(boldFont);
        rrReadyQueueArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Ready Queue / Execution Sequence", 0, 0, normalFont, Color.LIGHT_GRAY));

        JPanel rrBottomPanel = new JPanel(new BorderLayout());
        rrBottomPanel.setBackground(panelBg);
        rrBottomPanel.add(rrGantt, BorderLayout.CENTER);
        rrBottomPanel.add(rrReadyQueueArea, BorderLayout.SOUTH);

        rrMetricsLabel = createWhiteLabel("RR Metrics: Avg WT = 0.0 | Avg TAT = 0.0 | Avg RT = 0.0");
        rrPanel.add(rrScroll, BorderLayout.CENTER);
        rrPanel.add(rrBottomPanel, BorderLayout.SOUTH);
        rrPanel.add(rrMetricsLabel, BorderLayout.NORTH);

        // --- SRTF Panel ---
        JPanel srtfPanel = new JPanel(new BorderLayout(5, 5));
        srtfPanel.setBackground(panelBg);
        srtfPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor),
                "SRTF Results", 0, 0, boldFont, Color.WHITE));

        srtfModel = new DefaultTableModel(new String[] { "Process", "Arrival", "Burst", "WT", "TAT", "RT" }, 0);
        srtfTable = createStyledTable(srtfModel);
        JScrollPane srtfScroll = new JScrollPane(srtfTable);
        srtfScroll.getViewport().setBackground(bgColor);

        srtfGantt = new GanttPanel();
        srtfGantt.setPreferredSize(new Dimension(800, 90));

        srtfMetricsLabel = createWhiteLabel("SRTF Metrics: Avg WT = 0.0 | Avg TAT = 0.0 | Avg RT = 0.0");
        srtfPanel.add(srtfScroll, BorderLayout.CENTER);
        srtfPanel.add(srtfGantt, BorderLayout.SOUTH);
        srtfPanel.add(srtfMetricsLabel, BorderLayout.NORTH);

        centerPanel.add(rrPanel);
        centerPanel.add(srtfPanel);
        frame.add(centerPanel, BorderLayout.CENTER);

        // ================= SOUTH: CONCLUSION PANEL =================
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(panelBg);
        southPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.YELLOW),
                "Comparison Summary & Conclusion", 0, 0, boldFont, Color.YELLOW));

        conclusionArea = new JTextArea(4, 50);
        conclusionArea.setEditable(false);
        conclusionArea.setFont(boldFont);
        conclusionArea.setBackground(inputBg);
        conclusionArea.setForeground(new Color(200, 255, 200));
        southPanel.add(new JScrollPane(conclusionArea), BorderLayout.CENTER);

        frame.add(southPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        addBtn.addActionListener(e -> addProcess());
        clearBtn.addActionListener(e -> clearAll());
        runBtn.addActionListener(e -> runSimulation());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================= HELPER METHODS =================
    private JLabel createWhiteLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(boldFont);
        return l;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(boldFont);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        return btn;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(new Color(40, 40, 60));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(90, 90, 120));
        table.setRowHeight(25);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(accentColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(boldFont);
        return table;
    }

    // ================= ACTIONS LOGIC =================

    private void loadAndRunScenario(ScenarioData.Scenario sc) {

        clearAll();

        for (ScenarioData.ProcessEntry p : sc.processes) {
            if (p.arrival < 0 || p.burst <= 0) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid input for " + p.id + ": rejected.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            processList.add(new Process(p.id, p.arrival, p.burst));
            inputModel.addRow(new Object[] { p.id, p.arrival, p.burst });
        }

        quantumField.setText(String.valueOf(sc.quantum));

        runSimulation();
    }

    private void addProcess() {
        try {
            String id = nameField.getText().trim();
            int arrival = Integer.parseInt(arrivalField.getText().trim());
            int burst = Integer.parseInt(burstField.getText().trim());

            if (id.isEmpty() || arrival < 0 || burst <= 0) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Burst must be > 0, Arrival >= 0.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            processList.add(new Process(id, arrival, burst));
            inputModel.addRow(new Object[] { id, arrival, burst });

            nameField.setText("");
            arrivalField.setText("");
            burstField.setText("");
            nameField.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid integers for Arrival and Burst time.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAll() {
        processList.clear();
        inputModel.setRowCount(0);
        rrModel.setRowCount(0);
        srtfModel.setRowCount(0);
        rrGantt.setLogs(new ArrayList<>());
        srtfGantt.setLogs(new ArrayList<>());
        rrReadyQueueArea.setText("");
        conclusionArea.setText("");
        rrMetricsLabel.setText("RR Metrics: Avg WT = 0.0 | Avg TAT = 0.0 | Avg RT = 0.0");
        srtfMetricsLabel.setText("SRTF Metrics: Avg WT = 0.0 | Avg TAT = 0.0 | Avg RT = 0.0");
    }

    private void runSimulation() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please add at least one process or load a scenario first.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantum = 0;
        try {
            quantum = Integer.parseInt(quantumField.getText().trim());
            if (quantum <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Time Quantum! Must be a positive integer.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- RUN RR ---
        RoundRobinEngine rr = new RoundRobinEngine();
        ArrayList<ScheduleLog> rrLogs = rr.calculate(copyList(processList), quantum);
        double[] rrAverages = calculateAndDisplayMetrics(copyList(processList), rrLogs, rrModel, rrMetricsLabel, "RR");
        rrGantt.setLogs(rrLogs);

        StringBuilder queueTrace = new StringBuilder();
        for (ScheduleLog log : rrLogs) {
            queueTrace.append(log.getProcessName()).append(" \u2192 ");
        }
        if (queueTrace.length() > 0)
            queueTrace.setLength(queueTrace.length() - 3);
        rrReadyQueueArea.setText(queueTrace.toString());

        // --- RUN SRTF ---
        SRTFEngine srtf = new SRTFEngine();
        ArrayList<ScheduleLog> srtfLogs = srtf.calculate(copyList(processList));
        double[] srtfAverages = calculateAndDisplayMetrics(copyList(processList), srtfLogs, srtfModel, srtfMetricsLabel,
                "SRTF");
        srtfGantt.setLogs(srtfLogs);

        // --- CONCLUSION GENERATION ---
        generateConclusion(rrAverages, srtfAverages, quantum);
    }

    // ================= METRICS CALCULATION =================
    private double[] calculateAndDisplayMetrics(ArrayList<Process> list, ArrayList<ScheduleLog> logs,
            DefaultTableModel model, JLabel label, String algoName) {
        model.setRowCount(0);

        HashMap<String, Integer> completionTimes = new HashMap<>();
        HashMap<String, Integer> firstStartTimes = new HashMap<>();

        for (ScheduleLog log : logs) {
            completionTimes.put(log.getProcessName(), log.getEndTime());
            if (!firstStartTimes.containsKey(log.getProcessName())) {
                firstStartTimes.put(log.getProcessName(), log.getStartTime());
            }
        }

        double totalWT = 0, totalTAT = 0, totalRT = 0;

        for (Process p : list) {
            int ct = completionTimes.getOrDefault(p.getId(), 0);
            int firstStart = firstStartTimes.getOrDefault(p.getId(), 0);

            int tat = ct - p.getArrivalTime();
            int wt = tat - p.getBurstTime();
            int rt = firstStart - p.getArrivalTime();

            totalTAT += tat;
            totalWT += wt;
            totalRT += rt;

            model.addRow(new Object[] { p.getId(), p.getArrivalTime(), p.getBurstTime(), wt, tat, rt });
        }

        int n = list.size();
        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
        double avgRT = totalRT / n;

        label.setText(String.format("%s Metrics: Avg WT = %.2f | Avg TAT = %.2f | Avg RT = %.2f", algoName, avgWT,
                avgTAT, avgRT));

        return new double[] { avgWT, avgTAT, avgRT };
    }

    private void generateConclusion(double[] rr, double[] srtf, int quantum) {
        StringBuilder sb = new StringBuilder();

        sb.append("🔹 Fairness vs Efficiency: SRTF is more efficient (Average WT: SRTF=")
                .append(String.format("%.2f", srtf[0])).append(" vs RR=").append(String.format("%.2f", rr[0]))
                .append(").\n");
        sb.append(
                "🔹 Round Robin ensures fairness (time-slicing), giving better chances for processes not to starve.\n");

        if (rr[2] < srtf[2]) {
            sb.append("🔹 Round Robin gave a BETTER average Response Time (").append(String.format("%.2f", rr[2]))
                    .append("), confirming it's better for interactive systems.\n");
        } else {
            sb.append("🔹 SRTF gave a BETTER average Response Time (").append(String.format("%.2f", srtf[2]))
                    .append(") in this specific workload.\n");
        }

        sb.append("🔹 Quantum Effect: A quantum of ").append(quantum)
                .append(" dictated the context switch frequency for RR.\n");
        sb.append(
                "🔹 Conclusion: SRTF generally minimizes waiting time giving advantage to short jobs, while RR prioritizes fair CPU sharing.");

        conclusionArea.setText(sb.toString());
    }

    private ArrayList<Process> copyList(ArrayList<Process> list) {
        ArrayList<Process> copy = new ArrayList<>();
        for (Process p : list) {
            copy.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime()));
        }
        return copy;
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new SchedulerGUI();
    }

    // ================= GANTT PANEL =================
    class GanttPanel extends JPanel {
        ArrayList<ScheduleLog> logs;

        public GanttPanel() {
            setBackground(new Color(28, 28, 40));
        }

        public void setLogs(ArrayList<ScheduleLog> logs) {
            this.logs = logs;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (logs == null || logs.isEmpty())
                return;

            int x = 20;
            int y = 15;
            int h = 40;
            int scale = 30;

            for (int i = 0; i < logs.size(); i++) {
                ScheduleLog l = logs.get(i);

                if (i == 0 && l.getStartTime() > 0) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, l.getStartTime() * scale, h);
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, l.getStartTime() * scale, h);
                    g.drawString("Idle", x + 5, y + 25);
                    x += l.getStartTime() * scale;
                }

                int width = (l.getEndTime() - l.getStartTime()) * scale;

                Color c = new Color(
                        (l.getProcessName().hashCode() * 50) % 255,
                        (l.getProcessName().hashCode() * 80) % 255,
                        (l.getProcessName().hashCode() * 120) % 255);

                g.setColor(c);
                g.fillRect(x, y, width, h);

                g.setColor(Color.WHITE);
                g.drawRect(x, y, width, h);

                g.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g.drawString(l.getProcessName(), x + Math.max(5, width / 2 - 10), y + 25);

                g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g.drawString("" + l.getStartTime(), x, y + 70);

                x += width;
            }

            ScheduleLog last = logs.get(logs.size() - 1);
            g.drawString("" + last.getEndTime(), x, y + 70);
        }
    }
}