package SinglePlay;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


public class llkdjb implements ActionListener {
    static JProgressBar jpb=null;//游戏时间控制条
    timeprocess process;
    JButton spause;//游戏暂停、继续功能
	JFrame mainFrame; // 主面板
	panel1 centerPanel;
	panel2 saidPanel; // 子面板
	JButton diamondsButton[][] = new JButton[10][10];// 游戏按钮数组
	JButton firstButton, secondButton; // 分别记录两次被选中的按钮
	JButton remarkButton, newlyButton, startButton,recommendbButton;// 重列，重新,开始|暂停,提示按钮按钮
	public byte grid[][] = new byte[12][12];
	
	static boolean pressInformation = false; /* 判断是否有按钮被选中*/boolean iflian=true;//连线
	static boolean pos=true;//继续暂停控制逻辑
	static boolean gameo=false;//游戏结束逻辑
	boolean keypressed=false;
	int x0 = 0, y0 = 0, x = 0, y = 0, fristMsg = 0, secondMsg = 0; // 游戏按钮的位置坐标
	int r0=0,t0=0,r=0,t=0;
	int i, j, k, n;// 消除方法控制

	
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

	public void AddGif() {
		for (int cols = 0; cols < 10; cols++) {
			for (int rows = 0; rows < 10; rows++) {
				diamondsButton[cols][rows] = new JButton(new ImageIcon(
						String.valueOf(grid[cols + 1][rows + 1]) + ".gif"));
				diamondsButton[cols][rows].setBounds(60*rows+60,60*cols+60,59,59);
				diamondsButton[cols][rows].addActionListener(this);
				diamondsButton[cols][rows].setBorder(null);
				diamondsButton[cols][rows].setContentAreaFilled(false);
				centerPanel.add(diamondsButton[cols][rows]);
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
	void healicons(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				diamondsButton[i][j].setIcon(new ImageIcon(String.valueOf(grid[i+1][j+1]+".gif")));
			}
		}
	}

	public void create(){
		
		AePlayWaveR backAePlayWaveR=new AePlayWaveR("Beauty And A Beat.wav");
		backAePlayWaveR.start();
		
		mainFrame = new JFrame("连连看");
		mainFrame.setLayout(null);
		centerPanel = new panel1();
		centerPanel.setBackground(Color.green);
		saidPanel = new panel2();
		saidPanel.setLayout(null);
		saidPanel.setBackground(Color.yellow);

		centerPanel.setLayout(null);
		//实例化timebar
        jpb=new JProgressBar();
        jpb.setOrientation(JProgressBar.VERTICAL);
        jpb.setMaximum(1000);jpb.setMinimum(0);jpb.setValue(1000);
        jpb.setStringPainted(false);
        jpb.setForeground(Color.red);
        jpb.setBounds(720, 0, 10, 720);
        

        //暂停/继续控制按钮
        spause=new JButton(new ImageIcon(
				String.valueOf("暂停.gif")));
        spause.setBorder(null);
        spause.setContentAreaFilled(false);
        spause.addActionListener(this);
        
        //提示按钮
        recommendbButton=new JButton();
        recommendbButton.setIcon(new ImageIcon("提示.gif"));
        recommendbButton.setBorder(null);
        recommendbButton.setContentAreaFilled(false);
        recommendbButton.addActionListener(this);
        
        
		AddGif();

		remarkButton = new JButton();
		remarkButton.setIcon(new ImageIcon("重列.gif"));
		remarkButton.setBorder(null);
		remarkButton.setContentAreaFilled(false);
		remarkButton.addActionListener(this);
		newlyButton = new JButton();
		newlyButton.setIcon(new ImageIcon("重玩.gif"));
		newlyButton.setBorder(null);
		newlyButton.setContentAreaFilled(false);
		newlyButton.addActionListener(this);

		mainFrame.setBounds(180, 0, 1066, 740);
		mainFrame.add(centerPanel);
		mainFrame.add(jpb);
		mainFrame.add(saidPanel);
		saidPanel.add(remarkButton);
		saidPanel.add(newlyButton);
		saidPanel.add(spause);
		saidPanel.add(recommendbButton);
		
		
		remarkButton.setBounds(55, 400, 96, 60);
		newlyButton.setBounds(170, 450, 96, 60);
		spause.setBounds(55, 300, 96, 60);
		recommendbButton.setBounds(170, 350, 96, 60);
		saidPanel.setBounds(730, 0, 320, 720);
		centerPanel.setBounds(0, 0, 720, 720);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		process=new timeprocess();
		process.start();
	}

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

	public void reload() {
		byte save[] = new byte[100];
		int n = 0, cols, rows;
		byte grid[][] = new byte[12][12];
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

	public void estimateEven(int placeX, int placeY, JButton bz) {
		if (pressInformation == false) {
			x = placeX;
			y = placeY;
			diamondsButton[x-1][y-1].setIcon(new ImageIcon(String.valueOf((-grid[x][y]) + ".gif")));
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
			diamondsButton[x-1][y-1].setIcon(new ImageIcon(String.valueOf((-grid[x][y]) + ".gif")));
			if (x!=x0||y!=y0)
				diamondsButton[x0-1][y0-1].setIcon(new ImageIcon(String.valueOf((grid[x0][y0]) + ".gif")));
			secondMsg = grid[x][y];
			secondButton = bz;
			if (fristMsg == secondMsg && (x0!=x||y0!=y)) {
				if (xiao()) {
					remove();
				}
			}
		}
	}
	
	public class tishi extends Thread{
		public void run() {
			super.run();
			diamondsButton[x0-1][y0-1].setVisible(false);
			diamondsButton[x-1][y-1].setVisible(false);
			try {
				sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			diamondsButton[x0-1][y0-1].setVisible(true);
			diamondsButton[x-1][y-1].setVisible(true);
		
		}
	}
	
	
	//提示
	void recommend(){
		if (gameo) {
			diamondsButton[x-1][y-1].setIcon(new ImageIcon(String.valueOf((grid[x][y]) + ".gif")));
		}
		if(find()){
			new tishi().start();
		}
	}
	//找到
	boolean find(){
		int temp;
		for (int kx = 1; kx <= 10; kx++) {
			for (int ky = 1; ky <= 10; ky++) {
				temp=ky;
				for (int lx = kx; lx <=10; lx++) {
					for (int ly = temp+1; ly <= 10; ly++) {
						temp=0;
						x0=kx;
						y0=ky;
						x=lx;
						y=ly;
						pressInformation=false;
						if (grid[x0][y0]==grid[x][y]&&xiao()&&grid[x0][y0]!=0) {
							return true;
						}
					}
					temp=0;
				}
			}
		}
		return false;
	}
//改.....................................................
	public boolean xiao() {
		if ((x0 == x && (y0 == y + 1 || y0 == y - 1))
				|| ((x0 == x + 1 || x0 == x - 1) && (y0 == y))) { // 判断是否相邻
//			remove();
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
//						remove();
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
//								remove();
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
//									remove();
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
//									remove();
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
//								remove();
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
//								remove();
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

	public void remove() {
		firstButton.setVisible(false);
		secondButton.setVisible(false);
		pressInformation = false;
		k = 0;
		if (grid[x0][y0]==5||grid[x0][y0]==-5) {
			process.addx(5000);
		}
		new lian().start();
		grid[x0][y0] = 0;
		grid[x][y] = 0;
//		centerPanel.repaint();
		iflian=true;
		process.addx(1000);
		iswin();
		
		AePlayWave ok=new AePlayWave("ok.wav");
		ok.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==recommendbButton&&!gameo) {
			recommend();
		}
		if (e.getSource()==spause&&!gameo) {
			pos=!pos;
			if (pos) {
				healicons();
			}
			else {
				pauseicons();
			}
		}
		if (e.getSource() == newlyButton) {
			randomBuild();
			centerPanel.removeAll();
			AddGif();
			mainFrame.show();
			centerPanel.repaint();
			process.setx(1000);
			if (gameo) {
				process=new timeprocess();
				process.start();
			}
			gameo=false;
			pos=true;
		}
		if (e.getSource() == remarkButton&&!gameo)
			reload();
		for (int cols = 0; cols < 10; cols++) {
			for (int rows = 0; rows < 10; rows++) {
				if (e.getSource() == diamondsButton[cols][rows]&&gameo==false&&pos==true){
					estimateEven(cols + 1, rows + 1, diamondsButton[cols][rows]);
					keypressed=true;
					AePlayWave click=new AePlayWave("click.wav");
					click.start();
				}
			}
		}
	}
	
	class panel1 extends JPanel{
		
//		class panel1 extends JPanel{
			Image img;
			public panel1() {
				super();
				ImageIcon back=null;
				back=new ImageIcon("test.gif");
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
			back=new ImageIcon("table2.png");
			img=back.getImage();
		}
		public void paintComponent(Graphics page){
			super.paintComponent(page);
			page.drawImage(img,0,0,320,720,null);
		}
	}

	
	public class lian extends Thread{
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
	
	void iswin(){
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++){
				if (grid[i][j]!=0) 
					return;
			}
		}
		int talk1 = JOptionPane.showConfirmDialog(null,
				"YouWin!!!!"
						+ "\nDo You Want To Play Again?");
		if (talk1==JOptionPane.YES_OPTION) {
			randomBuild();
			centerPanel.removeAll();
			AddGif();
			mainFrame.show();
			centerPanel.repaint();
			process.setx(1000);
			if (gameo) {
				process=new timeprocess();
				process.start();
			}
			gameo=false;
			pos=true;
		}
		if (talk1==JOptionPane.NO_OPTION) {
			mainFrame.dispose();
		}
	}
	
	public class timeprocess extends Thread{
		int iprocess=1000;
		public void run(){
			while (iprocess>0){
				iprocess--;
				try {
					sleep(60);
					jpb.setValue(iprocess);
					while (pos==false) {
						sleep(100);
					}
				} catch (InterruptedException ie) {
				}
			}
			gameo=true;
			pauseicons();
			int talk1 = JOptionPane.showConfirmDialog(null,
					"GameOver!!!!"
							+ "\nDo You Want To Play Again?");
			if (talk1==JOptionPane.YES_OPTION) {
				randomBuild();
				centerPanel.removeAll();
				AddGif();
				mainFrame.show();
				centerPanel.repaint();
				process.setx(10);
				if (gameo) {
					process=new timeprocess();
					process.start();
				}
				gameo=false;
				pos=true;
			}
			if (talk1==JOptionPane.NO_OPTION) {
				mainFrame.dispose();
			}
			
		}
		
		public void setx(int i){
			this.iprocess=i;
		}
		
		public void addx(int time){
			iprocess+=time/100;
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
	
		llkdjb(){
			randomBuild();
			create();
		}
		public static void main(String[] arg){
			new llkdjb();
		}
}