(ns zmqguide.tasksink2
  (:require [zeromq.zmq :as zmq]))

(defn -main []
  (println "Start tasksink2")
  (let [context (zmq/zcontext)]

    (with-open [receiver (doto
                           (zmq/socket context :pull)
                           (zmq/bind "tcp://*:5558"))
                controller (doto
                             (zmq/socket context :pub)
                             (zmq/bind "tcp://*:5559"))]

      ;; Wait for start of batch
      (zmq/receive receiver)

      ;; Process 100 confirmations
      (let [tstart (System/currentTimeMillis)]
        (dotimes [i 100]
          (let [msg (zmq/receive-str receiver)]
            (if (= (mod i 10) 0)
              (print ":")
              (print "."))
            (flush)))
        (println (format "\nTotal elapsed time: %d msec"
                         (- (System/currentTimeMillis) tstart))))

      ;; Tell worker to stop
      (println "Send kill signal to workers")
      (zmq/send-str controller "KILL")
      (Thread/sleep 1000))

    (zmq/destroy context)))
