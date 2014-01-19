package Model;

import Model.abstractInterface.Observable;
import Model.abstractInterface.Observer;
import java.util.Timer;
import java.util.TimerTask;

public final class Clock implements Observable {

    private Timer timer;
    private String clock;
    private int minutes;
    private int seconds;
    private Observer observer;

    public Clock() {
//        this.resetClock();
    }
//    public Clock(Observer observer) {
//        this.observer = observer;
//    }

    public void startClock() {
        this.resetClock();
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task();
            }
        }, 1000, 1000);
    }

    public void stopClock() {
        try {
            this.timer.cancel();
            this.timer.purge();
        } catch (NullPointerException ex) {
        }
        this.timer = null;
    }

    public final void resetClock() {
        if (this.timer != null) stopClock();
        minutes = 0;
        seconds = 0;
        refreshClock("00:00");
    }

    private void task() {
        seconds++;
        refreshClock(analizeTime());
    }

    private void refreshClock(String string) {
        this.clock = string;
        notifyObservers(clock);
    }

    private String analizeTime() {
        if (seconds > 59) {
            seconds = 0;
            minutes++;
        }
        return buildTime();
    }

    private String buildTime() {
        return ((minutes > 9) ? minutes : "0" + minutes) + ":"
                + ((seconds > 9) ? seconds : "0" + seconds);
    }

    @Override
    public void addObserver(Observer oberver) {
        this.observer = oberver;
    }

    @Override
    public void removeObserver(Observer oberver) {
    }

    @Override
    public void notifyObservers(String event) {
        this.observer.update(event);
    }
}
