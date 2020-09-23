import java.util.concurrent.*;

public class ProducerConsumer {

	public static void main(String[] args) {
		ProducerTask producerTask = new ProducerTask();
		ConsumerTask consumerTask = new ConsumerTask(producerTask);

		// create threads for the producer and consumer tasks we created
		Thread producerThread = new Thread(producerTask); // will use the run() method of ProducerTask
		Thread consumerThread = new Thread(consumerTask); // will use the run() method of ConsumerTask

		/* call the run method for the consumer thread's task (we call this before running the producerThread
		because the consumer thread calls wait on the producer thread so we immediately shift to the
		producer thread to begin populating the elements of the buffer before using notifyAll to
		return execution to the consumer thread) */
		consumerThread.start();
		// call the run method of the producer thread's task
		producerThread.start();
	}
}

class ProducerTask extends Thread {
	// create the buffer using ArrayBlockingQueue so we can handle attempts to access outside the capacity
	ArrayBlockingQueue<Integer> buffer;

	ProducerTask() {
		buffer = new ArrayBlockingQueue<>(10);
	}

	public void run() {
		// have a synchronized block that controls access to the buffer
		synchronized (buffer) {
			// populate the buffer with incremental numbers starting from 0 until the buffer is full
			for (int i = 0; i < 10; i++) {
				try {
					buffer.add(i);
					System.out.println("Produced " + i);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("Buffer is now FULL");
			// returns execution to the consumer thread
			buffer.notifyAll();
		}
	}
}

class ConsumerTask extends Thread {

	ProducerTask producer;;

	ConsumerTask(ProducerTask prod) {
		producer = prod;
	}

	public void run() {
		synchronized (producer.buffer) {
			try {
				// causes the consumer thread to wait for the producer thread to invoke the notifyAll method
				// after it populates the contents of the buffer
				producer.buffer.wait();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			Integer queuePoll = producer.buffer.poll();
			// removes and returns all of the elements in the buffer
			while (queuePoll != null) {
				try {
					System.out.println("Consumed " + queuePoll);
					queuePoll = producer.buffer.poll();
				}
				catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			// at the end of the while loop we have successfully removed all of the elements of the buffer
			System.out.println("Buffer is now EMPTY");
		}
	}
}

