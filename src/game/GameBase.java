package game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

class GameBase
{
	static final String[] STYLES = {"跨平台风格", "Windows风格", "Motif风格"};
	static final String[] SOUNDS = {"sound1.mid", "sound2.mid", "sound3.mid", "sound4.mid", "sound5.mid", "sound6.mid"};
	static final int UP = 0;
	static final int LEFT = 1;
	static final int DOWN = 2;
	static final int RIGHT = 3;
	
	static boolean isNewGame = true;
	
	static AudioClip getSound(String sound)
	{
		try
		{
			URI soundUri;
			File f = new File("./sounds/" + sound);
			if (!f.exists())
			{
				throw new FileNotFoundException();
			}
			soundUri = f.toURI(); 
			return Applet.newAudioClip(soundUri.toURL()); 
		}
		catch (Exception e)
		{
			int select = JOptionPane.showConfirmDialog(null, "背景音乐加载失败，是否继续？", "错误提示", JOptionPane.YES_NO_OPTION);
			if (1 == select)
				System.exit(0);
			return null;
		}
		
	}
	
	/**
	 * 设置窗口风格
	 * @param style
	 */
	static void setLookAndFeel(Component c, String style)
	{
		try
		{
			//判断来自于哪个菜单项
			if (style.equals(STYLES[0])) 
			{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			else if (style.equals(STYLES[1])) 
			{  
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  //设置界面样式
			}
			else if (style.equals(STYLES[2])) 
			{
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			}
			javax.swing.SwingUtilities.updateComponentTreeUI(c);//更新界面
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(c, "设置窗口风格失败", "错误提示", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	static GameData readMyData()
	{
		GameData data = new GameData();
		
		FileReader fr = null;
		BufferedReader br = null;
		try
		{
			fr = new FileReader("game.dat");
			br = new BufferedReader(fr);
			data.isSoundOn = new Boolean(br.readLine());
			data.isVisualOn = new Boolean(br.readLine());
			data.sound = br.readLine();
			data.lookAndFeel = br.readLine();
			data.score = new Integer(br.readLine());
			for (int i = 0; i <= 3; i++)
			{
				for (int j = 0; j <= 3; j++)
				{
					data.cardsNumber[i][j] = new Integer(br.readLine());
				}
			}
		}
        catch (IOException e)
        {
			JOptionPane.showMessageDialog(null, "游戏加载失败", "错误提示", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();		
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "游戏加载失败", "错误提示", JOptionPane.ERROR_MESSAGE);
			}
		}
		return data;
	}
	
	/**
	 * 
	 * @param d
	 */
	static void writeMyData(GameData data)
	{
		FileWriter fw = null;
		try
		{
			fw = new FileWriter("game.dat");
			fw.write(data.isSoundOn + "\r\n");
			fw.append(data.isVisualOn + "\r\n");
			fw.append(data.sound + "\r\n");
			fw.append(data.lookAndFeel + "\r\n");
			fw.append(data.score + "\r\n");
			for (int i = 0; i <= 3; i++)
			{
				for (int j = 0; j <= 3; j++)
				{
					fw.append(data.cardsNumber[i][j] + "\r\n");
				}
			}
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "数据写入发生异常", "错误提示", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try
			{
				if (fw != null)
					fw.close();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "数据写入发生异常", "错误提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * 初始化游戏数据
	 */
	static void initData()
	{
		GameData data = new GameData();
		data.isSoundOn = true;
		data.isVisualOn = true;
		data.sound = SOUNDS[0];
		data.lookAndFeel = STYLES[0];
		data.score = 0;
		data.cardsNumber = new int[4][4];
		GameBase.writeMyData(data);
	}
	
	/**
	 * 重置游戏数据，与initData的区别在于它只将分数重设为初始值
	 */
	static void resetData(GameData data)
	{
		GameData newData = data;
		newData.score = 0;
		newData.cardsNumber = new int[4][4];
		GameBase.writeMyData(newData);
	}
}

/**
 *用于保存数据的类
 */
class GameData 
{
	boolean isSoundOn = true;
	boolean isVisualOn = true;
	String sound = null;
	String lookAndFeel = null;
	int score = 0;
	int[][] cardsNumber = new int[4][4];
}