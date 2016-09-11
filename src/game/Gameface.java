package game;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.LineBorder;

class Gameface extends JFrame
{
	private static final long serialVersionUID = 8428440371925024393L;
	
	private JLabel scoreLabel;
	private JLabel[][] cards;
	private JLabel rootBackgroundLabel;
	private JLabel layeredBackgroundLabel1;
	private JLabel layeredBackgroundLabel2;
	
	private JButton upButton;
	private JButton downButton;
	private JButton leftButton;
	private JButton rightButton;
	
	private JPanel scorePanel;
	private JPanel cardsPanel;
	private JPanel visualPanel;
	private JPanel buttonsPanel;

	private GameData data = null;
	private static boolean isSoundOn = true;
	private static boolean isVisualOn = true;
	private static AudioClip selectedSound = null;	
	private static String selectedLookAndFeel = null;
	private static int score = 0;
	private static int[][] cardsNumber; 
	
	private Timer timer;
	
	public Gameface()
	{
		data = GameBase.readMyData();
		isSoundOn = data.isSoundOn;
		isVisualOn = data.isVisualOn;
		selectedSound = GameBase.getSound(data.sound);
		selectedLookAndFeel = data.lookAndFeel;
		score = data.score;
		cardsNumber = data.cardsNumber;
		if (GameBase.isNewGame)	
			initCardsNumber();
		initGameface();
	}
	
	private void initGameface()
	{
		JLabel scoreBoardLabel = new JLabel(new ImageIcon("./images/scorebg.png"));
		scoreBoardLabel.setBounds(0, 0, scoreBoardLabel.getIcon().getIconWidth(), scoreBoardLabel.getIcon().getIconHeight());
		scoreLabel = new JLabel("0");
		scoreLabel.setForeground(Color.RED);
		scoreLabel.setFont(new Font("����", Font.BOLD, 28));
		scoreLabel.setBounds(280, 10, 100, 30);
		scorePanel = new JPanel(null);
		scorePanel.add(scoreBoardLabel);
		getContentPane().add(scoreLabel, 0);
		scorePanel.setBounds(0, 0, 550, 50);
			
		rootBackgroundLabel = new JLabel(new ImageIcon("./images/daynight.gif"));//���ñ���ͼƬ
		rootBackgroundLabel.setBounds(scorePanel.getWidth(), 0, rootBackgroundLabel.getIcon().getIconWidth(), rootBackgroundLabel.getIcon().getIconHeight());
		//��������ͼƬ�ı�ǩ����RootPane��
		getRootPane().add(rootBackgroundLabel, new Integer(Integer.MIN_VALUE));
		
		layeredBackgroundLabel1 = new JLabel(new ImageIcon("./images/runningConan.gif"));//���ñ���ͼƬ
		layeredBackgroundLabel1.setBounds(75, 88, layeredBackgroundLabel1.getIcon().getIconWidth(), layeredBackgroundLabel1.getIcon().getIconHeight());
		layeredBackgroundLabel2 = new JLabel(new ImageIcon("./images/centerSpinning2048.gif"));//���ñ���ͼƬ
		layeredBackgroundLabel2.setBounds(0, 0, layeredBackgroundLabel2.getIcon().getIconWidth(), layeredBackgroundLabel2.getIcon().getIconHeight());
		visualPanel = new JPanel(null);
		visualPanel.setBounds(scorePanel.getWidth(), 0, layeredBackgroundLabel2.getIcon().getIconWidth(), layeredBackgroundLabel2.getIcon().getIconHeight());
		visualPanel.add(layeredBackgroundLabel1);
		visualPanel.add(layeredBackgroundLabel2);
		visualPanel.setOpaque(false);
		//��������ͼƬ�ı�ǩ����LayeredPane��
		getLayeredPane().add(visualPanel, 0);
		getLayeredPane().setOpaque(false);
		//��ContentPane������Ϊ͸��./images/GameOver.png
		((JComponent) getContentPane()).setOpaque(false);
		
		
		buttonsPanel = new JPanel(null);
		buttonsPanel.setBounds(25, 50, 200, 200);
		buttonsPanel.setBackground(Color.GRAY);
		upButton = new JButton();
		leftButton = new JButton();
		downButton = new JButton();
		rightButton = new JButton();
		setMyButton(upButton, 1, "./images/up.png");
		setMyButton(leftButton, 2, "./images/left.png");
		setMyButton(downButton, 3, "./images/down.png");
		setMyButton(rightButton, 4, "./images/right.png");
		
		JLabel connectLabel = new JLabel("  ��ӭ���ҽ������ۣ�656165271");
		connectLabel.setFont(new Font("�����п�", Font.BOLD, 16));
		connectLabel.setOpaque(true);//JLabelĬ����͸��
		connectLabel.setForeground(Color.RED);
		connectLabel.setBackground(Color.GREEN);
		connectLabel.setBounds(0, 130 + buttonsPanel.getHeight(), visualPanel.getWidth(), 20);
		JPanel jp = new JPanel(null);
		jp.setBounds(scorePanel.getWidth(), visualPanel.getHeight(), 250, 350);
		jp.setBackground(Color.GRAY);
		jp.add(buttonsPanel);
		jp.add(connectLabel);
		
		cardsPanel = new JPanel(null);
		cardsPanel.setBounds(0, scoreBoardLabel.getIcon().getIconHeight(), scoreBoardLabel.getIcon().getIconWidth(), scoreBoardLabel.getIcon().getIconWidth());
		cardsPanel.setBorder(new LineBorder(Color.MAGENTA, 10));
		cardsPanel.setBackground(new Color(100, 80, 70));
		cards = new JLabel[4][4];
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				cards[i][j] = new JLabel();
				cards[i][j].setBounds(20 + 130 * j, 20 + 130 * i, 120, 120);
				cards[i][j].setLayout(new FlowLayout(FlowLayout.CENTER));
				cards[i][j].setOpaque(true);
				cards[i][j].setBackground(new Color(130, 70, 50));
				cardsPanel.add(cards[i][j]);
			}
		}
		displayCards();
			
		setLayout(null);
		add(scorePanel);
		add(visualPanel);
		add(jp);
		add(cardsPanel);
		if (new Boolean(isSoundOn))
			selectedSound.loop();
		if (!new Boolean(isVisualOn))
			setVisualOff();
		GameBase.setLookAndFeel(this, selectedLookAndFeel);
		setTitle("Fighting  2048");
		setIconImage(Toolkit.getDefaultToolkit().getImage("./images/icon.png"));
		setResizable(false);
		setLocationRelativeTo(null);
		myEvent();
		setVisible(true);
		buttonsPanel.requestFocusInWindow();//�����Ϊ���㣬�Ա�������ϵ�һЩKeyEvent
		setSize(800 + getInsets().left + getInsets().right, 600 + getInsets().top + getInsets().bottom);
		//��ȡ��Ļ��С
	    Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	    //���þ�����ʾ
	    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
	}
	
	/**
	 * �����¼�
	 */
	private void myEvent()
	{
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				selectedSound.stop();
				data.score = score;
				data.cardsNumber = cardsNumber;
				GameBase.writeMyData(data);
				new Preface();
				dispose();		
			}
		});
		
		upButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				moveCards(GameBase.UP);
				buttonsPanel.requestFocusInWindow();//ÿִ����һ��������buttonsPanel���³�Ϊ����
			}
		});
		
		downButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				moveCards(GameBase.DOWN);
				buttonsPanel.requestFocusInWindow();
			}
		});
		
		leftButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				moveCards(GameBase.LEFT);
				buttonsPanel.requestFocusInWindow();
			}
		});
		
		rightButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				moveCards(GameBase.RIGHT);
				buttonsPanel.requestFocusInWindow();
			}
		});
		
		buttonsPanel.addKeyListener(new KeyAdapter() 
		{	
			public void keyPressed(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
					upButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
					downButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
					leftButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
					rightButton.doClick();
			}
		});
	}
	
	/**
	 * ���ð�ť
	 * @param jbt
	 * @param pic
	 */
	private void setMyButton(JButton jbt, int n, String pic)
	{
		ImageIcon jbtIcon = new ImageIcon(pic);
		jbt.setIcon(jbtIcon);
		jbt.setBackground(Color.BLACK);
		jbt.setSize(jbt.getIcon().getIconWidth(), jbt.getIcon().getIconHeight());
		if (n % 2 == 1)
			jbt.setLocation(70, (n - 1) * 70);
		else
			jbt.setLocation((n - 2) * 70, 70);
		//jbt.setContentAreaFilled(false);//��ť͸����������Ҫ��ͼ����͸����
		jbt.setBorderPainted(false);//��ť�����߿�
		buttonsPanel.add(jbt); 
	}
	
	/**
	 * ���ö�̬����Ч��
	 */
	private void setVisualOff()
	{
		rootBackgroundLabel.setIcon(new ImageIcon("./images/daynightStop.png"));
		layeredBackgroundLabel1.setIcon(new ImageIcon("./images/runningConanStop.png"));
		layeredBackgroundLabel2.setIcon(new ImageIcon("./images/centerSpinning2048Stop.png"));
	}
	
	/**
	 * �ƶ�cards
	 * @param direction
	 * @throws InterruptedException 
	 */
	private void moveCards(int direction)
	{
		int presentCardsNumber[][] = new int[4][4];//���浱ǰcardsnumber��ֵ
		//ʵ��cardsNumber��presentcardsNumber�����
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				presentCardsNumber[i][j] = cardsNumber[i][j];
			}
		}
		if (isMovable(presentCardsNumber, direction))
		{
			displayCards();
			createCard();
			timer = new Timer();
		    timer.schedule(new GameEndTask(), 100);
		}
	}
	
	/**
	 * ��Ϸ����
	 * ������Ϸͨ������Ϸʧ��
	 */
	class GameEndTask extends TimerTask
	{
		public void run() 
		{
			JLabel gameEndLabel = new JLabel(new ImageIcon(""));
			if (isPassed())
			{
				gameEndLabel.setIcon(new ImageIcon("./images/GamePass.png"));
				selectedSound.stop();
				selectedSound = GameBase.getSound("congratulation.wav");
				selectedSound.loop();
				GameBase.isNewGame = true;
			}
			if (isGameOver())
			{
				gameEndLabel.setIcon(new ImageIcon("./images/GameOver.png"));
				selectedSound.stop();
				selectedSound = GameBase.getSound("restart.wav");
				selectedSound.loop();
				GameBase.isNewGame = true;
			}
			gameEndLabel.setBounds(50, 150, gameEndLabel.getIcon().getIconWidth(), gameEndLabel.getIcon().getIconHeight());
			((JComponent) getContentPane()).add(gameEndLabel, 0);
			javax.swing.SwingUtilities.updateComponentTreeUI(Gameface.this);//���½���
			timer.cancel();
		}	
	}
	
	/**
	 * ��ʼ��card��ֵ
	 */
	private void initCardsNumber()
	{
		int x1 = (int) (Math.random() * 4);
		int y1 = (int) (Math.random() * 4);
		int x2 = (int) (Math.random() * 4);
		int y2 = (int) (Math.random() * 4);
		while (x1 == x2 || y1 == y2)
		{
			x2 = (int) (Math.random() * 4);
			y2 = (int) (Math.random() * 4);
		}
		cardsNumber[x1][y1] = cardsNumber[x2][y2] = 2;
	}
	
	/**
	 * �������һ����card
	 */
	private void createCard()
	{
		int x = (int) (Math.random() * 4);
		int y = (int) (Math.random() * 4);
		while (cardsNumber[x][y] != 0)
		{
			x = (int) (Math.random() * 4);
			y = (int) (Math.random() * 4);
		}
		if ((int) (100 * Math.random() % 9) == 0)
			cardsNumber[x][y] = 4;
		else
			cardsNumber[x][y] = 2;
		cards[x][y].setIcon(new ImageIcon("./images/" + cardsNumber[x][y] + "-pre.gif"));		
		timer = new Timer();
        timer.schedule(new ChangeTask(x, y), 100);
	}
	
	class ChangeTask extends TimerTask
	{
		int x, y;
		public ChangeTask(int x, int y) 
		{	
			this.x = x;
			this.y = y;
		}
		public void run() 
		{
			cards[x][y].setIcon(new ImageIcon("./images/" + cardsNumber[x][y] + ".png"));
			timer.cancel();
		}
	}
	
	/**
	 * �ж��ܷ��ƶ�
	 * @param preCardsNumber----��ǰ״̬��cardsnumber
	 * @param direction
	 * @return
	 */
	private boolean isMovable(int[][] presentCardsNumber, int direction)
	{
		adjustCardsNumber(cardsNumber, direction);
		moveCardsNumber(cardsNumber);
		adjustCardsNumber(cardsNumber, direction);
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				if (cardsNumber[i][j] != presentCardsNumber[i][j])
					return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж��Ƿ�Game Over
	 * @return
	 */
	private boolean isGameOver()
	{
		int presentCardsNumber[][] = new int[4][4];//���浱ǰ״̬cardsnumber��ֵ
		int presentScore = score;  //���浱ǰ����
		//ʵ��cardsNumber��presentcardsNumber�����
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				presentCardsNumber[i][j] = cardsNumber[i][j];
			}
		}
		if (isMovable(presentCardsNumber, GameBase.UP)    ||
			isMovable(presentCardsNumber, GameBase.DOWN)  ||
			isMovable(presentCardsNumber, GameBase.LEFT)  ||
			isMovable(presentCardsNumber, GameBase.RIGHT))
		{
			for (int i = 0; i <= 3; i++)
			{
				for (int j = 0; j <= 3; j++)
				{
					cardsNumber[i][j] = presentCardsNumber[i][j];
				}
			}
			score = presentScore;
			return false;
		}
		else 
			return true;
	}
	
	/**
	 * �ж��Ƿ����
	 * @return
	 */
	private boolean isPassed()
	{
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				if (cardsNumber[i][j] == 2048)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ʾcards
	 */
	private void displayCards()
	{
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				cards[i][j].setIcon(new ImageIcon("./images/" + cardsNumber[i][j] + ".png"));
			}
		}
		scoreLabel.setText(score + "");
	}
	
	/**
	 * ����cards�Է����ƶ�����ʾ
	 * @param cards
	 * @param startPosition
	 */
	private void adjustCardsNumber(int[][] array, int direction)
	{
		cardsNumber = new int [4][4];
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++) 
			{
				if (direction == GameBase.UP)
					cardsNumber[i][j] = array[j][i];
				if (direction == GameBase.LEFT)
					cardsNumber[i][j] = array[i][j];
				if (direction == GameBase.DOWN)
					cardsNumber[i][j] = array[3 - j][3 - i];
				if(direction == GameBase.RIGHT)
					cardsNumber[i][j] = array[i][3 - j];		
			}	
		}
	}
	
	/**
	 * �ƶ�ȫ��
	 * @param cards
	 */
	private void moveCardsNumber(int[][] cards)
	{
		for (int i = 0; i <= 3; i++)
		{
			moveLineNumber(cards[i]);
		}
	}
	
	/**
	 * �ƶ�һ��
	 * @param a
	 */
	private void moveLineNumber(int[] a)
	{
		int p;  //pָ����ǰ��Ŀհ�
		int s;  //s����ɨ������
		boolean flag = true;  //�����ܷ����.�磺2,2,4������Ӧ����4,4
		for (p = s = 0; s <= 3; s++)
		{
			if (s == 0 && a[s] != 0)
				p++;
			if (s != 0 && a[s] != 0)
			{
				if (p != 0 && a[s] == a[p - 1] && flag)
				{
					a[p - 1] += a[s];
					score += a[p - 1];
					a[s] = 0;
					flag = false;
				}
				else
				{
					a[p] = a[s];
					if (p != s)
						a[s] = 0;
					p++;
					flag = true;
				}
			}
		}
	}
}