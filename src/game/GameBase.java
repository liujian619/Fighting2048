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
	static final String[] STYLES = {"��ƽ̨���", "Windows���", "Motif���"};
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
			int select = JOptionPane.showConfirmDialog(null, "�������ּ���ʧ�ܣ��Ƿ������", "������ʾ", JOptionPane.YES_NO_OPTION);
			if (1 == select)
				System.exit(0);
			return null;
		}
		
	}
	
	/**
	 * ���ô��ڷ��
	 * @param style
	 */
	static void setLookAndFeel(Component c, String style)
	{
		try
		{
			//�ж��������ĸ��˵���
			if (style.equals(STYLES[0])) 
			{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			else if (style.equals(STYLES[1])) 
			{  
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  //���ý�����ʽ
			}
			else if (style.equals(STYLES[2])) 
			{
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			}
			javax.swing.SwingUtilities.updateComponentTreeUI(c);//���½���
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(c, "���ô��ڷ��ʧ��", "������ʾ", JOptionPane.WARNING_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "��Ϸ����ʧ��", "������ʾ", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "��Ϸ����ʧ��", "������ʾ", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "����д�뷢���쳣", "������ʾ", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "����д�뷢���쳣", "������ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * ��ʼ����Ϸ����
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
	 * ������Ϸ���ݣ���initData������������ֻ����������Ϊ��ʼֵ
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
 *���ڱ������ݵ���
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