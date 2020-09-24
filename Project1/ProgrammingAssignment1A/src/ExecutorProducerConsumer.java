import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ExecutorProducerConsumer {
	// initialize the buffer that producer and consumer tasks will access
	private static PCBuffer buffer = new PCBuffer(10, 5, 3);

	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new ProducerTask());
		executor.execute(new ConsumerTask());
		executor.shutdown();
		
		/*
		// create threads for the producer and consumer tasks
		Thread producerThread = new Thread(new ProducerTask()); // will use the run() method of ProducerTask
		Thread consumerThread = new Thread(new ConsumerTask()); // will use the run() method of ConsumerTask

		//call the run method for the consumer thread's task (we call this before running the producerThread
		//because the consumer thread calls wait on the producer thread so we immediately shift to the
		//producer thread to begin populating the elements of the buffer before using notifyAll to
		//return execution to the consumer thread)
		// call the run method of the producer thread's task
		producerThread.start();
		consumerThread.start();
		*/


	}

	// this task will be passed to the thread using executor.execute(new ProducerTask());
	private static class ProducerTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					buffer.produce();
					System.out.println("Buffer elements after produce: " + buffer);
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
					System.out.println("Buffer elements after consume: " + buffer);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	private static class PCBuffer extends ArrayBlockingQueue<Integer> {
		int prodInt, consInt;
		// prodInt = number of elements you wish to produce
		// consInt = number of elements you wish to consume
		public PCBuffer(int capacity, int prodInt, int consInt) {
			super(capacity);
			this.prodInt = prodInt;
			this.consInt = consInt;
			
		}
		private static Lock lock = new ReentrantLock();

		private static Condition isFull = lock.newCondition();
		private static Condition isEmpty = lock.newCondition();
		
		

		public void consume() {
			// acquire the lock
			lock.lock();
			try {
				// while the number of elements in the buffer (this.size) is equal to 0 we cannot consumer any elements
				while (this.size() == 0) {
					System.out.println("Buffer is EMPTY, must produce before attempting to consume again");
					isEmpty.await();
				}
				Integer queuePoll = this.poll();
				for (int j = 0; j < this.consInt; j++) {
					try {
						System.out.println("Consumed " + queuePoll);
						queuePoll = this.poll();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				if (this.size() == 0) {
					System.out.println("Buffer is now EMPTY");
				}
				else {
					System.out.println("Buffer is not empty, there are " + this.size() + " more elements that can be consumed");
				}
				isEmpty.signal();
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
				for (int i = 0; i < this.prodInt; i++) {
					try {
						this.add(i);
						System.out.println("Produced " + i);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (this.remainingCapacity() == 0) {
					System.out.println("Buffer is now FULL");

				}
				else {
					System.out.println("Buffer is not full, you can add " + this.remainingCapacity() + " more elements");
				}
				isFull.signal();
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

