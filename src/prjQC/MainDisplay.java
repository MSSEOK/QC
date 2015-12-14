package prjQC;

import features.ChartSamplePanel;

import com.steema.teechart.styles.MarksStyle;
import com.steema.teechart.styles.PointerStyle;
import com.steema.teechart.styles.Line;
import com.steema.teechart.axis.Axis;
import com.steema.teechart.axis.AxisLabelItem;
import com.steema.teechart.axis.AxisLabelsItems;
import com.steema.teechart.drawing.Color;
import com.steema.teechart.legend.LegendAlignment;
import com.steema.teechart.legend.LegendStyle;
//import com.steema.teechart.styles.Series;
//import com.steema.teechart.styles.SeriesPointer;
import com.steema.teechart.styles.*;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.*;

public class MainDisplay extends ChartSamplePanel implements ActionListener{
	// DB ���� ���� ����
	Connection connection;
	PreparedStatement preparedStatement;
	Statement statement;
	ResultSet resultSet;
	
	String[] columnName;
	JCheckBox[] checkbox;
	
	int totalRecord;
 	int colCount = 0;
 	
 	float maxValue;
 	float meanValue;
 	float minValue;
	
//	private Line lineSeries;
	
	public MainDisplay(){
		super();
        loadButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == loadButton) {
		
//			chart1.getLegend().setAlignment(LegendAlignment.RIGHT);
//			chart1.getLegend().setLegendStyle(LegendStyle.SERIES);
//			chart1.getLegend().setVisible(true);
        	
        	// ������ �˻�Ƿ� Series���� �����ϰ� QC����� ������->  CheckBox �� �ѱ�
        	
//        	for (int i=0; i < columnName.length; i++) {
//        		if (checkbox[i].isSelected() == true) {
//        			try{
//        				System.out.println(checkbox[i].toString());
//        			}catch (Exception e) {
//        				System.out.println("Error");
//        			}
//        		}
//        	}
			
	        chart1.getSeries().clear();
	
	        Line lineSeries = new Line(chart1.getChart());
	        
	        lineSeries.setTitle("�λ�޸����");
//	        lineSeries.setColorEach(true);
	        lineSeries.setColorEachLine(false);
	        lineSeries.getMarks().setVisible(true);
	        lineSeries.getMarks().setStyle(MarksStyle.VALUE);
	        
	        lineSeries.setClickableLine(false);

	        SeriesPointer tmpPointer = lineSeries.getPointer();
	        tmpPointer.setInflateMargins(true);
	        tmpPointer.setStyle(PointerStyle.RECTANGLE);
	        tmpPointer.setVisible(true);
	        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	
	        try {
	            JDBC_addData(lineSeries);
	        } finally {
	            setCursor(Cursor.getDefaultCursor());
	        }
	        
	        addCustomLabels();	// Max, Mean, Min�� ȣ���ؼ� �࿡ ǥ��
	        
        }
	}
	
	protected void addCustomLabels() {
        
        try {
	    	Class.forName(DatabaseConnection.DRIVERNAME);
	    	connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USER, DatabaseConnection.PASSWORD);
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery("Select max(qcrst + 0), ROUND(AVG(qcrst+0),2), MIN(qcrst+0) from qcrst where hnm = 'mrn' and qcitem = 'WBC' and qcrst not in (':::::', '-----','.....') and mid(qcdt, 1, 10) = '2012-06-27'");
    		
	    	while (resultSet.next()) {
	    		maxValue = resultSet.getFloat(1);
	    		meanValue = resultSet.getFloat(2);
	    		minValue = resultSet.getFloat(3);
//	    		System.out.println(maxValue + " " + meanValue + " " + minValue);
	    		}
	    	}
	    	
		    catch (ClassNotFoundException e) {
		    	System.out.println("[�ε� ����]\n" + e.getStackTrace());
		    }
		    catch (SQLException e) {
		    	System.out.println("[���� ����]\n" +  e.getStackTrace());
		    }
		    finally {
		    	CloseDatabase();	
		    }
        
        Axis axis = chart1.getAxes().getLeft();
        AxisLabelsItems items = axis.getCustomLabels();
        //remove all custom labels
        items.clear();
        //add custom labels
        AxisLabelItem item;
        
        item = items.add(meanValue, "MEAN" + " " + meanValue);	//MEAN��
//        item.getFont().setSize(12);
//        item.setTransparency(50);
        item = items.add(maxValue-0.000001, "MAX" + " " + maxValue);	// MAX��
//        item.setTransparent(false);
//        item.setTransparency(50);
        item = items.add(minValue+0.000001, "MIN" + " " + minValue);		//MIN��
//        item.setTransparent(false);
//        item.setTransparency(50);
//        item.setColor(Color.BLUE);
        
    }

	protected void initChart() {
        super.initChart();
		chart1.getLegend().setAlignment(LegendAlignment.RIGHT);
		chart1.getLegend().setLegendStyle(LegendStyle.SERIES);
		chart1.getLegend().setVisible(true);
		
		chart1.getAxes().getTop().setVisible(false);
        chart1.getAxes().getRight().setVisible(false);
        chart1.getAxes().getLeft().setMaxRound(true);
        chart1.getAxes().getLeft().setMinRound(true);        
        chart1.getAxes().getLeft().setIncrement(10);
        chart1.getAxes().getBottom().setIncrement(10);
    }
	
	protected void initComponents() {
        super.initComponents();
        loadButton = new JButton("��ȸ");
    }

	protected void initGUI() {
        super.initGUI();
        chart1.getAspect().setView3D(false);
        JLabel tmpLabel = new JLabel("�˻�� ����:");
        
        try {
	    	Class.forName(DatabaseConnection.DRIVERNAME);
	    	connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USER, DatabaseConnection.PASSWORD);
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery("Select distinct hspnm from hinfo");
	    	
	    	// ���� ���̺��� �� ���ڵ� �� ��������
    		resultSet.last();// ���� Ŀ���� ���������� ������				
    		totalRecord = resultSet.getRow();//���� Ŀ���� ��ġ�� ���ڵ� ��ȣ
    		
    		columnName = new String[totalRecord];
    		
    		resultSet.beforeFirst();
    		
	    	while (resultSet.next()) {
	    		columnName[colCount] = resultSet.getString(1);
//	    		System.out.println(columnName[colCount]);
	    		colCount++;
	    		}
	    	}
	    	
		    catch (ClassNotFoundException e) {
		    	System.out.println("[�ε� ����]\n" + e.getStackTrace());
		    }
		    catch (SQLException e) {
		    	System.out.println("[���� ����]\n" +  e.getStackTrace());
		    }
		    finally {
		    	CloseDatabase();	
		    }
        
//        JCheckBox checkbox1 = new JCheckBox("�λ�޸����");
//        JCheckBox checkbox2 = new JCheckBox("�̿��Ƿ����");
//        JCheckBox checkbox3 = new JCheckBox("�����뺴��");
    
        JPanel tmpPane = getButtonPane();
        {
            tmpPane.add(Box.createHorizontalStrut(10));
            tmpPane.add(tmpLabel);
            
            // ���ڵ�� ������ŭ CheckBox ����
    		JCheckBox[] checkbox = new JCheckBox[columnName.length];

    		for(int i=0; i< columnName.length; i++ ){
    			checkbox[i] = new JCheckBox(columnName[i]);
            	tmpPane.add(checkbox[i]);	// �гο� ����
			}
//            tmpPane.add(checkbox1);
//            tmpPane.add(checkbox2);
//            tmpPane.add(checkbox3);
            tmpPane.add(loadButton);
            tmpPane.add(Box.createHorizontalGlue());
        }
    }
	
	private JButton loadButton;
	
	// �˻�� �̸� ��ȸ-> JCheckBox ����
	public void JDBC_selectHspName(String[] hspNm){
	    // DB ����
	    try {
	    	Class.forName(DatabaseConnection.DRIVERNAME);
	    	connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USER, DatabaseConnection.PASSWORD);
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery("Select distinct hnm from qcrst");
	    	
	    	// ���� ���̺��� �� ���ڵ� �� ��������
    		resultSet.last();// ���� Ŀ���� ���������� ������				
    		totalRecord = resultSet.getRow();//���� Ŀ���� ��ġ�� ���ڵ� ��ȣ
    		
    		columnName = new String[totalRecord];
    		
    		resultSet.beforeFirst();
    		
	    	while (resultSet.next()) {
	    		columnName[colCount] = resultSet.getString(1);
	    		System.out.println(resultSet.getString(1));
	    		colCount++;
	    		}

//	    		new MainDisplay().initGUI(hspNm);
	    	}
	    	
		    catch (ClassNotFoundException e) {
		    	System.out.println("[�ε� ����]\n" + e.getStackTrace());
		    }
		    catch (SQLException e) {
		    	System.out.println("[���� ����]\n" +  e.getStackTrace());
		    }
		    finally {
		    	CloseDatabase();	
		    }

		}

	// DB Connect-> Select Query
	public void JDBC_addData(Series series){
    	// DB ����
    	try {
    		Class.forName(DatabaseConnection.DRIVERNAME);
    		connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USER, DatabaseConnection.PASSWORD);
    		statement = connection.createStatement();
    		
    		resultSet = statement.executeQuery("Select distinct qcdt, qcrst from qcrst where hnm = 'mrn' and qcitem = 'WBC' and qcrst not in (':::::','-----','.....') and mid(qcdt, 1, 10) = '2012-06-27'");

    			series.getYValues().setDataMember("qcrst");
                series.setLabelMember("qcdt");	//x�࿡ ��¥
    			
                series.setDataSource(resultSet);

    		}
    	
	    	catch (ClassNotFoundException e) {
	    		System.out.println("[�ε� ����]\n" + e.getStackTrace());
	    	}
	    	catch (SQLException e) {
	    		System.out.println("[���� ����]\n" +  e.getStackTrace());
	    	}
	    	finally {
	    		CloseDatabase();	
	    	}
		}

	
	/**
	 * [�����ͺ��̽��� �ݴ� �޼ҵ�]
	 * connection, statement, resultSet�� null ���θ� üũ�ѵ� ������� ��� �ݾ��ش�.
	 * @param ����
	 * @return ����
	 */
	private void CloseDatabase() {
		// TODO Auto-generated method stub
		try {
			if( resultSet != null ) {
				resultSet.close();
			}
			if( statement != null ) {
				statement.close();
			}
			if( connection != null ) {
				connection.close();
			}
		}
		catch (SQLException e) {
			System.out.println("[�ݱ� ����]\n" +  e.getStackTrace());
		}
		
	}
	
	// Mean�� ���ϴ� Method
	public static double getMean(double []array){
		double nTotal = 0;
		double nMean = 0;
		
		for (int i = 0; i < array.length; i++){
			nTotal += array[i];
		}
		nMean = nTotal / array.length;
		
		return nMean;
	}
	
	// SD�� ���ϴ� Method
	public static double getSD(double []array){
		double nTemp = 0;
		double nVar = 0;
		double nSD = 0;
		
		for (int i = 0; i < array.length; i++){
			nTemp += (array[i] - getMean(array)) * (array[i] - getMean(array));
		}
		nVar = nTemp / (array.length - 1);
		nSD = Math.sqrt(nVar);
		
		return nSD;
	}
	
	// CV�� ���ϴ� Method
	public static double getCV(double []array){
		double nTemp1 = 0;
		double nCV = 0;
		
		for (int i= 0; i < array.length; i++){
			nTemp1 += (array[i] - getMean(array)) * (array[i] - getMean(array));
		}
		nCV = Math.sqrt(nTemp1 / ( array.length - 1)) / getMean(array);
		
		return nCV;
	}
	
}
