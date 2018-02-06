
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

import java.util.*;
import java.io.*;

public class HighscoreManager {

	private ArrayList<Score> _scores;
	private String _fileName;

	ObjectOutputStream _outputStream = null;
	ObjectInputStream _inputStream = null;

	private boolean _fileExist = false;

	public HighscoreManager(int index) {
		_scores = new ArrayList<Score>();
		_fileName = "scores" + index + ".dat";
	}

	public void addScore(String name, int score) throws FileNotFoundException {
		loadScoreFile();
		_scores.add(new Score(name, score));
		updateScoreFile();
	}

	public ArrayList<Score> getScores() throws FileNotFoundException {
		loadScoreFile();
		sort();
		return _scores;
	}

	public String getHighscoreString() throws FileNotFoundException {
		String highscoreString = "";
		int max = 10;

		ArrayList<Score> scores;
		scores = getScores();

		int i = 0;
		int x = scores.size();
		if (x > max) {
			x = max;
		}
		while (i < x) {
			highscoreString += (i + 1) + ".\t" + scores.get(i).getName() + "\t\t" + scores.get(i).getScore() + "\n";
			i++;
		}
		return highscoreString;
	}

	public void updateScoreFile() throws FileNotFoundException {
		try {
			_outputStream = new ObjectOutputStream(new FileOutputStream(_fileName));
			_outputStream.writeObject(_scores);
		} catch (FileNotFoundException e) {
			System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
		} catch (IOException e) {
			System.out.println("[Update] IO Error: " + e.getMessage());
		} finally {
			try {
				if (_outputStream != null) {
					_outputStream.flush();
					_outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Update] Error: " + e.getMessage());
			}
		}
		loadScoreFile();
	}

	@SuppressWarnings("unchecked")
	public void loadScoreFile() throws FileNotFoundException {
		try {
			_inputStream = new ObjectInputStream(new FileInputStream(_fileName));
			_scores = (ArrayList<Score>) _inputStream.readObject();

			_fileExist = true;
		} catch (FileNotFoundException e) {
			System.out.println("[Load] FNF Error: " + e.getMessage());
			_fileExist = false;
			// updateScoreFile();
			// throw new FileNotFoundException();
		} catch (IOException e) {
			System.out.println("[Load] IO Error: " + e.getMessage());
			_fileExist = false;
		} catch (ClassNotFoundException e) {
			System.out.println("[Load] CNF Error: " + e.getMessage());
			_fileExist = false;
		} finally {
			try {
				if (_outputStream != null) {
					_outputStream.flush();
					_outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Load] IO Error: " + e.getMessage());
			}
		}
	}

	private void sort() {
		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(_scores, comparator);
	}

	boolean getExistFile() {
		return _fileExist;
	}

}