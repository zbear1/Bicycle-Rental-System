// specify the package
package userinterface;

// system imports
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.GridLayout;
import java.util.Properties;
import java.util.EventObject;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.awt.*;

import javax.swing.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


// project imports
import impresario.IModel;
import model.*;

public class WorkerView extends JPanel implements ActionListener {
	private Peon peon;
	private JLabel firstNameLabel;
	private JTextField firstNameTextField;
	private JLabel lastNameLabel;
	private JTextField lastNameTextField;
	private JLabel bannerLabel;
	private JTextField bannerTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel phoneLabel;
	private JTextField phoneTextField;
	private JLabel passwordLabel;
	private JPasswordField passwordTextField;
	private JButton submitButton;
	private JButton backButton;
	private JComboBox adminComboBox, dayComboBox, monthComboBox, yearComboBox;
	private MessageView statusLog;
	
	private JDatePickerImpl registrationDatePicker;


    public ResourceBundle localizedBundle;
	
	public WorkerView(Peon p) {
		peon = p;
		Locale currentLocale = LocaleConfig.currentLocale();
		localizedBundle = ResourceBundle.getBundle("BicycleStringsBundle", currentLocale);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel lbl = new JLabel(localizedBundle.getString("addWorker"));
		Font myFont = new Font("Helvetica", Font.BOLD, 20);
		lbl.setFont(myFont);
		titlePanel.add(lbl);
		add(titlePanel);

		add(dataEntryPanel());
		add(createDate());
		//add(choiceBox());
		
		add(navigationPanel());

		add(createStatusLog("                          "));
	}
	private JPanel createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}

	private JPanel dataEntryPanel() {
		JPanel entryPanel = new JPanel();
		// set the layout for this panel
		entryPanel.setLayout(new GridLayout(7,2,20,20));
		entryPanel.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

		// data entry fields		
		JLabel bannerLabel = new JLabel(localizedBundle.getString("bannerID") + ": ");
		bannerTextField = new JTextField(20);
		bannerTextField.addActionListener(this);
		entryPanel.add(bannerLabel);
		entryPanel.add(bannerTextField);
		
		JLabel passwordLabel = new JLabel(localizedBundle.getString("password") + ": ");
		passwordTextField = new JPasswordField(20);
		passwordTextField.addActionListener(this);
		entryPanel.add(passwordLabel);
		entryPanel.add(passwordTextField);
		
		JLabel firstNameLabel = new JLabel(localizedBundle.getString("firstName") + ": ");
		firstNameTextField = new JTextField(20);
		firstNameTextField.addActionListener(this);
		entryPanel.add(firstNameLabel);
		entryPanel.add(firstNameTextField);
		
		JLabel lastNameLabel = new JLabel(localizedBundle.getString("lastName") + ": ");
		lastNameTextField = new JTextField(20);
		lastNameTextField.addActionListener(this);
		entryPanel.add(lastNameLabel);
		entryPanel.add(lastNameTextField);
		
		JLabel phoneLabel = new JLabel(localizedBundle.getString("phoneNumber") + ": ");
		phoneTextField = new JTextField(20);
		phoneTextField.addActionListener(this);
		entryPanel.add(phoneLabel);
		entryPanel.add(phoneTextField);
		
		JLabel emailLabel = new JLabel(localizedBundle.getString("email") + ": ");
		emailTextField = new JTextField(20);
		emailTextField.addActionListener(this);
		entryPanel.add(emailLabel);
		entryPanel.add(emailTextField);
		return entryPanel;
	}

	// Create the navigation buttons
	//-------------------------------------------------------------
	private JPanel navigationPanel()
	{
		JPanel navPanel = new JPanel();		// default FlowLayout is fine
		FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
		f1.setVgap(50);
		f1.setHgap(25);
		navPanel.setLayout(f1);

		// create the buttons, listen for events, add them to the panel
		backButton = new JButton(localizedBundle.getString("back"));
		backButton.addActionListener(this);
		navPanel.add(backButton);

		submitButton = new JButton(localizedBundle.getString("submit"));
		submitButton.addActionListener(this);
		navPanel.add(submitButton);

		return navPanel;
	}
	
	//Create Date
	private JPanel createDate()
	{
		JPanel temp = new JPanel();
		temp.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel rentDateLabel = new JLabel("Date: ");
		temp.add(rentDateLabel);

		UtilDateModel model = new UtilDateModel();
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		registrationDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());		 
		temp.add(registrationDatePicker);
		
		return temp;
	}
	
	private JPanel createCalendar()
	{
		final JPanel p = new JPanel();
		
		FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
		
		//DatePicker dp = new DatePicker(p);
		
		JLabel label = new JLabel(localizedBundle.getString("registrationDate") + ": ");
        final JTextField text = new JTextField(20);
        JButton b = new JButton("...");
        //JPanel p = new JPanel();
        p.add(label);
        p.add(text);
        p.add(b);
        b.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {
                         text.setText(new DatePicker(p).setPickedDate());
                 }
         });
		
		return p;
	}
	
	public void displayErrorMessage(String message) {
		statusLog.displayErrorMessage(message);
	}

	public void clearErrorMessage() {
		statusLog.clearErrorMessage();
	}
	
	public void displayMessage(String message) {
		statusLog.displayMessage(message);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == submitButton) {
			if (bannerTextField.getText().equals("")) {
				peon.errorMessagePopup("bannerId");
			} else if (passwordTextField.getText().equals("")) {
				peon.errorMessagePopup("password");
			} else if(firstNameTextField.getText().equals("")) {
				peon.errorMessagePopup("firstName");
			} else if(lastNameTextField.getText().equals("")) {
				peon.errorMessagePopup("lastName");
			} else if(phoneTextField.getText().length() != 11) {
				peon.errorMessagePopup("phoneNumber");
			} else if(emailTextField.getText() == null) {
				peon.errorMessagePopup("email");
			} else {
				
				String day = String.valueOf(registrationDatePicker.getModel().getDay());
				String month = String.valueOf(registrationDatePicker.getModel().getMonth() + 1);
				String year = String.valueOf(registrationDatePicker.getModel().getYear());
					
				Properties workerProperties = new Properties();
				workerProperties.setProperty("bannerId",bannerTextField.getText());
				workerProperties.setProperty("password",passwordTextField.getText());
				workerProperties.setProperty("adminLevel",(String)adminComboBox.getSelectedItem());
				workerProperties.setProperty("firstName",firstNameTextField.getText());
				workerProperties.setProperty("lastName",lastNameTextField.getText());
				workerProperties.setProperty("phoneNumber",phoneTextField.getText());
				workerProperties.setProperty("email",emailTextField.getText());
				workerProperties.setProperty("registrationDate", day + "-" + month + "-" + year);

				peon.processWorkerData(workerProperties);

				bannerTextField.setText("");
				passwordTextField.setText("");
				firstNameTextField.setText("");
				lastNameTextField.setText("");
				phoneTextField.setText("");
				emailTextField.setText("");
				dayComboBox.setSelectedIndex(0);
				monthComboBox.setSelectedIndex(0);
				yearComboBox.setSelectedIndex(0);
			}
		} else if (event.getSource() == backButton) {
			peon.workerDataDone();
		}
	}
}