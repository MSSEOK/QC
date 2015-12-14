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
        window = new JFrame("�������� ���α׷�");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	//������ ������ ������ ���α׷� ����
        
      //������(������)�� ���� ���� ó���� ����
        window.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        window.setContentPane(new MainDisplay());
        window.pack();

        window.setLocationRelativeTo(null);	//ȭ�� �߾� ��ġ
//        window.setSize(1200,400);
        window.setVisible(true);
    }

	}

