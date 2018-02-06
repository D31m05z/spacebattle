
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

import java.util.concurrent.TimeUnit;

public class GameTime {

	private long _starts;
	private boolean _paused;
	private long _pauseElapsed;
	private long _pauseStarted;

	GameTime() {
		reset();
	}

	public void pause(boolean on) {
		_paused = on;

		if (_paused) {
			_pauseStarted = System.currentTimeMillis();
		} else {
			_pauseElapsed += System.currentTimeMillis() - _pauseStarted;
		}
	}

	public void start() {
		reset();
	}

	public GameTime reset() {
		_starts = System.currentTimeMillis();

		return this;
	}

	public long time() {
		long ends = System.currentTimeMillis();

		return ends - _starts - _pauseElapsed;
	}

	public long time(TimeUnit unit) {
		return unit.convert(time(), TimeUnit.MILLISECONDS);
	}

}