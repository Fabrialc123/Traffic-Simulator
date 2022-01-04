package simulator.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ChangeDefaultDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	protected int status;
	
	private String description;
	private String firstOptionName;
	private String[] firstOptionValues;
	private String secondOptionName;
	private String[] secondOptionValues;
	
	
	protected JComboBox<String> firstOption;
	protected JComboBox<String>	secondOption;
	protected JSpinner	ticks;
	
	public ChangeDefaultDialog(Frame frameParent,String title,String desc, String firstName, String[] firstOptValues, String secName, String[] secOptValues) {
		super(frameParent,true);
		
		this.description = desc;
		this.firstOptionName = firstName;
		this.firstOptionValues = firstOptValues;
		this.secondOptionName = secName;
		this.secondOptionValues = secOptValues;
		
		this.setTitle(title);
		
		this.status = 0;
		
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
					// DESCRIPCION
		JLabel descriptionLabel = new JLabel(this.description);
		descriptionLabel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(descriptionLabel);
		
					// CONFIGURACION Y OPCIONES
		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 20));
		
		optionsPane.add(new JLabel(this.firstOptionName));
		this.firstOption = new JComboBox<String>(this.firstOptionValues);		
		//firstOption.setSelectedIndex(0);					//SI NO HAY NADA LANZA ERROR!
		firstOption.setEditable(true);
		optionsPane.add(firstOption);
		
		optionsPane.add(new JLabel(this.secondOptionName));
		this.secondOption = new JComboBox<String>(this.secondOptionValues);
		optionsPane.add(this.secondOption);
		
		optionsPane.add(new JLabel("Ticks:"));
		ticks = new JSpinner(new SpinnerNumberModel(1,1,1000,1));
		optionsPane.add(ticks);
		
		mainPanel.add(optionsPane);
		
		
					// BOTONES
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		JButton changeButton = new JButton("Change");
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstOption.getSelectedItem() != null && secondOption.getSelectedItem() != null) 	{
					status = 1;
					ChangeDefaultDialog.this.setVisible(false);
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChangeDefaultDialog.this.setVisible(false);
			}
		});
		
		buttonsPane.add(changeButton);
		buttonsPane.add(cancelButton);
		mainPanel.add(buttonsPane);
		
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);		
	}
	
	
	public int getStatus() {
		return this.status;
	}
	
	public int getTicks() {
		return (int) this.ticks.getValue();
	}

}
