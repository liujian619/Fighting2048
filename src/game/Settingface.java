package game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

class Settingface extends JDialog
{
	private static final long serialVersionUID = -3392186960024791413L;
	
	private final static Dimension buttonDimension = new Dimension(60, 25);
	
	private JLabel lookAndFeelLabel;
	private static JComboBox<String> soundsComboBox;
	private static JComboBox<String> lookAndFeelComboBox;
	private static JCheckBox soundsCheckBox;
	private static JCheckBox visualCheckBox;
	private JButton okButton;
	private JButton cancelButton;
	private JButton applyButton;
	
	private JPanel soundsPanel;
	private JPanel lookAndFeelPanel;
	private JPanel selectPanel;
	private JPanel panel;

	private boolean appliedFlag = false;
	private GameData data;
	private static boolean[] isOnTemp = {true, true};
	
	Settingface(Frame c) 
	{
		super(c);
		data = GameBase.readMyData();
		initSettingface();
	}
	
	private void initSettingface()
	{
		soundsCheckBox = new JCheckBox("�򿪱�������");
		soundsCheckBox.setSelected(new Boolean(data.isSoundOn));
		visualCheckBox = new JCheckBox("�򿪿���Ч��");
		visualCheckBox.setSelected(new Boolean(data.isVisualOn));
		soundsComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(GameBase.SOUNDS));
		soundsComboBox.setPreferredSize(new Dimension(100, 30));
		soundsComboBox.setSelectedItem(data.sound);
		soundsComboBox.setEnabled(data.isSoundOn);
		soundsComboBox.setEditable(false);
		soundsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		soundsPanel.setPreferredSize(new Dimension(230, 100));
		soundsPanel.add(soundsCheckBox);
		soundsPanel.add(soundsComboBox);
		soundsPanel.add(visualCheckBox);
		soundsPanel.setBorder(new TitledBorder(null, "����Ч������", TitledBorder.DEFAULT_JUSTIFICATION, 
								TitledBorder.DEFAULT_POSITION, null, Color.BLUE));
		
		lookAndFeelLabel = new JLabel("ѡ����");
		lookAndFeelComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(GameBase.STYLES));
		lookAndFeelComboBox.setPreferredSize(new Dimension(110, 30));
		lookAndFeelComboBox.setSelectedItem(data.lookAndFeel);
		lookAndFeelComboBox.setEditable(false);
		lookAndFeelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
		lookAndFeelPanel.setPreferredSize(new Dimension(230, 70));
		lookAndFeelPanel.add(lookAndFeelLabel);
		lookAndFeelPanel.add(lookAndFeelComboBox);
		lookAndFeelPanel.setBorder(new TitledBorder(null, "���ڷ������", TitledBorder.DEFAULT_JUSTIFICATION, 
								TitledBorder.DEFAULT_POSITION, null, Color.BLUE));
		
		
		okButton = new JButton("ȷ��");
		okButton.setPreferredSize(buttonDimension);
		//this.getRootPane().setDefaultButton(okButton);//����Ĭ�ϰ�ť
		cancelButton = new JButton("ȡ��");
		cancelButton.setPreferredSize(buttonDimension);
		applyButton = new JButton("Ӧ��");
		applyButton.setPreferredSize(buttonDimension);
		selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		selectPanel.setPreferredSize(new Dimension(230, 30));
		selectPanel.add(okButton);
		selectPanel.add(cancelButton);
		selectPanel.add(applyButton);
		
		panel = new JPanel(new GridLayout(2, 1, 0, 10));
		panel.add(soundsPanel);
		panel.add(lookAndFeelPanel);
		
		setLayout(new BorderLayout(10, 10));
		add(panel, BorderLayout.CENTER);
		add(selectPanel, BorderLayout.SOUTH);
		myEvent();
		setIconImage(Toolkit.getDefaultToolkit().getImage("./images/icon.png"));
		setTitle("��Ϸ����");
		setModal(true);
		pack();
		//setSize(250, 220);
		setResizable(false);
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
			
	private void myEvent()
	{
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				int select = JOptionPane.showConfirmDialog(Settingface.this, "�Ƿ񱣴����ã�", "��ʾ", JOptionPane.YES_NO_OPTION);
				if (0 == select)
					save();
			}
		});
		
		okButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (!appliedFlag)
					save();
				dispose();
			}
		});
		
		cancelButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				dispose();
			}
		});
		
		applyButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				appliedFlag = true;
				save();
			}
		});
		
		okButton.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (!appliedFlag)
						save();
					dispose();
				}
			}
		});
		
		cancelButton.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					appliedFlag = true;
					save();
				}
			}
		});
		
		applyButton.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					dispose();
				}
			}
		});
		
		soundsCheckBox.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e) 
			{
				boolean b = ((JCheckBox) e.getItemSelectable()).isSelected();
				isOnTemp[0] = b;
				soundsComboBox.setEnabled(b);
			}
		});
	
		visualCheckBox.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e) 
			{
				boolean b = ((JCheckBox) e.getItemSelectable()).isSelected();
				isOnTemp[1] = b;
			}
		});
	}
	
	private void save()
	{
		data.isSoundOn = isOnTemp[0];
		data.isVisualOn = isOnTemp[1];
		data.sound = (String) soundsComboBox.getSelectedItem();
		data.lookAndFeel = (String) lookAndFeelComboBox.getSelectedItem();
		GameBase.setLookAndFeel(getOwner(), data.lookAndFeel);
		GameBase.setLookAndFeel(this, data.lookAndFeel);
		GameBase.writeMyData(data);
	}
}

