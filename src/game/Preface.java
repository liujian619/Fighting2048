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
	 * 复写run方法
	public void run()
	{
		getRootPane().paintComponents(getRootPane().getGraphics());
		getLayeredPane().paintComponents(getLayeredPane().getGraphics());
	}
	*/
	
	private void initInterface()
	{
		setLayout(null);//为了将panel等组建放在指定位置
		settingsButton = new JButton();
		layeredBackgroundLabel = new JLabel(new ImageIcon("./images/snowyNight.gif"));//设置背景图片
		layeredBackgroundLabel.setBounds(0, 0, layeredBackgroundLabel.getIcon().getIconWidth(), layeredBackgroundLabel.getIcon().getIconHeight());
		//将含背景图片的标签放在RootPane层
		getLayeredPane().add(layeredBackgroundLabel, new Integer(Integer.MIN_VALUE));
		
		//将ContentPane层设置为透明
		((JComponent) getContentPane()).setOpaque(false);
		
		panel = new JPanel(new GridLayout(4, 1, 0, 10));	
		//为了不挡住后面的层，故也需要设置为透明
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
		//获取屏幕大小
	    Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	    //设置居中显示
	    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
	}
	
	/**
	 * 设置按钮图标并将其设为透明，然后加到panel中
	 * @param jbt
	 * @param pic
	 */
	private void setMyButton(JButton jbt, String pic)
	{
		ImageIcon jbtIcon = new ImageIcon(pic);
		jbt.setIcon(jbtIcon);
		jbt.setContentAreaFilled(false);//按钮透明背景，但要求图标是透明的
		jbt.setBorderPainted(false);//按钮不画边框
		panel.add(jbt);
	}
	
	/**
	 * 各种事件
	 */
	private void myEvent() 
	{
//**********************************窗体事件**************************************
		//停用窗口事件
		addWindowListener(new WindowAdapter()
		{
			public void windowDeactivated(WindowEvent e)
			{
				layeredBackgroundLabel.setIcon(new ImageIcon("./images/snowyNightStop.png"));
				//new Thread(preface).start();
				bgSound.stop();
			}
		});
		//激活窗口事件
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
		
//**********************************鼠标事件**************************************
		
		//以下4个button对mouseEntered、mouseExited、mousePressed、mouseReleased
		//的响应一样，故将这些事件封装在函数sameMouseEvent中
		sameMouseEvent(newGameButton);
		sameMouseEvent(continueGameButton);
		sameMouseEvent(quitGameButton);
		sameMouseEvent(settingsButton);
	
		//单击游戏设置按钮
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
		
		//单击继续游戏按钮
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
					JOptionPane.showMessageDialog(Preface.this, "没有可继续的游戏", "提示", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		//单击游戏设置按钮
		settingsButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				new Settingface(Preface.this);
			}
		});
		
		//单击退出游戏按钮
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
	 * 封装了mouseEntered、mouseExited这2个鼠标事件
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
