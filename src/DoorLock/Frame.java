package DoorLock;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame {

    DoorLock doorLock = new DoorLock();
    boolean doorBoolean = false;


    public Frame() {

        final String[] btn_Title = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        final String[] btn_Title2 = {"*", "#"};
        JFrame frame = new JFrame();
        JLabel label = new JLabel("WELCOME");
        JPanel btnPanel = new JPanel();
        JPanel TextPanel = new JPanel();
        JButton[] btns = new JButton[10];
        JButton[] btnsT = new JButton[2];

        JPasswordField passwordtext = new JPasswordField("", 10);

        frame.setLayout(new BorderLayout());

        frame.add(label, BorderLayout.NORTH);
        frame.add(TextPanel, BorderLayout.SOUTH);
        frame.add(btnPanel, BorderLayout.CENTER);

        TextPanel.add(passwordtext);
        btnPanel.setLayout(new GridLayout(4, 2));
        label.setHorizontalAlignment(JLabel.CENTER);
        frame.setTitle("DoorLock");
        frame.setVisible(true);
        frame.setSize(440, 600 / 12 * 9);
        //사이즈 고정false
        frame.setResizable(true);
        //실행시 Frame 위치 센터
        frame.setLocationRelativeTo(null);
        //X버튼 클릭시 프로그램 영구 종료
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        for (int i = 0; i < 12; i++) {
            if (i == 9) btnPanel.add(btnsT[0] = new JButton(btn_Title2[0]));
            else if (i == 11) btnPanel.add(btnsT[1] = new JButton(btn_Title2[1]));
            else {
                if (i == 10) btnPanel.add(btns[i - 1] = new JButton(btn_Title[i - 1]));
                else btnPanel.add(btns[i] = new JButton(btn_Title[i]));
            }
        }

        // *버튼 클릭시 일치 비교
        btnsT[0].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                char[] pass = passwordtext.getPassword();

                for (char cha : pass) {
                    Character.toString(cha);
                    // Pass에 저장
                    DoorLock.Pass += cha;
                }

                //inputPassword 에 입력 비밀번호 저장
                DoorLock.inputPassword = DoorLock.Pass.split("");

                //패널의 값 변경
                label.setText(ComparedKeypad());
                passwordtext.setText("");
            }
        });

        // #버튼 클릭시 새로운 비밀번호 저장
        btnsT[1].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                char[] pass = passwordtext.getPassword();
                for (char cha : pass) {
                    Character.toString(cha);
                    // Pass1에 저장
                    DoorLock.Pass2 += cha;
                }

                if (doorBoolean == true) {
                    if (CheckPassword() == true) {
                        DoorLock.myPassword = DoorLock.Pass2.split("");
                        label.setText(CipheredKeypad());
                    } else {
                        DoorLock.Pass2 = "";
                        label.setText("The password you entered is a weak password, so please reset it.");
                    }
                }
                passwordtext.setText("");
            }
        });

        //숫자 버튼
        for (int i = 0; i < btns.length; i++) {
            btns[i].addActionListener(new ActionListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton pushButton = (JButton) e.getSource();
                    String temp = pushButton.getText();

                    passwordtext.setText(passwordtext.getText() + temp + "");

                }
            });
        }
    }



    //*버튼 메소드
    public String ComparedKeypad() {

        int count = 0;
        int b = 3;
        for (int i = 0; i < DoorLock.myPassword.length; i++) {

            if (DoorLock.myPassword.length != DoorLock.inputPassword.length) break;
            if (DoorLock.myPassword[i].equals(DoorLock.inputPassword[i])) count++;

        }

        // 초기화
        for (int i = 0; i < DoorLock.inputPassword.length; i++) DoorLock.inputPassword[i] = "";

        DoorLock.Pass = "";
        DoorLock.Pass1 = "";
        DoorLock.Pass2 = "";

        if (DoorLock.myPassword.length == count) {
            doorBoolean = true;
            return String.format("Unlocked successfully.");
        }
        doorBoolean = false;

        DoorLock.num_count++;

        if (DoorLock.num_count == 3) {
            DoorLock.num_count = 0;

            try {
                Thread.sleep(b * 10000);
            } catch (InterruptedException e) {
            }


            return String.format("Invalid number entered. You failed " + DoorLock.num_count + "times");
        }


        return String.format("Invalid number entered. You failed  " + DoorLock.num_count + "times");

    }



    //#버튼 메소드
    public String CipheredKeypad() {

        if (doorBoolean == false) return String.format("Please try after unlocking.");

        else {
            for (int i = 0; i < DoorLock.inputPassword.length; i++) DoorLock.inputPassword[i] = "";
            DoorLock.Pass = "";
            DoorLock.Pass1 = "";
            DoorLock.Pass2 = "";
        }
        return String.format("Your new password has been saved.");

    }

    
    
    //비밀번호 설정 메소드
    public Boolean CheckPassword() {

        String a = "";
        String temp = String.join(a, DoorLock.Pass2);
        String[] shelter = temp.split("");
        int[] temp2 = new int[DoorLock.Pass2.length()];
        boolean run = true;
        int count = 0;

        for (int i = 0; i < temp2.length; i++) {
            try {
                temp2[i] = Integer.parseInt(shelter[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            }
        }

        //4이상 8이하의 자릿수 확인
        if (temp2.length < 4 || temp2.length > 8) return false;

        //중복된 숫자 3회 이상 확인
        for (int i = 0; i < temp2.length - 2; i++) {
            if (temp2[i] == temp2[i + 1] && temp2[i] == temp2[i + 2]) return false;
        }

        //패턴 확인
        if (run) {
            //1씩 상승하거나 1씩 감소하는 패턴 확인
            for (int i = 0; i < temp2.length - 3; i++) {
                if (temp2[i] == temp2[i] && temp2[i + 1] == temp2[i] + 1 && temp2[i + 2] == temp2[i] + 2) run = false;
                else if (temp2[i + 1] == temp2[i + 1] && temp2[i + 2] == temp2[i + 1] + 1 && temp2[i + 3] == temp2[i + 1] + 2)
                    run = false;
                else if (temp2[i] == temp2[i] && temp2[i + 1] == temp2[i] - 1 && temp2[i + 2] == temp2[i] - 2)
                    run = false;
                else if (temp2[i + 1] == temp2[i + 1] && temp2[i + 2] == temp2[i + 1] - 1 && temp2[i + 3] == temp2[i + 1] - 2)
                    run = false;
            }
            //+2칸 연속 패턴 확인 ex)3434
            for (int i = 0; i < temp2.length; i++) {
                for (int j = 0; j < temp2.length - 2; j++) {
                    if (temp2[j] == temp2[j] && temp2[j] == temp2[j + 2]) count++;
                }
            }

            if (count >= 2) run = false;
            return run;
        }

        return true;
    }
}
