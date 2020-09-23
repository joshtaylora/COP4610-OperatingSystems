import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ProducerConsumer {
	// initialize the buffer that producer and consumer tasks will access
	private static PCBuffer buffer = new PCBuffer(10);

	public static void main(String[] args) {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new ProducerTask());
		executor.execute(new ConsumerTask());
		executor.shutdown();
		/*
		// create threads for the producer and consumer tasks
		Thread producerThread = new Thread(new ProducerTask(buffer)); // will use the run() method of ProducerTask
		Thread consumerThread = new Thread(new ConsumerTask(buffer)); // will use the run() method of ConsumerTask

		//call the run method for the consumer thread's task (we call this before running the producerThread
		//because the consumer thread calls wait on the producer thread so we immediately shift to the
		//producer thread to begin populating the elements of the buffer before using notifyAll to
		//return execution to the consumer thread)
		consumerThread.start();
		// call the run method of the producer thread's task
		producerThread.start();

		producerThread.join();
		*/

	}


	private static class ProducerTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					buffer.produce();
					Thread.sleep(1000);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static class ConsumerTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					buffer.consume();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	private static class PCBuffer extends ArrayBlockingQueue<Integer> {
		public PCBuffer(int capacity) {
			super(capacity);
		}
		private static Lock lock = new ReentrantLock();

		private static Condition isFull = lock.newCondition();
		private static Condition isEmpty = lock.newCondition();

		public void consume() {
			// acquire the lock
			lock.lock();
			try {
				while (this.size() == 0) {
					System.out.println("Buffer is EMPTY, must produce before attempting to consume again");
					isEmpty.await();
				}
				Integer queuePoll = this.poll();
				while (queuePoll != null) {
					try {
						System.out.println("Consumed " + queuePoll);
						queuePoll = this.poll();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Buffer is now EMPTY");
				isEmpty.signalAll();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				lock.unlock();
			}
		}

		public void produce() {
			// acquire the lock
			lock.lock();
			try {
				while(this.remainingCapacity() == 0) {
					System.out.println("Buffer is FULL, must consume before attempting to produce again");
					isFull.await();
				}
				for (int i = 0; i < 10; i++) {
					try {
						this.add(i);
						System.out.println("Produced " + i);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Buffer is now FULL");
				isFull.signalAll();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				lock.unlock();
			}
		}
	}
}

