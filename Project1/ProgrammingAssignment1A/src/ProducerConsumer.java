import java.util.concurrent.*;

public class ProducerConsumer {
	private static ArrayBlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(10);
	
	public static void main(String[] args) throws InterruptedException {
		ProducerTask producerTask = new ProducerTask(buffer);
		ConsumerTask consumerTask = new ConsumerTask(buffer);

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
		/*
		while(consumerThread.isAlive() == true || producerThread.isAlive() == true) {
            System.out.println("\nRetrieving Thread States...\nConsumer Thread State: " 
            		+consumerThread.getState()+"\nProducer Thread State: "+producerThread.getState() + "\n");
        }
		*/
		producerThread.join();
		consumerThread.join();
	}
}

class ProducerTask extends Thread {
	// create the buffer using ArrayBlockingQueue so we can handle attempts to access outside the capacity
	ArrayBlockingQueue<Integer> buffer;

	ProducerTask(ArrayBlockingQueue<Integer> buffer) {
		this.buffer = buffer; 
	}

	public void run() {
		// have a synchronized block that controls access to the buffer
		synchronized (buffer) {
			/*
			try {
				buffer.wait();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			*/
			// populate the buffer with incremental numbers starting from 0 until the buffer is full
			int counter = buffer.remainingCapacity();
			System.out.println("Producing " + counter + " elements in the buffer");
			for (int i = 0; i < counter; i++) {
				try {
					buffer.add(i);
					System.out.println("Produced " + i);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (buffer.remainingCapacity() == 0) {
				System.out.println("Buffer is now FULL");
			}
			else {
				System.out.println("Buffer is not full, you can add " + buffer.remainingCapacity() + " more elements");
			}
			System.out.println("Buffer elements after produce: " + buffer);
			// returns execution to the consumer thread
			buffer.notifyAll();
		}
	}
}

class ConsumerTask extends Thread {

	ArrayBlockingQueue<Integer> buffer;

	ConsumerTask(ArrayBlockingQueue<Integer> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		synchronized (buffer) {
			try {
				// causes the consumer thread to wait for the producer thread to invoke the notifyAll method
				// after it populates the contents of the buffer
				//System.out.println("\tConsumer Thread now waiting");
				buffer.wait();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			int counter = buffer.size();
			System.out.println("Consuming " + counter + " elements in the buffer");
			Integer queuePoll = 0;
			// removes and returns all of the elements in the buffer
			while (queuePoll != null && (counter > 0)) {
				try {
					queuePoll = buffer.poll();
					System.out.println("Consumed " + queuePoll);
				}
				catch (NullPointerException e) {
					e.printStackTrace();
				}
				counter--;
			}
			if (buffer.size() == 0) {
				// at the end of the while loop we have successfully removed all of the elements of the buffer
				System.out.println("Buffer is now EMPTY");
			}
			else {
				System.out.println("Buffer is not empty, can still consume " + buffer.size() + " elements");
			}
			System.out.println("Buffer elements after consume: " + buffer);
		}
	}
}

