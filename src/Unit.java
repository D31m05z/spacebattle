
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

public class Unit {
	private double _x;
	private double _y;

	private int _dx;
	private int _dy;

	private int _destination;
	private boolean _ai;
	private Color _color;

	private double _preX = 0;
	private double _preY = 0;

	public Unit(int x, int y, int dx, int dy, int destination, boolean ai, Color color) {
		_x = x;
		_y = y;

		_dx = dx;
		_dy = dy;

		_destination = destination;
		_ai = ai;

		_color = color;
	}

	public int getX() {
		return (int) _x;
	}

	public int getY() {
		return (int) _y;
	}

	public int getDestination() {
		return _destination;
	}

	public boolean getAi() {
		return _ai;
	}

	public boolean update() {
		double rtx = 1;
		double rty = 1;

		double distx = _dx - _x;
		double disty = _dy - _y;
		double rate = Math.abs(distx / disty);

		if (distx >= -10 && distx <= 10 && disty >= -10 && disty <= 10) {
			return false;
		}

		rtx = Math.sqrt((8) / (1 + rate * rate));
		rty = rtx;

		if (_x < _dx) {
			_x += rtx * rate;
		} else if (_x > _dx) {
			_x -= rtx * rate;
		}

		if (_y < _dy) {
			_y += rty;
		} else if (_y > _dy) {
			_y -= rty;
		}

		if (_preX == _x && _preY == _y) {
			return false;
		}

		_preX = _x;
		_preY = _y;

		return true;
	}

	public Color getColor() {
		return _color;
	}
}
