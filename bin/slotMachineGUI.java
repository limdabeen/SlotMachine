package arcktect;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class slotMachineGUI extends JFrame {
    private static final Random random = new Random();
    private JLabel[] slots = new JLabel[3];
    private JButton playButton;
    private JButton stopButton;
    private JPanel panel;
    private Timer[] timers = new Timer[3];
    private boolean[] isRunning = new boolean[3]; // 각 슬롯이 돌아가고 있는지를 추적합니다.
    private ImageIcon[] images = {
            resizeImage("/img/image1.png", 400, 400), // 이미지 크기를 더 키움
            resizeImage("/img/image2.png", 400, 400),
            resizeImage("/img/image3.png", 400, 400),
            resizeImage("/img/image4.png", 400, 400),
            resizeImage("/img/image5.png", 400, 400),
            resizeImage("/img/image6.png", 400, 400),
            resizeImage("/img/image7.png", 400, 400)
    };

    public SlotMachineGUI() {
        setTitle("슬롯머신 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 슬롯 레이블 초기화
        panel = new JPanel(new FlowLayout());
        panel.setBackground(getRandomColor()); // 랜덤 배경색 설정
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new JLabel(images[random.nextInt(images.length)]);
            panel.add(slots[i]);
        }
        add(panel, BorderLayout.CENTER);

        // 시작 버튼 초기화 및 이벤트 핸들러 추가
        playButton = new JButton("시작");
        playButton.addActionListener(e -> startSlots());

        // 멈춤 버튼 초기화 및 이벤트 핸들러 추가
        stopButton = new JButton("멈춤");
        stopButton.addActionListener(e -> stopSlot());

        // 버튼 패널 초기화
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 타이머 설정
        initializeTimers();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ImageIcon resizeImage(String imagePath, int width, int height) {
        try {
            InputStream stream = getClass().getResourceAsStream(imagePath);
            BufferedImage originalImage = ImageIO.read(stream);

            // Resize
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeTimers() {
        for (int i = 0; i < timers.length; i++) {
            int slotIndex = i;
            timers[i] = new Timer();
            timers[i].scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (isRunning[slotIndex]) {
                        ImageIcon icon = images[random.nextInt(images.length)]; // 랜덤 이미지 아이콘
                        slots[slotIndex].setIcon(icon); // 현재 슬롯에 이미지 아이콘 설정
                    }
                }
            }, 0, 50); // 회전 속도를 더 빠르게 조절
        }
    }

    private void startSlots() {
        for (int i = 0; i < isRunning.length; i++) {
            isRunning[i] = true;
        }
        playButton.setEnabled(false); // 시작 버튼 비활성화
        stopButton.setEnabled(true); // 멈춤 버튼 활성화
    }

    private void stopSlot() {
        for (int i = 0; i < isRunning.length; i++) {
            if (isRunning[i]) {
                isRunning[i] = false;
                break; // 한 번에 하나의 슬롯만 멈춤
            }
        }

        // 모든 슬롯이 멈췄는지 확인
        boolean allStopped = true;
        for (boolean running : isRunning) {
            if (running) {
                allStopped = false;
                break;
            }
        }

        // 모든 슬롯이 멈추면 게임 결과 확인
        if (allStopped) {
            checkWin();
            playButton.setEnabled(true); // 시작 버튼 다시 활성화
            stopButton.setEnabled(false); // 멈춤 버튼 비활성화
        }
    }

    private void checkWin() {
        if (slots[0].getIcon().equals(slots[1].getIcon()) && slots[1].getIcon().equals(slots[2].getIcon())) {
            JOptionPane.showMessageDialog(this, "잭팟이다 잭팟!!!! 축하해요 ", "승리", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "아쉽다.....한판 더?ㄱㄱㄱㄱ", "패배 ", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Color getRandomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlotMachineGUI());
    }
}
