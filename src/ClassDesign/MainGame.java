package ClassDesign;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 连连看游戏
 * 
 * @author Administrator 2018年06月14日
 */
public class MainGame implements ActionListener {
	JFrame mainFrame; // 主面板
	Container thisContainer;
	JPanel centerPanel, southPanel, northPanel, eastPanel; // 子面板
	JButton diamondsButton[][];// 游戏按钮数组
	Icon icon[][];// 游戏按钮图标位置
	JButton exitButton, resetButton, newlyButton, hint; // 退出，重列，重新开始,提示按钮
	JLabel fractionLable = new JLabel("0"); // 分数标签
	JButton firstButton, secondButton; // 分别记录两次被选中的按钮
	// int grid[][] = new int[8][7];// 储存游戏按钮位置
	static boolean pressInformation = false; // 判断是否有按钮被选中
	int x0 = 0, y0 = 0, x = 0, y = 0, firstMsg = 0, secondMsg = 0, validateLV; // 游戏按钮的位置坐标
	Icon iconmsg1 = new ImageIcon(), iconmsg2 = new ImageIcon();// 游戏按钮图片路径
	int i, j, k, n;// 消除方法控制
	int N;// 选择游戏难度
	int a, b;// a是纵，b是横
	JButton df1, df2, df3;// 开始按钮。难度1 2 3按钮
	int time = 90;// 时间
	JLabel timeLable, fenshu, shijian; // 时间标签
	TimeThread tt;// 线程处理
	boolean hintFlag = false;

	// 加载开始页面
	public void begin() {

		df1 = new JButton("简单");
		df2 = new JButton("一般");
		df3 = new JButton("困难");

		// 加载图片
		ImageIcon icon = new ImageIcon(
				"E:\\myec2014Code\\day00_00ClassDesign\\src\\imgs\\0.jpg");
		// 将图片放入label中
		JLabel label = new JLabel(icon);

		// 设置label的大小
		label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

		mainFrame = new JFrame("凹凸曼连连看小游戏");

		// 获取窗口的第二层，将label放入
		mainFrame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

		// 获取frame的顶层容器,并设置为透明
		JPanel j = (JPanel) mainFrame.getContentPane();
		j.setOpaque(false);

		JPanel panel = new JPanel();
		// JTextField jta=new JTextField(10);
		// JTextArea jta=new JTextArea(10,60);

		// 必须设置为透明的。否则看不到图片
		panel.setOpaque(false);
		// 放入难度按钮并添加监听
		panel.add(df1);
		panel.add(df2);
		panel.add(df3);
		df1.addActionListener(this);
		df2.addActionListener(this);
		df3.addActionListener(this);
		mainFrame.add(panel);
		// mainFrame.setSize(icon.getIconWidth(), icon.getIconHeight());
		mainFrame.setBounds(280, 100, 800, 850);
		mainFrame.setVisible(true);
		// 点击窗体关闭时同时关闭后台服务
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void init() {
		System.out.println("来到init模块");
		mainFrame = new JFrame("凸凹曼连连看小游戏");
		thisContainer = mainFrame.getContentPane();
		thisContainer.setLayout(new BorderLayout());
		centerPanel = new JPanel();
		southPanel = new JPanel();
		northPanel = new JPanel();
		eastPanel = new JPanel();
		thisContainer.add(centerPanel, "Center");
		thisContainer.add(southPanel, "South");
		thisContainer.add(northPanel, "North");
		// thisContainer.add(eastPanel, "North");
		// thisContainer.add(eastPanel, "North");
		centerPanel.setLayout(new GridLayout(a, b)); // 设置页面按钮排版布局（竖，横）个数
		for (int cols = 0; cols < a; cols++) {
			if (cols == 0) {
				System.out.println("开始填充按钮");
			}
			for (int rows = 0; rows < b; rows++) {
				// // 把随机数组的值赋入到按钮中
				// diamondsButton[cols][rows] = new JButton(
				// String.valueOf(grid[cols + 1][rows + 1]));
				// 将图片容器放入按钮中
				diamondsButton[cols][rows] = new JButton(
						icon[cols + 1][rows + 1]);
				diamondsButton[cols][rows].addActionListener(this);
				// diamondsButton[cols][rows];
				centerPanel.add(diamondsButton[cols][rows]);// 把按钮加入到面板中
			}
		}

		exitButton = new JButton("退出");
		exitButton.addActionListener(this);
		resetButton = new JButton("重列");
		resetButton.addActionListener(this);
		newlyButton = new JButton("再来一局");
		newlyButton.addActionListener(this);
		hint = new JButton("提示");
		hint.addActionListener(this);
		// 把按钮设置到指定面板上
		southPanel.add(exitButton);
		southPanel.add(resetButton);
		southPanel.add(newlyButton);
		southPanel.add(hint);
		// 文本标签
		fractionLable.setText(String.valueOf(Integer.parseInt(fractionLable
				.getText())));
		fenshu = new JLabel("分数: ");
		shijian = new JLabel("　　　　　剩余时间: ");
		northPanel.add(fenshu);
		northPanel.add(fractionLable);
		northPanel.add(shijian);
		northPanel.add(timeLable);
		mainFrame.setBounds(280, 100, 800, 850);
		mainFrame.setVisible(true);
		// 点击窗体关闭时同时关闭后台服务
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("到了启动线程前");
		if (tt == null) {
			System.out.println("启动线程");
			tt = new TimeThread();
			tt.start();
		}
		System.out.println("init结束");
	}

	// 生成随机数组 再生成位置
	public void randomBuild() {
		System.out.println("来到随机生成模块");
		String path = "E:\\myec2014Code\\day00_00ClassDesign\\src\\imgs\\";
		int randoms, cols, rows;
		for (int twins = 1; twins <= N; twins++) {
			randoms = (int) (Math.random() * 30 + 1);
			for (int alike = 1; alike <= 2; alike++) {
				cols = (int) (Math.random() * a + 1); // 1--6
				rows = (int) (Math.random() * b + 1);
				// while (grid[cols][rows] != 0) {
				// cols = (int) (Math.random() * 6 + 1);
				// rows = (int) (Math.random() * 5 + 1);
				// }
				while (this.icon[cols][rows] != null) {
					cols = (int) (Math.random() * a + 1);
					rows = (int) (Math.random() * b + 1);
				}
				// this.grid[cols][rows] = randoms;
				this.icon[cols][rows] = new ImageIcon(path + randoms + ".jpg");
			}
		}
		System.out.println("随机生成完成");

	}

	// 计分板
	public void fraction() {
		fractionLable.setText(String.valueOf(Integer.parseInt(fractionLable
				.getText()) + 100));
		time = time + 3;
		if (N * 100 == Integer.parseInt(fractionLable.getText())) {

			mainFrame.setVisible(false);
			begin();
			JOptionPane.showMessageDialog(null, "恭喜你胜利了!!!", "胜利",
					JOptionPane.INFORMATION_MESSAGE);

		}
	}

	// 计时板
	public void timeout() {
		// System.out.println("timeout中的 :  " + time);
		timeLable.setText(String.valueOf(time));
	}

	// 重新排序 重列
	public void reload() {
		// int save[] = new int[30];
		Icon copy[] = new ImageIcon[2 * N];
		int n = 0, cols, rows;
		// int grid[][] = new int[8][7];
		Icon icon[][] = new ImageIcon[a + 2][b + 2];
		// 取出剩下的方块
		for (int i = 0; i <= a; i++) {
			for (int j = 0; j <= b; j++) {
				// if (this.grid[i][j] != 0) { // 若不等于0，则取出值
				// save[n] = this.grid[i][j]; // 把剩下的方块 存到save数组中
				// n++;
				// }
				if (this.icon[i][j] != null) { // 若不等于0，则取出值
					copy[n] = this.icon[i][j]; // 把剩下的方块 存到save数组中
					n++;
				}
			}
		}
		n = n - 1;
		// this.grid = grid; // 保留下来的值
		this.icon = icon;
		// 将save中的方块进行随机打乱
		while (n >= 0) {
			cols = (int) (Math.random() * a + 1);
			rows = (int) (Math.random() * b + 1);
			// while (grid[cols][rows] != 0) {
			// cols = (int) (Math.random() * 6 + 1);
			// rows = (int) (Math.random() * 5 + 1);
			// }
			while (icon[cols][rows] != null) {
				cols = (int) (Math.random() * a + 1);
				rows = (int) (Math.random() * b + 1);
			}
			this.icon[cols][rows] = copy[n];
			n--;
		}
		mainFrame.setVisible(false);
		pressInformation = false; // 这里一定要将按钮点击信息归为初始，若没有这个操作，则重列之后会有bug
		init(); // 调用初始化方法重新加载面板内容

		// 将等于0的方块设置不可见
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				// if (grid[i + 1][j + 1] == 0)
				// diamondsButton[i][j].setVisible(false);
				if (icon[i + 1][j + 1] == null)
					diamondsButton[i][j].setVisible(false);
			}
		}
		time = time - 10;
	}

	// 记录按钮数据并代用判断方法
	public void estimateEven(int placeX, int placeY, JButton bz) {
		if (pressInformation == false) {// 判断是否有按钮被按中
			x = placeX;
			y = placeY;
			// secondMsg = grid[x][y];
			iconmsg2 = icon[x][y];
			secondButton = bz;
			pressInformation = true;
			System.out.println("第一次点击的时候--" + iconmsg1);
		} else {
			// 先用第上一次点的按钮的数据存入到第一组 判断条件中
			x0 = x;
			y0 = y;
			// firstMsg = secondMsg;
			iconmsg1 = iconmsg2;
			firstButton = secondButton;
			// 保存本次点击的按钮的数据 到第二组判断条件中
			x = placeX;
			y = placeY;
			// secondMsg = grid[x][y];
			iconmsg2 = icon[x][y];
			secondButton = bz;
			System.out.println(iconmsg1 + "-----" + iconmsg2);
			System.out.println(iconmsg1.equals(iconmsg2));
			System.out.println(firstButton != secondButton);
			System.out.println();
			if (iconmsg1.toString().equals(iconmsg2.toString())
					&& secondButton != firstButton) { // 判断两次点击的按钮
				// 值是否相同
				// 和是否为同一个按钮
				xiao();
			}
		}
	}

	public void hint() {
		hintFlag = false;
		for (int i = 0; i < a + 2; i++) {
			for (int j = 0; j < b + 2; j++) {
				if (icon[i][j] != null) {
					for (int k = i; k < a + 2; k++) {
						System.out.println("在提示的循环中");
						for (int l = (k == i ? j + 1 : 0); l < b + 2; l++) {
							if (icon[k][l] != null) {
								if (icon[i][j].toString().equals(
										icon[k][l].toString())) {
									x0 = i;
									y0 = j;
									x = k;
									y = l;
									firstButton = diamondsButton[i - 1][j - 1];
									secondButton = diamondsButton[k - 1][l - 1];
									xiao();
									if (hintFlag) {
										time = time - 5;
										hintFlag = false;
										return;
									}
								}
							}
						}

					}

				}

			}
		}

	}

	/**
	 * 1、先分别判断两次点击的按钮同行之间有无通路 若各同行间有通路，则继续判断在满足通路的列（该列直线到按钮是通路）下是否有通路到第二按钮的行数
	 * 2、先分别判断两次点击的按钮同列之间有无通路 若各同列间有通路，则继续判断在满足通路的行（该行直线到按钮是通路）下是否有通路到第二按钮的列数
	 */
	public void xiao() { // 相同的情况下能不能消去。仔细分析，不一条条注释
		System.out.println("进入到消去方法");
		if ((x0 == x && (y0 == y + 1 || y0 == y - 1))
				|| ((x0 == x + 1 || x0 == x - 1) && (y0 == y))) { // 判断是否相邻
			remove();
			return;
		} else {
			for (j = 0; j < b + 2; j++) {
				if (icon[x0][j] == null) { // 判断第一个按钮同行哪个按钮为空
					// 判断第二按钮的左边有无通路
					if (y > j) { // 如果第二个按钮的Y坐标大于空按钮的Y坐标说明第一按钮在第二按钮左边
						for (i = y - 1; i >= j; i--) { // 判断第二按钮左侧直到第一按钮中间有没有按钮
							// if (grid[x][i] != 0) {
							if (icon[x][i] != null) {
								k = 0;
								break;
							} else {
								k = 1;
							} // K=1说明通过了第一次验证
						}
						if (k == 1) {
							linePassOne();
						}
					}
					// 判断第二按钮的右边有无通路
					if (y < j) { // 如果第二个按钮的Y坐标小于空按钮的Y坐标说明第一按钮在第二按钮右边
						for (i = y + 1; i <= j; i++) { // 判断第二按钮左侧直到第一按钮中间有没有按钮
							// if (grid[x][i] != 0) {
							if (icon[x][i] != null) {
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
					if (y == j) {
						linePassOne();
					}
				}
				// 二次验证通过时，进行判断列方向上是否有通路
				if (k == 2) {
					if (x0 == x) { // x0 和 x 在同一行上
						remove();
						return;
					}
					if (x0 < x) { // x0 在 x 的下面的行上
						// 判断该列（x0 的行 至 x 的行之间）上是否为通路
						for (n = x0; n <= x - 1; n++) {
							// if (grid[n][j] != 0) {
							if (icon[n][j] != null) {
								k = 0;
								break;
							}
							// if (grid[n][j] == 0 && n == x - 1) {
							if (icon[n][j] == null && n == x - 1) {
								remove();
								return;
							}
						}
					}
					if (x0 > x) { // x0 在 x 的上面的行上
						// 判断该列（x0 的行 至 x 的行之间）上是否为通路
						for (n = x0; n >= x + 1; n--) {
							// if (grid[n][j] != 0) {
							if (icon[n][j] != null) {
								k = 0;
								break;
							}
							// if (grid[n][j] == 0 && n == x + 1) {
							if (icon[n][j] == null && n == x + 1) {
								remove();
								return;
							}
						}
					}
				}
			}

			// 2、先从列数开始判断，二次判断为行是否连通，原理如上
			for (i = 0; i < a + 2; i++) { // 列
				// if (grid[i][y0] == 0) {
				if (icon[i][y0] == null) {
					if (x > i) {
						for (j = x - 1; j >= i; j--) {
							// if (grid[j][y] != 0) {
							if (icon[j][y] != null) {
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
					if (x < i) {
						for (j = x + 1; j <= i; j++) {
							// if (grid[j][y] != 0) {
							if (icon[j][y] != null) {
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
					if (x == i) {
						rowPassOne();
					}
				}
				if (k == 2) {
					if (y0 == y) {
						remove();
						return;
					}
					if (y0 < y) {
						for (n = y0; n <= y - 1; n++) {
							// if (grid[i][n] != 0) {
							if (icon[i][n] != null) {
								k = 0;
								break;
							}
							// if (grid[i][n] == 0 && n == y - 1) {
							if (icon[i][n] == null && n == y - 1) {
								remove();
								return;
							}
						}
					}
					if (y0 > y) {
						for (n = y0; n >= y + 1; n--) {
							// if (grid[i][n] != 0) {
							if (icon[i][n] != null) {
								k = 0;
								break;
							}
							// if (grid[i][n] == 0 && n == y + 1) {
							if (icon[i][n] == null && n == y + 1) {
								remove();
								return;
							}
						}
					}
				}
			}
		}
	}

	// 判断同行之间有无通路
	public void linePassOne() {
		if (y0 > j) { // 第一按钮同行空按钮在右边，判断左边边有无通路
			for (i = y0 - 1; i >= j; i--) { // 判断第一按钮同左侧空按钮之间有没按钮
				// if (grid[x0][i] != 0) {
				if (icon[x0][i] != null) {
					k = 0;
					break;
				} else {
					k = 2;
				} // K=2说明通过了第二次验证
			}
		}
		if (y0 < j) { // 第一按钮同行空按钮在与第二按钮之间
			for (i = y0 + 1; i <= j; i++) {
				// if (grid[x0][i] != 0) {
				if (icon[x0][i] != null) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}
	}

	// 判断同列之间是否为通路
	public void rowPassOne() {
		if (x0 > i) {
			for (j = x0 - 1; j >= i; j--) {
				// if (grid[j][y0] != 0) {
				if (icon[j][y0] != null) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}
		if (x0 < i) {
			for (j = x0 + 1; j <= i; j++) {
				// if (grid[j][y0] != 0) {
				if (icon[j][y0] != null) {
					k = 0;
					break;
				} else {
					k = 2;
				}
			}
		}
	}

	// 消除方法
	public void remove() {
		// 设置第一第二按钮不可见
		firstButton.setVisible(false);
		secondButton.setVisible(false);
		fraction();// 调用计分板方法进行加分
		pressInformation = false;// 恢复未选中按钮状态，清除之前点击按钮的数据
		k = 0;// 使验证步骤恢复到0状态
		// 消去方块数据
		// grid[x0][y0] = 0;
		// grid[x][y] = 0;
		icon[x0][y0] = null;
		icon[x][y] = null;
		hintFlag = true;
	}

	// 监听器
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == df1) {
			N = 15;
			a = 6;
			b = 5;
			diamondsButton = new JButton[a + 2][b + 2];
			icon = new ImageIcon[a + 2][b + 2];
			df1.setVisible(false);
			df2.setVisible(false);
			df3.setVisible(false);
			time = 45;
			timeLable = new JLabel(String.valueOf(time));
			mainFrame.setVisible(false);
			randomBuild();
			init();
			fractionLable.setText("0");
		}
		if (e.getSource() == df2) {
			N = 21;
			a = 7;
			b = 6;
			diamondsButton = new JButton[a + 2][b + 2];
			icon = new ImageIcon[a + 2][b + 2];
			df1.setVisible(false);
			df2.setVisible(false);
			df3.setVisible(false);
			time = 45;
			timeLable = new JLabel(String.valueOf(time));
			mainFrame.setVisible(false);
			randomBuild();
			init();
			fractionLable.setText("0");
		}
		if (e.getSource() == df3) {
			N = 50;
			a = 10;
			b = 10;
			diamondsButton = new JButton[a + 2][b + 2];
			icon = new ImageIcon[a + 2][b + 2];
			df1.setVisible(false);
			df2.setVisible(false);
			df3.setVisible(false);
			time = 45;
			timeLable = new JLabel(String.valueOf(time));
			mainFrame.setVisible(false);
			randomBuild();
			init();
			fractionLable.setText("0");
		}

		if (e.getSource() == newlyButton) { // 重新开始 监听
			System.out.println("重新开始");

			// 创建一个空白数组覆盖原来的数组
			// int grid[][] = new int[8][7];
			// this.grid = grid;
			Icon icon[][] = new ImageIcon[a + 2][b + 2];
			this.icon = icon;
			// 重新生成一个随机数组
			time = 45;
			randomBuild();
			mainFrame.setVisible(false);
			pressInformation = false;
			fractionLable.setText("0");

			init();

		}
		if (e.getSource() == exitButton) {// 退出 监听
			System.exit(0);
		}

		if (e.getSource() == resetButton) { // 重新排列 监听

			reload();

		}
		if (e.getSource() == hint) { // 提示按钮

			hint();

		}

		// 若不是上面的三个按钮，则 判断是否为 方块按钮的其中一个
		for (int cols = 0; cols < a; cols++) {
			for (int rows = 0; rows < b; rows++) {
				if (e.getSource() == diamondsButton[cols][rows])// 若是，则调用记录按钮数据的方法
					estimateEven(cols + 1, rows + 1, diamondsButton[cols][rows]);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		MainGame mg = new MainGame();
		mg.begin();
		// mg.randomBuild();
		// mg.init();

		/*
		 * while (true) { System.out.println("------begin"); if (mg.flag ==
		 * true) { new Thread() { public void run() { MainGame mg = new
		 * MainGame(); try { Thread.sleep(1000); } catch (InterruptedException
		 * e) { e.printStackTrace(); } System.out.println(mg.time); while
		 * (mg.time > 0) { mg.time--; mg.timeout(); } } }.start(); } }
		 */

	}

	public class TimeThread extends Thread {

		@Override
		public void run() {

			System.out.println("进入线程中了");
			time--;
			System.out.println(time);
			while (time > 0) {
				// System.out.println("循环中");
				// System.out.println(time);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeout();
				time--;
			}
			// 时间到弹窗
			mainFrame.setVisible(false);
			begin();
			// 弹窗
			JOptionPane.showMessageDialog(null, "时间到，你输了!!!  你的得分是 : "
					+ fractionLable.getText(), "失败",
					JOptionPane.INFORMATION_MESSAGE);
			tt = null;

			System.out.println("循环外面");
		}

	}

}
