package MultiPlay;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;


public class lianliankan implements ActionListener {
	JFrame mainFrame; //主游戏的JFrame框
	panel1 centerPanel;//游戏的主面板
	RivalPanels[] otherpanels;
	//panel2[] others=new panel2[3];
	panel2 saidPanel; // 子面板
	JButton diamondsButton[][] = new JButton[10][10];// 游戏按钮数组
	JButton firstButton, secondButton; // 分别记录两次被选中的按钮
	JButton start;//开始按钮
	JButton remarkButton,recommend,cannotsee,cannotdo;//道具
	JButton exit;//退出
	
	int  istart=1,iremarkButton=3,irecommend=3,icannotsee=3,icannotdo=3;
	
	public int grid[][] = new int[12][12];//这个数组便是保存每个图标信息的数组，没有图标的为零
	int[][][] grids;
	
	
	
	static boolean pressInformation = false; /* 判断是否有按钮被选中*/
	boolean iflian=true;//连线
	boolean pos=true;//暂停控制
	boolean zhetian=true;
	boolean dengyan=true;
	int x0 = 0, y0 = 0, x = 0, y = 0, fristMsg = 0, secondMsg = 0; // 游戏按钮的位置坐标
	int r0=0,t0=0,r=0,t=0;//记录拐点的坐标
	int i, j, k, n;// 消除方法控制
	
	Socket s=null;//套接字		
	DataInputStream dis=null;//传输流
	DataOutputStream dos=null;
	
	String ip="127.0.0.1";//ip
	int pour=5555;//端口
	int id=0;//id
	
	String name="haha~";
	String win="50%";
	String Level="lala~";
	
	String str="";//接收信息的载体
	
	boolean isConnect=false;
	
	Thread recieve=new Thread(new Recieve());
	
	JPanel [] panels=new JPanel[3];
	
	JLabel [] labels=new JLabel[3];
	JLabel label0;
//	Vector <int[][][]> grids =new Vector <int[][][]>();
	

	//在游戏每次开始时实例化按钮，并将其添加到centerpanel里面
	void pause(){
		pos=!pos;
		if (pos) {
			healicons();
		}
		else {
			pauseicons();
		}
	}
	void healicons(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				diamondsButton[i][j].setIcon(new ImageIcon(String.valueOf(grid[i+1][j+1]+".gif")));
			}
		}
	}
	void pauseicons(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				diamondsButton[i][j].setIcon(new ImageIcon("26.gif"));
			}
		}
	
		
	}

	public void AddGif() {
		for (int cols = 0; cols < 10; cols++) {
			for (int rows = 0; rows < 10; rows++) {
				diamondsButton[cols][rows] = new JButton(new ImageIcon(
						String.valueOf(grid[cols + 1][rows + 1]) + ".gif"));
//						"E:/本地磁盘D/课程设计/1.gif"));
				diamondsButton[cols][rows].setBounds(60*rows+60,60*cols+60,59,59);
				diamondsButton[cols][rows].addActionListener(this);
				diamondsButton[cols][rows].setBorder(null);
				diamondsButton[cols][rows].setContentAreaFilled(false);
				centerPanel.add(diamondsButton[cols][rows]);
			}
		}
	}
	
	
//  在游戏启动时调用一次，主要实例化并添加一些组件容器
	public void create() {
		recommend=new JButton();
		recommend.setIcon(new ImageIcon("锦囊.png"));/////添加图片
//		recommend.setBorder(null);
		recommend.setContentAreaFilled(false);
		
		cannotdo=new JButton();
		cannotdo.setIcon(new ImageIcon("干瞪眼.png"));/////添加图片
//		cannotdo.setBorder(null);
		cannotdo.setContentAreaFilled(false);
		
		cannotsee=new JButton();
		cannotsee.setIcon(new ImageIcon("遮天.png"));/////添加图片
//		cannotsee.setBorder(null);
		cannotsee.setContentAreaFilled(false);
		
		start=new JButton();
		start.setIcon(new ImageIcon("开始.gif"));
//		start.setBorder(null);
		start.setContentAreaFilled(false);
		
		remarkButton=new JButton();
		remarkButton.setIcon(new ImageIcon("天助.png"));
//		remarkButton.setBorder(null);
		remarkButton.setContentAreaFilled(false);
		
		
		recommend.addActionListener(this);
		cannotdo.addActionListener(this);
		cannotsee.addActionListener(this);
		start.addActionListener(this);
		remarkButton.addActionListener(this);
		
		Information inf=new Information();
		name=inf.getname();
		win=inf.getRate();
		Level=inf.getLevel();
		
		label0=new JLabel("");
		label0.setText(name+"    胜率："+win+"    "+Level);
		
		
		grids=new int[3][12][12];
		for (int tempi = 0; tempi < 3; tempi++) {
			for(int tempj=0;tempj<12;tempj++){
				for(int tempk=0;tempk<12;tempk++){
					grids[tempi][tempj][tempk]=26;
				}
			}
		}
		
		mainFrame = new JFrame("连连看");
		mainFrame.setLayout(null);
		centerPanel = new panel1();
		centerPanel.setBackground(Color.green);
		saidPanel = new panel2();
		saidPanel.setLayout(null);
		saidPanel.setBackground(Color.yellow);

		centerPanel.setLayout(null);

		AddGif();

		exit=new JButton("逃跑");
		exit.setVisible(false);
		exit.addActionListener(this);
		
		mainFrame.setBounds(0, 0, 1355, 740);
		saidPanel.add(label0);

		saidPanel.add(recommend);
		saidPanel.add(cannotdo);
		saidPanel.add(cannotsee);
		saidPanel.add(exit);
		saidPanel.add(start);
		saidPanel.add(remarkButton);
		mainFrame.add(saidPanel);
		
		otherpanels=new RivalPanels[3];
		otherpanels[0]=new RivalPanels(grids[0]);
		otherpanels[1]=new RivalPanels(grids[1]);
		otherpanels[2]=new RivalPanels(grids[2]);
		otherpanels[0].setLayout(null);
		otherpanels[1].setLayout(null);
		otherpanels[2].setLayout(null);
		otherpanels[0].setBounds(0,0,300,230);
		otherpanels[1].setBounds(0,230,300,230);
		otherpanels[2].setBounds(0,460,300,230);
		mainFrame.add(otherpanels[0]);
		mainFrame.add(otherpanels[1]);
		mainFrame.add(otherpanels[2]);
		mainFrame.add(centerPanel);
	
		exit.setBounds(100,200,90,30);
		start.setBounds(100,300,96,60);
		recommend.setBounds(50,400,80,80);
		cannotdo.setBounds(150,400,80,80);
		cannotsee.setBounds(50,500,80,80);
		remarkButton.setBounds(150,500,80,80);
		
		
		saidPanel.setBounds(1020, 0, 345, 720);
		label0.setBounds(100, 50, 300, 50);
		centerPanel.setBounds(300, 0, 720, 720);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//--------------------------------------------------------------------------------------------------------
	private class RivalPanels extends JPanel
	{
		int[][] information;
		RivalPanels(int[][] information){
			this.information=information;
		}
		public void paintComponent(Graphics pag)
		{
			super.paintComponent(pag);
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < 11; j++) {
					pag.drawImage((new ImageIcon(String.valueOf(information[j][i]) + ".gif")).getImage(),21*i-1,21*j-1,20,20,null);
				}
			}

		}
		void setinfo(int[][] information){
			this.information=information;
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	
	//构造随机功能，每次开始时会调用
	public void randomBuild() {
		for (int i = 1; i < 11; i++) {
			for (int j = 1; j < 11; j++) {
				grid[i][j]=0;
			}
		}
		byte randoms;
		int cols, rows;
		for (int twins = 1; twins <= 25; twins++) {// 一共15对button,30个
			randoms = (byte) (Math.random() * 25 + 1);// button上的数字
			for (int alike = 1; alike <= 4; alike++) {
				cols = (int) (Math.random() * 10 + 1);
				rows = (int) (Math.random() * 10 + 1);
				while (grid[cols][rows] != 0) // 不等于0说明这个空格有了button
				{
					cols = (int) (Math.random() * 10 + 1);
					rows = (int) (Math.random() * 10 + 1);
				}
				this.grid[cols][rows] = randoms;
			}
		}
	}

	//实现随机重排功能，响应“重列”按钮
	public void reload() {
		int save[] = new int[100];
		int n = 0, cols, rows;
		int grid[][] = new int[12][12];
		for (int i = 0; i <= 10; i++) {
			for (int j = 0; j <= 10; j++) {
				if (this.grid[i][j] != 0) {
					save[n] = this.grid[i][j];// 记下每个button的数字
					n++;// 有几个没有消去的button
				}
			}
		}
		n = n - 1;
		this.grid = grid;
		while (n >= 0) {// 把没有消去的button重新放一次
			cols = (int) (Math.random() * 10 + 1);
			rows = (int) (Math.random() * 10 + 1);
			while (grid[cols][rows] != 0) {
				cols = (int) (Math.random() * 10 + 1);
				rows = (int) (Math.random() * 10 + 1);
			}
			this.grid[cols][rows] = save[n];
			n--;
		}
		pressInformation = false; // 这里一定要将按钮点击信息归为初始
		centerPanel.removeAll();
		AddGif();
		mainFrame.show();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (grid[i + 1][j + 1] == 0)
					diamondsButton[i][j].setVisible(false);
			}
		}
		centerPanel.repaint();
	}

	//用来判断是不是两个相同的图标，并且实现按钮的流动控制和按钮被选中的效果
	public void estimateEven(int placeX, int placeY, JButton bz) {
		if (pressInformation == false) {
			x = placeX;
			y = placeY;
			if (zhetian) {
				diamondsButton[x-1][y-1].setIcon(new ImageIcon(String.valueOf((-grid[x][y]) + ".gif")));
			}
			secondMsg = grid[x][y];
			secondButton = bz;
			pressInformation = true;
		} else {
			x0 = x;
			y0 = y;
			fristMsg = secondMsg;
			firstButton = secondButton;
			x = placeX;
			y = placeY;
			if (zhetian) {
				diamondsButton[x-1][y-1].setIcon(new ImageIcon(String.valueOf((-grid[x][y]) + ".gif")));
				if (x!=x0||y!=y0)
					diamondsButton[x0-1][y0-1].setIcon(new ImageIcon(String.valueOf((grid[x0][y0]) + ".gif")));
			}
			secondMsg = grid[x][y];
			secondButton = bz;
			if (fristMsg == secondMsg && (x0!=x||y0!=y)) {
				if (xiao()) {
					remove();
				}
			}
		}
	}

	//这个主要用来判断两个相同的图标是否可以连起来
	public boolean xiao() {
		if ((x0 == x && (y0 == y + 1 || y0 == y - 1))
				|| ((x0 == x + 1 || x0 == x - 1) && (y0 == y))) { // 判断是否相邻
			r0=x0;
			t0=y0;
			r=x;
			t=y;
			return true;
		} else {
			//如果两个在同行
			if (x==x0) {
				if (y0<y) {
					for (int i = y0+1; i <= y-1; i++) {
						if (grid[x][i]!=0) {
							k=0;
							break;
						}
						else {
							k=1;
						}
					}
					if (k==1) {
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}
				}
				if (y0>y) {
					for (int i = y+1; i <= y0-1; i++) {
						if (grid[x][i]!=0) {
							k=0;
							break;
						}
						else {
							k=1;
						}
					}
					if (k==1) {
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}
				}
			}
			//在同列
			if (y==y0) {
				if (x0<x) {
					for (int i = x0+1; i <= x-1; i++) {
						if (grid[i][y]!=0) {
							k=0;
							break;
						}
						else {
							k=1;
						}
					}
					if (k==1) {
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}
				}
				if (x0>x) {
					for (int i = x+1; i <= x0-1; i++) {
						if (grid[i][y]!=0) {
							k=0;
							break;
						}
						else {
							k=1;
						}
					}
					if (k==1) {
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}
				}
			}
			
			//折一次
			if (grid[x0][y]==0) {
				if (pass(x0, x0, y0, y)&&pass(x0, x, y, y)) {
					r0=x0;
					t0=y0;
					r=x0;
					t=y;
					return true;
				}
			}
			if (grid[x][y0]==0) {
				if (pass(x0, x, y0, y0)&&pass(x, x, y0, y)) {
					r0=x0;
					t0=y0;
					r=x;
					t=y0;
					return true;
				}
			}
			
			//折两次
			if (y0 < y) {
				for (j = y0; j <=y; j++) {
					if (grid[x0][j] == 0) { // 判断和第一个按钮同行的哪个按钮为空
						// 如果找到一个为空的，就按列值的三种情况比较第二个按钮与空按钮的位置

						if (y > j) {// 第二个按钮在空按钮右边
							for (i = y - 1; i >= j; i--) { // 检测从第二个按钮横向左边到空格所在列为止是否全是空格
								if (grid[x][i] != 0) {
									k = 0;
									break;// 存在非空格的就退出，这一退出就不可能k==2了，所以就会到下而215行出同理的判断列
								} else {
									k = 1;
								} // K=1说明全是空格通过了第一次验证，也就是从第二个按钮横向左边到空格所在列为止全是空格
							}
							if (k == 1) {
								linePassOne();// 进入第二次验证，也就是从第一个按钮到它同行的空格之间的空格判断
							}
						}

						if (y < j) { // 第二个按钮在空按钮左边
							for (i = y + 1; i <= j; i++) {// 检测从第二个按钮横向右边到空格所在列为止是否全是空格
								if (grid[x][i] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								linePassOne();
							}
						}

						if (y == j) {// 第二个按钮和空按钮同列
							linePassOne();
						}
					}

					// 第三次检测，检测确定为空的第j列的那个按钮竖向到第二个按钮，看是不是有按钮
					if (k == 2) {
						if (x0 == x) {// 第一，二按钮在同行
						// remove();
							r0 = x0;
							t0 = y0;
							r = x;
							t = y;
							return true;
						}

						if (x0 < x) {// 第一按钮在第二按钮下边
							for (n = x0; n <= x - 1; n++) {// 从空按钮竖向到第二个按钮所在行是否有按钮
								if (grid[n][j] != 0) {
									k = 0;
									break;
								}
								// 没有按钮，说明这条路经就通了
								if (grid[n][j] == 0 && n == x - 1) {
									// remove();
									r0 = x0;
									t0 = j;
									r = x;
									t = j;
									return true;
								}
							}
						}

						if (x0 > x) {// 第一按钮在第二按钮上边
							for (n = x0; n >= x + 1; n--) {
								if (grid[n][j] != 0) {
									k = 0;
									break;
								}
								if (grid[n][j] == 0 && n == x + 1) {
									// remove();
									r0 = x0;
									t0 = j;
									r = x;
									t = j;
									return true;
								}
							}
						}
					}

				}
			} else {
				for (j = y; j <= y0; j++) {
					if (grid[x0][j] == 0) { // 判断和第一个按钮同行的哪个按钮为空
						// 如果找到一个为空的，就按列值的三种情况比较第二个按钮与空按钮的位置

						if (y > j) {// 第二个按钮在空按钮右边
							for (i = y - 1; i >= j; i--) { // 检测从第二个按钮横向左边到空格所在列为止是否全是空格
								if (grid[x][i] != 0) {
									k = 0;
									break;// 存在非空格的就退出，这一退出就不可能k==2了，所以就会到下而215行出同理的判断列
								} else {
									k = 1;
								} // K=1说明全是空格通过了第一次验证，也就是从第二个按钮横向左边到空格所在列为止全是空格
							}
							if (k == 1) {
								linePassOne();// 进入第二次验证，也就是从第一个按钮到它同行的空格之间的空格判断
							}
						}

						if (y < j) { // 第二个按钮在空按钮左边
							for (i = y + 1; i <= j; i++) {// 检测从第二个按钮横向右边到空格所在列为止是否全是空格
								if (grid[x][i] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								linePassOne();
							}
						}

						if (y == j) {// 第二个按钮和空按钮同列
							linePassOne();
						}
					}

					// 第三次检测，检测确定为空的第j列的那个按钮竖向到第二个按钮，看是不是有按钮
					if (k == 2) {
						if (x0 == x) {// 第一，二按钮在同行
							r0 = x0;
							t0 = y0;
							r = x;
							t = y;
							return true;
						}

						if (x0 < x) {// 第一按钮在第二按钮下边
							for (n = x0; n <= x - 1; n++) {// 从空按钮竖向到第二个按钮所在行是否有按钮
								if (grid[n][j] != 0) {
									k = 0;
									break;
								}
								// 没有按钮，说明这条路经就通了
								if (grid[n][j] == 0 && n == x - 1) {
									r0 = x0;
									t0 = j;
									r = x;
									t = j;
									return true;
								}
							}
						}

						if (x0 > x) {// 第一按钮在第二按钮上边
							for (n = x0; n >= x + 1; n--) {
								if (grid[n][j] != 0) {
									k = 0;
									break;
								}
								if (grid[n][j] == 0 && n == x + 1) {
									r0 = x0;
									t0 = j;
									r = x;
									t = j;
									return true;
								}
							}
						}
					}

				}

			}
			for (j = 0; j < 12; j++) {
				if (grid[x0][j] == 0) { // 判断和第一个按钮同行的哪个按钮为空
					// 如果找到一个为空的，就按列值的三种情况比较第二个按钮与空按钮的位置

					if (y > j) {// 第二个按钮在空按钮右边
						for (i = y - 1; i >= j; i--) { // 检测从第二个按钮横向左边到空格所在列为止是否全是空格
							if (grid[x][i] != 0) {
								k = 0;
								break;// 存在非空格的就退出，这一退出就不可能k==2了，所以就会到下而215行出同理的判断列
							} else {
								k = 1;
							} // K=1说明全是空格通过了第一次验证，也就是从第二个按钮横向左边到空格所在列为止全是空格
						}
						if (k == 1) {
							linePassOne();// 进入第二次验证，也就是从第一个按钮到它同行的空格之间的空格判断
						}
					}

					if (y < j) { // 第二个按钮在空按钮左边
						for (i = y + 1; i <= j; i++) {// 检测从第二个按钮横向右边到空格所在列为止是否全是空格
							if (grid[x][i] != 0) {
								k = 0;
								break;
							} else {
								k = 1;
							}
						}
						if (k == 1) {
							linePassOne();
						}
					}

					if (y == j) {// 第二个按钮和空按钮同列
						linePassOne();
					}
				}

				// 第三次检测，检测确定为空的第j列的那个按钮竖向到第二个按钮，看是不是有按钮
				if (k == 2) {
					if (x0 == x) {// 第一，二按钮在同行
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}

					if (x0 < x) {// 第一按钮在第二按钮下边
						for (n = x0; n <= x - 1; n++) {// 从空按钮竖向到第二个按钮所在行是否有按钮
							if (grid[n][j] != 0) {
								k = 0;
								break;
							}
							// 没有按钮，说明这条路经就通了
							if (grid[n][j] == 0 && n == x - 1) {
								r0=x0;
								t0=j;
								r=x;
								t=j;
								return true;
							}
						}
					}

					if (x0 > x) {// 第一按钮在第二按钮上边
						for (n = x0; n >= x + 1; n--) {
							if (grid[n][j] != 0) {
								k = 0;
								break;
							}
							if (grid[n][j] == 0 && n == x + 1) {
								r0 = x0;
								t0 = j;
								r = x;
								t = j;
								return true;
							}
						}
					}
				}

			}

			if (x0 < x) {
				for (i = x0; i <=x; i++) {
					if (grid[i][y0] == 0) {// 判断和第一个按钮同列的哪个按钮为空
						if (x > i) {// 第二个按钮在这个空按钮的下面
							for (j = x - 1; j >= i; j--) {
								if (grid[j][y] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								rowPassOne();
							}
						}

						if (x < i) {// 第二个按钮在这个空按钮的上面
							for (j = x + 1; j <= i; j++) {
								if (grid[j][y] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								rowPassOne();
							}
						}

						if (x == i) {// 第二个按钮与这个空按钮同行
							rowPassOne();
						}
					}

					if (k == 2) {
						if (y0 == y) {// 第二个按钮与第一个按钮同列
//							remove();
							r0=x0;
							t0=y0;
							r=x;
							t=y;
							return true;
						}
						if (y0 < y) {// 第二个按钮在第一个按钮右边
							for (n = y0; n <= y - 1; n++) {
								if (grid[i][n] != 0) {
									k = 0;
									break;
								}
								if (grid[i][n] == 0 && n == y - 1) {
//									remove();
									r0=i;
									t0=y0;
									r=i;
									t=y;
									return true;
								}
							}
						}
						if (y0 > y) {// 第二个按钮在第一个按钮左边
							for (n = y0; n >= y + 1; n--) {
								if (grid[i][n] != 0) {
									k = 0;
									break;
								}
								if (grid[i][n] == 0 && n == y + 1) {
									r0=i;
									t0=y0;
									r=i;
									t=y;
									return true;
								}
							}
						}
					}
				}
			} else {
				for (i =x; i < x0; i++) {
					if (grid[i][y0] == 0) {// 判断和第一个按钮同列的哪个按钮为空
						if (x > i) {// 第二个按钮在这个空按钮的下面
							for (j = x - 1; j >= i; j--) {
								if (grid[j][y] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								rowPassOne();
							}
						}

						if (x < i) {// 第二个按钮在这个空按钮的上面
							for (j = x + 1; j <= i; j++) {
								if (grid[j][y] != 0) {
									k = 0;
									break;
								} else {
									k = 1;
								}
							}
							if (k == 1) {
								rowPassOne();
							}
						}

						if (x == i) {// 第二个按钮与这个空按钮同行
							rowPassOne();
						}
					}

					if (k == 2) {
						if (y0 == y) {// 第二个按钮与第一个按钮同列
							r0=x0;
							t0=y0;
							r=x;
							t=y;
							return true;
						}
						if (y0 < y) {// 第二个按钮在第一个按钮右边
							for (n = y0; n <= y - 1; n++) {
								if (grid[i][n] != 0) {
									k = 0;
									break;
								}
								if (grid[i][n] == 0 && n == y - 1) {
									r0=i;
									t0=y0;
									r=i;
									t=y;
									return true;
								}
							}
						}
						if (y0 > y) {// 第二个按钮在第一个按钮左边
							for (n = y0; n >= y + 1; n--) {
								if (grid[i][n] != 0) {
									k = 0;
									break;
								}
								if (grid[i][n] == 0 && n == y + 1) {
									r0=i;
									t0=y0;
									r=i;
									t=y;
									return true;
								}
							}
						}
					}
				}

			}
			for (i = 0; i < 12; i++) {
				if (grid[i][y0] == 0) {// 判断和第一个按钮同列的哪个按钮为空
					if (x > i) {// 第二个按钮在这个空按钮的下面
						for (j = x - 1; j >= i; j--) {
							if (grid[j][y] != 0) {
								k = 0;
								break;
							} else {
								k = 1;
							}
						}
						if (k == 1) {
							rowPassOne();
						}
					}

					if (x < i) {// 第二个按钮在这个空按钮的上面
						for (j = x + 1; j <= i; j++) {
							if (grid[j][y] != 0) {
								k = 0;
								break;
							} else {
								k = 1;
							}
						}
						if (k == 1) {
							rowPassOne();
						}
					}

					if (x == i) {// 第二个按钮与这个空按钮同行
						rowPassOne();
					}
				}

				if (k == 2) {
					if (y0 == y) {// 第二个按钮与第一个按钮同列
//						remove();
						r0=x0;
						t0=y0;
						r=x;
						t=y;
						return true;
					}
					if (y0 < y) {// 第二个按钮在第一个按钮右边
						for (n = y0; n <= y - 1; n++) {
							if (grid[i][n] != 0) {
								k = 0;
								break;
							}
							if (grid[i][n] == 0 && n == y - 1) {
								r0=i;
								t0=y0;
								r=i;
								t=y;
								return true;
							}
						}
					}
					if (y0 > y) {// 第二个按钮在第一个按钮左边
						for (n = y0; n >= y + 1; n--) {
							if (grid[i][n] != 0) {
								k = 0;
								break;
							}
							if (grid[i][n] == 0 && n == y + 1) {
								r0=i;
								t0=y0;
								r=i;
								t=y;
								return true;
							}
						}
					}
				}
			}
		}
		return false;// -------------else
	}
	
	//这个不用管
	public void linePassOne() {
		if (y0 > j) { // 第一按钮同行空按钮在左边
			for (i = y0 - 1; i >= j; i--) { // 判断第一按钮同左侧空按钮之间有没按钮
				if (grid[x0][i] != 0) {
					k = 0;
					break;
				} else {
					k = 2;
				} // K=2说明通过了第二次验证
			}
		}

		if (y0 < j) { // 第一按钮同行空按钮在右边
			for (i = y0 + 1; i <= j; i++) {
				if (grid[x0][i] != 0) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}
	}

	//这个也不用管
	public void rowPassOne() {
		if (x0 > i) {// 第一个按钮在与它同列的那个空格按钮下面
			for (j = x0 - 1; j >= i; j--) {
				if (grid[j][y0] != 0) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}

		if (x0 < i) {// 第一个按钮在与它同列的那个空格按钮上面
			for (j = x0 + 1; j <= i; j++) {
				if (grid[j][y0] != 0) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}
	}

	//这个任然不用管
	boolean pass(int sx1, int sx2, int sy1, int sy2) {
		if ((sx1 == sx2 && (sy1 == sy2 + 1 || sy1 == sy2 - 1)) || (sy1 == sy2)
				&& (sx1 == sx2 + 1 || sx1 == sx2 - 1)) {
			return true;
		}
		if (sx1 == sx2) {
			if (sy1 < sy2) {
				for (int i = sy1 + 1; i <= sy2 - 1; i++) {
					if (grid[sx1][i] != 0) {
						k = 0;
						break;
					} else {
						k = 1;
					}
				}
				if (k==1) {
					return true;
				}
			}
			if (sy1>sy2) {
				for (int i = sy2+1; i <= sy1-1; i++) {
					if (grid[sx1][i]!=0) {
						k=0;
						break;
					}
					else {
						k=1;
					}
				}
				if (k==1) {
					return true;
				}
			}
		}
		if (sy1==sy2) {
			if (sx1<sx2) {
				for (int i = sx1+1; i <= sx2-1; i++) {
					if (grid[i][sy1]!=0) {
						k=0;
						break;
					}
					else {
						k=1;
					}
				}
				if (k==1) {
					return true;
				}
			}
			if (sx1>sx2) {
				for (int i = sx2+1; i <= sx1-1; i++) {
					if (grid[i][sy1]!=0) {
						k=0;
						break;
					}
					else {
						k=1;
					}
				}
				if (k==1) {
					return true;
				}
			}
		}
		return false;
	}

	void recommend() {
		if (pressInformation) {
			diamondsButton[x - 1][y - 1].setIcon(new ImageIcon(String
					.valueOf((grid[x][y]) + ".gif")));
		}
		if (find()) {
			new tishi().start();
		}
	}
	boolean find() {
		int temp;
		for (int kx = 1; kx <= 10; kx++) {
			for (int ky = 1; ky <= 10; ky++) {
				temp = ky;
				for (int lx = kx; lx <= 10; lx++) {
					for (int ly = temp + 1; ly <= 10; ly++) {
						temp = 0;
						x0 = kx;
						y0 = ky;
						x = lx;
						y = ly;
						pressInformation = false;
						if (grid[x0][y0] == grid[x][y] && xiao()
								&& grid[x0][y0] != 0) {
							return true;
						}
					}
					temp = 0;
				}
			}
		}
		return false;
	}
	
	public class tishi extends Thread {
		public void run() {
			super.run();
			diamondsButton[x0 - 1][y0 - 1].setVisible(false);
			diamondsButton[x - 1][y - 1].setVisible(false);
			try {
				sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			diamondsButton[x0 - 1][y0 - 1].setVisible(true);
			diamondsButton[x - 1][y - 1].setVisible(true);

		}
	}
	
	public class gandengyan extends Thread{
		public void run(){
			super.run();
			dengyan=false;
			try {
				sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dengyan=true;
			
		}
	}
	public class zhetianbiri extends Thread{
		public void run(){
			super.run();
			change();
			zhetian=false;
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			zhetian=true;
			healicons();
		}
	}
	
	void change(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				diamondsButton[i][j].setIcon(new ImageIcon("26.gif"));
			}
		}
	}


	
	
	
	
	//消去方法。每当两个一样的图标可以消去时，必定调用这个方法将它消去
	public void remove() {
		firstButton.setVisible(false);
		secondButton.setVisible(false);
		pressInformation = false;
		k = 0;
		grid[x0][y0] = 0;
		grid[x][y] = 0;
		iflian=true;
		new lian().start();
		
		AePlayWave ok=new AePlayWave("ok.wav");
		ok.start();
		
		try {
			dos.writeUTF(id+" "+transcode.t(grid));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//判断输赢
		iswin();
	}
	void iswin(){
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++){
				if (grid[i][j]!=0) 
					return;
			}
		}
		try {
			dos.writeUTF("e");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	//这个方法是重写父类ActionListener的方法，实现对各种按钮事件的响应
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==exit)
		{
			disConnect();
			System.exit(0);
		}
		if (e.getSource()==recommend) {
			
			if(irecommend>0)
			{
				recommend();
				irecommend--;
			}
			
		}
		if (e.getSource()==start) {
			if(istart>0)
			{
			Connect();
			istart--;
			}
	
			
		}
		if (e.getSource()==remarkButton) {
			
			if(iremarkButton>0)
			{
				reload();
				iremarkButton--;
			}
			
		}
		if (e.getSource()==cannotdo) {
			if(icannotdo>0)
			{
				try {
					dos.writeUTF(id+"b");
				} catch (IOException e1) {
	                e1.printStackTrace();
				}
				icannotdo--;
			}
		}
		if (e.getSource()==cannotsee) {
			
			if(icannotsee>0)
			{
				try {
					dos.writeUTF(id+"a");
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				icannotsee--;
			}
		}
	
//		if (e.getSource() == remarkButton)
//			reload();
		for (int cols = 0; cols < 10; cols++) {
			for (int rows = 0; rows < 10; rows++) {
				if (e.getSource() == diamondsButton[cols][rows]&&pos&&dengyan)
				{
					estimateEven(cols + 1, rows + 1, diamondsButton[cols][rows]);
					AePlayWave click=new AePlayWave("click.wav");
					click.start();
				}
			}
		}
	}

	//这个类继承JPanel类，重写paintComponent方法，用来实现消去时连线功能。
	class panel1 extends JPanel{
		Image img;
		public panel1() {
			super();
			ImageIcon back=null;
			back=new ImageIcon("table.png");
			img=back.getImage();
		}
		public void paintComponent(Graphics page){
			super.paintComponent(page);
			int[] tempxs=new int[4];
			int[] tempys=new int[4];
			tempxs[0]=60*y0+30;
			tempxs[1]=60*t0+30;
			tempxs[2]=60*t+30;
			tempxs[3]=60*y+30;
			tempys[0]=60*x0+30;
			tempys[1]=60*r0+30;
			tempys[2]=60*r+30;
			tempys[3]=60*x+30;
			page.drawImage(img,2,1,720,709,null);
			if (iflian) {
				page.setColor(Color.red);
				page.drawPolyline(tempxs, tempys, 4);
			}
		}
	}
	
	
	class panel2 extends JPanel{
		Image img;
		public panel2() {
			super();
			ImageIcon back=null;
			back=new ImageIcon("table3.png");
			img=back.getImage();
		}
		public void paintComponent(Graphics page){
			super.paintComponent(page);
			page.drawImage(img,0,0,320,720,null);
		}
	}
	
	
	
	//这个主要实现消去时连线的效果，消去成功时会调用
	public class lian extends Thread{
		@Override
		public void run() {
			try 
			{
				centerPanel.repaint();
				Thread.sleep(500);
				iflian=false;
				centerPanel.repaint();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*内部静态类，主要作用就是
	 * 1，将数组转化为可解析的字符串
	 * 2，将字符串解析出来返回二维数组
	 * 使用方法
	 * 1，String a=transcode.t(int [][] b);参数为二维int型数组。
	 * 2，int[][] b=transcode.t(String a);参数为用上面方法转化出来的字符串
	 * 
	 * 这个类经过验证，无bug，并且可以将任意的二维int型数组转化，传递，并解析。
	 * */
	private static class transcode{
		//将数组转化为可解析的字符串用来传输信息
		static String t(int[][] suzus){
			String strings="";
			for (int i = 0; i < suzus.length; i++) {
				for (int j = 0; j < suzus[i].length; j++) {
					if (suzus[i].length>(j+1)) {
						strings=strings+suzus[i][j]+" ";
					}
					else {
						strings=strings+suzus[i][j];
					}
				}
				if ((i+1)<suzus.length) {
					strings=strings+"  ";
				}
			}
			return strings;
		}
		//将字符串解析出来返回二维数组
		static int[][] t(String s){
			String[] Ss=s.split("  ");
			String[] Sss=Ss[0].split(" ");
			int[][] a=new int[Ss.length][Sss.length];
			for (int i = 0; i < Ss.length; i++) {
				Sss=Ss[i].split(" ");
				for (int j = 0; j < Sss.length; j++) {
					a[i][j]=Integer.parseInt(Sss[j]);
				}
			}
			return a;
		}
	}
	
	
	//联网链接-----------------------------------------------------------------------------------------------
	public void Connect()
	{
		try {
			s=new Socket(ip,pour);
			
			dos=new DataOutputStream(s.getOutputStream());
			dis=new DataInputStream(s.getInputStream());
			
			id=dis.readInt();
			
			dos.writeUTF(id+" "+name+" "+win+" "+Level);
			if(id==5)
				dos.writeUTF("s");
//			dos.writeInt(1);
			
			
//			dos.writeUTF(id+" "+22);
//			try {
//				dos.writeUTF(id+" "+transcode.t(grid));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			isConnect=true;
			
			recieve.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void disConnect()
	{
		try {
			
			dis.close();
			dos.close();
			
			s.close();
			
		} catch (Exception e) {

			System.out.println("我不想断开~~~~~");
			e.printStackTrace();
		}
	}
	
	private class Recieve implements Runnable
	{

		public void run() 
		{
			while(isConnect)
			{
				try {
					str=dis.readUTF();
					System.out.println(str);
				} catch (IOException e) {
//					e.printStackTrace();
				}

				
				if(str.length()>100)
				{
					int t=Integer.parseInt(str.substring(0, 1));
					
					try
					{
					    if(t<id)
					    {
						    grids[t-2]=transcode.t((str.substring(2,str.length())));
						    otherpanels[t-2].setinfo(grids[t-2]);
						    otherpanels[t-2].repaint();

					    }
					    if(t>id)
					    {
						     grids[t-3]=transcode.t((str.substring(2,str.length())));
							    otherpanels[t-3].setinfo(grids[t-3]);
							    otherpanels[t-3].repaint();

					    }	
					}
					catch (Exception e)
					{
						System.out.println("收错了！！！！");
						e.printStackTrace();
					}
				}
				
				if(str.length()<100&&str.length()>6)
				{
					int t=Integer.parseInt(str.substring(0, 1));
//					if(t==id)
//					{
//						String msg[]=str.split(" ");
////						label0=new JLabel(msg[1]+"    "+msg[2]+"    "+msg[3]);
//						label0.setText(msg[1]+"    "+msg[2]+"    "+msg[3]);
//						System.out.println("111111111111111111111111111111111111111111");
//					}
					if(t!=id)
					{
						String msg[]=str.split(" ");
						if(t<id)
						{
							labels[t-2]=new JLabel(msg[1]+"\r\n\r\n"+msg[2]+"\r\n\r\n"+msg[3]);
							labels[t-2].setFont(new Font("黑体",Font.BOLD,12));
							labels[t-2].setBounds(130, -15,150,50);
							otherpanels[t-2].add(labels[t-2]);
							otherpanels[t-2].repaint();
							
						}
						if(t>id)
						{
							labels[t-3]=new JLabel(msg[1]+"\r\n\r\n"+msg[2]+"\r\n\r\n"+msg[3]);
							labels[t-3].setFont(new Font("黑体",Font.BOLD,12));
							labels[t-3].setBounds(130,-15,150,50);
							otherpanels[t-3].add(labels[t-3]);
							otherpanels[t-3].repaint();
						}
					}
				}
				
				//就绪开始
					if(str.equals("s")){
//					System.out.println("succeed in starting!");
					pause();
					try {
						Thread.sleep(id*50);
					} catch (InterruptedException e) {
					
					}
					try {
						dos.writeUTF(id+" "+(transcode.t(grid)));
					} catch (IOException e) {
	
					}
					}

				// 判斷結束否
				if (str.equals("e")) {

					int a = 0;
					for (int i = 1; i < 11; i++) {
						for (int j = 1; j < 11; j++) {
							if (grid[i][j] != 0) {
								a++;
							}
						}
					}
					if (a==0) {
						int talk1= JOptionPane.showConfirmDialog(null,"Congratulations!!!YouWin!!!");
						mainFrame.dispose();
					}else {
						int talk1= JOptionPane.showConfirmDialog(null,"Sorry!!YouLost!!!");
						mainFrame.dispose();
					}
				}

				if (str.length()==2)
					{
					//启动对外道具
					if(str.substring(1,2).equals("a"))
					{
						if(Integer.parseInt(str.substring(0,1))!=id)
						new zhetianbiri().start();
					}
					if(str.substring(1,2).equals("b"))
					{
						if(Integer.parseInt(str.substring(0,1))!=id)
						new gandengyan().start();
					}
					}
				
			}
		}
	}
	
	//音效单曲
	class AePlayWave extends Thread
	{
		private String filename;
		public AePlayWave(String name)
		{
			filename=name;
		}
		
		public void run()
		{
			File soundfile=new File(filename);
			AudioInputStream ais=null;
			
			try {
				ais=AudioSystem.getAudioInputStream(soundfile);
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			AudioFormat format=ais.getFormat();
			SourceDataLine auline=null;
			DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);/////////////////////////
			
			try {
				
				auline=(SourceDataLine) AudioSystem.getLine(info);/////////////////////////////////
				auline.open(format);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			auline.start();
			
			int n=0;
			byte [] adDate=new byte[1024];
			try {
				
				while(n!=-1)
				{
					n=ais.read(adDate,0,adDate.length);/////////////////////////////////////
					if(n>=0)
					{
						auline.write(adDate,0,n);
						
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally
			{
				auline.drain();
				auline.close();
				try {
					ais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//音效循环
	class AePlayWaveR extends Thread
	{
		private String filename;
		public AePlayWaveR(String name)
		{
			filename=name;
		}
		
		public void run()
		{
			File soundfile=new File(filename);
			AudioInputStream ais=null;
			
			try {
				ais=AudioSystem.getAudioInputStream(soundfile);
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			AudioFormat format=ais.getFormat();
			SourceDataLine auline=null;
			DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);/////////////////////////
			
			try {
				
				auline=(SourceDataLine) AudioSystem.getLine(info);/////////////////////////////////
				auline.open(format);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			auline.start();
			
			int n=0;
			byte [] adDate=new byte[1024];
			try {
				
				while(n!=-1)
				{
					n=ais.read(adDate,0,adDate.length);/////////////////////////////////////
					if(n>=0)
					{
						auline.write(adDate,0,n);
						
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally
			{
				auline.drain();
				auline.close();
				try {
					ais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			new AePlayWaveR(filename).start();
		}
	}
	
	//主方法
	public static void main(String[] args) {
		//这三个方法必须依次调用
		lianliankan llk = new lianliankan();
	}
	
	lianliankan(){
		randomBuild();
		create();
//		Connect();
//		recieve.start();
		pause();
		
		AePlayWaveR back=new AePlayWaveR("Lenka-Trouble Is A Friend.wav");
		back.start();
		
	}
}


















