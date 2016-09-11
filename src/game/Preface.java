package game;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.AudioClip;

class Preface extends JFrame
{
	private static final long serialVersionUID = 5356614680191189802L;

	private JPanel panel;
	
	private JButton newGameButton;
	private JButton continueGameButton;
	private JButton quitGameButton;
	private JButton settingsButton;

	private JLabel layeredBackgroundLabel;
	
	AudioClip bgSound; 
	
	Preface()
	{
		if (GameBase.isNewGame)
			GameBase.resetData(GameBase.readMyData());
		
		initInterface();
	}
	
	/**
	 * ��дrun����
	public void run()
	{
		getRootPane().paintComponents(getRootPane().getGraphics());
		getLayeredPane().paintComponents(getLayeredPane().getGraphics());
	}
	*/
	
	private void initInterface()
	{
		setLayout(null);//Ϊ�˽�panel���齨����ָ��λ��
		settingsButton = new JButton();
		layeredBackgroundLabel = new JLabel(new ImageIcon("./images/snowyNight.gif"));//���ñ���ͼƬ
		layeredBackgroundLabel.setBounds(0, 0, layeredBackgroundLabel.getIcon().getIconWidth(), layeredBackgroundLabel.getIcon().getIconHeight());
		//��������ͼƬ�ı�ǩ����RootPane��
		getLayeredPane().add(layeredBackgroundLabel, new Integer(Integer.MIN_VALUE));
		
		//��ContentPane������Ϊ͸��
		((JComponent) getContentPane()).setOpaque(false);
		
		panel = new JPanel(new GridLayout(4, 1, 0, 10));	
		//Ϊ�˲���ס����Ĳ㣬��Ҳ��Ҫ����Ϊ͸��
		panel.setOpaque(false);
		
		newGameButton = new JButton();
		continueGameButton = new JButton();
		quitGameButton = new JButton();
		settingsButton = new JButton();
		setMyButton(newGameButton, "./images/newGame.png");
		setMyButton(continueGameButton, "./images/continueGame.png");
		setMyButton(settingsButton, "./images/settings.png");
		setMyButton(quitGameButton, "./images/quitGame.png");
		
		panel.setLocation((layeredBackgroundLabel.getIcon().getIconWidth() - newGameButton.getIcon().getIconWidth()) / 2,
						  (layeredBackgroundLabel.getIcon().getIconHeight() - 4 * newGameButton.getIcon().getIconHeight() - 10 * 3) * 3 / 4);
		panel.setSize(newGameButton.getIcon().getIconWidth(), 4 * newGameButton.getIcon().getIconHeight() + 10 * 3);
		add(panel);
		
		bgSound = GameBase.getSound("preface.mid");
		bgSound.loop();
		myEvent();
		
		setTitle("Fighting  2048");
		setIconImage(Toolkit.getDefaultToolkit().getImage("./images/icon.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(435, 175);
		
		setVisible(true);
		panel.requestFocusInWindow();
		setSize(layeredBackgroundLabel.getIcon().getIconWidth() + getInsets().left + getInsets().right,
				layeredBackgroundLabel.getIcon().getIconHeight() + getInsets().top + getInsets().bottom);
		//��ȡ��Ļ��С
	    Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	    //���þ�����ʾ
	    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
	}
	
	/**
	 * ���ð�ťͼ�겢������Ϊ͸����Ȼ��ӵ�panel��
	 * @param jbt
	 * @param pic
	 */
	private void setMyButton(JButton jbt, String pic)
	{
		ImageIcon jbtIcon = new ImageIcon(pic);
		jbt.setIcon(jbtIcon);
		jbt.setContentAreaFilled(false);//��ť͸����������Ҫ��ͼ����͸����
		jbt.setBorderPainted(false);//��ť�����߿�
		panel.add(jbt);
	}
	
	/**
	 * �����¼�
	 */
	private void myEvent() 
	{
//**********************************�����¼�**************************************
		//ͣ�ô����¼�
		addWindowListener(new WindowAdapter()
		{
			public void windowDeactivated(WindowEvent e)
			{
				layeredBackgroundLabel.setIcon(new ImageIcon("./images/snowyNightStop.png"));
				//new Thread(preface).start();
				bgSound.stop();
			}
		});
		//������¼�
		addWindowListener(new WindowAdapter()
		{
			public void windowActivated(WindowEvent e)
			{
				layeredBackgroundLabel.setIcon(new ImageIcon("./images/snowyNight.gif"));
				//new Thread(preface).start();
				bgSound.loop();
			}
		});
//*****************************************************************************
		
//**********************************����¼�**************************************
		
		//����4��button��mouseEntered��mouseExited��mousePressed��mouseReleased
		//����Ӧһ�����ʽ���Щ�¼���װ�ں���sameMouseEvent��
		sameMouseEvent(newGameButton);
		sameMouseEvent(continueGameButton);
		sameMouseEvent(quitGameButton);
		sameMouseEvent(settingsButton);
	
		//������Ϸ���ð�ť
		newGameButton.addMouseListener(new MouseAdapter()	
		{
			public void mouseClicked(MouseEvent e)
			{
				GameBase.isNewGame = true;
				GameBase.resetData(GameBase.readMyData());
				new Gameface();
				bgSound.stop();
				Preface.this.dispose();
				GameBase.isNewGame = false;
			}
		});
		
		//����������Ϸ��ť
		continueGameButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (!GameBase.isNewGame)
				{
					GameBase.isNewGame = false;
					new Gameface();
					bgSound.stop();
					Preface.this.dispose();			
				}
				else
					JOptionPane.showMessageDialog(Preface.this, "û�пɼ�������Ϸ", "��ʾ", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		//������Ϸ���ð�ť
		settingsButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				new Settingface(Preface.this);
			}
		});
		
		//�����˳���Ϸ��ť
		quitGameButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});
	}
//****************************************************************************
	
	
	/**
	 * ��װ��mouseEntered��mouseExited��2������¼�
	 * @param jbt
	 */
	private void sameMouseEvent(final JButton jbt)
	{
		jbt.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				jbt.setContentAreaFilled(true);
			}
		});	
		
		jbt.addMouseListener(new MouseAdapter()
		{
			public void mouseExited(MouseEvent e)
			{
				jbt.setContentAreaFilled(false);
			}
		});
	}
}
