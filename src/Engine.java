
/** * * * Gergely Boross - 2012_11_25 * * * * * * * * * * * * *\
*    _ _____   _____        __ _                              *
*   (_)  __ \ / ____|      / _| |                             *
*   |_| |__)| (___    ___ | |_| |___      ____ _ _ __ ___     *
*   | |  _  / \___ \ / _ \|  _| __\ \ /\ / / _` | '__/ _ \    *
*   | | | \ \ ____) | (_) | | | |_ \ V  V / (_| | | |  __/    *
*   |_|_|  \_\_____/ \___/|_|  \__| \_/\_/ \__,_|_|  \___|    *
*                                                             *
*                http://irsoftware.darktl.com                 *
*                                                             *
*              contact_adress: sk8Geri@gmail.com               *
*                                                               *
*       This file is a part of the work done by aFagylaltos.     *
*         You are free to use the code in any way you like,      *
*         modified, unmodified or copied into your own work.     *
*        However, I would like you to consider the following:    *
*                                                               *
*  -If you use this file and its contents unmodified,         *
*              or use a major part of this file,               *
*     please credit the author and leave this note untouched.   *
*  -If you want to use anything in this file commercially,      *
*                please request my approval.                    *
*                                                              *
\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

//ctrl + shift +o

public class Engine extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final int width = 854;
	private final int height = 480;

	private boolean _inGame = false;
	private boolean _winner = false;
	private boolean _run = false;

	private Timer _timer;
	private Image _space;

	private Base _base;

	private Image _background;
	private Image _highScore_bg;

	private boolean _levelSeletScreen = false;
	private boolean _enableScoreScreen = false;
	private boolean _pause = false;

	private ArrayList<Button> _buttons;
	private ArrayList<String> _levels;
	private String _name;
	private int _selected = 0;

	private ArrayList<HighscoreManager> _highScores = new ArrayList<HighscoreManager>();

	GameTime _gameTime = new GameTime();

	void start(int index) {
		_inGame = true;
		_levelSeletScreen = false;

		_base.newGame("base/configs/level" + index + ".map");
		_gameTime.reset();
	}

	ActionListener startButton = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			System.out.println("START");
			_levelSeletScreen = true;
		}
	};

	ActionListener recordButton = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			System.out.println("RECORD");
			_enableScoreScreen = true;
		}
	};

	ActionListener exitButton = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			System.out.println("EXIT");
			System.exit(0);
		}
	};

	public Engine() {
		setBackground(Color.black);

		ImageIcon imageBg = new ImageIcon("base/textures/bg.jpg");
		_space = imageBg.getImage();

		ImageIcon scoreBg = new ImageIcon("base/textures/high_score.png");
		_highScore_bg = scoreBg.getImage();

		_levels = new ArrayList<String>();
		_name = "player";

		try {
			FileInputStream fstream = new FileInputStream("base/configs/maps.list");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			int i = 0;
			while ((strLine = br.readLine()) != null) {
				_levels.add(strLine);
				_highScores.add(new HighscoreManager(++i));
				_highScores.get(i - 1).loadScoreFile();
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		_base = new Base();

		addKeyListener(new TAdapter());
		addMouseListener(this);
		addMouseMotionListener(this);

		setFocusable(true);

		_timer = new Timer(25, this);
		_timer.start();

		new Timer(5000, taskAI).start();

		ImageIcon imageMenuBg = new ImageIcon("base/textures/menu_bg.jpg");
		_background = imageMenuBg.getImage();

		// START x:404 y:184 140x74
		// ABOUT x:385 y:276 182x81
		// EXIT x:426 y:382 93x68

		_buttons = new ArrayList<Button>();
		_buttons.add(
				new Button("base/textures/menu_start.png", "base/textures/menu_start_s.png", 404, 184, startButton));

		_buttons.add(
				new Button("base/textures/menu_record.png", "base/textures/menu_record_s.png", 385, 276, recordButton));

		_buttons.add(new Button("base/textures/menu_exit.png", "base/textures/menu_exit_s.png", 426, 382, exitButton));
	}

	ActionListener taskAI = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (_run)
				_base.AI();
		}
	};

	public void paint(Graphics g) {
		super.paint(g);

		Font font = new Font("Helvetica", Font.BOLD, 52);
		Font small = new Font("Helvetica", Font.BOLD, 20);
		FontMetrics metr = this.getFontMetrics(font);
		g.setColor(Color.white);
		g.setFont(font);

		if (_inGame) {

			if (_run = checkGameOver()) {

				if (_pause) {
					g.drawString("Paused", 300, 200);
				} else {
					g.drawImage(_space, 0, 0, this);
					_base.draw(g, this);

					int msecs = (int) _gameTime.time();
					int hours = msecs / (1000 * 60 * 60);
					int minutes = (msecs - (hours * 1000 * 60 * 60)) / (1000 * 60);
					int seconds = (msecs - (minutes * 1000 * 60) - (hours * 1000 * 60 * 60)) / 1000;
					int milliseconds = msecs - (seconds * 1000) - (minutes * 1000 * 60) - (hours * 1000 * 60 * 60);

					g.setColor(Color.white);
					g.setFont(small);

					String msg = String.format("Time: %d:%d:%d", minutes, seconds, milliseconds / 10);
					g.drawString(msg, 10, 30);
				}

			} else {
				gameOver(g);
			}
		} else {
			if (_enableScoreScreen) {
				g.drawImage(_highScore_bg, 0, 0, this);

				g.setColor(Color.white);
				g.setFont(font);

				g.drawString("$$HighScores | level" + (_selected + 1) + "$$", 150, 50);

				if (_highScores.get(_selected).getExistFile()) {
					try {
						ArrayList<Score> scores;
						scores = _highScores.get(_selected).getScores();

						for (int i = 0; i < scores.size() && i < 5; i++) {

							String msg = scores.get(i).getName() + " - " + scores.get(i).getScore();
							g.drawString(msg, 300, 150 + i * 50);
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					String msg = "Nincs HighScore lista meg!";

					g.drawString(msg, (width - metr.stringWidth(msg)) / 2, height / 2);
				}

			} else if (_levelSeletScreen) {
				g.drawImage(_highScore_bg, 0, 0, this);
				g.drawString("Select level", 300, 50);

				for (int i = 0; i < _levels.size(); i++) {
					if (_selected == i)
						g.setColor(Color.red);
					else
						g.setColor(Color.white);

					g.drawString(_levels.get(i), 300, 150 + i * 50);
				}

			} else {
				g.drawImage(_background, 0, 0, this);

				for (int i = 0; i < _buttons.size(); i++) {
					_buttons.get(i).Draw(g, this);
				}
			}

		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private boolean checkGameOver() {
		if (_base.getWin()) {
			_winner = true;
			return false;
		}

		if (_base.getGameOver()) {
			_winner = false;
			return false;
		}
		return true;
	}

	public void gameOver(Graphics g) {
		String msg = _winner ? "Enter your name" : "Game Over";

		Font small = new Font("Helvetica", Font.BOLD, 32);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (width - metr.stringWidth(msg)) / 2, height / 2);

		if (_winner)
			g.drawString(_name, 300, 200);

	}

	public void actionPerformed(ActionEvent e) {
		repaint();

	}

	private class TAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (_inGame) {
				if ((key == KeyEvent.VK_ESCAPE))
					_inGame = false;
				else if (key == KeyEvent.VK_P) {
					_pause = !_pause;
					_gameTime.pause(_pause);
				}

				if (!_run) {
					if (_winner) {
						if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z) {

							if (_name.length() < 15)
								_name += e.getKeyChar();

						} else if (key == KeyEvent.VK_BACK_SPACE) {
							if (_name.length() > 0) {
								_name = _name.substring(0, _name.length() - 1);
							}
						} else if (key == KeyEvent.VK_ENTER) {
							try {
								_highScores.get(_selected).addScore(_name, (int) _gameTime.time(TimeUnit.SECONDS));
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							_inGame = false;
						}
					} else {
						if (key == KeyEvent.VK_ENTER) {
							start(_selected + 1);
						}
					}
				}
			} else {

				if (key == KeyEvent.VK_ESCAPE)
					if (_enableScoreScreen)
						_enableScoreScreen = false;
					else if (_levelSeletScreen)
						_levelSeletScreen = false;
					else
						System.exit(0);

				if (_levelSeletScreen) {
					if (key == KeyEvent.VK_UP) {
						if (_selected > 0)
							_selected--;
					} else if (key == KeyEvent.VK_DOWN) {
						int max = _levels.size() - 1;
						if (_selected < max)
							_selected++;
					} else if (key == KeyEvent.VK_ENTER) {
						if (_levelSeletScreen)
							start(_selected + 1);
					}
				} else if (_enableScoreScreen) {
					if (key == KeyEvent.VK_LEFT) {
						if (_selected > 0)
							_selected--;
					} else if (key == KeyEvent.VK_RIGHT) {
						int max = _levels.size() - 1;
						if (_selected < max)
							_selected++;
					}
				} else {
					boolean enter = false;
					if (key == KeyEvent.VK_UP) {
						if (_selected > 0)
							_selected--;
					} else if (key == KeyEvent.VK_DOWN) {
						if (_selected < 2)
							_selected++;
					} else if (key == KeyEvent.VK_ENTER) {
						enter = true;
					}
					for (int i = 0; i < _buttons.size(); i++) {
						if (i == _selected)
							_buttons.get(i).KeyPressd(true, enter);
						else
							_buttons.get(i).KeyPressd(false, false);
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (_levelSeletScreen) {
			for (int j = 0; j < _levels.size(); j++) {

				int xx = 300;
				int yy = 150 + (j - 1) * 50;

				if (x > xx && x < xx + 300 && y > yy && y < yy + 50) {
					_selected = j;

					start(_selected + 1);
					// System.out.println("selected: " + selected);
				}
			}
		}

		if (_inGame)
			_base.Touch(x, y, e.getButton());
		else if (!_enableScoreScreen && !_levelSeletScreen) {
			for (int i = 0; i < _buttons.size(); i++) {
				_buttons.get(i).Touch(x, y);
			}
		}

		// START x:404 y:184 140x74
		// ABOUT x:385 y:276 182x81
		// EXIT x:426 y:382 93x68
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (!_inGame) {
			for (int i = 0; i < _buttons.size(); i++) {
				_buttons.get(i).Move(x, y);
			}

			if (_levelSeletScreen) {
				for (int j = 0; j < _levels.size(); j++) {

					int xx = 300;
					int yy = 150 + (j - 1) * 50;

					if (x > xx && x < xx + 300 && y > yy && y < yy + 50) {
						_selected = j;
						// System.out.println("selected: " + selected);
					}
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

}
