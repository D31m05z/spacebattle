
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
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Base {

	private ArrayList<Unit> _units;
	private ArrayList<Planet> _planets;

	public Base() {
		_units = new ArrayList<Unit>();
		_planets = new ArrayList<Planet>();
	}

	private void clear() {
		_units.clear();
		_planets.clear();
	}

	public void newGame(String name) {
		clear();

		try {
			FileInputStream fstream = new FileInputStream(name);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(" ");

				if (words[0].equals("None")) {
					System.out.println("add None : " + words[1] + " | " + words[2]);
					_planets.add(new Planet(0, Integer.parseInt(words[1]), Integer.parseInt(words[2]),
							Integer.parseInt(words[3]), Integer.parseInt(words[4])));
				} else if (words[0].equals("Player")) {
					System.out.println("add Player : " + words[1] + " | " + words[2]);
					_planets.add(new Planet(1, Integer.parseInt(words[1]), Integer.parseInt(words[2]),
							Integer.parseInt(words[3]), Integer.parseInt(words[4])));
				} else if (words[0].equals("Computer")) {
					System.out.println("add Computer : " + words[1] + " | " + words[2]);
					_planets.add(new Planet(2, Integer.parseInt(words[1]), Integer.parseInt(words[2]),
							Integer.parseInt(words[3]), Integer.parseInt(words[4])));
				}
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g, Engine engine) {
		for (int i = 0; i < _planets.size(); i++) {
			Planet curr = _planets.get(i);
			curr.draw(g, engine);

			// strong
			Font small = new Font("Helvetica", Font.BOLD, 14);

			g.setColor(Color.white);
			g.setFont(small);
			g.drawString(Integer.toString(curr.getStrong()), curr.getX() + 35, curr.getY() + 40);

		}

		// units
		for (int j = 0; j < _units.size(); j++) {
			int x = _units.get(j).getX();
			int y = _units.get(j).getY();

			g.setColor(_units.get(j).getColor());
			g.drawLine(x, y, x + 1, y + 1);

			// if the unit ...
			if (!_units.get(j).update()) {

				Unit u = _units.get(j);

				if (u.getAi()) {
					Planet victim = _planets.get(u.getDestination());
					int strong = victim.getStrong();

					if (victim.getState() == 1 || victim.getState() == 0) // PLAYER or NONE
						victim.setStrong(strong - 1);
					else
						victim.setStrong(strong + 1);

					if (victim.getStrong() < 0) {
						victim.setState(2);
						victim.setStrong(-victim.getStrong());
						victim.setSelected(false);
					} else if (victim.getStrong() == 0) {
						victim.setState(0);
					}

				} else {
					Planet victim = _planets.get(u.getDestination());
					int strong = victim.getStrong();

					if (victim.getState() == 2 || victim.getState() == 0) { // COMPUTER or NONE
						victim.setStrong(strong - 1);
					} else
						victim.setStrong(strong + 1);

					if (victim.getStrong() < 0) {
						victim.setState(1);
						victim.setStrong(-victim.getStrong());
					} else if (victim.getStrong() == 0) {
						victim.setState(0);
					}
				}
				_units.remove(j);
			}
		}
	}

	public void Touch(int x2, int y2, int button) {
		for (int i = 0; i < _planets.size(); i++) {
			Planet curr = _planets.get(i);
			int x = curr.getX();
			int y = curr.getY();

			if (curr.getState() == 0) { // NONE
				if (x <= x2 && (x + 80) >= x2 && y <= y2 && (y + 80) >= y2) {

					attack(i, false);
					allSelect(false);
				}
			} else if (curr.getState() == 1) { // PLAYER
				if (x <= x2 && (x + 80) >= x2 && y <= y2 && (y + 80) >= y2) {

					if (button == 1)
						curr.setSelected(!curr.isSelected());
					else {
						attack(i, false);
					}
				}
			} else if (curr.getState() == 2) { // COMPUTER
				if (x <= x2 && (x + 80) >= x2 && y <= y2 && (y + 80) >= y2) {

					attack(i, false);
					allSelect(false);
				}
			}
		}
	}

	private void attack(int destination, boolean ai) {

		if (ai) {
			for (int i = 0; i < _planets.size(); i++) {
				Planet curr = _planets.get(i);
				int x = curr.getX();
				int y = curr.getY();

				if (curr.getState() == 2) { // attack PLAYER
					for (int j = 0; j < (int) Math.floor((double) curr.getStrong() / 2); j++) {
						Unit u = new Unit(x + getRnd(), y + getRnd(), _planets.get(destination).getX() + 30,
								_planets.get(destination).getY() + 30, destination, true, Color.red);

						_units.add(u);
					}

					curr.setStrong((int) Math.ceil((double) curr.getStrong() / 2));
				}
			}
		} else {
			for (int i = 0; i < _planets.size(); i++) {
				Planet curr = _planets.get(i);
				int x = curr.getX();
				int y = curr.getY();

				if (curr.isSelected()) {
					for (int j = 0; j < (int) Math.floor((double) curr.getStrong() / 2); j++) {
						Unit u = new Unit(x + getRnd(), y + getRnd(), _planets.get(destination).getX() + 30,
								_planets.get(destination).getY() + 30, destination, false, Color.green);
						_units.add(u);
					}

					curr.setStrong((int) Math.ceil((double) curr.getStrong() / 2));
				}
			}
		}
	}

	private void allSelect(boolean b) {
		for (int i = 0; i < _planets.size(); i++) {
			_planets.get(i).setSelected(b);
		}
	}

	private int getRnd() {
		return randomNumber(40, 80);
	}

	public int randomNumber(int min, int max) {
		return min + (new Random()).nextInt(max - min);
	}

	public boolean getWin() {
		for (int i = 0; i < _planets.size(); i++) {
			if (_planets.get(i).getState() == 2) { // if we have COMPUTER planet we don't win
				return false;
			}
		}

		if (_units.size() != 0)
			return false;

		return true;
	}

	public boolean getGameOver() {
		for (int i = 0; i < _planets.size(); i++) {
			if (_planets.get(i).getState() == 1) { // if all planet is PLAYER
				return false;
			}
		}

		return true;
	}

	public void AI() {
		boolean need_victim = true;

		while (need_victim) {
			int destination = randomNumber(0, _planets.size());
			if (_planets.get(destination).getState() == 1 || _planets.get(destination).getState() == 0) {
				need_victim = false;
				attack(destination, true);
			} else
				need_victim = true;
		}
	}
}
