
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Planet {
	private int _x;
	private int _y;
	private int _strong;

	private Image _none;
	private Image _player;
	private Image _computer;

	private Image _select;
	private boolean _selected;

	private int _state;

	ActionListener taskInc = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (_state != 0)
				_strong++;
		}
	};

	public Planet(int state, int x, int y, int strong, int intensity) {
		this.setState(state);

		_x = x;
		_y = y;
		_strong = strong;

		_none = new ImageIcon("base/textures/planetW.png").getImage();
		_player = new ImageIcon("base/textures/planetG.png").getImage();
		_computer = new ImageIcon("base/textures/planetR.png").getImage();

		ImageIcon selImg = new ImageIcon("base/textures/planetS.png");
		_select = selImg.getImage();

		new Timer(intensity, taskInc).start();
	}

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		_x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		_y = y;
	}

	public int getStrong() {
		return _strong;
	}

	public void setStrong(int strong) {
		this._strong = strong;
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean selected) {
		_selected = selected;
	}

	public void draw(Graphics g, Engine e) {
		if (_selected)
			g.drawImage(_select, _x, _y, e);
		else if (_state == 0)
			g.drawImage(_none, _x, _y, e);
		else if (_state == 1)
			g.drawImage(_player, _x, _y, e);
		else if (_state == 2)
			g.drawImage(_computer, _x, _y, e);
	}

	public int getState() {
		return _state;
	}

	public void setState(int state) {
		this._state = state;
	}
}
