/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2011, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala.concurrent
package default

import scala.util.Duration

private[concurrent] final class SchedulerImpl extends Scheduler {
  private val timer =
    new java.util.Timer(true) // the associated thread runs as a daemon

  def schedule(delay: Duration, frequency: Duration)(thunk: => Unit): Cancellable = ???
  
  def scheduleOnce(delay: Duration, task: Runnable): Cancellable = {
    val timerTask = new java.util.TimerTask {
      def run(): Unit =
        task.run()
    }
    timer.schedule(timerTask, delay.toMillis)
    new Cancellable {
      def cancel(): Unit =
        timerTask.cancel()
    }
  }

  def scheduleOnce(delay: Duration)(task: => Unit): Cancellable = {
    val timerTask = new java.util.TimerTask {
      def run(): Unit =
        task
    }
    timer.schedule(timerTask, delay.toMillis)
    new Cancellable {
      def cancel(): Unit =
        timerTask.cancel()
    }
  }

}
