
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

public class Button {

	private Image _background;
	private Image _select;
	private boolean _selected;

	private int _x;
	private int _y;

	private int _width;
	private int _height;

	ActionListener _action;

	Button(String base_texture_name, String select_texture_name, int x, int y, ActionListener action) {
		ImageIcon base_texture = new ImageIcon(base_texture_name);
		_background = base_texture.getImage();

		ImageIcon select_texture = new ImageIcon(select_texture_name);
		_select = select_texture.getImage();

		_selected = false;

		_x = x;
		_y = y;
		_width = base_texture.getIconWidth();
		_height = base_texture.getIconHeight();
		_action = action;
	}

	void Draw(Graphics g, Engine engine) {
		if (_selected)
			g.drawImage(_select, _x, _y, engine);
		else
			g.drawImage(_background, _x, _y, engine);
	}

	void Touch(int x, int y) {
		if (x > _x && x < _x + _width && y > _y && y < _y + _height) {

			ActionEvent e = new ActionEvent(this, 0, "click");
			_action.actionPerformed(e);
		}
	}

	public void Move(int x, int y) {
		if (x > _x && x < _x + _width && y > _y && y < _y + _height) {
			_selected = true;
		} else
			_selected = false;

	}

	public void KeyPressd(boolean b, boolean enter) {
		_selected = b;
		if (enter) {
			ActionEvent e = new ActionEvent(this, 0, "click");
			_action.actionPerformed(e);
		}
	}

}
