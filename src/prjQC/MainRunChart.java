package prjQC;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainRunChart {
	
	private static JFrame window;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    createAndShowGUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
	
	private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        window = new JFrame("정도관리 프로그램");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	//프레임 윈도우 닫으면 프로그램 종료
        
      //프레임(윈도우)이 닫힐 때의 처리를 정의
        window.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        window.setContentPane(new MainDisplay());
        window.pack();

        window.setLocationRelativeTo(null);	//화면 중앙 위치
//        window.setSize(1200,400);
        window.setVisible(true);
    }

	}

